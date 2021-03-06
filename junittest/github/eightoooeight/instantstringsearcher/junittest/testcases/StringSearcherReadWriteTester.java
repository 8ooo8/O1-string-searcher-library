package github.eightoooeight.instantstringsearcher.junittest.testcases;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import github.eightoooeight.instantstringsearcher.junittest.util.*;
import github.eightoooeight.instantstringsearcher.rawdata.*;
import github.eightoooeight.instantstringsearcher.*;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.util.*;
import java.util.stream.*;
import java.util.function.*;

public class StringSearcherReadWriteTester extends TestCase
{
    protected static String storagePath;
    protected static String trieDirPath;
    protected static String rawDataFilepath;

    @Before
    public void setUp()
    {
        storagePath = "./build/data";
        trieDirPath = storagePath + "/trie/";
        rawDataFilepath = storagePath + "/rawData.txt";
    }

    @Test
    public void testO1StringSearcher()
    {
        IInstantStringSearcher instantStringSearcher = O1StringSearcher.getInstance();
        instantStringSearcher.setStoragePath(storagePath);
        testInstantStringSearcher(instantStringSearcher);
    }

    @Test
    public void testAsyncO1StringSearcher()
    {
        IInstantStringSearcher instantStringSearcher = AsyncO1StringSearcher.getInstance();
        instantStringSearcher.setStoragePath(storagePath);
        testInstantStringSearcher(instantStringSearcher);
    }

    @Test
    public void testImprovedSyncStringSearcher()
    {
        IInstantStringSearcher baseStringSearcher = AsyncO1StringSearcher.getInstance();
        IInstantStringSearcher instantStringSearcher = ImprovedSyncStringSearcher.getInstance(baseStringSearcher);
        instantStringSearcher.setStoragePath(storagePath);
        testInstantStringSearcher(instantStringSearcher);
    }

    private void testInstantStringSearcher(IInstantStringSearcher instantStringSearcher)
    {
        FileDeleter.deleteDirIfExists(new File(trieDirPath));
        FileDeleter.deleteFileIfExists(rawDataFilepath);

        List<String> toInsert = Arrays.asList(new String[]{"Hi, this is Jack.", "Nice to meet you. I am Sarah.", "You are so pretty, Sarah."});
        
        toInsert.forEach(pieceToInsert -> instantStringSearcher.insertString(pieceToInsert));
        List<String> is_searchResult = instantStringSearcher.searchString("is");
        List<String> you_searchResult = instantStringSearcher.searchString("you");
        List<String> jack_searchResult = instantStringSearcher.searchString("Jack");

        assertEquals(is_searchResult.size(), 2);
        assertEquals(is_searchResult.get(0), toInsert.get(0));
        assertEquals(is_searchResult.get(1), toInsert.get(0));

        assertEquals(you_searchResult.size(), 2);
        assertEquals(you_searchResult.get(0), toInsert.get(1));
        assertEquals(you_searchResult.get(1), toInsert.get(2));

        assertEquals(jack_searchResult.size(), 1);
        assertEquals(jack_searchResult.get(0), toInsert.get(0));
    }
}
