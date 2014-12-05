package NSU13222;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class PersistentDLList {
    private int[] N;
    private Node[] first;
    private Node[] last;
    public int version;
    private int ar_size = 512;

    /**
     * <p>Constructor of list. Creates empty persistent list.</p>
     */
    public PersistentDLList() {
        first  = new Node[ar_size];
        last = new Node[ar_size];
        version = 0;
        N = new int[ar_size];
        N[0] = 0;
    }

    /**
     * <p>Constructor of persistent list. Creates new list which contains elements from given list.</p>
     * @param lst List which contains data.
     */
    public PersistentDLList (List lst) {
        first  = new Node[ar_size];
        last = new Node[ar_size];
        version = 1;
        N = new int[ar_size];
        // N[0] = 0;

        for(Iterator<Object> i = lst.iterator(); i.hasNext(); ) {
            addLastConstr(i.next());
        }
        N[version] = lst.size();
    }

    private void addLastConstr(Object item) {
        if(N[version] == 0) {
            last[version] = new Node(item, null, null, version);
            first[version] = last[version];
        }
        else {
            last[version].next[version] = new Node(item, null, last[version], version);
            last[version] = last[version].next[version];
        }
        N[version] = N[version] + 1;
    }

    private PersistentDLList(Node[] first, Node[] last, int version, int[] N) {
        this.first  = first;
        this.last = last;
        this.version = version;
        this.N = N;
        N[0] = 0;
    }

    private class Node {
        private final Object item;
        private Node[] next;
        private Node[] prev;

        public Node(Object item, Node n, Node p, int v) {
            this.item = item;
            next  = new Node[ar_size];
            prev = new Node[ar_size];
            this.next[v] = n;
            this.prev[v] = p;
        }
    }

    /**
     * <p>Compares two persistent lists.</p>
     * @param lst List to compare.
     * @return True if lists are equal.
     */
    public boolean equals(PersistentDLList lst) {
        if (lst.size() != this.size())
            return false;
        PersistentDLListIterator i = lst.iterator();
        PersistentDLListIterator i2 = this.iterator();
        boolean res = true;
        for(; i.hasNext() && i2.hasNext(); ) {
            if (!Utils.equals(i.next(), i2.next()))
                res = false;
        }
        return res;
    }

    /**
     * <p>Checks if list is empty.</p>
     * @return True if list is empty.
     */
    public boolean isEmpty() {
        return N[version] == 0;
    }

    /**
     * <p>Returns size of current list.</p>
     * @return size of list.
     */
    public int size() {
        return N[version];
    }

    /**
     * <p>Getter for current version of list.</p>
     * @return Version of list.
     */
    public int version() {
        return version;
    }

    /**
     * <p>Puts given item into the end of the list. Doesn't change base list and returns a new one.</p>
     * @param item Item which needed to place.
     * @return New persistent list with added value.
     */
    public PersistentDLList addLast(Object item) {
        if(N[version] == 0) {
            last[version + 1] = new Node(item, null, null, version + 1);
            first[version + 1] = last[version + 1];
        }
        else {
            int l_idx = getLastIndex(last, version);
            last[l_idx].next[version + 1] = new Node(item, null, last[l_idx], version + 1);
            last[version + 1] = last[l_idx].next[version + 1];
        }
        N[version + 1] = N[version] + 1;
        return new PersistentDLList(first, last, version + 1, N);
    }

    /**
     * <p>Puts given item into the given place of the list. Doesn't change base list and returns a new one.</p>
     * @param index Place where value will be stored.
     * @param item Object to store.
     * @return New persistent list with stored value.
     */
    public PersistentDLList add(int index, Object item) {
        if(N[version] == 0 || index > (N[version] - 1)) {
            return addLast(item);
        }
        if(index == 0) {
            int f_idx = getLastIndex(first, version);
            Node p = first[f_idx];
            first[version + 1] = new Node(item, p, null, version + 1);
            p.prev[version + 1] = first[version + 1];
            N[version + 1] = N[version] + 1;
            return new PersistentDLList(first, last, version + 1, N);
        }
        int f_idx = getLastIndex(first, version);
        Node pred = first[f_idx];
        for(int k = 1; k <= index - 1; k++) {
            int n_idx = getLastIndex(pred.next, version);
            pred = pred.next[n_idx];
        }
        int n_idx = getLastIndex(pred.next, version);
        Node nx = pred.next[n_idx];
        Node middle = new Node(item, nx, pred, version + 1);
        pred.next[version + 1] = middle;
        nx.prev[version + 1] = middle;
        N[version + 1] = N[version] + 1;
        return new PersistentDLList(first, last, version + 1, N);
    }

    private int getLastIndex(Node[] arr, int v) {
        for (int i = v; i > 0; i--) {
            if (arr[i] != null)
                return i;
        }
        return 0;
    }

    /**
     * <p>Removes last value from list. Doesn't change base list and creates new one with removed value.</p>
     * @return New persistent list with removed vlaue.
     */
    public PersistentDLList removeLast() {
        if (N[version] == 0) {
            return new PersistentDLList(first, last, version, N);
        }
        if(N[version] == 1) {
            first[version + 1] = last[version + 1] = null;
            N[version + 1] = N[version] - 1;
            return new PersistentDLList(first, last, version + 1, N);
        }
        else {
            int l_idx = getLastIndex(last, version);
            int p_idx = getLastIndex(last[l_idx].prev, version);
            last[l_idx].prev[p_idx].next[version + 1] = null;
            last[version + 1] = last[l_idx].prev[p_idx];
            N[version + 1] = N[version] - 1;
            return new PersistentDLList(first, last, version + 1, N);
        }
    }

    /**
     * <p>Removes value from list on the given position. Doesn't change base list and creates new one with removed value.</p>
     * @param index Place from which value will be removed.
     * @return New persistent list with removed value.
     */
    public PersistentDLList remove(int index) {
        if (index > N[version] - 1) {
            return new PersistentDLList(first, last, version, N);
        }
        if (index == N[version] - 1) {
            return removeLast();
        }
        int f_idx = getLastIndex(first, version);
        Node target = first[f_idx];
        for(int k = 1; k <= index; k++) {
            int t_idx = getLastIndex(target.next, version);
            target = target.next[t_idx];
        }
        int p_idx = getLastIndex(target.prev, version);
        int n_idx = getLastIndex(target.next, version);
        Node pred = target.prev[p_idx];
        Node nx = target.next[n_idx];
        if(pred == null)
            first[version + 1] = nx;
        else
            pred.next[version + 1] = nx;
        nx.prev[version + 1] = pred;
        N[version + 1] = N[version] - 1;
        return new PersistentDLList(first, last, version + 1, N);
    }

    public void print() {
        if (N[version] == 0)
            return;
        int f_idx = getLastIndex(first, version);
        Node target = first[f_idx];
        for (int i = 0; i < N[version]; i++) {
            System.out.println(target.item);
            int t_idx = getLastIndex(target.next, version);
            target = target.next[t_idx];
        }
    }

    public void printBackward() {
        if (N[version] == 0)
            return;
        int l_idx = getLastIndex(last, version);
        Node target = last[l_idx];
        for (int i = 0; i < N[version]; i++) {
            System.out.println(target.item);
            int t_idx = getLastIndex(target.prev, version);
            target = target.prev[t_idx];
        }
    }

    public PersistentDLListIterator iterator()  { return new PersistentDLListIterator(); }

    public class PersistentDLListIterator {
        private Node current;
        private Node lastAccessed = null;
        private int index = 0;

        public PersistentDLListIterator() {
            int f_idx = getLastIndex(first, version);
            current = first[f_idx];
        }

        public boolean hasNext()      { return index < N[version]; }
        public boolean hasPrevious()  { return index > 0; }
        public int previousIndex()    { return index - 1; }
        public int nextIndex()        { return index;     }

        public Object next() {
            if (!hasNext()) throw new NoSuchElementException();
            lastAccessed = current;
            Object item = current.item;
            int c_idx = getLastIndex(current.next, version);
            current = current.next[c_idx];
            index++;
            return item;
        }

        public Object previous() {
            if (!hasPrevious()) throw new NoSuchElementException();
            int c_idx = getLastIndex(current.prev, version);
            current = current.prev[c_idx];
            index--;
            lastAccessed = current;
            return current.item;
        }

        public PersistentDLList add(Object item) {
            if(index > N[version]) {
                throw new IndexOutOfBoundsException();
            }
            if(index == 0) {
                int f_idx = getLastIndex(first, version);
                Node p = first[f_idx];
                first[version + 1] = new Node(item, p, null, version + 1);
                p.prev[version + 1] = first[version + 1];
                N[version + 1] = N[version] + 1;
                return new PersistentDLList(first, last, version + 1, N);
            }
            int c_idx = getLastIndex(current.prev, version);
            Node x = current.prev[c_idx];
            Node z = current;
            Node y = new Node(item, z, x, version + 1);
            x.next[version + 1] = y;
            z.prev[version + 1] = y;
            N[version + 1] = N[version] + 1;
            return new PersistentDLList(first, last, version + 1, N);
        }

        public PersistentDLList remove() {
            if (lastAccessed == null) throw new IllegalStateException();
            if (index == N[version]) {
                return removeLast();
            }
            int lp_idx = getLastIndex(lastAccessed.prev, version);
            int ln_idx = getLastIndex(lastAccessed.next, version);
            Node pred = lastAccessed.prev[lp_idx];
            Node nx = lastAccessed.next[ln_idx];
            if (pred == null)
                first[version + 1] = nx;
            else
                pred.next[version + 1] = nx;
            nx.prev[version + 1] = pred;
            N[version + 1] = N[version] - 1;
            return new PersistentDLList(first, last, version + 1, N);
        }
    }
}