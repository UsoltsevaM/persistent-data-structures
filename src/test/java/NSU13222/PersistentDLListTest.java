package NSU13222;

import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Iterator;

public class PersistentDLListTest extends TestCase {

    public void testFromJavaList() throws Exception {
        ArrayList<Object> ls = new ArrayList<Object>();
        ls.add(1);
        ls.add(2);
        ls.add(3);

        PersistentDLList list = new PersistentDLList();
        list = list.addLast(1);
        list = list.addLast(2);
        list = list.addLast(3);
        PersistentDLList list1 = new PersistentDLList(ls);
        assert (list.equals(list1));

        ArrayList<Object> ls1 = new ArrayList<Object>();
        ls1.add("aaa");
        ls1.add("bbb");
        ls1.add("ccc");

        PersistentDLList list2 = new PersistentDLList();
        list2 = list2.addLast("aaa");
        list2 = list2.addLast("bbb");
        list2 = list2.addLast("ccc");
        PersistentDLList list3 = new PersistentDLList(ls1);
        assert (list2.equals(list3));
    }

    public void testListSize() throws Exception {
        PersistentDLList list = new PersistentDLList();
        list = list.addLast(1);
        list = list.addLast(2);
        assert (list.size() == 2);
        list = list.removeLast();
        assert (list.size() == 1);
        list = list.removeLast();
        assert (list.size() == 0);
        list = list.removeLast();
        assert (list.size() == 0);
    }

    public void testAddLast() throws Exception {
        PersistentDLList list = new PersistentDLList();
        list = list.addLast(1);
        list = list.addLast(2);
        list = list.addLast(3);
        list = list.addLast(4);
        list = list.addLast(5);

        ArrayList<Object> ls = new ArrayList<Object>();
        ls.add(1);
        ls.add(2);
        ls.add(3);
        ls.add(4);
        ls.add(5);
        PersistentDLList list1 = new PersistentDLList(ls);
        assert (list.equals(list1));
    }

    public void testAddLast_1() throws Exception {
        PersistentDLList list = new PersistentDLList();
        list = list.addLast(1);
        list = list.addLast("aaa");
        list = list.addLast(3);
        list = list.addLast("bbb");
        list = list.addLast(5);

        ArrayList<Object> ls = new ArrayList<Object>();
        ls.add(1);
        ls.add("aaa");
        ls.add(3);
        ls.add("bbb");
        ls.add(5);
        PersistentDLList list1 = new PersistentDLList(ls);
        assert (list.equals(list1));
    }

    public void testRemove() throws Exception {
        PersistentDLList list = new PersistentDLList();
        list = list.addLast(1);
        list = list.addLast(2);
        list = list.addLast(3);
        list = list.addLast(4);
        list = list.addLast(5);
        list = list.remove(2);
        ArrayList<Object> ls = new ArrayList<Object>();
        ls.add(1);
        ls.add(2);
        ls.add(4);
        ls.add(5);
        PersistentDLList list1 = new PersistentDLList(ls);
        assert (list.equals(list1));
    }

    public void testRemoveLast() throws Exception {
        PersistentDLList list = new PersistentDLList();
        PersistentDLList list2;
        PersistentDLList list3;
        PersistentDLList list4;

        list = list.addLast(1);
        list = list.addLast(2);
        list = list.addLast(3);
        list = list.addLast(4);
        list2 = list.addLast(5);
        list3 = list2.removeLast();
        list4 = list2.removeLast();

        ArrayList<Object> ls = new ArrayList<Object>();
        ls.add(1);
        ls.add(2);
        ls.add(3);
        ls.add(4);
        PersistentDLList list1 = new PersistentDLList(ls);
        assert (list.equals(list1));
        assert (list3.equals(list1));
        assert (list4.equals(list1));
    }

