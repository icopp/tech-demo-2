package ventures.crooked.tech_demo_2;

import java.io.PrintWriter;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class App {
    private static Map<Integer, Integer> DISTRIBUTION;
    static {
        DISTRIBUTION = new HashMap<Integer, Integer>();
        DISTRIBUTION.put(1, 83000);
        DISTRIBUTION.put(2, 83000);
        DISTRIBUTION.put(3, 83000);
        DISTRIBUTION.put(4, 83000);
        DISTRIBUTION.put(5, 83000);
        DISTRIBUTION.put(6, 83000);
        DISTRIBUTION.put(7, 83000);
        DISTRIBUTION.put(8, 83000);
        DISTRIBUTION.put(9, 83000);
        DISTRIBUTION.put(10, 83000);
        DISTRIBUTION.put(11, 83000);
        DISTRIBUTION.put(12, 83000);
        DISTRIBUTION.put(13, 1000);
        DISTRIBUTION.put(14, 500);
        DISTRIBUTION.put(15, 250);
        DISTRIBUTION.put(16, 100);
        DISTRIBUTION.put(17, 50);
        DISTRIBUTION.put(18, 25);
        DISTRIBUTION.put(19, 10);
        DISTRIBUTION.put(20, 5);
    }
    private static int NUMBER_OF_NUMBERS = DISTRIBUTION.values().stream()
            .reduce(0, (a, b) -> a + b);

    /**
     * Generates an array of numbers with the given distribution, in order.
     */
    private static List<Integer> initialNumbers() {
        List<Integer> numbers = new ArrayList<Integer>(App.NUMBER_OF_NUMBERS);
        App.DISTRIBUTION.forEach((k, v) -> {
            for (int i = 0; i < v; i++) {
                numbers.add(k);
            }
        });
        return numbers;
    }

    /**
     * Given the list, find how many adjacent elements are identical.
     */
    private static <T> int adjacencyCount(List<T> list) {
        int count = 0;
        int size = list.size();
        for (int i = 0; i < size; i++) {
            // Note that we don't actually have to 'wrap around' with the
            // modulo, but it means not having to specially handle the list end.
            if (list.get(i).equals(list.get((i + 1) % size))) {
                count++;
            }
        }
        return count;
    }

    /**
     * Given the list, unsort it. That is to say, make sure there are no two
     * equal elements that are adjacent. Note that this is a heuristic, NOT a
     * true algorithm, so there are some edge cases (e.g. small lists, really
     * hard sets of elements to unsort) where it won't work well. But, for a
     * list like this it should work fine and generally be O(n^2).
     */
    private static <T> List<T> unsort(List<T> list) {
        int size = list.size();
        // This can be increased if the heuristic fails, but it's actually just
        // arbitrary in the first place, for wibbly-wobbly heuristic purposes.
        // That way we're not cycling forever if there's some pathologic edge
        // case that comes up.
        int giveUpAfter = list.size();

        while (App.adjacencyCount(list) > 0 && giveUpAfter > 0) {
            // Look for adjacent pairs, 'wrapping around' at the start/end so
            // that we don't need other special handling for the start/end
            for (int i = 0; i < size; i++) {
                int prevIndex = i - 1 + ((i - 1 >= 0) ? 0 : size);
                if (list.get(i).equals(list.get(prevIndex))) {
                    // Since this item matches the prev item, swap this item
                    // with the next item.
                    int nextIndex = (i + 1) % size;
                    T thisItem = list.get(i);
                    T nextItem = list.get(nextIndex);
                    list.set(i, nextItem);
                    list.set(nextIndex, thisItem);
                }
            }

            giveUpAfter--;
        }

        return list;
    }

    /**
     * Generates an array numbers with the given distribution, but with no two
     * consecutive numbers in the output the same.
     */
    public static void main(String[] args) {
        System.out.println("Assembling initial numbers.");
        List<Integer> numbers = App.initialNumbers();

        // Shuffle for initial randomness.
        // Note that for better performance, we could replace the use of
        // Collections.shuffle with something that operates directly on int
        // primitives, and replace all the `List<Integer>`s with `int[]`s. It's
        // pretty weird the standard lib doesn't have that, isn't it?
        System.out.println("Shuffling numbers.");
        Collections.shuffle(numbers);

        // Unsort remaining adjacent elements.
        System.out.println("Unsorting numbers.");
        numbers = App.unsort(numbers);

        // Write to a file named test.output
        System.out.println("Writing numbers to output file.");
        try {
            PrintWriter writer = new PrintWriter("test.output", "UTF-8");
            numbers.forEach((n) -> writer.println(n));
            writer.close();
        } catch (Exception e) {
            System.err.printf("Error while writing to file: %s\n",
                    e.getMessage());
        }

        System.out.println("Outputting indexes of 20s.");
        // I'd really like to do a simple forEach here, but Java is weird and
        // doesn't have an easy way to pass an index even for a plain List.
        int size = numbers.size();
        for (int i = 0; i < size; i++) {
            if (numbers.get(i).equals(20)) {
                System.out.printf("[%d]: 20\n", i);
            }
        }
    }
}
