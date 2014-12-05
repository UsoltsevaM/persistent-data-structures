package NSU13222;

public interface INode {
    INode insert(int shift, int hash, Object key, Object val);
    INode remove(int hash, Object key);
    LeafNode find(int hash, Object key);
    int getHash();
}