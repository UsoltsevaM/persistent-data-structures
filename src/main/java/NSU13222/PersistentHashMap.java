package NSU13222;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class PersistentHashMap {

    int count;
    INode root;

    final private static PersistentHashMap EMPTY = new PersistentHashMap(0, new EmptyNode());

    private PersistentHashMap(int count, INode root) {
        this.count = count;
        this.root = root;
    }

    /**
     * <p>Constructor of persistent hash map. Creates empty structure.</p>
     */
    public PersistentHashMap ()
    {
        this.count = 0;
        this.root = new EmptyNode();
    }

    /**
     * <p>Constucto of persistent hash map. Using data from Map to fill new persistent map.</p>
     * @param data Map of data.
     */
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

    /**
     * <p>Method which adds new pair key/value into hash map. Doesn't change base hash map and creates new one with added value.</p>
     * @param key Key on which new value will be stored.
     * @param val New value.
     * @return New hashmap with added element.
     */
    public PersistentHashMap insert(Object key, Object val) {
        INode newroot = root.insert(0, Utils.hash(key), key, val);
        if(newroot == root)
            return this;
        return new PersistentHashMap(count + 1, newroot);
    }

    /**
     * <p>Removes from hashmap pair key/value which stored with given key. Doesn't change base hash map and creates new one with added value.</p>
     * @param key Key where pair stored.
     * @return New hash map with removed value.
     */
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

    /**
     * <p>Checks if hash map contains given key.</p>
     * @param key Key to search.
     * @return True if hash map contains given key.
     */
    public boolean containsKey(Object key) {
        return valueAt(key) != null;
    }

    /**
     * <p>Returns object stored on key position. If there's no such key, returns null.</p>
     * @param key Key on which object is stored.
     * @return Object stored on given key.
     */
    public Object valueAt(Object key) {
        LeafNode node = root.find(Utils.hash(key), key);
        if(node != null)
        {
            return node.val();
        }
        return null;
    }

}
