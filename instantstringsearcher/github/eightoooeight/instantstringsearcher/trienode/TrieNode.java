package github.eightoooeight.instantstringsearcher.trienode;

import github.eightoooeight.instantstringsearcher.trienode.ITrieNodeAndFilepathMapper;
import java.io.File;

public class TrieNode implements ITrieNode
{
    protected String nodeKeychain;
    protected ITrieNodeAndFilepathMapper mapper;

    public void init(String nodeKeychain, ITrieNodeAndFilepathMapper mapper)
    {
        this.nodeKeychain = nodeKeychain;
        this.mapper = mapper;
    }

    public File getNode()
    {
        return new File(mapper.getNodeFilepath(nodeKeychain));
    }
}
