package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import misc.BaseTest;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        assertEquals(0, heap.size());
        for (int i=1; i<=500; i++) {
            heap.insert(i);
            assertEquals(i, heap.size());
        }
        for (int i=500; i>0; i--) {
            assertEquals(i, heap.size());
            heap.removeMin();
        }
        assertEquals(0, heap.size());
    }

    @Test(timeout = SECOND)
    public void testResize() {
        IPriorityQueue<Integer> heap = makeInstance();
        int num = 31;
        for (int i = num; i >= 0; i--) {
            heap.insert(i);
            assertEquals(heap.peekMin(), i);
            assertEquals(heap.size(),  1 + num - i);
        }
    }

    @Test(timeout=SECOND)
    public void testBasicRemoveMin() {
        IPriorityQueue<Integer> heap = makeInstance();
        heap.insert(1);
        int result = heap.removeMin();
        assertEquals(1, result);
    }

    @Test(timeout = SECOND)
    public void testRandomRemoveMin() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(13);
        heap.insert(42);
        heap.insert(11);
        heap.insert(35);
        heap.insert(3);
        heap.insert(22);
        heap.insert(8);
        heap.insert(9);
        int res = heap.removeMin();
        assertEquals(3, res);
        assertEquals(8, heap.peekMin());
        assertTrue(!heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testRemoveMinMultiple() {
        IPriorityQueue<Integer> heap = makeInstance();
        for (int i = 0; i < 25; i++) {
            heap.insert(i);
        }
        for (int i = 0; i < heap.size(); i++) {
            int result = heap.removeMin();
            assertEquals(i, result);
        }
    }

    // Test if removeMin method throws exception
    @Test(timeout=SECOND)
    public void testRemoveMinException() {
        IPriorityQueue<Integer> heap = makeInstance();
        try {
            heap.removeMin();
            // We didn't throw an exception? Fail now.
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testBasicInsert() {
        IPriorityQueue<String> heap = makeInstance();
        IPriorityQueue<String> compare = makeInstance();
        // inorder insertion
        compare.insert("a");
        compare.insert("c");
        compare.insert("b");
        // reverse order insertion
        heap.insert("c");
        heap.insert("b");
        heap.insert("a");
        assertEquals(heap.peekMin(), compare.peekMin());
        assertEquals(3, heap.size());
        assertEquals(3, compare.size());

        while (heap.size() != 0) {
            String min = heap.removeMin();
            String actualMin = compare.removeMin();
            assertEquals(min, actualMin);
        }
    }

    @Test(timeout=SECOND)
    public void testInsertMultiple() {
        IPriorityQueue<Integer> heap = makeInstance();
        IPriorityQueue<Integer> compare = makeInstance();
        compare.insert(1);
        compare.insert(4);
        compare.insert(3);
        compare.insert(7);
        compare.insert(2);
        compare.insert(10);
        compare.insert(9);
        compare.insert(6);
        compare.insert(5);
        compare.insert(8);
        heap.insert(2);
        heap.insert(10);
        heap.insert(8);
        heap.insert(7);
        heap.insert(1);
        heap.insert(9);
        heap.insert(6);
        heap.insert(5);
        heap.insert(4);
        heap.insert(3);
        while (!heap.isEmpty()) {
            assertEquals(compare.removeMin(), heap.removeMin());
            assertEquals(compare.size(), heap.size());
        }
    }

    @Test(timeout=SECOND)
    public void testInsertDuplicate() {
        IPriorityQueue<Integer> heap = makeInstance();
        IPriorityQueue<Integer> compare = makeInstance();
        compare.insert(1);
        compare.insert(1);
        compare.insert(4);
        compare.insert(4);
        compare.insert(2);
        compare.insert(2);
        compare.insert(1);
        heap.insert(2);
        heap.insert(2);
        heap.insert(4);
        heap.insert(4);
        heap.insert(1);
        heap.insert(1);
        heap.insert(1);
        while (!heap.isEmpty()) {
            assertEquals(compare.removeMin(), heap.removeMin());
            assertEquals(compare.size(), heap.size());
        }
    }

    // Test if insert method throws exception
    @Test(timeout=SECOND)
    public void testInsertException() {
        IPriorityQueue<Integer> heap = makeInstance();
        try {
            heap.insert(null);
            // We didn't throw an exception? Fail now.
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException ex) {
            // Do nothing: this is ok
        }
    }

    // Try insert in descending order and check peekMin
    @Test(timeout=SECOND)
    public void testBasicPeekMin() {
        IPriorityQueue<Integer> heap = makeInstance();
        heap.insert(5);
        assertEquals(5, heap.peekMin());
        heap.insert(1);
        assertEquals(1, heap.peekMin());
    }


    // Try insert in descending order and check peekMin
    @Test(timeout=SECOND)
    public void testPeekMinMultiple() {
        IPriorityQueue<Integer> heap = makeInstance();
        for (int i = 500; i > 0; i--) {
            heap.insert(i);
            assertEquals(i, heap.peekMin());
        }
        heap.insert(-20);
        assertEquals(-20, heap.peekMin());
    }

    // Test if peekMin method throws exception
    @Test(timeout=SECOND)
    public void testPeekMinException() {
        IPriorityQueue<Integer> heap = makeInstance();
        try {
            heap.peekMin();
            // We didn't throw an exception? Fail now.
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException ex) {
            // Do nothing: this is ok
        }
    }

    @Test(timeout=SECOND)
    public void testHeapFundamental() {
        IPriorityQueue<Integer> heap = makeInstance();
        for (int i = 0; i < 7; i++) {
            heap.insert(i);
        }
        for (int i = 0; i < 72; i++) {
            heap.insert(i);
        }
        int min = heap.removeMin();
        while (heap.size() > 0) {
            int child = heap.removeMin();
            assertTrue(min <= child);
            min = child;
        }
    }

    @Test(timeout=SECOND)
    public void testIsEmpty() {
        IPriorityQueue<Integer> heap = makeInstance();
        assertTrue(heap.isEmpty());
        heap.insert(1);
        assertTrue(!heap.isEmpty());
        heap.insert(2);
        assertTrue(!heap.isEmpty());
        heap.removeMin();
        assertTrue(!heap.isEmpty());
        heap.removeMin();
        assertTrue(heap.isEmpty());
    }

    @Test(timeout=SECOND)
    public void testOtherType() {
        IPriorityQueue<String> heap = makeInstance();
        heap.insert("test");
        assertEquals("test", heap.removeMin());
    }

}
