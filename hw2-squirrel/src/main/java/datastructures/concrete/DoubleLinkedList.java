// CSE 373 HW2
// Charles TungFang and Leon Zhang
// Jan 25th 2019

package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;
//import misc.exceptions.NotYetImplementedException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods:
 * @see datastructures.interfaces.IList
 * (You should be able to control/command+click "IList" above to open the file from IntelliJ.)
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back = null;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.size = 0;
    }

    // Adds the given item to the end of the list. Updates the size
    @Override
    public void add(T item) {
        // Check if the input is valid (null or empty?)
        if (front == null) {
            this.front = new Node(item);
            back = front;
        } else {
            back.next = new Node(back, item, null);
            back = back.next;
        }
        size++;
    }

    // Pre : Throws EmptyContainerException if the container is empty and there is no element to remove.
    // Post: Removes and returns the item from the end of this IList.
    @Override
    public T remove() {
        if (front ==  null) {
            throw new EmptyContainerException();
        }
        T result = back.data;
        if (front != back) {
            back = back.prev;
            back.next = null;
        } else { // One element in list
            front = null;
            back = null;
        }
        size--;
        return result;
    }

    // Pre : Throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
    // Post: Returns the item located at the given index.
    @Override
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> result = skipTo(index);
        return result.data;
    }

    // Pre : Throws IndexOutOfBoundsException if the index < 0 or index >= this.size()
    // Post: Overwrites the element located at the given index with the new item.
    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException();
        }
        Node newNode;
        if (index == 0) {
            // front case
            // single node
            if (front == back) {
                front = new Node(null, item, null);
                back  = front;
            } else {
                // multiple nodes
                newNode = new Node(null, item, front.next);
                front = newNode;
                front.next.prev = front;
            }
        } else if (index == size - 1) { // end case
            // end case
            newNode  = new Node(back.prev, item, null);
            back = newNode;
            back.prev.next = back;
        } else {
            // middle case
            Node<T> cur = skipTo(index);
            newNode = new Node(cur.prev, item, cur.next);
            cur.prev.next = newNode;
            cur.next.prev = newNode;
        }
    }

    // Helper Method
    // Pre: Accepts an index. Index must be within the range of list.
    // Post:  Create a node reference to the node at that index
    private Node<T> skipTo(int index) {
        if (index < size/2) {
            Node<T> current = front;
            for (int i = 0; i < index; i++) {
                current = current.next;
            }
            return current;
        } else {
            Node<T> current = back;
            for (int i = size-1; i > index; i--) {
                current = current.prev;
            }
            return current;
        }
    }

    // Pre: Accepts an index and an item. Index must be >= 0 or < size + 1; otherwise,
    //      throws IndexOutOfBoundsException.
    // Post: Inserts the given item at the given index. Updates the size.
    @Override
    public void insert(int index, T item) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException();
        }
        Node<T> temp = new Node(item);
        // Case 1: the list is empty, must insert at 0 or 1
        // Case 2: the list is not empty, insert at end
        if (front == null || index == size) {
            add(item);
        } else if (index == 0) { // Case 3: the list is not empty, but insert at 0
            temp.next = front;
            front.prev = temp;
            front = temp;
            size++;
        } else { // Case 4: the list is not empty, insert in the middle
            Node<T> current = skipTo(index);
            temp.next = current;
            temp.prev =  current.prev;
            current.prev = temp;
            temp.prev.next = temp;
            size++;
        }

    }

    // Pre: Accepts an index. Index must be >= 0 or < size, otherwise throws
    //      IndexOutOfBoundsException.
    // Post: Deletes the item at the given index. Returns the data of deleted
    //       item. Updates the size.
    @Override
    public T delete(int index) {
        if (index < 0 || index >= size) { // Guarantees the list is null or empty
            throw new IndexOutOfBoundsException();
        }
        T result;
        // Case 1: Only one element in list
        // Case 2: deletes back
        if (this.size == 1 || index == this.size - 1) {
            result = remove();
        } else if (index == 0) { // Case 2: deletes front when list is not empty
            result = front.data;
            front = front.next;
            front.prev = null;
            size--;
        } else { // Case 4; deletes middle
            Node<T> del = skipTo(index);
            result = del.data;
            Node<T> previous = del.prev;
            previous.next = del.next;
            del.next.prev = previous;
            size--;
        }
        return result;
    }

    // Pre: Accepts a item of the corresponding type
    // Post: Returns the index corresponding to the first occurrence of the given
    // item. Returns -1 if the item is not found.
    @Override
    public int indexOf(T item) {
         if (front != null) {
             Node<T> current =  this.front;
             int index = 0;
             while (current.next != null && current.data != item) {
                 index++;
                 current = current.next;
             }
             if (item == null && current.data == null || current.data.equals(item)) {
                  return index;
             }
         }
        return -1;
    }

    // Post: Returns the number of elements in the container.
    @Override
    public int size() {
        return this.size;
    }

    // Post: Returns 'true' if this container contains the given element, and 'false' otherwise.
    @Override
    public boolean contains(T other) {
        return indexOf(other) != -1;
    }

    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T result = current.data; // current.data or current.next.data ???
            current = current.next;
            return result;
        }
    }
}
