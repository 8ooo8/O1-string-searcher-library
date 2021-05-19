package github.eightoooeight.instantstringsearcher.trienode;

import github.eightoooeight.instantstringsearcher.trienode.ITrieNode;
import java.util.List;

public interface ITrieNodeReader {
    public List<String> read(ITrieNode node);
}
