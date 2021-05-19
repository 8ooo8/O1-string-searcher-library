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

public class RawDataClassesTester extends TestCase {
    protected static String _rawDataFilepath;

    @Before
    public void setUp() {
        _rawDataFilepath = "build/data/rawData.txt";
        FileDeleter.deleteFileIfExists(_rawDataFilepath);
    }

    @Test
    public void testRawDataReaderWriter() {
        List<String> rawData = Arrays.asList(new String[]{"Hi, this is Jack.", "Nice to meet you. I am Sarah.", "You are so pretty, Sarah."});
        HashMap<String, String> rawDataDict = _writeRawData(rawData);
        rawDataDict.forEach((dataID, dataValue) -> assertEquals(_readRawData(dataID), dataValue));
    }

    /*
     * @return HashMap<data-ID, data-value>
     */
    private HashMap<String, String> _writeRawData(List<String> rawData) {
        IRawDataWriter rawDataWriter = new RawDataWriter();
        rawDataWriter.setDataFilepath(_rawDataFilepath);
        HashMap<String, String> result = new HashMap<>();
        rawData.forEach(dataPiece -> result.put(rawDataWriter.write(dataPiece), dataPiece));
        return result;
    }

    private String _readRawData(String dataID) {
        IRawDataReader rawDataReader = new RawDataReader();
        rawDataReader.setDataFilepath(_rawDataFilepath);
        return rawDataReader.read(dataID);
    }
}
