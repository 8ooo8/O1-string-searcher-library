package github.eightoooeight.instantstringsearcher.trienode;

import github.eightoooeight.instantstringsearcher.trienode.ITrieNode;
import java.io.*;
import java.util.Optional;

public class TrieNodeWriter implements ITrieNodeWriter
{
    protected static TrieNodeWriter self = null;

    private TrieNodeWriter() {}

    public static TrieNodeWriter getInstance()
    {
        return Optional.ofNullable(self).orElseGet(() -> self = new TrieNodeWriter());
    }
 
    public synchronized void write(ITrieNode node, String toWrite)
    {
        try
        {
            File nodeFile = node.getNode();
            nodeFile.getParentFile().mkdirs();
            nodeFile.createNewFile(); //create a new file if not exists
            try ( BufferedWriter writer = new BufferedWriter(new FileWriter(nodeFile, true)); )
            {
                writer.write(toWrite + "\n");
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }
}
