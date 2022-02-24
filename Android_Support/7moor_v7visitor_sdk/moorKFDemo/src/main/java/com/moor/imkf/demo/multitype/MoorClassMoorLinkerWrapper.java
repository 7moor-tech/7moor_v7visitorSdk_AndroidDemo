package com.moor.imkf.demo.multitype;

import android.support.annotation.NonNull;

import java.util.Arrays;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
final class MoorClassMoorLinkerWrapper<T> implements MoorLinker<T> {

    private final @NonNull
    MoorClassLinker<T> moorClassLinker;
    private final @NonNull
    MoorItemViewBinder<T, ?>[] binders;


    private MoorClassMoorLinkerWrapper(
            @NonNull MoorClassLinker<T> moorClassLinker,
            @NonNull MoorItemViewBinder<T, ?>[] binders) {
        this.moorClassLinker = moorClassLinker;
        this.binders = binders;
    }


    static @NonNull
    <T> MoorClassMoorLinkerWrapper<T> wrap(
            @NonNull MoorClassLinker<T> moorClassLinker,
            @NonNull MoorItemViewBinder<T, ?>[] binders) {
        return new MoorClassMoorLinkerWrapper<T>(moorClassLinker, binders);
    }


    @Override
    public int index(int position, @NonNull T t) {
        Class<?> userIndexClass = moorClassLinker.index(position, t);
        for (int i = 0; i < binders.length; i++) {
            if (binders[i].getClass().equals(userIndexClass)) {
                return i;
            }
        }
        throw new IndexOutOfBoundsException(
                String.format("%s is out of your registered binders'(%s) bounds.",
                        userIndexClass.getName(), Arrays.toString(binders))
        );
    }
}
