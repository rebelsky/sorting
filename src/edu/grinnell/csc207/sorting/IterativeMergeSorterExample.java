package edu.grinnell.csc207.sorting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.util.Arrays;
import java.util.Comparator;

/**
 * An example of the iterative merge sorter in action.
 *
 * @author Samuel A. Rebelsky
 */
public class IterativeMergeSorterExample {
    // +------+------------------------------------------------------------
    // | Main |
    // +------+

    /**
     * Short the sorting working.
     */
    public static void main(String[] args) throws Exception {
        IterativeMergeSorter<String> example = 
                new IterativeMergeSorter<String>();
        Comparator<String> order = StandardStringComparator.comparator;
        PrintWriter pen = new PrintWriter(System.out, true);
        BufferedReader eyes = new BufferedReader(
                new InputStreamReader(System.in));

        example.log(pen, eyes);
        String[] values = "alphabeticalstuff".split("");
        values = Arrays.copyOfRange(values, 1, values.length);
        example.sort(values, order);
    } // main(String[])
} // class IterativeMergeSorterExample
