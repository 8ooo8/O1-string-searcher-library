package github.eightoooeight.instantstringsearcher.trienode;

import github.eightoooeight.instantstringsearcher.trienode.ITrieNode;
import java.util.*;
import java.io.*;
import java.util.Optional;

public class TrieNodeReader implements ITrieNodeReader{
    public List<String> read(ITrieNode node) {
        List<String> result = new ArrayList<String>();
        File nodeFile = node.getNode();
        try ( BufferedReader reader = new BufferedReader(new FileReader(nodeFile)); ) {
            String line;
            while ((line = reader.readLine()) != null) {
                Optional.of(line).filter(l -> !l.isEmpty()).ifPresent(l -> result.add(l));
            }
        } finally {
            return result;
        }
    }
}
