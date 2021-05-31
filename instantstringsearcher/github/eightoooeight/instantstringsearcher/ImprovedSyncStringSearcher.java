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
public class ImprovedSyncStringSearcher implements IInstantStringSearcher{
    protected ConcurrentHashMap<String, CustomReadWriteLock> _fileLocks;
    protected static IInstantStringSearcher _baseStringSearcher;
    protected static ImprovedSyncStringSearcher _this;
    protected String _storagePath;

    private ImprovedSyncStringSearcher() {
        _fileLocks = new ConcurrentHashMap<>();
    }

    /*
     * Invoke below getInstance() to assign an IInstantStringSearcher object to be wrapped by this object.
     * The wrapped object's insertString() and searchString() methods will be used for the string insertion and search operations
     * but on top of it, a read write synchronization mechanism will be added.
     */
    public static ImprovedSyncStringSearcher getInstance(IInstantStringSearcher baseStringSearcher) {
        _baseStringSearcher = baseStringSearcher;
        return Optional.ofNullable(_this).orElseGet(() -> { _this = new ImprovedSyncStringSearcher(); return _this; });
    }

    public static ImprovedSyncStringSearcher getInstance() {
        _baseStringSearcher = AsyncO1StringSearcher.getInstance();
        return Optional.ofNullable(_this).orElseGet(() -> { _this = new ImprovedSyncStringSearcher(); return _this; });
    }

    public void setStoragePath(String storagePath) {
        _storagePath = storagePath;
        _baseStringSearcher.setStoragePath(_storagePath);
    }
    public String getStoragePath() { return _storagePath; }
    public void insertString(String toInsert) {
        // get the files to lock
        List<String> filesToLock = IntStream.range(0, toInsert.length())
            .mapToObj((i) -> toInsert.substring(i))
            .collect(Collectors.toList());

        // lock the write locks
        List<CustomReadWriteLock> locks = filesToLock.stream().map((v) -> _getLock(v)).collect(Collectors.toList());
        locks.forEach((v) -> v.getReadWriteLock().writeLock().lock());

        // write
        _baseStringSearcher.insertString(toInsert);
        
        // release the locks
        locks.forEach((v) -> v.getReadWriteLock().writeLock().unlock());
        filesToLock.stream()
            .forEach((v) -> _releaseLock(v));
    }
    public List<String> searchString(String searchStr) {
        CustomReadWriteLock lock = _getLock(searchStr);
        
        // lock
        lock.getReadWriteLock().readLock().lock();

        // search string
        List<String> searchResult = _baseStringSearcher.searchString(searchStr);
        
        // release the lock
        lock.getReadWriteLock().readLock().unlock();
        _releaseLock(searchStr);

        // return the search result
        return searchResult;
    }

    private synchronized CustomReadWriteLock _getLock(String keychain) {
        _fileLocks.putIfAbsent(keychain, new CustomReadWriteLock());
        CustomReadWriteLock requestedLock = _fileLocks.get(keychain);
        requestedLock.add1Ref();
        return requestedLock;
    }

    private synchronized void _releaseLock(String keychain) {
        CustomReadWriteLock requestedLock = _fileLocks.get(keychain);
        requestedLock.remove1Ref();
        if (requestedLock.getRefNum() == 0) {
            _fileLocks.remove(keychain);
        }
    }
}

class CustomReadWriteLock {
    protected ReadWriteLock _readWriteLock;
    protected AtomicInteger _numOfRef; //number of threads referencing/using this lock

    protected CustomReadWriteLock() {
        _readWriteLock = new ReentrantReadWriteLock();
        _numOfRef = new AtomicInteger(0);
    }

    protected ReadWriteLock getReadWriteLock() { return _readWriteLock; }
    protected void add1Ref() { _numOfRef.incrementAndGet(); }
    protected void remove1Ref() { _numOfRef.decrementAndGet(); }
    protected int getRefNum() { return _numOfRef.get(); }
}
