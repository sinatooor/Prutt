import static org.junit.Assert.*;

import org.junit.Test;

public class SparseVecTestA {

        @Test
        public void testEmpty() {
            SparseVecA<Integer> vec = new SparseVecA<>();
            assertEquals(vec.size(), 0);
            assertEquals(vec.minIndex(), -1);
            assertEquals(vec.minIndex(), -1);
            assertNull(vec.get(4));
            assertEquals(vec.toArray().length, 0);
            assertEquals(vec.sortedValues().size(), 0);
        }

        @Test
        public void testIndex() {
            SparseVecA<Integer> vec = new SparseVecA<>();
            vec.add(3, 5);
            assertEquals(vec.size(), 1);
            vec.add(3, 6);
            assertEquals(vec.size(), 1);
            vec.add(-7);
            assertEquals(vec.size(), 2);
            vec.add(9, 43);
            assertEquals(vec.size(), 3);
            assertEquals(vec.maxIndex(), 9);
            assertEquals(vec.minIndex(), 0);
            vec.add(-4);
            assertEquals(vec.size(), 4);
            assertEquals(vec.get(1).compareTo(-4), 0);
            assertNull(vec.get(2));
        }

        @Test
        public void testRemove() {
            SparseVecA<Integer> vec = new SparseVecA<>();
            vec.add(2, 9);
            vec.add(4,-2);
            vec.add(9, 98);
            vec.removeAt(1);
            assertEquals(vec.size(), 3);
            vec.removeAt(2);
            assertEquals(vec.size(), 2);
            assertNull(vec.get(2));
            vec.removeElem(98);
            assertNull(vec.get(9));
        }
}
