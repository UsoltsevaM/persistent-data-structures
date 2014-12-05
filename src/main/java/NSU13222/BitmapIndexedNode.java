package NSU13222;

public class BitmapIndexedNode implements INode {
    final int bitmap;
    final INode[] nodes;
    final int shift;
    final int _hash;

    final int index(int bit) {
        return Integer.bitCount(bitmap & (bit - 1));
    }

    BitmapIndexedNode(int bitmap, INode[] nodes, int shift) {
        this.bitmap = bitmap;
        this.nodes = nodes;
        this.shift = shift;
        this._hash = nodes[0].getHash();
    }

    static INode create(int bitmap, INode[] nodes, int shift) {
        return new BitmapIndexedNode(bitmap, nodes, shift);
    }

    static INode create(int shift, INode branch, int hash, Object key, Object val) {
        return (new BitmapIndexedNode(Utils.bitpos(branch.getHash(), shift), new INode[]{branch}, shift))
                .insert(shift, hash, key, val);
    }

    public INode insert(int levelShift, int hash, Object key, Object val) {
        int bit = Utils.bitpos(hash, shift);
        int idx = index(bit);
        if((bitmap & bit) != 0)
        {
            INode n = nodes[idx].insert(shift + 5, hash, key, val);
            if(n == nodes[idx])
                return this;
            else {
                INode[] newnodes = nodes.clone();
                newnodes[idx] = n;
                return new BitmapIndexedNode(bitmap, newnodes, shift);
            }
        } else {
            INode[] newnodes = new INode[nodes.length + 1];
            System.arraycopy(nodes, 0, newnodes, 0, idx);
            newnodes[idx] = new LeafNode(hash, key, val);
            System.arraycopy(nodes, idx, newnodes, idx + 1, nodes.length - idx);
            return create(bitmap | bit, newnodes, shift);
        }
    }

    public INode remove(int hash, Object key) {
        int bit = Utils.bitpos(hash, shift);
        if((bitmap & bit) != 0) {
            int idx = index(bit);
            INode n = nodes[idx].remove(hash, key);
            if(n != nodes[idx]) {
                if(n == null) {
                    if(bitmap == bit)
                        return null;
                    INode[] newnodes = new INode[nodes.length - 1];
                    System.arraycopy(nodes, 0, newnodes, 0, idx);
                    System.arraycopy(nodes, idx + 1, newnodes, idx, nodes.length - (idx + 1));
                    return new BitmapIndexedNode(bitmap & ~bit, newnodes, shift);
                }
                INode[] newnodes = nodes.clone();
                newnodes[idx] = n;
                return new BitmapIndexedNode(bitmap, newnodes, shift);
            }
        }
        return this;
    }

    public LeafNode find(int hash, Object key) {
        int bit = Utils.bitpos(hash, shift);
        if((bitmap & bit) != 0) {
            return nodes[index(bit)].find(hash, key);
        }
        else
            return null;
    }

    public int getHash(){
        return _hash;
    }
}