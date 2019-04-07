package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Sorter;
import org.junit.Test;
import static org.junit.Assert.fail;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSorterFunctionality extends BaseTest {
    @Test(timeout=SECOND)
    public void testSimpleUsage() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(5, list);
        assertEquals(5, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(15 + i, top.get(i));
        }
    }

    @Test(timeout=SECOND)
    public void testReverseOrder() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 50; i > 0; i--) {
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(10, list);
        assertEquals(10, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(41 + i, top.get(i));
        }
    }

    // Test if input greater than input size works
    @Test(timeout=SECOND)
    public void testKGreaterThanInputSize() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(25, list);
        assertEquals(20, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }

    // Test if k equals to input size works
    @Test(timeout=SECOND)
    public void testKEqualsInputSize() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(20, list);
        assertEquals(20, top.size());
        for (int i = 0; i < top.size(); i++) {
            assertEquals(i, top.get(i));
        }
    }

    // Test if sorter works for k is 0
    @Test(timeout=SECOND)
    public void testKIsZero() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(0, list);
        assertEquals(0, top.size());
        assertEquals(false, top == null);
    }

    // Test if sorter works for k is 1
    @Test(timeout=SECOND)
    public void testKIsOne() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(1, list);
        assertEquals(1, top.size());
        assertEquals(19, top.get(0));
        assertEquals(false, top == null);
    }

    @Test(timeout=SECOND)
    public void testInputIsZero() {
        IList<Integer> list = new DoubleLinkedList<>();
        IList<Integer> top1 = Sorter.topKSort(0, list);
        assertEquals(0, top1.size());
        IList<Integer> top2 = Sorter.topKSort(1, list);
        assertEquals(0, top2.size());
    }

    @Test(timeout=SECOND)
    public void testInputIsOne() {
        IList<Integer> list = new DoubleLinkedList<>();
        list.add(1);
        IList<Integer> top1 = Sorter.topKSort(1, list);
        assertEquals(1, top1.size());
        assertEquals(1, top1.get(0));
        IList<Integer> top2 = Sorter.topKSort(1, list);
        assertEquals(1, top2.size());
        assertEquals(1, top2.get(0));
    }

    // Test the KSort is not modifying the original input list
    @Test(timeout=SECOND)
    public void testListIsMaintained() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        Sorter.topKSort(15, list);
        for (int i = 0; i < 20; i++) {
            assertEquals(i, list.get(i));
        }
    }

    // Test inserting multiple duplicate int into sorter
    @Test(timeout = SECOND)
    public void testDuplicates() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(1);
        }
        for (int i = 0; i < 20; i++) {
            list.add(2);
        }
        IList<Integer> sorted = Sorter.topKSort(40, list);
        for (int i = 0; i < sorted.size()/2; i++) {
            assertEquals(1, sorted.get(i));
        }
        for (int i = 20; i < sorted.size()/2; i++) {
            assertEquals(2, sorted.get(i));
        }
    }

    // Test if sorter throws exception when input k is negative
    @Test(timeout=SECOND)
    public void testExceptionNegativeK() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        try {
            Sorter.topKSort(-1, list);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException ex) {
            // Do nothing
        }
    }

    // Test if sorter throws exception when input is null
    @Test(timeout=SECOND)
    public void testExceptionNullInput() {
        try {
            IList<Integer> top = Sorter.topKSort(-1, null);
        } catch (IllegalArgumentException ex) {
            // Do nothing
        }
    }

    // Test if work for other type other than int
    @Test(timeout=SECOND)
    public void testOtherType() {
        IList<String> list = new DoubleLinkedList<>();
        list.add("b");
        list.add("a");
        list.add("c");
        IList<String> top = Sorter.topKSort(3, list);
        assertEquals("a", top.get(0));
        assertEquals("b", top.get(1));
        assertEquals("c", top.get(2));
    }
}
