package datastructures.concrete.dictionaries;

import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;

//import javax.xml.bind.annotation.XmlType;

/**
 * @see datastructures.interfaces.IDictionary
 */
public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field.
    // We will be inspecting it in our private tests.
    private Pair<K, V>[] pairs;

    // You may add extra fields or helper methods though!
    private int size;

    private static final int DEFAULT_CAPACITY = 87;

    public ArrayDictionary() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayDictionary(int arraySize) {
        if (arraySize <= 0) {
            throw new IllegalArgumentException();
        }
        pairs = this.makeArrayOfPairs(arraySize);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain Pair<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        // It turns out that creating arrays of generic objects in Java
        // is complicated due to something known as 'type erasure'.
        //
        // We've given you this helper method to help simplify this part of
        // your assignment. Use this helper method as appropriate when
        // implementing the rest of this class.
        //
        // You are not required to understand how this method works, what
        // type erasure is, or how arrays and generics interact. Do not
        // modify this method in any way.
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    @Override
    // Pre : throws NoSuchKeyException if the dictionary does not contain the given key
    // Post: Returns the value corresponding to the given key.
    public V get(K key) {
        if (!containsKey(key)) {
            throw new NoSuchKeyException();
        }
        return this.pairs[getKeyIndex(key)].value;
    }

    // Return the index position of the given key.
    // Return -1 if the dictionary doesn't contain the given key.
    private int getKeyIndex(K key) {
        for (int i = 0; i < size; i++) {
            if (this.pairs[i].key == key || (this.pairs[i].key != null && this.pairs[i].key.equals(key))) {
                    return i;
            }
        }
        return -1;
    }

    // Adds the key-value pair to the dictionary. If the key already exists in the dictionary,
    // replace its value with the given one.
    @Override
    public void put(K key, V value) {
        if (containsKey(key)) {
            this.pairs[getKeyIndex(key)].value = value;
        } else {
            if (this.size == this.pairs.length) {
                Pair<K, V>[] newPairs = makeArrayOfPairs(size*2);
                for (int i = 0; i < size; i++) {
                    newPairs[i] = this.pairs[i];
                }
                this.pairs = newPairs;
            }
            this.pairs[size] = new Pair<K, V>(key, value);
            size++;
        }
    }


    // Pre : Throws NoSuchKeyException if the dictionary does not contain the given key.
    // Post: Remove and return the key-value pair corresponding to the given key from the dictionary.
    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            throw new NoSuchKeyException();
        }
        int removedIndex = getKeyIndex(key);
        V removedVal = this.pairs[removedIndex].value;
        pairs[removedIndex] = pairs[size - 1];
        pairs[size - 1] = null;
        size--;
        return removedVal;
    }

    // Returns 'true' if the dictionary contains the given key and 'false' otherwise.
    @Override
    public boolean containsKey(K key) {
        return getKeyIndex(key) != -1;
    }

    // Returns the number of key-value pairs stored in this dictionary.
    @Override
    public int size() {
        return size;
    }

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
