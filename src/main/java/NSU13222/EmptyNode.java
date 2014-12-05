package NSU13222;

public class EmptyNode implements INode {
    public INode insert(int shift, int hash, Object key, Object val) {
        INode ret = new LeafNode(hash, key, val);
        return ret;
    }
    public INode remove(int hash, Object key) { return this; }
    public LeafNode find(int hash, Object key) { return null; }
    public int getHash() {
        return 0;
    }
}