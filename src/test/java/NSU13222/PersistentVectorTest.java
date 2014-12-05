package NSU13222;

import junit.framework.TestCase;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class PersistentVectorTest extends TestCase {

    public void testConstructorFromList() throws Exception {
        List<String> data = new ArrayList<String>();
        data.add("abc");
        PersistentVector vector1 = new PersistentVector(data);

        assert (vector1.size() == 1);
        assert (vector1.valueAt(0) == "abc");

        data.add("def");
        data.add("xyz");
        PersistentVector vector2 = new PersistentVector(data);

        assert (vector2.size() == 3);
        assert (vector2.valueAt(0) == "abc");
        assert (vector2.valueAt(1) == "def");
        assert (vector2.valueAt(2) == "xyz");
    }

    public void testIndexOutOfBound() throws Exception {
        boolean thrown = false;
        PersistentVector vector1 = new PersistentVector("123", "456");
        try
        {
            String test = (String) vector1.valueAt(-1);
        } catch (IndexOutOfBoundsException e)
        {
            thrown = true;
        }
        assert (thrown);

        thrown = false;
        try
        {
            String test = (String) vector1.valueAt(2);
        } catch (IndexOutOfBoundsException e)
        {
            thrown = true;
        }
        assert (thrown);
    }

    public void testConstructorFromMany() throws Exception {
        PersistentVector vector1 = new PersistentVector("abc");

        assert (vector1.size() == 1);
        assert (vector1.valueAt(0) == "abc");

        PersistentVector vector2 = new PersistentVector("abc", "def", "xyz");

        assert (vector2.size() == 3);
        assert (vector2.valueAt(0) == "abc");
        assert (vector2.valueAt(1) == "def");
        assert (vector2.valueAt(2) == "xyz");
    }

    public void testValueAt() throws Exception {
        //already covered
    }

    public void testAdd() throws Exception {
        PersistentVector vector1 = new PersistentVector();
        PersistentVector vector2 = vector1.add("abc");

        assert (vector1.size() == 0);
        assert (vector2.size() == 1);
        assert (vector2.valueAt(0) == "abc");

        PersistentVector vector3 = new PersistentVector("abc", "def");
        PersistentVector vector4 = vector3.add("xyz");

        assert (vector3.size() == 2);
        assert (vector4.size() == 3);
        assert (vector4.valueAt(0) == "abc");
        assert (vector4.valueAt(1) == "def");
        assert (vector4.valueAt(2) == "xyz");
    }

    public void testSet() throws Exception {
        PersistentVector vector1 = new PersistentVector();
        PersistentVector vector2 = vector1.set(0, "abc");

        assert (vector1.size() == 0);
        assert (vector2.size() == 1);
        assert (vector2.valueAt(0) == "abc");

        PersistentVector vector3 = new PersistentVector("abc", "def");
        PersistentVector vector4 = vector3.set(0, "xyz");

        assert (vector3.size() == 2);
        assert (vector3.valueAt(0) == "abc");
        assert (vector3.valueAt(1) == "def");
        assert (vector4.size() == 2);
        assert (vector4.valueAt(0) == "xyz");
        assert (vector4.valueAt(1) == "def");
    }

    public void testSize() throws Exception {
        //already covered
    }

    public void testEmpty() throws Exception {
        PersistentVector vector1 = new PersistentVector();

        assert (vector1.size() == 0);

        PersistentVector vector2 = new PersistentVector("abc", "xyz");
        PersistentVector vector3 = vector2.empty();

        assert (vector2.size() == 2);
        assert (vector3.size() == 0);
    }

    public void testPop() throws Exception {

    }
}