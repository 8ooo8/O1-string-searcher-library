package github.eightoooeight.instantstringsearcher.trienode;

public class TrieNodeAndFilepathMapper implements ITrieNodeAndFilepathMapper{
    protected String _storagePath;
    public void setStoragePath(String storagePath) { _storagePath = storagePath; }
    public String getNodeFilepath(String nodeKeychain) { return _storagePath + "/" + nodeKeychain + ".txt"; }
}
