package github.eightoooeight.instantstringsearcher.rawdata;

import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class RawDataReader implements IRawDataReader
{
    protected String dataFilepath;

    public void setDataFilepath(String dataFilepath)
    {
        this.dataFilepath = dataFilepath;
    }

    public String read(String dataID)
    {
        String result = null;
        try ( RandomAccessFile reader = new RandomAccessFile(dataFilepath, "r"); )
        {
            long startPosition = Long.parseLong(dataID.split("-")[0]);
            long endPosition = Long.parseLong(dataID.split("-")[1]);
            byte[] bytes = new byte[(int)(endPosition - startPosition)];
            reader.seek(startPosition);
            reader.read(bytes);
            result = new String(bytes, StandardCharsets.UTF_8);
        }
        finally
        {
            return result;
        }
    }
}
