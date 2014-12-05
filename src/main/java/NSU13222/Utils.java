package NSU13222;

/**
 * Created by knyaz_000 on 12/2/2014.
 */
public class Utils {
    public static int hash(Object o) {
        if(o == null)
            return 0;
        return o.hashCode();
    }

    public static int mask(int hash, int shift){
        return (hash >>> shift) & 0x01f;
    }

    public static int bitpos(int hash, int shift) {
        return 1 << mask(hash, shift);
    }

    static public boolean equals(Object k1, Object k2) {
        if(k1 == k2)
            return true;
        return ((k1 != null) && k1.equals(k2));
    }
}
