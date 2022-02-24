package com.moor.imkf.demo.multitype;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public interface MoorLinker<T> {

    /**
     * Returns the index of your registered binders for your item. The result should be in range of
     * {@code [0, one-to-multiple-binders.length)}.
     *
     * <p>Note: The argument of {@link MoorOneToManyFlow#to(MoorItemViewBinder[])} is the
     * one-to-multiple-binders.</p>
     *
     * @param position The position in items
     * @param t        Your item data
     * @return The index of your registered binders
     * @see MoorOneToManyFlow#to(MoorItemViewBinder[])
     * @see MoorOneToManyEndpoint#withLinker(MoorLinker)
     */
    @IntRange(from = 0)
    int index(int position, @NonNull T t);
}
