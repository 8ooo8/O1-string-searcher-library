package github.eightoooeight.instantstringsearcher;

import java.util.*;
import java.util.stream.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.*;
import java.io.*;

import github.eightoooeight.instantstringsearcher.rawdata.*;
import github.eightoooeight.instantstringsearcher.trienode.*;

/*
 * This is a class wrapping another IInstantStringSearcher object and on top of it, add a read write synchronization mechanism.
 */
public class ImprovedSyncStringSearcher implements IInstantStringSearcher
{
    protected ConcurrentHashMap<String, CustomReadWriteLock> fileLocks;
    protected static IInstantStringSearcher baseStringSearcher;
    protected static ImprovedSyncStringSearcher self;
    protected String storagePath;

    private ImprovedSyncStringSearcher()
    {
        fileLocks = new ConcurrentHashMap<>();
    }

    /*
     * Invoke below getInstance() to assign an IInstantStringSearcher object to be wrapped by this object.
     * The wrapped object's insertString() and searchString() methods will be used for the string insertion and search operations
     * but on top of it, a read write synchronization mechanism will be added.
     */
    public static ImprovedSyncStringSearcher getInstance(IInstantStringSearcher baseStringSearcher)
    {
        ImprovedSyncStringSearcher.baseStringSearcher = baseStringSearcher;
        return Optional.ofNullable(self).orElseGet(() -> self = new ImprovedSyncStringSearcher());
    }

    public static ImprovedSyncStringSearcher getInstance()
    {
        baseStringSearcher = AsyncO1StringSearcher.getInstance();
        return Optional.ofNullable(self).orElseGet(() -> self = new ImprovedSyncStringSearcher());
    }

    public void setStoragePath(String storagePath)
    {
        this.storagePath = storagePath;
        baseStringSearcher.setStoragePath(storagePath);
    }

    public String getStoragePath()
    {
        return storagePath;
    }

    public void insertString(String toInsert)
    {
        // get the files to lock
        List<String> filesToLock = IntStream.range(0, toInsert.length())
            .mapToObj((i) -> toInsert.substring(i))
            .collect(Collectors.toList());

        // lock the write locks
        List<CustomReadWriteLock> locks = filesToLock.stream().map((v) -> getLock(v)).collect(Collectors.toList());
        locks.forEach((v) -> v.getReadWriteLock().writeLock().lock());

        // write
        baseStringSearcher.insertString(toInsert);
        
        // release the locks
        locks.forEach((v) -> v.getReadWriteLock().writeLock().unlock());
        filesToLock.stream()
            .forEach((v) -> removeLockRef(v));
    }

    public List<String> searchString(String searchStr)
    {
        CustomReadWriteLock lock = getLock(searchStr);
        
        // lock
        lock.getReadWriteLock().readLock().lock();

        // search string
        List<String> searchResult = baseStringSearcher.searchString(searchStr);
        
        // release the lock
        lock.getReadWriteLock().readLock().unlock();
        removeLockRef(searchStr);

        // return the search result
        return searchResult;
    }

    private synchronized CustomReadWriteLock getLock(String keychain)
    {
        fileLocks.putIfAbsent(keychain, new CustomReadWriteLock());
        CustomReadWriteLock requestedLock = fileLocks.get(keychain);
        requestedLock.add1Ref();
        return requestedLock;
    }

    private synchronized void removeLockRef(String keychain)
    {
        CustomReadWriteLock requestedLock = fileLocks.get(keychain);
        requestedLock.remove1Ref();
        if (requestedLock.getRefNum() == 0)
        {
            fileLocks.remove(keychain);
        }
    }
}

class CustomReadWriteLock
{
    protected ReadWriteLock readWriteLock;
    protected AtomicInteger numOfRef; //number of threads referencing/using this lock

    protected CustomReadWriteLock()
    {
        readWriteLock = new ReentrantReadWriteLock();
        numOfRef = new AtomicInteger(0);
    }

    protected ReadWriteLock getReadWriteLock()
    {
        return readWriteLock;
    }

    protected void add1Ref()
    {
        numOfRef.incrementAndGet();
    }

    protected void remove1Ref()
    {
        numOfRef.decrementAndGet();
    }

    protected int getRefNum()
    {
        return numOfRef.get();
    }
}
