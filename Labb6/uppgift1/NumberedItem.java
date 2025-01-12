public class NumberedItem<T> implements Comparable<NumberedItem<T>>{

    T value;
    int index;

    NumberedItem(int index, T value) {
        if (index <= 0) {
            this.index = 0;
        } else {
            this.index = index;
        }
        this.value = value;
    }

    @Override
    public int compareTo(NumberedItem<T> o) {
        return this.index - o.index;
    }

    public boolean equals(NumberedItem<T> o) {
        System.out.println("hej");
        if (o == null) {
            return false;
        } else if (this.compareTo(o) == 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Index: " + this.index + " Value: " + this.value;
    }
}