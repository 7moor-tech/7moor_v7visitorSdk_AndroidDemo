package com.moor.imkf.demo.multitype;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/1/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorMultiMoorTypePool implements MoorTypePool {

    private final @NonNull
    List<Class<?>> classes;
    private final @NonNull
    List<MoorItemViewBinder<?, ?>> binders;
    private final @NonNull
    List<MoorLinker<?>> moorLinkers;


    /**
     * Constructs a MultiTypePool with default lists.
     */
    public MoorMultiMoorTypePool() {
        this.classes = new ArrayList<>();
        this.binders = new ArrayList<>();
        this.moorLinkers = new ArrayList<>();
    }


    /**
     * Constructs a MultiTypePool with default lists and a specified initial capacity.
     *
     * @param initialCapacity the initial capacity of the list
     */
    public MoorMultiMoorTypePool(int initialCapacity) {
        this.classes = new ArrayList<>(initialCapacity);
        this.binders = new ArrayList<>(initialCapacity);
        this.moorLinkers = new ArrayList<>(initialCapacity);
    }


    /**
     * Constructs a MultiTypePool with specified lists.
     *
     * @param classes     the list for classes
     * @param binders     the list for binders
     * @param moorLinkers the list for linkers
     */
    public MoorMultiMoorTypePool(
            @NonNull List<Class<?>> classes,
            @NonNull List<MoorItemViewBinder<?, ?>> binders,
            @NonNull List<MoorLinker<?>> moorLinkers) {
        MoorPreconditions.checkNotNull(classes);
        MoorPreconditions.checkNotNull(binders);
        MoorPreconditions.checkNotNull(moorLinkers);
        this.classes = classes;
        this.binders = binders;
        this.moorLinkers = moorLinkers;
    }


    @Override
    public <T> void register(
            @NonNull Class<? extends T> clazz,
            @NonNull MoorItemViewBinder<T, ?> binder,
            @NonNull MoorLinker<T> moorLinker) {
        MoorPreconditions.checkNotNull(clazz);
        MoorPreconditions.checkNotNull(binder);
        MoorPreconditions.checkNotNull(moorLinker);
        classes.add(clazz);
        binders.add(binder);
        moorLinkers.add(moorLinker);
    }


    @Override
    public boolean unregister(@NonNull Class<?> clazz) {
        MoorPreconditions.checkNotNull(clazz);
        boolean removed = false;
        while (true) {
            int index = classes.indexOf(clazz);
            if (index != -1) {
                classes.remove(index);
                binders.remove(index);
                moorLinkers.remove(index);
                removed = true;
            } else {
                break;
            }
        }
        return removed;
    }


    @Override
    public int size() {
        return classes.size();
    }


    @Override
    public int firstIndexOf(@NonNull final Class<?> clazz) {
        MoorPreconditions.checkNotNull(clazz);
        int index = classes.indexOf(clazz);
        if (index != -1) {
            return index;
        }
        for (int i = 0; i < classes.size(); i++) {
            if (classes.get(i).isAssignableFrom(clazz)) {
                return i;
            }
        }
        return -1;
    }


    @Override
    public @NonNull
    Class<?> getClass(int index) {
        return classes.get(index);
    }


    @Override
    public @NonNull
    MoorItemViewBinder<?, ?> getItemViewBinder(int index) {
        return binders.get(index);
    }


    @Override
    public @NonNull
    MoorLinker<?> getLinker(int index) {
        return moorLinkers.get(index);
    }
}
