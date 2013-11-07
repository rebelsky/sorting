package edu.grinnell.csc207.sorting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.util.Arrays;
import java.util.Comparator;

/**
 * An example of the Quicksorter in action.
 *
 * @author Samuel A. Rebelsky
 */
public class QuicksorterExample {
    // +------+------------------------------------------------------------
    // | Main |
    // +------+

    /**
     * Short the sorting working.
     */
    public static void main(String[] args) throws Exception {
        Quicksorter<String> example = new Quicksorter<String>();
        Comparator<String> order = StandardStringComparator.comparator;
        PrintWriter pen = new PrintWriter(System.out, true);
        BufferedReader eyes = new BufferedReader(
                new InputStreamReader(System.in));

        example.log(pen, eyes);
        String[] values = "alphabetical".split("");
        values = Arrays.copyOfRange(values, 1, values.length);
        example.sort(values, order);
    } // main(String[])
} // class QuicksorterExample
