package github.eightoooeight.instantstringsearcher.junittest.testcases;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import github.eightoooeight.instantstringsearcher.junittest.util.*;
import github.eightoooeight.instantstringsearcher.rawdata.*;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

public class IoCClassTester extends TestCase {
    protected static String _trieDirPath;
    protected static String _rawDataFilepath;

    @Before
    public void setUp() {
        _trieDirPath = "build/data/trie/";
        _rawDataFilepath = "build/data/rawData.txt";
        FileDeleter.deleteDirIfExists(new File(_trieDirPath));
        FileDeleter.deleteFileIfExists(_rawDataFilepath);
    }

    @Test
    public void testIoC() {
        System.out.println("Test case: " + this.getName());

        System.out.println("----------------------------");
    }
}
