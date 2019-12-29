import java.util.*;

public class ArrayMap<K, V> extends AbstractMap<K, V> {

    static class ArrayMapEntry<K, V> implements Map.Entry<K, V> {
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
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            ArrayMap.ArrayMapEntry<K, V> entry = (ArrayMap.ArrayMapEntry<K, V>) o;
            return ((entry.getKey()== null ? this.getKey()== null : this.getKey().equals(entry.getKey()))
                    && (this.getValue()== null ? entry.getValue()== null : this.getValue().equals(entry.getValue())));
        }

        public int hashCode() {
            int keyHash = (this.getKey() == null ? 0 : this.getKey().hashCode());
            int valueHash = (this.getValue() == null ? 0 : this.getValue().hashCode());
            return keyHash ^ valueHash;
        }
    }

    private ArrayList<ArrayMap.ArrayMapEntry<K, V>> list;

    public ArrayMap() {
        list = new ArrayList<>();
    }

    public V put(K key, V value) {
        ArrayMap.ArrayMapEntry<K, V> entry = null;
        int i;

        if (key == null) {
            for (i = 0; i < list.size(); i++) {
                entry = list.get(i);
                if (entry.getKey() == null) {
                    break;
                }
            }
        } else {
            for (i = 0; i < list.size(); i++) {
                entry = list.get(i);
                if (key.equals(entry.getKey())) {
                    break;
                }
            }
        }

        // if the key is equal to one of the keys in the Array Map
        V oldValue = null;
        if (i < list.size()) {
            oldValue = entry.getValue();
            entry.setValue(value);
        } else {
            list.add(new ArrayMap.ArrayMapEntry<>(key, value));
        }
        return oldValue;
    }

    public boolean containsKey(Object key) {
        for (ArrayMap.ArrayMapEntry<K, V> entry : list) {
            if (key.equals(entry.getKey())) {
                return true;
            }
        }
        return false;
    }

    public V get(Object key) {
        for (ArrayMap.ArrayMapEntry<K, V> entry : list) {
            if (key.equals(entry.getKey())) {
                return entry.getValue();
            }
        }
        return null;
    }

    public int size() {
        return list.size();
    }

    public Set<Entry<K, V>> entrySet() {
        return new HashSet<>(list);
    }
}
