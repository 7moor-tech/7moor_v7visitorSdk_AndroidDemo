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
public interface MoorOneToManyEndpoint<T> {

    /**
     * Sets a linker to link the items and binders by array index.
     *
     * @param moorLinker the row linker
     * @see MoorLinker
     */
    void withLinker(@NonNull MoorLinker<T> moorLinker);

    /**
     * Sets a class linker to link the items and binders by the class instance of binders.
     *
     * @param moorClassLinker the class linker
     * @see MoorClassLinker
     */
    void withClassLinker(@NonNull MoorClassLinker<T> moorClassLinker);
}
