package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
//import misc.exceptions.NotYetImplementedException;

/**
 * @see IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;

    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    private T[] heap;

    // Feel free to add more fields and constants.
    private int capacity;
    private int size;

    public ArrayHeap() {
        this.heap = makeArrayOfT(30);
        this.capacity = 30;
        this.size = 0;
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int arraySize) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[arraySize]);
    }

    /**
     * Removes and return the smallest element in the queue.
     *
     * If two elements within the queue are considered "equal"
     * according to their compareTo method, this method may break
     * the tie arbitrarily and return either one.
     *
     * @throws EmptyContainerException  if the queue is empty
     */
    @Override
    public T removeMin() {
        if (size == 0) {
            throw new EmptyContainerException("Queue is empty");
        }
        T result = heap[0]; // Saves the min, return at the end
        heap[0] = heap[size - 1]; // Replace last "node" with "root"
        heap[size - 1] = null; // Remove the last "node"
        this.size--; // Update size
        percolateDown(0, heap[0]); // Start percolation
        return result;
    }

    // Helper method for removeMin
    // Percolate down recursively
    private void percolateDown(int index, T value) {
        while (NUM_CHILDREN * index < size - 1) {
            int target = index * NUM_CHILDREN + 1; // the first left child of the node
            T smallest = heap[target]; // keep track of smallest one to be swapped later on
            for (int i = 2; i <= NUM_CHILDREN; i++) {
                int next = index * NUM_CHILDREN + i; // the rest of the child after the first left child
                // Only perform when there is more children to check and only update when find new smallest
                if (next < size && heap[next].compareTo(smallest) < 0) {
                    smallest = heap[next]; // update smallest
                    target = next; // update target spot to operate the swap
                }
            }
            if (smallest.compareTo(value) < 0) { // check if the smallest child is smaller than its parent
                heap[index] = smallest; // swap child to parent's location
                heap[target] = value; // swap parent to child's location
                index = target; // update index to check the rest that are unchecked
            } else {
                break;
            }
        }
    }

    /**
     * Returns, but does not remove, the smallest element in the queue.
     *
     * This method must break ties in the same way the removeMin
     * method breaks ties.
     *
     * @throws EmptyContainerException  if the queue is empty
     */
    public T peekMin() {
        if (size == 0) {
            throw new EmptyContainerException("Queue is empty");
        }
        return heap[0];
    }

    /**
     * Inserts the given item into the queue.
     *
     * @throws IllegalArgumentException  if the item is null
     */
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Item is null");
        }
        if (size == capacity) {
            resize();
        }
        heap[size] = item;
        size++;
        percolateUp(size - 1, item);
    }

    // Helper method for insert that percolates up
    private void percolateUp(int index, T item) {
        // There is more to do and child is smaller than parent
        while (index > 0 && item.compareTo(heap[(index - 1) / NUM_CHILDREN]) < 0) {
            int target = (index - 1) / NUM_CHILDREN;
            T parent = heap[target];
            heap[index] = parent; // Replace child with parent
            heap[target] = item;
            index = target;
        }
    }

    // Helper method that resize the heap by increasing its capacity by a factor (number of children per node)
    private void resize() {
        T[] result = makeArrayOfT(this.capacity*NUM_CHILDREN);
        for (int i = 0; i < size; i++) {
            result[i] = heap[i];
        }
        this.capacity *= NUM_CHILDREN;
        this.heap = result;
    }

    /**
     * Returns the number of elements contained within this queue.
     */
    @Override
    public int size() {
        return this.size;
    }
}
