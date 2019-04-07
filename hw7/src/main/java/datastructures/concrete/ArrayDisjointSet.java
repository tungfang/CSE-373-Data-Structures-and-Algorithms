package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;
//import misc.exceptions.NotYetImplementedException;

/**
 * @see IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {

    private static final int CAPACITY = 100;

    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;
    private int size;
    private IDictionary<T, Integer> pointerIndex; // keep track of the index of T in pointers

    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.

    public ArrayDisjointSet() {
        this.pointers = new int[CAPACITY];
        this.size = 0;
        this.pointerIndex = new ChainedHashDictionary<>();
    }

    // Helper method that checks if item is contained inside this disjoint set
    // Post: Returns true is disjoint set contains item. Returns false otherwise.
    private boolean contains(T item) {
        return pointerIndex.containsKey(item);
    }

    // Helper method that resize this disjoint set when full.
    private void resize() {
        int[] temp = new int[this.pointers.length*2]; // Create a new array with twice the capacity
        for (int i = 0; i < this.size; i++) { // Copy old array to new array
            temp[i] = pointers[i];
        }
        this.pointers = temp;
    }

    /**
     * Creates a new set containing just the given item.
     * The item is internally assigned an integer id (a 'representative').
     *
     * @throws IllegalArgumentException  if the item is already a part of this disjoint set somewhere
     */
    @Override
    public void makeSet(T item) {
        if (contains(item)) {
            throw new IllegalArgumentException("Item is already a part of this disjoint set");
        }
        if (this.size == this.pointers.length) { // Check if disjoint set is full
            resize(); // Resize disjoint set
        }
        this.size++;
        this.pointers[size-1] = -1; // Initial rank is -1
        this.pointerIndex.put(item, size-1); // Keep track of item and its index in pointers
    }

    /**
     * Returns the integer id (the 'representative') associated with the given item.
     *
     * @throws IllegalArgumentException  if the item is not contained inside this disjoint set
     */
    @Override
    public int findSet(T item) {
        if (!contains(item)) {
            throw new IllegalArgumentException("Item is not contained inside this disjoint set");
        }
        int itemIndex = this.pointerIndex.get(item);
        return findSet(itemIndex);
    }

    // Helper method for findSet
    // Recursively find the next index of item, until it reaches representative, while looking
    // for opportunities to path compress. Return the index of of the representative.
    private int findSet(int currentIndex) {
        int nextIndex = this.pointers[currentIndex]; // Need to look forward, because need to stop early
        if (nextIndex < 0) { // Base case: current item is a representative
            return currentIndex; // Return the index of representative
        } else { // Recursive case: current item is not a representative
            int repIndex = findSet(nextIndex); // Update currentIndex with nextIndex
            this.pointers[currentIndex] = repIndex; // Path Compress
            return repIndex;
        }
    }

    /**
     * Finds the two sets associated with the given items, and combines the two sets together.
     * Does nothing if both items are already in the same set.
     *
     * @throws IllegalArgumentException  if either item1 or item2 is not contained inside this disjoint set
     */
    @Override
    public void union(T item1, T item2) {
        if (!contains(item1) || !contains(item2)) {
            throw new IllegalArgumentException("Item is not contained inside this disjoint set");
        }
        int rep1Index = findSet(item1); // Index of item1's representative
        int rep2Index = findSet(item2); // Index of item2's representative
        int item1Rank = pointers[rep1Index]; // item1's set rank
        int item2Rank = pointers[rep2Index]; // item2's set rank
        if (item1Rank == item2Rank) {
            this.pointers[findSet(item2)] = rep1Index; // item2 representative points to item1's representative
            this.pointers[rep1Index]--; // Update the rank by 1 (minus 1)
        } else if (item1Rank < item2Rank) { // item1's rank is greater than item2's rank
            this.pointers[findSet(item2)] = rep1Index; // Change rank of item2 to item1's index
        } else { // item2's rank is greater than item1's rank
            this.pointers[findSet(item1)] = rep2Index; // Change rank of item1 to item2's index
        }
    }
}
