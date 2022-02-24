package com.moor.imkf.demo.view.flexbox;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The direction children items are placed inside the flex container, it determines the
 * direction of the main axis (and the cross axis, perpendicular to the main axis).
 */
@IntDef({FlexDirection.ROW, FlexDirection.ROW_REVERSE, FlexDirection.COLUMN,
        FlexDirection.COLUMN_REVERSE})
@Retention(RetentionPolicy.SOURCE)
public @interface FlexDirection {

    /**
     * Main axis direction -> horizontal. Main start to
     * main end -> Left to right (in LTR languages).
     * Cross start to cross end -> Top to bottom
     */
    int ROW = 0;

    /**
     * Main axis direction -> horizontal. Main start
     * to main end -> Right to left (in LTR languages). Cross start to cross end ->
     * Top to bottom.
     */
    int ROW_REVERSE = 1;

    /**
     * Main axis direction -> vertical. Main start
     * to main end -> Top to bottom. Cross start to cross end ->
     * Left to right (In LTR languages).
     */
    int COLUMN = 2;

    /**
     * Main axis direction -> vertical. Main start
     * to main end -> Bottom to top. Cross start to cross end -> Left to right
     * (In LTR languages)
     */
    int COLUMN_REVERSE = 3;
}
