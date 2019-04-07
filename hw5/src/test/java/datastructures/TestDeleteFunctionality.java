package datastructures;

import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

//import misc.exceptions.EmptyContainerException;

/**
 * This class should contain all the tests you implement to verify that
 * your 'delete' method behaves as specified.
 *
 * This test _extends_ your TestDoubleLinkedList class. This means that when
 * you run this test, not only will your tests run, all of the ones in
 * TestDoubleLinkedList will also run.
 *
 * This also means that you can use any helper methods defined within
 * TestDoubleLinkedList here. In particular, you may find using the
 * 'assertListMatches' and 'makeBasicList' helper methods to be useful.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDeleteFunctionality extends TestDoubleLinkedList {
    @Test(timeout=SECOND)
    public void testExample() {
        // Feel free to modify or delete this dummy test.
        assertTrue(true);
        assertEquals(3, 3);
    }

    @Test(timeout = SECOND)
    public void basicDeleteRandom() {
        IList<Integer> list = createList(10);
        assertEquals(5, list.delete(5));
        assertListMatches(new Integer[] {0, 1, 2, 3, 4, 6, 7, 8, 9}, list);
        assertEquals(0, list.delete(0));
        assertListMatches(new Integer[] {1, 2, 3, 4, 6, 7, 8, 9}, list);
        assertEquals(9, list.delete(7));
        list.add(15);
        list.insert(0, 77);
        assertEquals(15, list.delete(8));
        assertEquals(77, list.delete(0));
    }

    // Test middle front for non-empty list
    @Test(timeout=SECOND)
    public void basicTestDeleteMiddle() {
        IList<String> list = makeBasicList(); // Creates a list with a, b, c inside
        String result = list.delete(1); // Deletes b (second index)
        this.assertEquals("b", result);
        this.assertListMatches(new String[] {"a", "c"}, list);
    }

    // Test delete front for non-empty list
    @Test(timeout=SECOND)
    public void basicTestDeleteFront() {
        IList<String> list = makeBasicList(); // Creates a list with a, b, c inside
        String result = list.delete(0); // Deletes b (second index)
        this.assertEquals("a", result);
        this.assertListMatches(new String[] {"b", "c"}, list);
    }

    // Test delete back for non-empty list
    @Test(timeout=SECOND)
    public void basicTestDeleteBack() {
        IList<String> list = makeBasicList(); // Creates a list with a, b, c inside
        String result = list.delete(list.size()-1); // Deletes b (second index)
        this.assertEquals("c", result);
        this.assertListMatches(new String[] {"a", "b"}, list);
    }

    // Test delete single element list
    @Test(timeout=SECOND)
    public void testDeleteSingleElementList() {
        IList<String> list = new DoubleLinkedList<>();
        list.add("a"); // Creates a list with only one element
        String result = list.delete(0);
        this.assertEquals("a", result);
        this.assertEquals(0, list.size());
    }

    // Test delete many from front. Check if front is updated
    @Test(timeout=SECOND)
    public void testDeleteManyFromFront() {
        IList<Integer> list = createList(100);
        for (int i = 0; i < 100; i++) {
            this.assertEquals(i, list.delete(0)); // Checks if deleted correctly
            this.assertEquals(99-i, list.size()); // Check size is updated
        }
    }

    // Test delete many from back. Check if back is updated
    @Test(timeout=SECOND)
    public void testDeleteManyFromBack() {
        IList<Integer> list = createList(100);
        for (int i = 99; i >= 0; i--) {
            this.assertEquals(i, list.delete(list.size()-1)); // Checks if deleted correctly
            this.assertEquals(i, list.size()); // Check size is updated
        }
    }

    // Test delete many from middle
    @Test(timeout=SECOND)
    public void testDeleteManyFromMiddle() {
        IList<Integer> list = createList(100);
        for (int i = 0; i < 98; i++) {
            int deletedNum = list.delete(2);
            assertEquals(i+2, deletedNum);
            this.assertEquals(99-i, list.size()); // Check size is updated
        }
    }

    // Test IndexOutOfBoundsException when input is less than lower bound
    @Test(timeout=SECOND)
    public void testDeleteThrowsExceptionLowerBound() {
        IList<String> list = this.makeBasicList();
        try {
            list.delete(-1);
            // We didn't throw an exception? Fail now.
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
    }

    // Test IndexOutOfBoundsException when input is greater than upper bound
    @Test(timeout=SECOND)
    public void testDeleteThrowsExceptionUpperBound() {
        IList<String> list = this.makeBasicList();
        try {
            list.delete(list.size());
            // We didn't throw an exception? Fail now.
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
    }

    // Test IndexOutOfBoundsException delete when list is null
    @Test(timeout=SECOND)
    public void testDeleteThrowsExceptionNull() {
        IList<String> list = new DoubleLinkedList<>();
        try {
            list.delete(0);
            // We didn't throw an exception? Fail now.
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException ex) {
            // Do nothing: this is ok
        }
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