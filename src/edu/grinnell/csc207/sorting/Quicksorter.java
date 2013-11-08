package edu.grinnell.csc207.sorting;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

/**
 * Sort using Quicksort.
 *
 * @author Samuel A. Rebelsky
 * @author Your Name Here.
 */
public class Quicksorter<T> extends SorterBridge<T> {
    // +--------+----------------------------------------------------------
    // | Fields |
    // +--------+

    /**
     * The PrintWriter used for output.
     */
    PrintWriter pen;

    /**
     * The Buffered Reader used for input.
     */
    BufferedReader eyes;

    /**
     * Do we generate Docbook?
     */
    boolean docbook = false;

    /**
     * A random number generator.
     */
    Random generator;

    // +--------------+----------------------------------------------------
    // | Constructors |
    // +--------------+

    /**
     * Construct a new sorter.
     */
    public Quicksorter() {
        this.generator = new java.util.Random();
    } // Quicksorter

    // +----------------+--------------------------------------------------
    // | Public Methods |
    // +----------------+

    /**
     * Sort vals using Quicksort.  See the Sorter<T> interface
     * for additional details.
     */
    @Override
    public T[] sorti(T[] vals, Comparator<T> order) {
        qsort(vals, order, 0, vals.length);
        return vals;
    } // sorti(T[], Comparator<T>)

    /**
     * Make the sorter log its output using a PrintWriter.
     */
    public void log(PrintWriter pen) {
        this.pen = pen;
        this.eyes = null;
    } // log(PrintWriter)

    /**
     * Make the sorter log its output using a PrintWriter, pausing
     * after each step.
     */
    public void log(PrintWriter pen, BufferedReader eyes) {
        this.pen = pen;
        this.eyes = eyes;
    } // log(PrintWriter, BufferedReader)

    // +-----------------+-------------------------------------------------
    // | Sorting Methods |
    // +-----------------+

    /**
     * Sort the elements in positions [lb..ub) using Quicksort.
     */
    void qsort(T[] vals, Comparator<T> order, int lb, int ub) {
        // Observe
        Utils.printSubarray(pen, vals, 0, vals.length);
        this.printLabels(vals.length, lb, ub, -1, -1);
        this.nextStep("Sorting the subarray from " + lb + " to " + ub);

        // One element arrays are sorted.
        if (lb >= ub - 1) {
            this.nextStep("Zero or one elements.  Done.");
            return;
        } else {
            if (pen != null) {
                pen.println("Partitioning:");
            } // if pen is not null
            int mid = partition(vals, order, lb, ub);
            Utils.printSubarray(pen, vals, lb, ub);
            this.printMidpoint(pen, mid);
            this.nextStep("After partitioning, midpoint is at " + mid);
            qsort(vals, order, lb, mid);
            qsort(vals, order, mid+1, ub);
        } // More than one element
    } // qsort(T[], Comparator<T>, int, int)

    /**
     * Pick a random pivot and reorganize the elements in positions 
     * [lb..ub) of vals such that elements smaller than the pivot appear
     * to the left, elements bigger than the pivot appear to the right
     * of the pivot, and the pivot is in the middle.  
     *
     * @param
     *    values, an array.
     * @param
     *    order, a comparator.
     * @param
     *    lb, an integer.
     * @param
     *    ub, an integer.
     * @return
     *    mid, the index of the pivot.
     *
     * @pre
     *    order can be applied to any pair of elements in vals.
     * @pre
     *    0 <= lb < ub < values.length.
     * @post
     *    lb <= mid < ub
     * @post
     *    values[mid] == pivot, for some value pivot
     * @post
     *    For all i, lb <= i < mid, order.compare(values[i],pivot) <= 0
     *    For all i, mid < i < ub, order.compare(pivot, values[i]) < 0
     */
    int partition(T[] vals, Comparator<T> order, int lb, int ub) {
        int tmp = selectPivot(vals, order, lb, ub);
        int small = lb+1;
        int large = ub;

        // Watch it run!
        nextStep("Pivot is at " + tmp + "(" + vals[tmp] + ")");
        Utils.printSubarray(pen, vals, lb, ub);
        nextStep("Swapping pivot to start");
        Utils.swap(vals, lb, tmp);

        // Invariant
        // +-p+-----------------+--------------------+----------------+
        // | i| values <= pivot | unprocessed values | values > pivot |
        // +-v+-----------------+--------------------+----------------+
        // |  |                 |                    |                |
        // lb lb+1              small                large            ub
        //
        // That is,
        //   For all i, lb+1 <= i < small, vals[i] <= pivot
        //   For all j, large <= j < ub, vals[j] > pivot
        //
        // Termination:
        //   In each iteration, we either increment small or decrement large

        while (small < large) {
            // Watch it run!
            Utils.printSubarray(pen, vals, lb, ub);
            this.printLabels(vals.length, lb, ub, small, large);

            // Do the real work
            if (order.compare(vals[small], vals[lb]) <= 0) {
                nextStep("vals[" + small + "] is small.  Advancing.");
                small++;
            } else {
                nextStep("vals[" + small + "] is large.  Swapping.");
                large--;
                Utils.swap(vals, small, large);
            } // large value
        } // while

        // Invariant + Termination
        // +-p+-----------------+----------------+
        // | i| values <= pivot | values > pivot |
        // +-v+-----------------+----------------+
        // |  |                 |                |
        // lb lb+1              small,large      ub

        // Watch!
        Utils.printSubarray(pen, vals, lb, ub);
        this.printLabels(vals.length, lb, ub, small, large);

        // Almost done!  Swap the pivot into the middle
        nextStep("Swapping pivot to middle");
        Utils.swap(vals, lb, small-1);
        
        // +----------------+-p+----------------+
        // | values <= pivot| i| values > pivot |
        // +----------------+-v+----------------+
        // |                   |                |
        // lb                  small,large      ub

        return small-1;
    } // partition

