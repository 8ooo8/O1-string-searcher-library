package github.eightoooeight.instantstringsearcher;

public interface IInstantStringSearcher {
    public void setStoragePath(String storagePath);
    public String getStoragePath();
    public void insertString(String toInsert);
    public String searchString(String searchStr);
}