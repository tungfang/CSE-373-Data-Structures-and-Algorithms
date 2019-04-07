package datastructures;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertTrue;

/**
 * This file should contain any tests that check and make sure your
 * delete method is efficient.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteStress extends TestDoubleLinkedList{
    private static final int DEFAULT_CAPACITY = 5000000;

    @Test(timeout=SECOND)
    public void testExample() {
        // Feel free to modify or delete this dummy test.
        assertTrue(true);
        assertEquals(3, 3);
    }

    @Test(timeout=10 * SECOND)
    public void testDeleteFromEndIsEfficient() {
        IList<Integer> list = createList(DEFAULT_CAPACITY);
        for (int i = DEFAULT_CAPACITY - 1; i >= 0; i--) {
            int deletedNum = list.delete(i);
            assertEquals(list.size(), deletedNum);
        }
        assertEquals(0, list.size());
    }

    @Test(timeout=10 * SECOND)
    public void testDeleteFromFrontIsEfficient() {
        IList<Integer> list = createList(DEFAULT_CAPACITY);
        for (int i = 0; i < DEFAULT_CAPACITY; i++) {
            int deletedNum = list.delete(0);
            assertEquals(i, deletedNum);
        }
        assertEquals(0, list.size());
    }

    @Test(timeout=10 * SECOND)
    public void testDeleteNearEndIsEfficient() {
        IList<Integer> list = createList(DEFAULT_CAPACITY);
        for (int i = DEFAULT_CAPACITY - 1; i >= 5; i--) {
            list.delete(i - 5);
        }
        assertEquals(5, list.size());
    }

    @Test(timeout=10 * SECOND)
    public void testDeleteNearFrontIsEfficient() {
        IList<Integer> list = createList(DEFAULT_CAPACITY);
        for (int i = 0; i < DEFAULT_CAPACITY / 2; i++) {
            list.delete(2);
        }
        assertEquals(DEFAULT_CAPACITY / 2, list.size());
    }

    // Helper Method
    // Creates a list with given size
    protected IList<Integer> createList(int size) {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < size; i++) {
            list.add(i);
        }
        return list;
    }
}
