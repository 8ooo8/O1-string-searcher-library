package github.eightoooeight.instantstringsearcher.junittest.testcases;

import junit.framework.TestCase;
import static org.junit.Assert.*;
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
import java.util.*;

public class RawDataClassesTester extends TestCase
{
    protected static String rawDataFilepath;

    @Before
    public void setUp()
    {
        rawDataFilepath = "build/data/rawData.txt";
        FileDeleter.deleteFileIfExists(rawDataFilepath);
    }

    @Test
    public void testRawDataReaderWriter()
    {
        List<String> rawData = Arrays.asList(new String[]{"Hi, this is Jack.", "Nice to meet you. I am Sarah.", "You are so pretty, Sarah."});
        HashMap<String, String> rawDataDict = writeRawData(rawData);
        rawDataDict.forEach((dataID, dataValue) -> assertEquals(readRawData(dataID), dataValue));
    }

    /*
     * @return HashMap<data-ID, data-value>
     */
    private HashMap<String, String> writeRawData(List<String> rawData)
    {
        IRawDataWriter rawDataWriter = RawDataWriter.getInstance();
        rawDataWriter.setDataFilepath(rawDataFilepath);
        HashMap<String, String> result = new HashMap<>();
        rawData.forEach(dataPiece -> result.put(rawDataWriter.write(dataPiece), dataPiece));
        return result;
    }

    private String readRawData(String dataID)
    {
        IRawDataReader rawDataReader = new RawDataReader();
        rawDataReader.setDataFilepath(rawDataFilepath);
        return rawDataReader.read(dataID);
    }
}
