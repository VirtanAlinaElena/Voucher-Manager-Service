import java.util.*;

public class ArrayMap<K, V> extends AbstractMap<K, V> {
    private ArrayList<ArrayMapEntry<K, V>> map;

    public ArrayMap() {
        map = new ArrayList<>();
    }

    public V put(K key, V value) {
        ArrayMapEntry<K, V> entry = null;
        int i;

        for (i = 0; i < map.size(); i++) {
            entry = map.get(i);
            if (key.equals(entry.getKey())) {
                break;
            }
        }

        // if the key is equal to one of the keys in the Array Map, just update the value; else, add the entry
        V oldValue = null;
        if (i < map.size()) {
            oldValue = entry.getValue();
            entry.setValue(value);
            map.add(i, entry);
        } else {
            map.add(new ArrayMapEntry<>(key, value));
        }
        return oldValue;
    }

    public boolean containsKey(Object key) {
        for (ArrayMapEntry<K, V> entry : map) {
            if (key.equals(entry.getKey())) {
                return true;
            }
        }
        return false;
    }

    public V get(Object key) {
        for (ArrayMapEntry<K, V> entry : map) {
            if (key.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public int size() {
        return map.size();
    }

    public Set<Entry<K, V>> entrySet() {
        return new HashSet<>(map);
    }


    class ArrayMapEntry<K, V> implements Map.Entry<K, V> {
        private K key;
        private V value;

        public ArrayMapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
                this.value = value;
                return value;
        }

        public String toString() {
            return "Cheia: " + this.getKey() + ", Valoarea: " + this.getValue();
        }

        public boolean equals(Object o) {
            if (o instanceof ArrayMapEntry) {
                ArrayMapEntry<K, V> entry = (ArrayMapEntry<K, V>) o;
                return ((entry.getKey()== null ? this.getKey()== null : this.getKey().equals(entry.getKey()))
                    && (this.getValue()== null ? entry.getValue()== null : this.getValue().equals(entry.getValue())));
            } else {
                return false;
            }
        }

        public int hashCode() {
            int keyHash = (this.getKey() == null ? 0 : this.getKey().hashCode());
            int valueHash = (this.getValue() == null ? 0 : this.getValue().hashCode());
            return keyHash ^ valueHash;
        }
    }
}