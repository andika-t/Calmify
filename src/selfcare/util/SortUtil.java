// src/Selfcare/util/SortUtil.java
package selfCare.util;

import java.util.Comparator;
import java.util.List;

public class SortUtil {

    public static <T> void quickSort(List<T> list, Comparator<T> comparator) {
        if (list == null || list.size() <= 1) {
            return; 
        }
        quickSort(list, 0, list.size() - 1, comparator);
    }

    private static <T> void quickSort(List<T> list, int low, int high, Comparator<T> comparator) {
        if (low < high) {
            int pi = partition(list, low, high, comparator);

            quickSort(list, low, pi - 1, comparator);
            quickSort(list, pi + 1, high, comparator);
        }
    }

    private static <T> int partition(List<T> list, int low, int high, Comparator<T> comparator) {
        T pivot = list.get(high); 
        int i = (low - 1); 

        for (int j = low; j < high; j++) {
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;
                T temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        T temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);

        return i + 1;
    }

    public static <T> void bubbleSort(List<T> list, Comparator<T> comparator) {
        int n = list.size();
        boolean swapped; 

        for (int i = 0; i < n - 1; i++) {
            swapped = false; 
            for (int j = 0; j < n - i - 1; j++) {
                if (comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    T temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swapped = true; 
                }
            }
            if (!swapped) {
                break;
            }
        }
    }
}
