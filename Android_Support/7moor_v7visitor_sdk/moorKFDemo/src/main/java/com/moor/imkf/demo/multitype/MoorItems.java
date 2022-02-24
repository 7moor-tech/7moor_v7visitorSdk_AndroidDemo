package com.moor.imkf.demo.multitype;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorItems extends ArrayList<Object> {

    /**
     * Constructs an empty Items with an initial capacity of ten.
     */
    public MoorItems() {
        super();
    }


    /**
     * Constructs an empty Items with the specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the Items
     * @throws IllegalArgumentException if the specified initial capacity
     *                                  is negative
     */
    public MoorItems(int initialCapacity) {
        super(initialCapacity);
    }


    /**
     * Constructs a Items containing the elements of the specified
     * collection, in the order they are returned by the collection's
     * iterator.
     *
     * @param c the collection whose elements are to be placed into this Items
     * @throws NullPointerException if the specified collection is null
     */
    public MoorItems(@NonNull Collection<?> c) {
        super(c);
    }
}
