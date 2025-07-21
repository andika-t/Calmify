package calmifyStudio.util;

import java.util.Arrays;

public class ArrayList<E> {

    private Object[] arrayList;
    private static final int DEFAULT_CAPACITY = 10;
    private int size;

    public ArrayList() {
        this(DEFAULT_CAPACITY);
    }

    public ArrayList(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Kapasitas harus lebih besar dari 0.");
        }
        this.arrayList = new Object[capacity];
        this.size = 0;
    }

    public int size() {
        return this.size;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public boolean contains(Object obj) {
        return indexOf(obj) >= 0;
    }

    public int indexOf(Object obj) {
        for (int i = 0; i < this.size; i++) {
            if (obj == null ? arrayList[i] == null : obj.equals(this.arrayList[i])) {
                return i;
            }
        }
        return -1;
    }

    public void clear() {
        if (this.size > 0) {
            for (int i = 0; i < size; i++) {
                arrayList[i] = null;
            }
            this.size = 0;
        }
    }

    private boolean isFull() {
        return this.arrayList.length == this.size;
    }

    private void resizeArray() {
        int oldCapacity = this.arrayList.length;
        int newCapacity = oldCapacity + (oldCapacity >> 1);

        if (newCapacity < size) {
            newCapacity = size;
        }
        if (newCapacity <= oldCapacity) {
            newCapacity = oldCapacity + 1;
        }

        this.arrayList = Arrays.copyOf(this.arrayList, newCapacity);
    }

    public void add(E obj) {
        if (this.isFull()) {
            this.resizeArray();
        }
        this.arrayList[this.size] = obj;
        this.size++;
    }

    public void add(int index, E obj) {
        if (index < 0 || index > this.size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
        }

        if (this.isFull()) {
            this.resizeArray();
        }

        System.arraycopy(this.arrayList, index, this.arrayList, index + 1, this.size - index);

        this.arrayList[index] = obj;
        this.size++;
    }

    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
        }
        return (E) this.arrayList[index];
    }

    @SuppressWarnings("unchecked")
    public E set(int index, E obj) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
        }
        E oldValue = (E) this.arrayList[index];
        this.arrayList[index] = obj;
        return oldValue;
    }

    public boolean remove(Object obj) {
        int indexFound = this.indexOf(obj);
        if (indexFound != -1) {
            remove(indexFound);
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public E remove(int index) {
        if (index < 0 || index >= this.size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + this.size);
        }

        E oldValue = (E) this.arrayList[index];

        int numMoved = size - index - 1;
        if (numMoved > 0) {
            System.arraycopy(this.arrayList, index + 1, this.arrayList, index, numMoved);
        }
        this.arrayList[--size] = null;

        return oldValue;
    }

    public Object[] toArray() {
        return Arrays.copyOf(this.arrayList, size);
    }
}
