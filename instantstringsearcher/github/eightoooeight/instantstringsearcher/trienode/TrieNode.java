package github.eightoooeight.instantstringsearcher.trienode;

import github.eightoooeight.instantstringsearcher.trienode.ITrieNodeAndFilepathMapper;
import java.io.File;

public class TrieNode implements ITrieNode{
    protected String _nodeKeychain;
    protected ITrieNodeAndFilepathMapper _mapper;
    public void init(String nodeKeychain, ITrieNodeAndFilepathMapper mapper) {
        _nodeKeychain = nodeKeychain;
        _mapper = mapper;
    }
    public File getNode() { return new File(_mapper.getNodeFilepath(_nodeKeychain)); }
}