    /**
     * Select a pivot from the range lb (inclusive) to ub (exclusive)
     * of vals.  Return the index of the pivot.
     */
    int selectPivot(T[] vals, Comparator<T> order, int lb, int ub) {
        int i0 = lb + generator.nextInt(ub-lb);
        int i1 = lb + generator.nextInt(ub-lb);
        int i2 = lb + generator.nextInt(ub-lb);
        T p0 = vals[i0];
        T p1 = vals[i1];
        T p2 = vals[i2];

        if (pen != null) {
            pen.println("Selecting pivot from among " +
                        "vals[" + i0 + "] (" + p0 + "), " +
                        "vals[" + i1 + "] (" + p1 + "), and " +
                        "vals[" + i2 + "] (" + p2 + ")");
        }

        if (order.compare(p0, p1) <= 0) {
            // p0 <= p1
            if (order.compare(p1, p2) <= 0) {
                // p0 <= p1 <= p2
                return i1;
            } else {
                // p0 <= p1, p2 < p1
                if (order.compare(p0,p2) <= 0) {
                    // p0 <= p2 < p1 
                    return i2;
                } else {
                    // p2 < p0 <= p1
                    return i0;
                } // else: p2 < p0
            } // else: p2 < p1
        } else {
            // p1 < p0
            if (order.compare(p0, p2) <= 0) {
                // p1 < p0 <= p2
                return i0;
            } else {
                // p1 < p0, p2 < p0
                if (order.compare(p1, p2) <= 0) {
                    // p1 <= p2 < p0
                    return i2;
                } else {
                    // p2 < p1 < p0
                    return i1;
                } // else: p2 > p1
            } // else: p2 < p0
        } // else: p1 < p0
    } // selectPivot
    
    // +-------------+-----------------------------------------------------
    // | Misc. Utils |
    // +-------------+

    /**
     * Go on to the next step, pausing for input from the user in
     * certain cases.
     */
    void nextStep(String str) {
        if (pen != null) {
            pen.print(str);
            pen.print(" ... ");
            pen.flush();
        }
        if (eyes != null) {
            try {
                eyes.readLine();
            } catch (Exception e) {
            } // try/catch
            pen.println();
        } // if eyes != null
    } // nextStep()

    /**
     * Print the labels for Quicksort
     */
    void printLabels(int len, int lb, int ub, int small, int large) {
        if (pen == null) return;

        // Print vertical lines to help us read
        for (int i = 0; i <= len; i++) {
            if ((i == lb) || (i == ub) || (i == small) || (i == large)) {
                pen.print("|   ");
            } else {
                pen.print("    ");
            } // if it's not a key value
        } // for
        pen.println();

        // Print the labels.  
        // The strategy I'm using is inefficent, but easier to read
        for (int i = 0; i <= len; i++) {
            if (i == lb) {
                pen.print("lb  ");
            } else if ((i == ub) && (i == large) && (i == small)) {
                pen.print("ub,sm,lg");
            } else if ((i == ub) && (i == large)) {
                pen.print("ub,lg");
            } else if (i == ub) {
                pen.print("ub  ");
            } else if ((i == large) && (i == small)) {
                pen.print("s,l ");
            } else if (i == large) {
                pen.print("lg  ");
            } else if (i == small) {
                pen.print("sm  ");
            } else {
                pen.print("    ");
            } // nothing special
        } // for
        pen.println();
    } // printLabels

    /**
     * Print the midpoint.
     */
    void printMidpoint(PrintWriter pen, int midpoint) {
        if (pen == null) return;
        for (int i = 0; i < midpoint; i++) {
            pen.print("    ");
        } // for
        pen.println(" ***");
    } // printMidpoint

} // Quicksorter<T>
