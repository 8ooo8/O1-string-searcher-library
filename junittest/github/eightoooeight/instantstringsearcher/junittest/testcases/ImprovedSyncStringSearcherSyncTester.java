package github.eightoooeight.instantstringsearcher.junittest.testcases;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.Ignore;

import github.eightoooeight.instantstringsearcher.junittest.util.*;
import github.eightoooeight.instantstringsearcher.rawdata.*;
import github.eightoooeight.instantstringsearcher.*;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.lang.*;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.*;

public class ImprovedSyncStringSearcherSyncTester extends TestCase
{
    protected IInstantStringSearcher improvedSyncStringSearcher;
    protected AsyncO1StringSearcherWithDelayedEndInInsertAndSearch baseStringSearcher;
    protected String storagePath;
    protected String trieDirPath;
    protected String rawDataFilepath;
    protected long lateTimeInMethodsInMS;
    protected ExecutorService executor;

    public void setUp()
    {
        storagePath = "./build/data";
        trieDirPath = storagePath + "/trie/";
        rawDataFilepath = storagePath + "/rawData.txt";
        lateTimeInMethodsInMS = 1_000;

        FileDeleter.deleteDirIfExists(new File(trieDirPath));
        FileDeleter.deleteFileIfExists(rawDataFilepath);

        baseStringSearcher = AsyncO1StringSearcherWithDelayedEndInInsertAndSearch.getInstance();
        baseStringSearcher.setLateTimeInMethodsInMS(lateTimeInMethodsInMS);
        baseStringSearcher.clearRecordedLifeTimesOfInsertAndSearch();
        improvedSyncStringSearcher = ImprovedSyncStringSearcher.getInstance(baseStringSearcher);
        improvedSyncStringSearcher.setStoragePath(storagePath);

        executor = Executors.newWorkStealingPool();
    }

    public void tearDown()
    {
        try
        {
            if (!executor.isShutdown())
                executor.shutdownNow();
            if (!executor.isTerminated())
                executor.awaitTermination(lateTimeInMethodsInMS * 2, TimeUnit.MILLISECONDS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcurrentReadWriteDiffFiles()
    {
        try
        {
            // insert and search string at a close time
            executor.execute(() -> improvedSyncStringSearcher.insertString("A"));
            executor.execute(() -> improvedSyncStringSearcher.searchString("B"));
            executor.shutdown();
            executor.awaitTermination(lateTimeInMethodsInMS * 2, TimeUnit.MILLISECONDS);
            List<LifeTime> lifeTimes = baseStringSearcher.getLifeTimesOfInsertAndSearch();

            // check synchronization
            long startTimeInMS0 = lifeTimes.get(0).getStartTimeInMS();
            long endTimeInMS0 = lifeTimes.get(0).getEndTimeInMS();
            long startTimeInMS1 = lifeTimes.get(1).getStartTimeInMS();
            long endTimeInMS1 = lifeTimes.get(1).getEndTimeInMS();
            assertFalse(startTimeInMS0 - endTimeInMS1 >= lateTimeInMethodsInMS || startTimeInMS1 - endTimeInMS0 >= lateTimeInMethodsInMS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcurrentReadWriteSameFile() {
        try {
            // insert and search string at a close time
            executor.execute(() -> improvedSyncStringSearcher.insertString("A"));
            executor.execute(() -> improvedSyncStringSearcher.searchString("A"));
            executor.shutdown();
            executor.awaitTermination(lateTimeInMethodsInMS * 2, TimeUnit.MILLISECONDS);
            List<LifeTime> lifeTimes = baseStringSearcher.getLifeTimesOfInsertAndSearch();

            // check synchronization
            long startTimeInMS0 = lifeTimes.get(0).getStartTimeInMS();
            long endTimeInMS0 = lifeTimes.get(0).getEndTimeInMS();
            long startTimeInMS1 = lifeTimes.get(1).getStartTimeInMS();
            long endTimeInMS1 = lifeTimes.get(1).getEndTimeInMS();
            assertTrue(startTimeInMS0 - endTimeInMS1 >= lateTimeInMethodsInMS || startTimeInMS1 - endTimeInMS0 >= lateTimeInMethodsInMS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcurrentWriteDiffFiles() {
        try {
            // insert string parallelly
            executor.execute(() -> improvedSyncStringSearcher.insertString("A"));
            executor.execute(() -> improvedSyncStringSearcher.insertString("B"));
            executor.shutdown();
            executor.awaitTermination(lateTimeInMethodsInMS * 2, TimeUnit.MILLISECONDS);
            List<LifeTime> lifeTimes = baseStringSearcher.getLifeTimesOfInsertAndSearch();

            // check synchronization
            long startTimeInMS0 = lifeTimes.get(0).getStartTimeInMS();
            long endTimeInMS0 = lifeTimes.get(0).getEndTimeInMS();
            long startTimeInMS1 = lifeTimes.get(1).getStartTimeInMS();
            long endTimeInMS1 = lifeTimes.get(1).getEndTimeInMS();
            assertFalse(startTimeInMS0 - endTimeInMS1 >= lateTimeInMethodsInMS || startTimeInMS1 - endTimeInMS0 >= lateTimeInMethodsInMS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcurrentWriteSameFile()
    {
        try
        {
            // insert string parallelly
            executor.execute(() -> improvedSyncStringSearcher.insertString("A"));
            executor.execute(() -> improvedSyncStringSearcher.insertString("A"));
            executor.shutdown();
            executor.awaitTermination(lateTimeInMethodsInMS * 2, TimeUnit.MILLISECONDS);
            List<LifeTime> lifeTimes = baseStringSearcher.getLifeTimesOfInsertAndSearch();

            // check synchronization
            long startTimeInMS0 = lifeTimes.get(0).getStartTimeInMS();
            long endTimeInMS0 = lifeTimes.get(0).getEndTimeInMS();
            long startTimeInMS1 = lifeTimes.get(1).getStartTimeInMS();
            long endTimeInMS1 = lifeTimes.get(1).getEndTimeInMS();
            assertTrue(startTimeInMS0 - endTimeInMS1 >= lateTimeInMethodsInMS || startTimeInMS1 - endTimeInMS0 >= lateTimeInMethodsInMS);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcurrentRead()
    {
        try
        {
            // insert strings
            List<String> toInsert = Arrays.asList(new String[]{"Hi, this is Jack.", "Nice to meet you. I am Sarah.", "You are so pretty, Sarah."});
            toInsert.forEach(improvedSyncStringSearcher::insertString);
            baseStringSearcher.clearRecordedLifeTimesOfInsertAndSearch();

            // start concurrent read test
            executor.execute(() -> improvedSyncStringSearcher.searchString("Hi"));
            executor.execute(() -> improvedSyncStringSearcher.searchString("Hi"));
            executor.execute(() -> improvedSyncStringSearcher.searchString("Nice"));
            executor.shutdown();
            executor.awaitTermination(lateTimeInMethodsInMS * 2, TimeUnit.MILLISECONDS);
            List<LifeTime> lifeTimes = baseStringSearcher.getLifeTimesOfInsertAndSearch();

            // check synchronization
            lifeTimes.stream().map(LifeTime::getEndTimeInMS).forEach((endTimeInMS) -> {
                lifeTimes.stream().map(LifeTime::getStartTimeInMS).forEach((startTimeInMS) -> {
                    assertTrue(startTimeInMS - endTimeInMS < lateTimeInMethodsInMS);
                });
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
