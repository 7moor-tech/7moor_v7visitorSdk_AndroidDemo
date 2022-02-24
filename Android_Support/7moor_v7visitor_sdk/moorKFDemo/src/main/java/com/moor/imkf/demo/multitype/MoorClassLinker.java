package com.moor.imkf.demo.multitype;

import android.support.annotation.NonNull;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public interface MoorClassLinker<T> {

    /**
     * Returns the class of your registered binders for your item.
     *
     * @param position The position in items
     * @param t        The item
     * @return The index of your registered binders
     * @see MoorOneToManyEndpoint#withClassLinker(MoorClassLinker)
     */
    @NonNull
    Class<? extends MoorItemViewBinder<T, ?>> index(int position, @NonNull T t);
}
