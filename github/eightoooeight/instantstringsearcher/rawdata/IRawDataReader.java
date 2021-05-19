package github.eightoooeight.instantstringsearcher.rawdata;

public interface IRawDataReader {
    public void setDataFilepath(String dataFilepath);
    public String read(String dataID);
}
