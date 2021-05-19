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

public class TrieNodeClassesTester extends TestCase {
    protected static String _trieDirPath;

    @Before
    public void setUp() {
        _trieDirPath = "build/data/trie/";
        FileDeleter.deleteDirIfExists(new File(_trieDirPath));
    }

    @Test
    public void testTrieNodeAndFilepathMapper() {
        System.out.println("Test case: " + this.getName());

        System.out.println("----------------------------");
    }

    @Test
    public void testTrieNodeReaderWriter() {
        System.out.println("Test case: " + this.getName());

        System.out.println("----------------------------");
    }
}
