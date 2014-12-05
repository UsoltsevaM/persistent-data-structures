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


    public PersistentVector(Object... data) {
        for (Object item : data) {
            this.internalAdd(item);
        }
    }

    public PersistentVector(List data) {
        for (Object item : data) {
            this.internalAdd(item);
        }
    }

    public Object valueAt(int index) {
        Object[] node = nodeFor(index);
        return node[index & 0x01f];
    }

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

    public int size() {
        return count;
    }

    public PersistentVector empty()
    {
        return EMPTY;
    }

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
