package github.eightoooeight.instantstringsearcher.rawdata;

public interface IRawDataWriter {
    public void setDataFilepath(String dataFilepath);
    public String write(String toWrite);
}
