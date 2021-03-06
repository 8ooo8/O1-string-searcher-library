package github.eightoooeight.instantstringsearcher.junittest.testcases;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.AfterClass;
import org.junit.Test;

import github.eightoooeight.instantstringsearcher.junittest.util.*;
import github.eightoooeight.instantstringsearcher.rawdata.*;
import github.eightoooeight.instantstringsearcher.trienode.*;
import java.io.File;
import java.nio.file.Paths;
import java.nio.file.Files;
import java.io.IOException;
import java.util.*;
import java.util.function.*;
import java.util.stream.*;

public class TrieNodeClassesTester extends TestCase
{
    protected static String trieDirPath;

    @Before
    public void setUp()
    {
        trieDirPath = "build/data/trie";
        FileDeleter.deleteDirIfExists(new File(trieDirPath));
    }

    @Test
    public void testTrieNodeAndFilepathMapper()
    {
        ITrieNodeAndFilepathMapper mapper = new TrieNodeAndFilepathMapper();
        mapper.setStoragePath(trieDirPath);
        assertEquals(mapper.getNodeFilepath("apple"), trieDirPath + "/apple.txt");
        assertEquals(mapper.getNodeFilepath("orange"), trieDirPath + "/orange.txt");
        assertEquals(mapper.getNodeFilepath("A and B"), trieDirPath + "/A and B.txt");
    }

    @Test
    public void testTrieNodeReaderWriter()
    {
        List<String> toWriteToANode1 = Arrays.asList(new String[]{"ID-1"});
        List<String> toWriteToANode2 = Arrays.asList(new String[]{"ID-10"});
        List<String> toWriteToABNode1 = Arrays.asList(new String[]{"ID-10"});

        ITrieNodeAndFilepathMapper mapper = new TrieNodeAndFilepathMapper();
        mapper.setStoragePath(trieDirPath);
        Function<String, ITrieNode> getNode = nodeKeychain -> {
            ITrieNode node = new TrieNode();
            node.init(nodeKeychain, mapper);
            return node;
        };
        
        toWriteToANode1.forEach(toWrite -> writeTrieNode(getNode, "A", toWrite));
        toWriteToANode2.forEach(toWrite -> writeTrieNode(getNode, "A", toWrite));
        toWriteToABNode1.forEach(toWrite -> writeTrieNode(getNode, "AB", toWrite));

        List<String> allANodeContent = Stream.concat(toWriteToANode1.stream(), toWriteToANode2.stream()).collect(Collectors.toList());
        List<String> contentReadFromANode = readTrieNode(getNode, "A");
        assertEquals(allANodeContent.size(), contentReadFromANode.size());
        for (int i = 0; i < allANodeContent.size(); i++)
        {
            assertEquals(allANodeContent.get(i), contentReadFromANode.get(i));
        }

        List<String> contentReadFromABNode = readTrieNode(getNode, "AB");
        assertEquals(toWriteToABNode1.size(), contentReadFromABNode.size());
        for (int i = 0; i < toWriteToABNode1.size(); i++)
        {
            assertEquals(toWriteToABNode1.get(i), contentReadFromABNode.get(i));
        }
    }

    private void writeTrieNode(Function<String, ITrieNode> getNode, String nodeKeychain, String toWrite)
    {
        ITrieNodeWriter nodeWriter = TrieNodeWriter.getInstance();
        ITrieNode node = getNode.apply(nodeKeychain);
        nodeWriter.write(node, toWrite);
    }

    private List<String> readTrieNode(Function<String, ITrieNode> getNode, String nodeKeychain)
    {
        ITrieNodeReader nodeReader = new TrieNodeReader();
        ITrieNode node = getNode.apply(nodeKeychain);
        return nodeReader.read(node);
    }
}
