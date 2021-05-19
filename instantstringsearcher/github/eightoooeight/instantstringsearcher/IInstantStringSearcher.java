package github.eightoooeight.instantstringsearcher;

import java.util.List;

public interface IInstantStringSearcher {
    public void setStoragePath(String storagePath);
    public String getStoragePath();
    public void insertString(String toInsert);
    public List<String> searchString(String searchStr);
}
