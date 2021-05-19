package github.eightoooeight.instantstringsearcher.trienode;

import github.eightoooeight.instantstringsearcher.trienode.ITrieNodeAndFilepathMapper;
import java.io.File;

public interface ITrieNode {
    public void init(String nodeKeychain, ITrieNodeAndFilepathMapper mapper);
    public File getNode();
}
