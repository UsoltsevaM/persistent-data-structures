package NSU13222;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PersistentHashMap {

    int count;
    INode root;

    final private static PersistentHashMap EMPTY = new PersistentHashMap(0, new EmptyNode());

    PersistentHashMap(int count, INode root) {
        this.count = count;
        this.root = root;
    }

    public PersistentHashMap ()
    {
        this.count = 0;
        this.root = new EmptyNode();
    }

    public PersistentHashMap (Map <?, ?> data)
    {
        this.count = data.size();
        this.root = new EmptyNode();

        if(this.count == 0)
        {
            return;
        }

        for (Map.Entry<?, ?> entry : data.entrySet())
        {
            root = root.insert(0, Utils.hash(entry.getKey()), entry.getKey(), entry.getValue());
        }
    }

    public PersistentHashMap insert(Object key, Object val) {
        INode newroot = root.insert(0, Utils.hash(key), key, val);
        if(newroot == root)
            return this;
        return new PersistentHashMap(count + 1, newroot);
    }

    public PersistentHashMap remove(Object key) {
        INode newroot = root.remove(Utils.hash(key), key);
        if(newroot == root)
            return this;
        if(newroot == null)
        {
            return EMPTY;
        }
        return new PersistentHashMap(count - 1, newroot);
    }

    public boolean containsKey(Object key) {
        return valueAt(key) != null;
    }

    public Object valueAt(Object key) {
        LeafNode node = root.find(Utils.hash(key), key);
        if(node != null)
        {
            return node.val();
        }
        return null;
    }

}
