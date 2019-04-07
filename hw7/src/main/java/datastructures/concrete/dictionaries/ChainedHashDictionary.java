package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
// import javafx.util.Pair;
import misc.exceptions.NoSuchKeyException;
//import misc.exceptions.NotYetImplementedException;

import java.util.Iterator;
import java.util.NoSuchElementException;
//import java.util.NoSuchElementException;

/**
 * @see IDictionary and the assignment page for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int capacity; // Initial counts for chains
    private int size; // tracks the number of key-value pairs

    private static final double LOAD_FACTOR = 0.5;


    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        this.capacity = 30;
        this.chains = makeArrayOfChains(capacity);
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int chainSize) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[chainSize];
    }

    // Helper method that converts hash code into a corresponding index.
    private int convertHashCode(K key) {
        if (key == null) {
            return 0;
        }
        int hashCode = Math.abs(key.hashCode()); // Must be positive
        return hashCode % capacity;
    }


    private double calculateLoadFactor() {
        return (double) this.size/this.capacity;
    }


    private void resize() {
        IDictionary<K, V>[] oldChains = this.chains;
        this.capacity *= 2;
        this.chains = makeArrayOfChains(capacity);
        int oldSize = this.size;
        for (int i = 0; i < oldChains.length; i++) {
            if (oldChains[i] != null) {
                for (KVPair<K, V> pair : oldChains[i]) {
                    put(pair.getKey(), pair.getValue());
                }
            }
        }
        size = oldSize;
    }

    /**
     * Returns the value corresponding to the given key, if the key exists in the map.
     *
     * If the key does *not* contain the given key, returns the default value.
     *
     * Note: This method does not modify the map in any way. The interface also
     *       provides a default implementation, but you may optionally override
     *       it with a more efficient version.
     */
    @Override
    public V getOrDefault(K key, V defaultValue) {
        int index = convertHashCode(key);
        IDictionary<K, V> currentChain = chains[index];
        if (currentChain == null) {
            return defaultValue;
        } else {
            return currentChain.getOrDefault(key, defaultValue);
        }
    }

    // Returns the value corresponding to the given key.
    @Override
    public V get(K key) {
        if (!this.containsKey(key)) { // Check if key exist
            throw new NoSuchKeyException("Key is not found");
        }
        int index = convertHashCode(key);
        IDictionary<K, V> currentChain = chains[index];
        return currentChain.get(key);
    }

    // Adds the key-value pair to the dictionary. If the key already exists in the dictionary,
    // replace its value with the given one.
    @Override
    public void put(K key, V value) {
        if (calculateLoadFactor() > LOAD_FACTOR) {
            resize();
        }
        int index = convertHashCode(key);
        if (chains[index] == null) { // create a new chain if this index is empty
            chains[index] = new ArrayDictionary<>();
        }
        if (!chains[index].containsKey(key)){
            size++;
        }
        chains[index].put(key, value);

    }

    // Pre : Throws NoSuchKeyException if the dictionary does not contain the given key.
    // Post: Remove the key-value pair corresponding to the given key from the dictionary.
    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            throw new NoSuchKeyException("Key not found");
        }
        int index = convertHashCode(key);
        size--;
        return chains[index].remove(key);
    }

    // Returns 'true' if the dictionary contains the given key and 'false' otherwise.
    @Override
    public boolean containsKey(K key) {
        int index = convertHashCode(key);
        // Access the chain, need to check if chain contains key
        if (chains[index] != null) {
            return chains[index].containsKey(key);
        }
        return false;
    }

    // Returns the number of key-value pairs stored in this dictionary.
    @Override
    public int size() {
        return this.size;
    }

    // Returns an iterator that, when used, will yield all key-value pairs
    // contained within this dictionary
    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be
     *    true once the constructor is done setting up the class AND
     *    must *always* be true both before and after you call any
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int chainIndex;
        private Iterator<KVPair<K, V>> itr;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            this.chainIndex = findChainIndex(-1);
            if (chainIndex != chains.length) {
                this.itr = this.chains[chainIndex].iterator();
            }
        }

        // Helper method that finds the next valid index in chains.
        private int findChainIndex(int currentIndex) {
            for (int i = currentIndex + 1; i < chains.length; i++) {
                if (chains[i] != null && chains[i].size() > 0) {
                    return i;
                }
            }
            return chains.length; // No more valid chains to iterate
        }

        // Return true if the iteration has more elements
        @Override
        public boolean hasNext() {
            if (itr != null && itr.hasNext()) { // Still iterating a chain
                return true;
            } else if (findChainIndex(chainIndex) < chains.length) { // Has a next valid chain to iterate
                chainIndex = findChainIndex(chainIndex);
                itr = chains[chainIndex].iterator(); // Update the iterator to the next valid chain
                return itr.hasNext();
            }
            return false;
        }

        // Pre :  throw NoSuchElementException if the iteration has no more elements.
        // Post: Returns the next element in the iteration.
        @Override
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return itr.next();
        }
    }
}
