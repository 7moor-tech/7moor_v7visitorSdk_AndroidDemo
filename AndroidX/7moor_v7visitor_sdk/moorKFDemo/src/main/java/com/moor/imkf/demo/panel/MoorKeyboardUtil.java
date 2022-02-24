package com.moor.imkf.demo.panel;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.moor.imkf.demo.R;
import com.moor.imkf.demo.utils.MoorStatusBarUtil;
import com.moor.imkf.demo.utils.MoorViewUtil;
import com.moor.imkf.moorsdk.utils.MoorLogUtils;

/**
 * <pre>
 *     @author : Trial
 *     @time   : 1/19/21
 *     @desc   : 为了节省键盘高度，并提供有效的面板高度,调整面板高度与键盘高度有关
 *     @version: 1.0
 * </pre>
 */
public class MoorKeyboardUtil {

    private static ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;

    public static void showKeyboard(final View view) {
        view.requestFocus();
        InputMethodManager inputManager =
                (InputMethodManager) view.getContext().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(view, 0);
    }

    public static void hideKeyboard(final View view) {
        InputMethodManager imm =
                (InputMethodManager) view.getContext()
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    private static int lastSaveKeyboardHeight = 0;

    private static boolean saveKeyboardHeight(final Context context, int keyboardHeight) {
        if (lastSaveKeyboardHeight == keyboardHeight) {
            return false;
        }

        if (keyboardHeight < 0) {
            return false;
        }

        lastSaveKeyboardHeight = keyboardHeight;

        return MoorKeyBoardSharedPreferences.save(context, keyboardHeight);
    }


    public static int getKeyboardHeight(final Context context) {
        if (lastSaveKeyboardHeight == 0) {
            lastSaveKeyboardHeight = MoorKeyBoardSharedPreferences
                    .get(context, getMinPanelHeight(context.getResources()));
        }

        return lastSaveKeyboardHeight;
    }


    public static int getValidPanelHeight(final Context context) {
        final int maxPanelHeight = getMaxPanelHeight(context.getResources());
        final int minPanelHeight = getMinPanelHeight(context.getResources());

        int validPanelHeight = getKeyboardHeight(context);

        validPanelHeight = Math.max(minPanelHeight, validPanelHeight);
        validPanelHeight = Math.min(maxPanelHeight, validPanelHeight);
        return validPanelHeight;
    }


    private static int maxPanelHeight = 0;
    private static int minPanelHeight = 0;
    private static int minKeyboardHeight = 0;

    public static int getMaxPanelHeight(final Resources res) {
        if (maxPanelHeight == 0) {
            maxPanelHeight = res.getDimensionPixelSize(R.dimen.moor_max_panel_height);
        }

        return maxPanelHeight;
    }

    public static int getMinPanelHeight(final Resources res) {
        if (minPanelHeight == 0) {
            minPanelHeight = res.getDimensionPixelSize(R.dimen.moor_min_panel_height);
        }

        return minPanelHeight;
    }

    public static int getMinKeyboardHeight(Context context) {
        if (minKeyboardHeight == 0) {
            minKeyboardHeight = context.getResources()
                    .getDimensionPixelSize(R.dimen.moor_min_keyboard_height);
        }
        return minKeyboardHeight;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public static ViewTreeObserver.OnGlobalLayoutListener attach(final Activity activity,
                                                                 IPanelHeightTarget target,
            /* Nullable */
                                                                 OnKeyboardShowingListener lis) {
        final ViewGroup contentView = activity.findViewById(android.R.id.content);
//        final boolean isFullScreen = MoorViewUtil.isFullScreen(activity);
        final boolean isFullScreen = true;
        final boolean isTranslucentStatus = MoorViewUtil.isTranslucentStatus(activity);
        final boolean isFitSystemWindows = MoorViewUtil.isFitsSystemWindows(activity);

        // get the screen height.
        final Display display = activity.getWindowManager().getDefaultDisplay();
        final int screenHeight;
        final Point screenSize = new Point();
        display.getSize(screenSize);
        screenHeight = MoorKeyBoardSharedPreferences.getScreenHeight(activity, screenSize.y);

        globalLayoutListener = new KeyboardStatusListener(
                isFullScreen,
                isTranslucentStatus,
                isFitSystemWindows,
                contentView,
                target,
                lis,
                screenHeight);

        contentView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
        return globalLayoutListener;
    }

    public static ViewTreeObserver.OnGlobalLayoutListener attach(final Activity activity,
                                                                 IPanelHeightTarget target) {
        return attach(activity, target, null);
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public static void detach(Activity activity) {
        ViewGroup contentView = activity.findViewById(android.R.id.content);
        contentView.getViewTreeObserver().removeOnGlobalLayoutListener(globalLayoutListener);
        globalLayoutListener = null;
    }

    private static class KeyboardStatusListener implements ViewTreeObserver.OnGlobalLayoutListener {
        private static final String TAG = "KeyboardStatusListener";

        private int previousDisplayHeight = 0;
        private final ViewGroup contentView;
        private final IPanelHeightTarget panelHeightTarget;
        private final boolean isFullScreen;
        private final boolean isTranslucentStatus;
        private final boolean isFitSystemWindows;
        private final int statusBarHeight;
        private boolean lastKeyboardShowing;
        private final OnKeyboardShowingListener keyboardShowingListener;
        private final int screenHeight;

        private boolean isOverlayLayoutDisplayHContainStatusBar = false;

        KeyboardStatusListener(boolean isFullScreen, boolean isTranslucentStatus,
                               boolean isFitSystemWindows,
                               ViewGroup contentView, IPanelHeightTarget panelHeightTarget,
                               OnKeyboardShowingListener listener, int screenHeight) {
            this.contentView = contentView;
            this.panelHeightTarget = panelHeightTarget;
            this.isFullScreen = isFullScreen;
            this.isTranslucentStatus = isTranslucentStatus;
            this.isFitSystemWindows = isFitSystemWindows;
            this.statusBarHeight = MoorStatusBarUtil.getStatusBarHeight(contentView.getContext());
            this.keyboardShowingListener = listener;
            this.screenHeight = screenHeight;
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
        @Override
        public void onGlobalLayout() {
            final View userRootView = contentView.getChildAt(0);
            final View actionBarOverlayLayout = (View) contentView.getParent();

            // Step 1. calculate the current display frame's height.
            Rect r = new Rect();

            final int displayHeight;
            final int notReadyDisplayHeight = -1;
            if (isTranslucentStatus) {
                // status bar translucent.

                // In the case of the Theme is Status-Bar-Translucent, we calculate the keyboard
                // state(showing/hiding) and the keyboard height based on assuming that the
                // displayHeight includes the height of the status bar.

                actionBarOverlayLayout.getWindowVisibleDisplayFrame(r);

                final int overlayLayoutDisplayHeight = (r.bottom - r.top);

                if (!isOverlayLayoutDisplayHContainStatusBar) {
                    // in case of the keyboard is hiding, the display height of the
                    // action-bar-overlay-layout would be possible equal to the screen height.

                    // and if isOverlayLayoutDisplayHContainStatusBar has already been true, the
                    // display height of action-bar-overlay-layout must include the height of the
                    // status bar always.
                    isOverlayLayoutDisplayHContainStatusBar =
                            overlayLayoutDisplayHeight == screenHeight;
                }

                if (!isOverlayLayoutDisplayHContainStatusBar) {
                    // In normal case, we need to plus the status bar height manually.
                    displayHeight = overlayLayoutDisplayHeight + statusBarHeight;
                } else {
                    // In some case(such as Samsung S7 edge), the height of the
                    // action-bar-overlay-layout display bound already included the height of the
                    // status bar, in this case we doesn't need to plus the status bar height
                    // manually.
                    displayHeight = overlayLayoutDisplayHeight;
                }

            } else {
                if (userRootView != null) {
                    userRootView.getWindowVisibleDisplayFrame(r);
                    displayHeight = (r.bottom - r.top);
                } else {
                    MoorLogUtils.d(
                            "user root view not ready so ignore global layout changed!");
                    displayHeight = notReadyDisplayHeight;
                }

            }

            if (displayHeight == notReadyDisplayHeight) {
                return;
            }

            calculateKeyboardHeight(displayHeight);
            calculateKeyboardShowing(displayHeight);

            previousDisplayHeight = displayHeight;
        }

        private void calculateKeyboardHeight(final int displayHeight) {
            // first result.
            if (previousDisplayHeight == 0) {
                previousDisplayHeight = displayHeight;

                // init the panel height for target.
                panelHeightTarget.refreshHeight(MoorKeyboardUtil.getValidPanelHeight(getContext()));
                return;
            }

            int keyboardHeight;
            if (MoorSwitchConflictUtil.isHandleByPlaceholder(isFullScreen, isTranslucentStatus,
                    isFitSystemWindows)) {
                // the height of content parent = contentView.height + actionBar.height
                final View actionBarOverlayLayout = (View) contentView.getParent();

                int height = actionBarOverlayLayout.getHeight();
                keyboardHeight = height - displayHeight;


            } else {
                keyboardHeight = Math.abs(displayHeight - previousDisplayHeight);
            }
            // no change.
            if (keyboardHeight <= getMinKeyboardHeight(getContext())) {
                return;
            }


            // influence from the layout of the Status-bar.
            if (keyboardHeight == this.statusBarHeight) {
                return;
            }

            // save the keyboardHeight

            boolean changed = MoorKeyboardUtil.saveKeyboardHeight(getContext(), keyboardHeight);
            if (changed) {
                final int validPanelHeight = MoorKeyboardUtil.getValidPanelHeight(getContext());
                if (this.panelHeightTarget.getHeight() != validPanelHeight) {
                    // Step3. refresh the panel's height with valid-panel-height which refer to
                    // the last keyboard height
                    this.panelHeightTarget.refreshHeight(validPanelHeight);
                }
            }
        }

        private int maxOverlayLayoutHeight;

        private void calculateKeyboardShowing(final int displayHeight) {

            boolean isKeyboardShowing;

            // the height of content parent = contentView.height + actionBar.height
            final View actionBarOverlayLayout = (View) contentView.getParent();
            // in the case of FragmentLayout, this is not real ActionBarOverlayLayout, it is
            // LinearLayout, and is a child of DecorView, and in this case, its top-padding would be
            // equal to the height of status bar, and its height would equal to DecorViewHeight -
            // NavigationBarHeight.
            final int actionBarOverlayLayoutHeight = actionBarOverlayLayout.getHeight()
                    - actionBarOverlayLayout.getPaddingTop();

            if (MoorSwitchConflictUtil.isHandleByPlaceholder(isFullScreen, isTranslucentStatus,
                    isFitSystemWindows)) {
                if (!isTranslucentStatus
                        && actionBarOverlayLayoutHeight - displayHeight == this.statusBarHeight) {
                    // handle the case of status bar layout, not keyboard active.
                    isKeyboardShowing = lastKeyboardShowing;
                } else {
                    isKeyboardShowing = actionBarOverlayLayoutHeight - displayHeight > this.statusBarHeight;
                }

            } else {

                final int phoneDisplayHeight = contentView.getResources()
                        .getDisplayMetrics().heightPixels;
                if (!isTranslucentStatus
                        && phoneDisplayHeight == actionBarOverlayLayoutHeight) {
                    // no space to settle down the status bar, switch to fullscreen,
                    // only in the case of paused and opened the fullscreen page.
                    return;

                }

                if (maxOverlayLayoutHeight == 0) {
                    // non-used.
                    isKeyboardShowing = lastKeyboardShowing;
                } else {
                    isKeyboardShowing = displayHeight < maxOverlayLayoutHeight
                            - getMinKeyboardHeight(getContext());
                }

                maxOverlayLayoutHeight = Math
                        .max(maxOverlayLayoutHeight, actionBarOverlayLayoutHeight);
            }

            if (lastKeyboardShowing != isKeyboardShowing) {
                this.panelHeightTarget.onKeyboardShowing(isKeyboardShowing);
                if (keyboardShowingListener != null) {
                    keyboardShowingListener.onKeyboardShowing(isKeyboardShowing);
                }
            }

            lastKeyboardShowing = isKeyboardShowing;

        }

        private Context getContext() {
            return contentView.getContext();
        }
    }

    public interface OnKeyboardShowingListener {

        void onKeyboardShowing(boolean isShowing);

    }
}