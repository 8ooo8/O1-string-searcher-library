package github.eightoooeight.instantstringsearcher.rawdata;

public interface IRawDataWriter {
    public void setDataFilepath(String dataFilepath);
    /*
     * @return Data ID of the written data
     */
    public String write(String toWrite); 
}
