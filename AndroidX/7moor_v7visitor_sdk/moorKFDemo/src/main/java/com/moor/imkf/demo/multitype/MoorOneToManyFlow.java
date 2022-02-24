package com.moor.imkf.demo.multitype;

import androidx.annotation.CheckResult;
import androidx.annotation.NonNull;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public interface MoorOneToManyFlow<T> {

    /**
     * Sets some item view binders to the item type.
     *
     * @param binders the item view binders
     * @return end flow operator
     */
    @CheckResult
    @SuppressWarnings("unchecked")
    @NonNull
    MoorOneToManyEndpoint<T> to(@NonNull MoorItemViewBinder<T, ?>... binders);
}
