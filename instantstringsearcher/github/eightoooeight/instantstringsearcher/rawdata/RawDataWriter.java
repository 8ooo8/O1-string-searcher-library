package github.eightoooeight.instantstringsearcher.rawdata;

import java.io.*;
import java.util.*;

public class RawDataWriter implements IRawDataWriter
{
    protected static RawDataWriter self = null;
    protected String dataFilepath;

    private RawDataWriter() {}

    public static RawDataWriter getInstance()
    {
        return Optional.ofNullable(self).orElseGet(() -> self = new RawDataWriter());
    }

    public void setDataFilepath(String dataFilepath)
    {
        this.dataFilepath = dataFilepath;
    }

    public String write(String toWrite)
    {
        String dataID = null;
        File dataFile = new File(dataFilepath);
        try
        {
            dataFile.getParentFile().mkdirs();
            dataFile.createNewFile();
            try ( BufferedWriter writer = new BufferedWriter(new FileWriter(dataFile, true)); )
            {
                dataID = "" + dataFile.length();
                writer.write(toWrite);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            dataID += "-" + dataFile.length();
            return dataID;
        }
    }
}
