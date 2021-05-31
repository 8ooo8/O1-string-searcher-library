package github.eightoooeight.instantstringsearcher;

import java.util.*;

import github.eightoooeight.instantstringsearcher.rawdata.*;
import github.eightoooeight.instantstringsearcher.trienode.*;

/*
 * Supposed to be used along with a read(search) write(insert) synchronization mechanism, 
 * e.g. github.eightoooeight.instantstringsearcher.ImprovedSyncStringSearcher.
 */
public class AsyncO1StringSearcher implements IInstantStringSearcher{
    protected static AsyncO1StringSearcher _this;
    protected String _storagePath;
    protected ITrieNodeAndFilepathMapper _mapper;
    protected ITrieNodeReader _nodeReader;
    protected ITrieNodeWriter _nodeWriter;
    protected IRawDataReader _rawDataReader;
    protected IRawDataWriter _rawDataWriter;

    private AsyncO1StringSearcher(){
        _mapper = new TrieNodeAndFilepathMapper();
        _nodeReader = new TrieNodeReader();
        _rawDataReader = new RawDataReader();
        _nodeWriter = TrieNodeWriter.getInstance();
        _rawDataWriter = RawDataWriter.getInstance();
    }
    public static AsyncO1StringSearcher getInstance() {
        return Optional.ofNullable(_this).orElseGet(() -> { _this = new AsyncO1StringSearcher(); return _this; });
    }

    public void setStoragePath(String storagePath) {
        _storagePath = storagePath;
        _mapper.setStoragePath(_storagePath + "/trie");
        _rawDataWriter.setDataFilepath(_storagePath + "/rawData.txt");
        _rawDataReader.setDataFilepath(_storagePath + "/rawData.txt");
    }
    public String getStoragePath() { return _storagePath; }
    public void insertString(String toInsert) {
        String dataID = _rawDataWriter.write(toInsert);
        List<String> toInsertSlices = new ArrayList();
        for (int i = 0; i < toInsert.length(); i++) {
            toInsertSlices.add(toInsert.substring(i));
        }
        toInsertSlices.forEach(slice -> {
            for (int i = slice.length(); i > 0; i--) {
                String sliceOfSlice = slice.substring(0, i);
                ITrieNode node = new TrieNode();
                node.init(sliceOfSlice, _mapper);
                _nodeWriter.write(node, dataID);
            }
        });
    }
    public List<String> searchString(String searchStr) {
        ITrieNode nodeWithSearchResult = new TrieNode();
        nodeWithSearchResult.init(searchStr, _mapper);
        List<String> rawDataIDs = _nodeReader.read(nodeWithSearchResult);
        List<String> result = new ArrayList<String>();
        rawDataIDs.forEach(ID -> {
            result.add(_rawDataReader.read(ID));
        });
        return result;
    }
}
