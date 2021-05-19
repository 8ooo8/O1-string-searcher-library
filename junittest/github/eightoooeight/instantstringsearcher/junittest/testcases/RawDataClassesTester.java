package github.eightoooeight.instantstringsearcher.junittest.testcases;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import github.eightoooeight.instantstringsearcher.junittest.util.*;
import github.eightoooeight.instantstringsearcher.rawdata.*;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;

public class RawDataClassesTester extends TestCase {
    protected static String _rawDataFilepath;

    @Before
    public void setUp() {
        _rawDataFilepath = "build/data/rawData.txt";
        FileDeleter.deleteFileIfExists(_rawDataFilepath);
    }

    @Test
    public void testRawDataReaderWriter() {
        System.out.println("Test case: " + this.getName());

        System.out.println("----------------------------");
    }
}
