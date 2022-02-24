package com.moor.imkf.demo.multichat.base;

import androidx.annotation.NonNull;
import android.view.View;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 2/2/21
 *     @desc   :
 *     @version: 1.0
 * </pre>
 */
public class MoorBaseReceivedHolder {

    public MoorBaseReceivedViewBinder.ViewHolder viewHolder;

    public final View itemView;


    public MoorBaseReceivedHolder(@NonNull final View itemView) {
        this.itemView = itemView;
    }


    public @NonNull
    MoorBaseReceivedViewBinder.ViewHolder getParent() {
        return viewHolder;
    }


    public final int getAdapterPosition() {
        return getParent().getAdapterPosition();
    }


    public final int getLayoutPosition() {
        return getParent().getLayoutPosition();
    }


    public final int getOldPosition() {
        return getParent().getOldPosition();
    }


    public final boolean isRecyclable() {
        return getParent().isRecyclable();
    }


    public final void setIsRecyclable(boolean recyclable) {
        getParent().setIsRecyclable(recyclable);
    }
}
