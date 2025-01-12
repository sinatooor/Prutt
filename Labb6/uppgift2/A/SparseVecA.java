import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SparseVecA <E extends Comparable<E>> implements SparseVec<E> {

    private TreeMap<Integer, E> vec = new TreeMap<>();

        @Override
        public void add(E elem) {
            for (Integer i=0; ; i++) {
                if (!this.vec.containsKey(i)) {
                    this.add(i, elem);
                    break;
                }
            }
        }

        @Override
        public void add(int pos, E elem) {
            if (pos < 0) pos = 0;
            this.vec.put(pos, elem);
        }

        @Override
        public int indexOf(E elem) {
            int index = -1;
            for (Map.Entry<Integer, E> entry : this.vec.entrySet()) {
                if (entry.getValue().compareTo(elem) == 0) {
                    index = entry.getKey();
                    break;
                }
            }
            return index;
        }

        @Override
        public void removeAt(int pos) {
            this.vec.remove(pos);
        }

        @Override
        public void removeElem(E elem) {
            this.removeAt(this.indexOf(elem));
        }

        @Override
        public int size() {
            return this.vec.size();
        }

        @Override
        public int minIndex() {
            if (this.vec.isEmpty()) {
                return -1;
            } else {
                return this.vec.firstKey();
            }
        }

        @Override
        public int maxIndex() {
            if (this.vec.isEmpty()) {
                return -1;
            } else {
                return this.vec.lastKey();
            }
        }

        @Override
        public E get(int pos) {
            return this.vec.get(pos);
        }

        @Override
        public Object[] toArray() {
            Object[] array = new Object[this.maxIndex()+1];
            for (Map.Entry<Integer, E> entry : this.vec.entrySet()) {
                array[entry.getKey()] = entry.getValue();
            }
            return array;
        }

        @Override
        public List<E> sortedValues() {
            ArrayList<E> list = new ArrayList<>(this.vec.values());
            Collections.sort(list);
            return list;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Integer, E> entry : this.vec.entrySet()) {
                sb.append(entry.getKey());
                sb.append(" : ");
                sb.append(entry.getValue());
                sb.append("\n");
            }
            return sb.toString();
        }

        public static void main(String[] args) {
            SparseVecA<Integer> vecA = new SparseVecA<>();
            int[] elements = new int[]{1, 2, 3, 4, -3, -1};
            int[] indexses = new int[]{4, 3, 9, 5, 11, -2};
            for (int i=0 ; i<elements.length ; i++) {
                vecA.add(indexses[i], elements[i]);
            }
    
            for (Object i : vecA.toArray()) {
                System.out.println(i);
            }
    
            System.out.println(vecA.sortedValues());
            System.out.println(vecA.toString());
        }
    }