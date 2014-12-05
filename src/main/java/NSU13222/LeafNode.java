package NSU13222;

public class LeafNode implements INode {
    final int hash;
    final Object key;
    final Object val;

    public LeafNode(int hash, Object key, Object val) {
        this.hash = hash;
        this.key = key;
        this.val = val;
    }

    public INode insert(int shift, int hash, Object key, Object val) {
        if(Utils.equals(key, this.key)) {
            if(val == this.val)
                return this;
            return new LeafNode(hash, key, val);
        }
        return BitmapIndexedNode.create(shift, this, hash, key, val);
    }

    public INode remove(int hash, Object key) {
        if(hash == this.hash && Utils.equals(key, this.key))
            return null;
        return this;
    }

    public LeafNode find(int hash, Object key){
        if(hash == this.hash && Utils.equals(key, this.key))
            return this;
        return null;
    }

    public int getHash() {
        return hash;
    }

    public Object key() {
        return this.key;
    }

    public Object val() {
        return this.val;
    }
}