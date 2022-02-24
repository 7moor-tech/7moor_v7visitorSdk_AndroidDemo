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
final class MoorDefaultMoorLinker<T> implements MoorLinker<T> {

    @Override
    public int index(int position, @NonNull T t) {
        return 0;
    }
}
