package org.alpha.util;

import java.util.Comparator;

import org.joda.time.ReadablePartial;


public final class ReadablePartialComparator implements Comparator<ReadablePartial> {

    public static final ReadablePartialComparator NULLS_FIRST = new ReadablePartialComparator(true);
    public static final ReadablePartialComparator NULLS_LAST = new ReadablePartialComparator(false);


    private final boolean nullFirst;

    private ReadablePartialComparator(boolean nullFirst) {
        this.nullFirst = nullFirst;
    }


    public int compare(ReadablePartial lhs, ReadablePartial rhs) {
        if (lhs != null) {
            if (rhs != null) {
                return lhs.compareTo(rhs);
            } else {
                return nullFirst ? 1 : -1;
            }
        } else {
            if (rhs != null) {
                return nullFirst ? -1 : 1;
            } else {
                return 0;
            }
        }
    }


}
