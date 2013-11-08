package edu.grinnell.csc207.sorting;

import java.io.BufferedReader;
import java.io.PrintWriter;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Sort using iterative merge sort.
 *
 * @author Samuel A. Rebelsky
 */
public class IterativeMergeSorter<T> extends SorterBridge<T> {

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

    // +------------------+------------------------------------------------
    // | Exported Methods |
    // +------------------+

    /**
     * Sort vals using iterative merge sort.  See the Sorter<T> interface
     * for additional details.
     */
    @Override
    @SuppressWarnings({"unchecked"})
    public T[] sorti(T[] vals, Comparator<T> order) {
        T[] temp = (T[]) new Object[vals.length];
        int size = 1;
        while (size < vals.length) {
            int left = 0;        // The start of the left subarray
            int right;           // The start of the right subarray
            int ub;              // The end of the right subarray
 
            // Merge neighboring subarrays of size size
            while (left < vals.length) {
                right = Math.min(left+size, vals.length);
                ub = Math.min(right+size, vals.length);
                merge(order, vals, left, right, vals, right, ub, temp, left);
                left = ub;
            } // while

            // Report on activities
            if (pen != null) {
                Utils.printSubarray(pen, vals, 0, vals.length);
                for (int i = 0; i < vals.length; i++) {
                     if ((i % (2*size)) == 0) {
                         pen.print("|   ");
                     } else if ((i % size) == 0) {
                         pen.print(".   ");
                     } else {
                         pen.print("    ");
                     } // not a useful place to show something
                } // for
                pen.println("|");
                Utils.printSubarray(pen, temp, 0, temp.length, false);
                pen.print("Merged subarrays of size " + size + " ... ");
                pen.flush();
                if (eyes != null) {
                    try {
                        eyes.readLine();
                    } catch (Exception e) {
                    } // try/catch
                } // if (eyes != null)
                pen.println();
            } // if (pen != null)

            // Copy back to the main array
            System.arraycopy(temp, 0, vals, 0, vals.length);
 
            // The merged subarrays are now twice as large
            size += size;
        } // while
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
    // | Local Utilities |
    // +-----------------+

    /**
     * Merge the elements in subarrays of left and right into target.
     */
    void merge(Comparator<T> order, T[] left, int lbl, int ubl, 
            T[] right, int lbr, int ubr, T[] target, int pos) {
        while ((lbl < ubl) && (lbr < ubr)) {
            if (order.compare(left[lbl], right[lbr]) <= 0) {
                target[pos++] = left[lbl++];
            } else {
                target[pos++] = right[lbr++];
            } // else right[lbr] < left[lbl]
        } // while
        while (lbl < ubl) {
            target[pos++] = left[lbl++];
        } // while
        while (lbr < ubr) {
            target[pos++] = right[lbr++];
        } // while
    } // merge
} // IterativeMergeSorter<T>
