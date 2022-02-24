package com.moor.imkf.demo.multitype;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;

import java.util.List;

import static com.moor.imkf.demo.multitype.MoorPreconditions.checkNotNull;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public final class MoorMultiTypeAsserts {

    private MoorMultiTypeAsserts() {
        throw new AssertionError();
    }


    /**
     * Makes the exception to occur in your class for debug and index.
     *
     * @param adapter the MultiTypeAdapter
     * @param items   the items list
     * @throws MoorBinderNotFoundException if check failed
     * @throws IllegalArgumentException    if your Items/List is empty
     */
    @SuppressWarnings("unchecked")
    public static void assertAllRegistered(@NonNull MoorMultiTypeAdapter adapter, @NonNull List<?> items)
            throws MoorBinderNotFoundException, IllegalArgumentException, IllegalAccessError {
        checkNotNull(adapter);
        checkNotNull(items);
        if (items.isEmpty()) {
            throw new IllegalArgumentException("Your Items/List is empty.");
        }
        for (int i = 0; i < items.size(); i++) {
            adapter.indexInTypesOf(i, items.get(0));
        }
        /* All passed. */
    }


    /**
     * @param recyclerView the RecyclerView
     * @param adapter      the MultiTypeAdapter
     * @throws IllegalAccessError       The assertHasTheSameAdapter() method must be placed after
     *                                  recyclerView.setAdapter().
     * @throws IllegalArgumentException If your recyclerView's adapter.
     *                                  is not the sample with the argument adapter.
     */
    public static void assertHasTheSameAdapter(@NonNull RecyclerView recyclerView, @NonNull MoorMultiTypeAdapter adapter)
            throws IllegalArgumentException, IllegalAccessError {
        checkNotNull(recyclerView);
        checkNotNull(adapter);
        if (recyclerView.getAdapter() == null) {
            throw new IllegalAccessError("The assertHasTheSameAdapter() method must " +
                    "be placed after recyclerView.setAdapter()");
        }
        if (recyclerView.getAdapter() != adapter) {
            throw new IllegalArgumentException(
                    "Your recyclerView's adapter is not the sample with the argument adapter.");
        }
    }
}
