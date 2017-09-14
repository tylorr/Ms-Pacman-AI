package game.core;

import java.util.HashMap;

class KeyPair<K1, K2>
{
    private K1 key1;
    private K2 key2;

    public KeyPair(K1 _key1, K2 _key2)
    {
        key1 = _key1;
        key2 = _key2;
    }

    public K1 key1()
    {
        return key1;
    }

    public K2 key2()
    {
        return key2;
    }

    public int hashCode()
    {
        return key1.hashCode() ^ key2.hashCode();
    }

    public boolean equals(Object o)
    {
        if (this == o)
            return true;

        if (o == null || getClass() != o.getClass())
            return false;

        KeyPair<K1, K2> rhs = (KeyPair<K1,K2>) o;

        return rhs.key1 == key1 && rhs.key2 == key2;
    }
}

public class DualMap<K1, K2, V> extends HashMap<KeyPair<K1,K2>, V>
{
    public DualMap()
    {
        super();
    }

    public V get(K1 key1, K2 key2)
    {
        return super.get(new KeyPair<K1,K2>(key1, key2));
    }

    public void put(K1 key1, K2 key2, V value)
    {
        super.put(new KeyPair<K1,K2>(key1, key2), value);
    }
}
