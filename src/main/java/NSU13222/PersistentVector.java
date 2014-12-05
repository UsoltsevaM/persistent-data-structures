package NSU13222;

import NSU13222.Box;

import java.util.List;

public class PersistentVector {
    private int count = 0;
    private int shift = 5;
    private Object[] root = new Object[]{};
    private Object[] tail = new Object[]{};

    private static PersistentVector EMPTY = new PersistentVector(0, 5, new Object[]{}, new Object[]{});

    private PersistentVector(int count, int shift, Object[] root, Object[] tail) {
        this.count = count;
        this.shift = shift;
        this.root = root;
        this.tail = tail;
    }

    /**
     * <p>Constructor of persistent vector. Takes list of objects separated by comma</p>
     * @param data Variable amount of arguments.
     */
    public PersistentVector(Object... data) {
        for (Object item : data) {
            this.internalAdd(item);
        }
    }

    /**
     * <p>Constructor of persistent vector. Takes list List<Object>.</p>
     * @param data List<object>
     */
    public PersistentVector(List data) {
        for (Object item : data) {
            this.internalAdd(item);
        }
    }

    /**
     * <p>Method gets object on the particular place.</p>
     * @param index position in vector.
     * @return Object stored on position.
     */
    public Object valueAt(int index) {
        Object[] node = nodeFor(index);
        return node[index & 0x01f];
    }

    /**
     * <p>Adds value into vector. It places value on last place and set it index to size() + 1.
     * It doesn't change base vector and returns new copy of vector.</p>
     * @param value Object to store.
     * @return new copy of vector with added value.
     */
    public PersistentVector add(Object value) {
        if (tail.length < 32) {
            Object[] newTail = new Object[tail.length + 1];
            System.arraycopy(tail, 0, newTail, 0, tail.length);
            newTail[tail.length] = value;
            return new PersistentVector(count + 1, shift, root, newTail);
        }
        Box expansion = new Box(null);
        Object[] newroot = pushTail(shift - 5, root, tail, expansion);
        int newshift = shift;
        if (expansion.val != null) {
            newroot = new Object[]{newroot, expansion.val};
            newshift += 5;
        }
        return new PersistentVector(count + 1, newshift, newroot, new Object[]{value});
    }

    /**
     * <p>Sets element on given position to given value. It doesn't change base vector and returns new copy of vector.
on     * If index == size(), increases length of return vector to 1.</p>
     * @param index Position where new value places
     * @param value Object to set in vector.
     * @return New copy of vector with new value on given position.
     */
    public PersistentVector set(int index, Object value) {
        if (index >= 0 && index < count) {
            if (index >= tailoff()) {
                Object[] newTail = new Object[tail.length];
                System.arraycopy(tail, 0, newTail, 0, tail.length);
                newTail[index & 0x01f] = value;

                return new PersistentVector(count, shift, root, newTail);
            }

            return new PersistentVector(count, shift, doSet(shift, root, index, value), tail);
        }
        if (index == count)
            return add(value);
        throw new IndexOutOfBoundsException();
    }

    /**
     * <p>Returns size of vector.</p>
     * @return Size of vector.
     */
    public int size() {
        return count;
    }

    /**
     * <p>Simple constructor which returns empty vector.</p>
     * @return New vector.
     */
    public PersistentVector empty()
    {
        return EMPTY;
    }

    /**
     * <p>Removed last element in vector and returns vector with size--. Doesn't change base vector and returns new copy.</p>
     * @return New vector without last element.
     */
    public PersistentVector pop(){
        if(count == 0)
            throw new IllegalStateException("Can't pop empty vector");
        if(count == 1)
            return EMPTY;
        if(tail.length > 1)
        {
            Object[] newTail = new Object[tail.length - 1];
            System.arraycopy(tail, 0, newTail, 0, newTail.length);
            return new PersistentVector(count - 1, shift, root, newTail);
        }
        Box ptail = new Box(null);
        Object[] newroot = popTail(shift - 5, root, ptail);
        int newshift = shift;
        if(newroot == null)
        {
            newroot = new Object[]{};
        }
        if(shift > 5 && newroot.length == 1)
        {
            newroot = (Object[]) newroot[0];
            newshift -= 5;
        }
        return new PersistentVector(count - 1, newshift, newroot, (Object[]) ptail.val);
    }

    //------------------------------------------------------------------------------------------------------------------
    //Private methods
    //------------------------------------------------------------------------------------------------------------------

    private Object[] popTail(int shift, Object[] array, Box ptail){
        if(shift > 0)
        {
            Object[] newchild = popTail(shift - 5, (Object[]) array[array.length - 1], ptail);
            if(newchild != null)
            {
                Object[] ret = array.clone();
                ret[array.length - 1] = newchild;
                return ret;
            }
        }
        if(shift == 0)
            ptail.val = array[array.length - 1];
        //contraction
        if(array.length == 1)
            return null;
        Object[] ret = new Object[array.length - 1];
        System.arraycopy(array, 0, ret, 0, ret.length);
        return ret;
    }

    private static Object[] doSet(int level, Object[] array, int i, Object value){
        Object[] ret = array.clone();
        if(level == 0)
        {
            ret[i & 0x01f] = value;
        }
        else
        {
            int subidx = (i >>> level) & 0x01f;
            ret[subidx] = doSet(level - 5, (Object[]) array[subidx], i, value);
        }
        return ret;
    }



    private int tailoff(){
        return count - tail.length;
    }

    private Object[] nodeFor(int i){
        if(i >= 0 && i < count)
        {
            if(i >= tailoff())
                return tail;
            Object[] arr = root;
            for(int level = shift; level > 0; level -= 5)
                arr = (Object[]) arr[(i >>> level) & 0x01f];
            return arr;
        }
        throw new IndexOutOfBoundsException();
    }

    private void internalAdd(Object item){
        if(tail.length < 32)
        {
            Object[] newTail = new Object[tail.length + 1];
            System.arraycopy(tail, 0, newTail, 0, tail.length);
            newTail[tail.length] = item;
            count++;
            tail = newTail;
            return;
        }
        Box expansion = new Box(null);
        Object[] newroot = pushTail(shift - 5, root, tail, expansion);
        int newshift = shift;
        if(expansion.val != null)
        {
            newroot = new Object[]{newroot, expansion.val};
            newshift += 5;
        }
        count++;
        shift = newshift;
        root = newroot;
        tail = new Object[]{item};
    }

    private Object[] pushTail(int level, Object[] arr, Object[] tailNode, Box expansion){
        Object newchild;
        if(level == 0)
        {
            newchild = tailNode;
        }
        else
        {
            newchild = pushTail(level - 5, (Object[]) arr[arr.length - 1], tailNode, expansion);
            if(expansion.val == null)
            {
                Object[] ret = arr.clone();
                ret[arr.length - 1] = newchild;
                return ret;
            }
            else
                newchild = expansion.val;
        }
        //expansion
        if(arr.length == 32)
        {
            expansion.val = new Object[]{newchild};
            return arr;
        }
        Object[] ret = new Object[arr.length + 1];
        System.arraycopy(arr, 0, ret, 0, arr.length);
        ret[arr.length] = newchild;
        expansion.val = null;
        return ret;
    }
}
