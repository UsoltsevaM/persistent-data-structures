package NSU13222;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PersistentHashMapTest extends TestCase {

    public void testInsert() throws Exception {
        PersistentHashMap map = new PersistentHashMap();
        PersistentHashMap map1 = map.insert(0, "some string");

        assert (map.valueAt(0) == null);
        assert (map1.valueAt(0).equals("some string") );
    }

    public void testRemove() throws Exception {
        //Test removal if map becomes not empty
        PersistentHashMap map = new PersistentHashMap();
        map = map.insert(0, "some string");
        map = map.insert(1, "some string 1");
        PersistentHashMap map1 = map.remove(0);

        assert (map.valueAt(0).equals("some string") );
        assert (map1.valueAt(0) == null);

        //Test removal if map becomes empty
        PersistentHashMap map2 = new PersistentHashMap();
        map2 = map2.insert(0, "some string");
        PersistentHashMap map3 = map.remove(0);

        assert (map2.valueAt(0).equals("some string") );
        assert (map3.valueAt(0) == null);
    }

    public void testContainsKey() throws Exception {
        //Test with single element Map
        Map<String , String> data = new HashMap<String, String>();
        data.put("1", "2");

        PersistentHashMap map1 = new PersistentHashMap(data);
        assert (map1.containsKey("1"));
        assert (! map1.containsKey(1));
        assert (! map1.containsKey("2"));

        PersistentHashMap map2 = new PersistentHashMap();
        assert  (! map2.containsKey(0));

        //Test with multi elements Map
        data.put("2", "some new string");
        data.put("3", "or another one");
        PersistentHashMap map3 = new PersistentHashMap(data);

        assert (map3.containsKey("1"));
        assert (map3.containsKey("2"));
        assert (map3.containsKey("3"));
        assert (! map3.containsKey("some new string"));
    }

    public void testValueAt() throws Exception {
        Map<String , String> data = new HashMap<String, String>();
        data.put("1", "2");
        data.put("2", "some new string");
        data.put("3", "or another one");
        PersistentHashMap map1 = new PersistentHashMap(data);

        assert (map1.valueAt("1") == "2");
        assert (map1.valueAt("2") == "some new string");
        assert (map1.valueAt("3") == "or another one");
        assert (map1.valueAt("4") == null);
    }

    public void testConstructorFromHasmap()
    {
        //Test with single element Map
        Map<String , String> data = new HashMap<String, String>();
        data.put("1", "2");

        PersistentHashMap map1 = new PersistentHashMap(data);
        assert (map1.valueAt("1").equals("2") );

        //Test with multi elements Map
        data.put("2", "some new string");
        data.put("3", "or another one");

        PersistentHashMap map2 = new PersistentHashMap(data);
        assert (map2.valueAt("1").equals("2") );
        assert (map2.valueAt("2").equals("some new string") );
        assert (map2.valueAt("3").equals("or another one") );
    }
}