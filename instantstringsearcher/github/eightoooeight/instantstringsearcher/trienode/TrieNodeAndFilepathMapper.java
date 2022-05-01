package github.eightoooeight.instantstringsearcher.trienode;

public class TrieNodeAndFilepathMapper implements ITrieNodeAndFilepathMapper
{
    protected String storagePath;

    public void setStoragePath(String storagePath)
    {
        this.storagePath = storagePath;
    }

    public String getNodeFilepath(String nodeKeychain)
    {
        return storagePath + "/" + nodeKeychain + ".txt";
    }
}
