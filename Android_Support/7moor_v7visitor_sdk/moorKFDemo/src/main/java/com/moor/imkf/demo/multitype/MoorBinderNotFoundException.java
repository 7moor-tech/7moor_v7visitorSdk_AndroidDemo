package com.moor.imkf.demo.multitype;

import android.support.annotation.NonNull;


class MoorBinderNotFoundException extends RuntimeException {

    MoorBinderNotFoundException(@NonNull Class<?> clazz) {
        super("Do you have registered {className}.class to the binder in the adapter/pool?"
                .replace("{className}", clazz.getSimpleName()));
    }
}
