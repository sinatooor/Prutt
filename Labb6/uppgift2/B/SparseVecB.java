import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class SparseVecB <E extends Comparable<E>> extends TreeMap<Integer, E> implements SparseVec<E>{
        @Override
        public void add(E elem) {
            for (Integer i=0; ; i++) {
                if (!this.containsKey(i)) {
                    this.add(i, elem);
                    break;
                }
            }
        }

        @Override
        public void add(int pos, E elem) {
            if (pos < 0) pos = 0;
            this.put(pos, elem);
        }

        @Override
        public int indexOf(E elem) {
            int index = -1;
            for (Map.Entry<Integer, E> entry : this.entrySet()) {
                if (entry.getValue().compareTo(elem) == 0) {
                    index = entry.getKey();
                    break;
                }
            }
            return index;
        }

        @Override
        public void removeAt(int pos) {
            this.remove(pos);
        }

        @Override
        public void removeElem(E elem) {
            this.removeAt(this.indexOf(elem));
        }

        @Override
        public int size() {
            return super.size();
        }

        @Override
        public int minIndex() {
            if (this.isEmpty()) {
                return -1;
            } else {
                return this.firstKey();
            }
        }

        @Override
        public int maxIndex() {
            if (this.isEmpty()) {
                return -1;
            } else {
                return this.lastKey();
            }
        }

        @Override
        public E get(int pos) {
            return super.get(pos);
        }

        @Override
        public Object[] toArray() {
            Object[] array = new Object[this.maxIndex()+1];
            for (Map.Entry<Integer, E> entry : this.entrySet()) {
                array[entry.getKey()] = entry.getValue();
            }
            return array;
        }

        @Override
        public List<E> sortedValues() {
            ArrayList<E> list = new ArrayList<>(this.values());
            Collections.sort(list);
            return list;
        };

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<Integer, E> entry : this.entrySet()) {
                sb.append(entry.getKey());
                sb.append(" : ");
                sb.append(entry.getValue());
                sb.append("\n");
            }
            return sb.toString();
        }

    public static void main(String[] args) {
        SparseVecB<Integer> vecB = new SparseVecB<>();
        int[] elements = new int[]{1, 2, 3, 4, -3, -1};
        int[] indexses = new int[]{4, 3, 9, 5, 11, -2};
        for (int i=0 ; i<elements.length ; i++) {
            vecB.add(indexses[i], elements[i]);
        }

        for (Object i : vecB.toArray()) {
            System.out.println(i);
        }

        System.out.println(vecB.sortedValues());
        System.out.println(vecB.toString());
    }
}
