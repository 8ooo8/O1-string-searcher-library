package github.eightoooeight.instantstringsearcher.junittest.util;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.*;
import java.io.*;

import github.eightoooeight.instantstringsearcher.rawdata.*;
import github.eightoooeight.instantstringsearcher.trienode.*;
import github.eightoooeight.instantstringsearcher.*;

/*
 * This class implements github.eightoooeight.instantstringsearcher.IInstantStringSearcher
 * but its insertString() and searchString() methods are made late ended 
 * to test the read(search) write(insert) synchronization code in github.eightoooeight.instantstringsearcher.ImprovedSyncStringSearcher.
 * 
 * This class does not contain the business logic of string insertion and search, instead,
 * it makes use of github.eightoooeight.instantstringsearcher.AsyncO1StringSearcher
 * to handle the string insertion and the string search logic,
 * i.e. it is a wrapper of github.eightoooeight.instantstringsearcher.AsyncO1StringSearcher.
 */
public class AsyncO1StringSearcherWithDelayedEndInInsertAndSearch implements IInstantStringSearcher
{
    private IInstantStringSearcher asyncO1StringSearcher;
    private static AsyncO1StringSearcherWithDelayedEndInInsertAndSearch self;
    private long lateTimeInMethodsInMS;
    private List<LifeTime> recordedLifeTimesOfInsertAndSearch; // record the lifespans to validate the synchronization
    
    private AsyncO1StringSearcherWithDelayedEndInInsertAndSearch()
    {
        asyncO1StringSearcher = AsyncO1StringSearcher.getInstance();
        recordedLifeTimesOfInsertAndSearch = Collections.synchronizedList(new ArrayList<LifeTime>());
    }

    public static AsyncO1StringSearcherWithDelayedEndInInsertAndSearch getInstance()
    {
        return Optional.ofNullable(self).orElseGet(() -> self = new AsyncO1StringSearcherWithDelayedEndInInsertAndSearch());
    }

    public void setLateTimeInMethodsInMS(long lateTime)
    {
        lateTimeInMethodsInMS = lateTime;
    }

    public void setStoragePath(String storagePath)
    {
        asyncO1StringSearcher.setStoragePath(storagePath);
    }

    public String getStoragePath()
    {
        return asyncO1StringSearcher.getStoragePath();
    }

    public void insertString(String toInsert)
    {
        try
        {
            long startTimeInMS = System.currentTimeMillis();
            asyncO1StringSearcher.insertString(toInsert); // perform the insert operation
            long endTimeInMS = System.currentTimeMillis();
            recordedLifeTimesOfInsertAndSearch.add(new LifeTime(startTimeInMS, endTimeInMS)); // record the lifespan of an insert operatoin

            TimeUnit.MILLISECONDS.sleep(lateTimeInMethodsInMS);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public List<String> searchString(String searchStr)
    {
        List<String> result = null;
        try
        {
            long startTimeInMS = System.currentTimeMillis();
            result = asyncO1StringSearcher.searchString(searchStr); // perform the search operation
            long endTimeInMS = System.currentTimeMillis();
            recordedLifeTimesOfInsertAndSearch.add(new LifeTime(startTimeInMS, endTimeInMS)); // record the lifespan of a search opeartion

            TimeUnit.MILLISECONDS.sleep(lateTimeInMethodsInMS);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        finally
        {
            return result;
        }
    }

    public List<LifeTime> getLifeTimesOfInsertAndSearch()
    {
        return recordedLifeTimesOfInsertAndSearch;
    }
    
    public void clearRecordedLifeTimesOfInsertAndSearch()
    {
        recordedLifeTimesOfInsertAndSearch.clear();
    }
}
