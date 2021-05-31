package github.eightoooeight.instantstringsearcher.rawdata;

import java.io.*;
import java.util.*;

public class RawDataWriter implements IRawDataWriter{
    protected static RawDataWriter _this = null;
    protected String _dataFilepath;

    private RawDataWriter() {}
    public static RawDataWriter getInstance() {
        return Optional.ofNullable(_this).orElseGet(() -> { _this = new RawDataWriter(); return _this; });
    }

    public void setDataFilepath(String dataFilepath) { _dataFilepath = dataFilepath; }
    public String write(String toWrite) {
        String dataID = null;
        File dataFile = new File(_dataFilepath);
        try {
            dataFile.getParentFile().mkdirs();
            dataFile.createNewFile();
            try ( BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile, true)); ) {
                dataID = "" + dataFile.length();
                writer.write(toWrite);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            dataID += "-" + dataFile.length();
            return dataID;
        }
    }
}
