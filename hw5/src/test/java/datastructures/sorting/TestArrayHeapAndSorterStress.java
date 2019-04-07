package datastructures.sorting;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;
import misc.Sorter;
import misc.BaseTest;
import org.junit.Test;
//import static org.junit.Assert.assertTrue;



/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapAndSorterStress extends BaseTest {

    @Test(timeout = 5 * SECOND)
    public void testInsertStress() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        for (int i = 5000000; i > 0; i--) {
            heap.insert(i);
        }
    }

    @Test(timeout = 5 * SECOND)
    public void testRemoveMinStress() {
        IPriorityQueue<Integer> heap = new ArrayHeap<>();
        for (int i = 5000000; i > 0; i--) {
            heap.insert(i);
        }
        for (int i = 5000000; i > 0; i--) {
            heap.removeMin();
        }
    }

    @Test(timeout = 5 * SECOND)
    public void testTopKSortStress() {
        IList<Integer> list = new DoubleLinkedList<>();
        for (int i = 100000; i > 0; i--) {
            list.add(i);
        }
        IList<Integer> top = Sorter.topKSort(100000, list);
        for (int i = 1; i <= top.size(); i++) {
            assertEquals(i, top.get(i-1));
        }
    }

}
