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

public class ImprovedSyncStringSearcherSyncTester extends TestCase {
    protected IInstantStringSearcher _improvedSyncStringSearcher;
    protected AsyncO1StringSearcherWithDelayedEndInInsertAndSearch _baseStringSearcher;
    protected String _storagePath;
    protected String _trieDirPath;
    protected String _rawDataFilepath;
    protected long _lateTimeInMethodsInMS;
    protected ExecutorService _executor;

    @Before
    public void setUp() {
        _storagePath = "./build/data";
        _trieDirPath = _storagePath + "/trie/";
        _rawDataFilepath = _storagePath + "/rawData.txt";
        _lateTimeInMethodsInMS = 1_000;

        FileDeleter.deleteDirIfExists(new File(_trieDirPath));
        FileDeleter.deleteFileIfExists(_rawDataFilepath);

        _baseStringSearcher = AsyncO1StringSearcherWithDelayedEndInInsertAndSearch.getInstance();
        _baseStringSearcher.setLateTimeInMethodsInMS(_lateTimeInMethodsInMS);
        _baseStringSearcher.clearRecordedLifeTimesOfInsertAndSearch();
        _improvedSyncStringSearcher = ImprovedSyncStringSearcher.getInstance(_baseStringSearcher);
        _improvedSyncStringSearcher.setStoragePath(_storagePath);

        _executor = Executors.newWorkStealingPool();
    }

    @After
    public void tearDown() {
        try {
            if (!_executor.isShutdown())
                _executor.shutdownNow();
            if (!_executor.isTerminated())
                _executor.awaitTermination(_lateTimeInMethodsInMS * 2, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcurrentReadWriteDiffFiles() {
        try {
            // insert and search string at a close time
            _executor.execute(() -> _improvedSyncStringSearcher.insertString("A"));
            _executor.execute(() -> _improvedSyncStringSearcher.searchString("B"));
            _executor.shutdown();
            _executor.awaitTermination(_lateTimeInMethodsInMS * 2, TimeUnit.MILLISECONDS);
            List<LifeTime> lifeTimes = _baseStringSearcher.getLifeTimesOfInsertAndSearch();

            // check synchronization
            long startTimeInMS0 = lifeTimes.get(0).getStartTimeInMS();
            long endTimeInMS0 = lifeTimes.get(0).getEndTimeInMS();
            long startTimeInMS1 = lifeTimes.get(1).getStartTimeInMS();
            long endTimeInMS1 = lifeTimes.get(1).getEndTimeInMS();
            assertFalse(startTimeInMS0 - endTimeInMS1 >= _lateTimeInMethodsInMS || startTimeInMS1 - endTimeInMS0 >= _lateTimeInMethodsInMS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcurrentReadWriteSameFile() {
        try {
            // insert and search string at a close time
            _executor.execute(() -> _improvedSyncStringSearcher.insertString("A"));
            _executor.execute(() -> _improvedSyncStringSearcher.searchString("A"));
            _executor.shutdown();
            _executor.awaitTermination(_lateTimeInMethodsInMS * 2, TimeUnit.MILLISECONDS);
            List<LifeTime> lifeTimes = _baseStringSearcher.getLifeTimesOfInsertAndSearch();

            // check synchronization
            long startTimeInMS0 = lifeTimes.get(0).getStartTimeInMS();
            long endTimeInMS0 = lifeTimes.get(0).getEndTimeInMS();
            long startTimeInMS1 = lifeTimes.get(1).getStartTimeInMS();
            long endTimeInMS1 = lifeTimes.get(1).getEndTimeInMS();
            assertTrue(startTimeInMS0 - endTimeInMS1 >= _lateTimeInMethodsInMS || startTimeInMS1 - endTimeInMS0 >= _lateTimeInMethodsInMS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcurrentWriteDiffFiles() {
        try {
            // insert string parallelly
            _executor.execute(() -> _improvedSyncStringSearcher.insertString("A"));
            _executor.execute(() -> _improvedSyncStringSearcher.insertString("B"));
            _executor.shutdown();
            _executor.awaitTermination(_lateTimeInMethodsInMS * 2, TimeUnit.MILLISECONDS);
            List<LifeTime> lifeTimes = _baseStringSearcher.getLifeTimesOfInsertAndSearch();

            // check synchronization
            long startTimeInMS0 = lifeTimes.get(0).getStartTimeInMS();
            long endTimeInMS0 = lifeTimes.get(0).getEndTimeInMS();
            long startTimeInMS1 = lifeTimes.get(1).getStartTimeInMS();
            long endTimeInMS1 = lifeTimes.get(1).getEndTimeInMS();
            assertFalse(startTimeInMS0 - endTimeInMS1 >= _lateTimeInMethodsInMS || startTimeInMS1 - endTimeInMS0 >= _lateTimeInMethodsInMS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcurrentWriteSameFile() {
        try {
            // insert string parallelly
            _executor.execute(() -> _improvedSyncStringSearcher.insertString("A"));
            _executor.execute(() -> _improvedSyncStringSearcher.insertString("A"));
            _executor.shutdown();
            _executor.awaitTermination(_lateTimeInMethodsInMS * 2, TimeUnit.MILLISECONDS);
            List<LifeTime> lifeTimes = _baseStringSearcher.getLifeTimesOfInsertAndSearch();

            // check synchronization
            long startTimeInMS0 = lifeTimes.get(0).getStartTimeInMS();
            long endTimeInMS0 = lifeTimes.get(0).getEndTimeInMS();
            long startTimeInMS1 = lifeTimes.get(1).getStartTimeInMS();
            long endTimeInMS1 = lifeTimes.get(1).getEndTimeInMS();
            assertTrue(startTimeInMS0 - endTimeInMS1 >= _lateTimeInMethodsInMS || startTimeInMS1 - endTimeInMS0 >= _lateTimeInMethodsInMS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConcurrentRead() {
        try {
            // insert strings
            List<String> toInsert = Arrays.asList(new String[]{"Hi, this is Jack.", "Nice to meet you. I am Sarah.", "You are so pretty, Sarah."});
            toInsert.forEach(_improvedSyncStringSearcher::insertString);
            _baseStringSearcher.clearRecordedLifeTimesOfInsertAndSearch();

            // start concurrent read test
            _executor.execute(() -> _improvedSyncStringSearcher.searchString("Hi"));
            _executor.execute(() -> _improvedSyncStringSearcher.searchString("Hi"));
            _executor.execute(() -> _improvedSyncStringSearcher.searchString("Nice"));
            _executor.shutdown();
            _executor.awaitTermination(_lateTimeInMethodsInMS * 2, TimeUnit.MILLISECONDS);
            List<LifeTime> lifeTimes = _baseStringSearcher.getLifeTimesOfInsertAndSearch();

            // check synchronization
            lifeTimes.stream().map(LifeTime::getEndTimeInMS).forEach((endTimeInMS) -> {
                lifeTimes.stream().map(LifeTime::getStartTimeInMS).forEach((startTimeInMS) -> {
                    assertTrue(startTimeInMS - endTimeInMS < _lateTimeInMethodsInMS);
                });
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