    public void testAdd() throws Exception {
        ArrayList<Object> ls = new ArrayList<Object>();
        ls.add(1);
        ls.add(2);
        ls.add(3);
        ls.add(4);

        PersistentDLList list = new PersistentDLList(ls);
        list = list.add(0, 22);
        ls.add(0, 22);
        PersistentDLList list1 = new PersistentDLList(ls);
        assert (list.equals(list1));
    }

    public void testIteratorNextPrev() throws Exception {
        ArrayList<Object> ls = new ArrayList<Object>();
        ls.add(1);
        ls.add(2);
        ls.add(3);
        ls.add(4);
        PersistentDLList list = new PersistentDLList(ls);

        PersistentDLList.PersistentDLListIterator iterator = list.iterator();
        assert (iterator.next().equals(1));
        assert (iterator.next().equals(2));
        assert (iterator.next().equals(3));
        assert (iterator.previous().equals(3));
        assert (iterator.previous().equals(2));
        assert (iterator.previous().equals(1));
        assert (iterator.next().equals(1));

        Iterator<Object> it = ls.iterator();
        PersistentDLList.PersistentDLListIterator iterator2 = list.iterator();
        while (iterator2.hasNext()) {
        	assert (iterator2.next().equals(it.next()));
        }
    }

    public void testIteratorAdd() throws Exception {
        ArrayList<Object> ls = new ArrayList<Object>();
        ls.add(1);
        ls.add(2);
        ls.add(3);
        ls.add(4);
        PersistentDLList list = new PersistentDLList(ls);

        PersistentDLList.PersistentDLListIterator iterator = list.iterator();
        iterator.next();
        iterator.next();
        PersistentDLList list1 = iterator.add(10);

        ArrayList<Object> ls1 = new ArrayList<Object>();
        ls1.add(1);
        ls1.add(2);
        ls1.add(10);
        ls1.add(3);
        ls1.add(4);
        PersistentDLList list2 = new PersistentDLList(ls1);

        assert (list2.equals(list1));
    }

    public void testIteratorRemove() throws Exception {
        ArrayList<Object> ls = new ArrayList<Object>();
        ls.add(1);
        ls.add(2);
        ls.add(3);
        ls.add(4);
        PersistentDLList list = new PersistentDLList(ls);

        PersistentDLList.PersistentDLListIterator iterator = list.iterator();
        iterator.next();
        iterator.next();
        PersistentDLList list1 = iterator.remove();

        ArrayList<Object> ls1 = new ArrayList<Object>();
        ls1.add(1);
        ls1.add(3);
        ls1.add(4);
        PersistentDLList list2 = new PersistentDLList(ls1);

        assert (list2.equals(list1));
    }

    public void testVersions_1() throws Exception {
        PersistentDLList list = new PersistentDLList();
        list = list.addLast(1);
        list = list.addLast(2);
        list = list.addLast(3);
        list = list.addLast(4);
        list = list.addLast(5);
        PersistentDLList list2 = list.add(0, 11);
        list2 = list2.addLast(12);
        list2 = list2.addLast(13);
        PersistentDLList list3 = list2.removeLast();
        PersistentDLList list4 = list3.remove(0);
        PersistentDLList list5 = list4.remove(3);
        PersistentDLList list6 = list5.add(4, 100);

        ArrayList<Object> ls = new ArrayList<Object>();
        ls.add(1);
        ls.add(2);
        ls.add(3);
        ls.add(4);
        ls.add(5);
        PersistentDLList list_ = new PersistentDLList(ls);
        assert (list.equals(list_));
        ls.add(0, 11);
        ls.add(6, 12);
        ls.add(7, 13);
        list_ = new PersistentDLList(ls);
        assert (list2.equals(list_));
        ls.remove(7);
        list_ = new PersistentDLList(ls);
        assert (list3.equals(list_));
        ls.remove(0);
        list_ = new PersistentDLList(ls);
        assert (list4.equals(list_));
        ls.remove(3);
        list_ = new PersistentDLList(ls);
        assert (list5.equals(list_));
        ls.add(4, 100);
        list_ = new PersistentDLList(ls);
        assert (list6.equals(list_));
    }
}




