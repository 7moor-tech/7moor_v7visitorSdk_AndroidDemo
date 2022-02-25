package com.moor.imkf.demo.multitype;

import static com.moor.imkf.demo.multitype.MoorPreconditions.checkNotNull;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
class MoorMoorMoorOneToManyBuilder<T> implements MoorOneToManyFlow<T>, MoorOneToManyEndpoint<T> {

    private final @NonNull
    MoorMultiTypeAdapter adapter;
    private final @NonNull
    Class<? extends T> clazz;
    private MoorItemViewBinder<T, ?>[] binders;


    MoorMoorMoorOneToManyBuilder(@NonNull MoorMultiTypeAdapter adapter, @NonNull Class<? extends T> clazz) {
        this.clazz = clazz;
        this.adapter = adapter;
    }


    @Override
    @CheckResult
    @SafeVarargs
    public final @NonNull
    MoorOneToManyEndpoint<T> to(@NonNull MoorItemViewBinder<T, ?>... binders) {
        checkNotNull(binders);
        this.binders = binders;
        return this;
    }


    @Override
    public void withLinker(@NonNull MoorLinker<T> moorLinker) {
        checkNotNull(moorLinker);
        doRegister(moorLinker);
    }


    @Override
    public void withClassLinker(@NonNull MoorClassLinker<T> moorClassLinker) {
        checkNotNull(moorClassLinker);
        doRegister(MoorClassMoorLinkerWrapper.wrap(moorClassLinker, binders));
    }


    private void doRegister(@NonNull MoorLinker<T> moorLinker) {
        for (MoorItemViewBinder<T, ?> binder : binders) {
            adapter.register(clazz, binder, moorLinker);
        }
    }
}
