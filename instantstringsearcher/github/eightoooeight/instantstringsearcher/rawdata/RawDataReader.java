package github.eightoooeight.instantstringsearcher.rawdata;

import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;

public class RawDataReader implements IRawDataReader{
    protected String _dataFilepath;

    public void setDataFilepath(String dataFilepath) { _dataFilepath = dataFilepath; }
    public String read(String dataID) {
        RandomAccessFile reader = null;
        String result = null;
        try {
            reader = new RandomAccessFile(_dataFilepath, "r");
            long startPosition = Long.parseLong(dataID.split("-")[0]);
            long endPosition = Long.parseLong(dataID.split("-")[1]);
            byte[] bytes = new byte[(int)(endPosition - startPosition)];
            reader.seek(startPosition);
            reader.read(bytes);
            result = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Optional.ofNullable(reader).ifPresent(r -> {
                try {
                    r.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            return result;
        }
    }
}
