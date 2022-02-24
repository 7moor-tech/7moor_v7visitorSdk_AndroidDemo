package com.moor.imkf.demo.multitype;

import androidx.annotation.NonNull;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
@SuppressWarnings("WeakerAccess")
public final class MoorPreconditions {

    @SuppressWarnings("ConstantConditions")
    public static @NonNull
    <T> T checkNotNull(@NonNull final T object) {
        if (object == null) {
            throw new NullPointerException();
        }
        return object;
    }


    private MoorPreconditions() {
    }
}
