package github.eightoooeight.instantstringsearcher;

import java.util.*;

import github.eightoooeight.instantstringsearcher.rawdata.*;
import github.eightoooeight.instantstringsearcher.trienode.*;

/*
 * Supposed to be used along with a read(search) write(insert) synchronization mechanism, 
 * e.g. github.eightoooeight.instantstringsearcher.ImprovedSyncStringSearcher.
 */
public class AsyncO1StringSearcher implements IInstantStringSearcher
{
    protected static AsyncO1StringSearcher self;
    protected String storagePath;
    protected ITrieNodeAndFilepathMapper mapper;
    protected ITrieNodeReader nodeReader;
    protected ITrieNodeWriter nodeWriter;
    protected IRawDataReader rawDataReader;
    protected IRawDataWriter rawDataWriter;

    private AsyncO1StringSearcher()
    {
        mapper = new TrieNodeAndFilepathMapper();
        nodeReader = new TrieNodeReader();
        rawDataReader = new RawDataReader();
        nodeWriter = TrieNodeWriter.getInstance();
        rawDataWriter = RawDataWriter.getInstance();
    }

    public static AsyncO1StringSearcher getInstance()
    {
        return Optional.ofNullable(self).orElseGet(() -> self = new AsyncO1StringSearcher());
    }

    public void setStoragePath(String storagePath)
    {
        this.storagePath = storagePath;
        mapper.setStoragePath(storagePath + "/trie");
        rawDataWriter.setDataFilepath(storagePath + "/rawData.txt");
        rawDataReader.setDataFilepath(storagePath + "/rawData.txt");
    }

    public String getStoragePath()
    {
        return storagePath;
    }

    public void insertString(String toInsert)
    {
        String dataID = rawDataWriter.write(toInsert);
        List<String> toInsertSlices = new ArrayList();
        for (int i = 0; i < toInsert.length(); i++)
        {
            toInsertSlices.add(toInsert.substring(i));
        }
        toInsertSlices.forEach(slice -> {
            for (int i = slice.length(); i > 0; i--)
            {
                String sliceOfSlice = slice.substring(0, i);
                ITrieNode node = new TrieNode();
                node.init(sliceOfSlice, mapper);
                nodeWriter.write(node, dataID);
            }
        });
    }

    public List<String> searchString(String searchStr)
    {
        ITrieNode nodeWithSearchResult = new TrieNode();
        nodeWithSearchResult.init(searchStr, mapper);
        List<String> rawDataIDs = nodeReader.read(nodeWithSearchResult);
        List<String> result = new ArrayList<String>();
        rawDataIDs.forEach(ID -> {
            result.add(rawDataReader.read(ID));
        });
        return result;
    }
}
