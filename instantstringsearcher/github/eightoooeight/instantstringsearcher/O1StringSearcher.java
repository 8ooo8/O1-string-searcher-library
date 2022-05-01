package github.eightoooeight.instantstringsearcher;

import java.util.*;

import github.eightoooeight.instantstringsearcher.AsyncO1StringSearcher;
import github.eightoooeight.instantstringsearcher.rawdata.*;
import github.eightoooeight.instantstringsearcher.trienode.*;

public class O1StringSearcher implements IInstantStringSearcher
{
    protected static O1StringSearcher self;
    protected static IInstantStringSearcher baseStringSearcher;

    private O1StringSearcher()
    {
        baseStringSearcher = AsyncO1StringSearcher.getInstance();
    }

    public static O1StringSearcher getInstance()
    {
        return Optional.ofNullable(self).orElseGet(() -> self = new O1StringSearcher());
    }

    public void setStoragePath(String storagePath)
    {
        baseStringSearcher.setStoragePath(storagePath);
    }

    public String getStoragePath()
    {
        return baseStringSearcher.getStoragePath();
    }

    public synchronized void insertString(String toInsert)
    {
        baseStringSearcher.insertString(toInsert);
    }

    public synchronized List<String> searchString(String searchStr)
    {
        return baseStringSearcher.searchString(searchStr);
    }
}
