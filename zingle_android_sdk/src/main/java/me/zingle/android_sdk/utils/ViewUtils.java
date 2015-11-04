package me.zingle.android_sdk.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by SLAVA 09 2015.
 */
public class ViewUtils {

    public static View findChildById(ViewGroup group, int id) {
        for (int i = 0; i < group.getChildCount(); i++) {
            View child = group.getChildAt(i);
            if (child.getId() == id) return child;
        }
        return null;
    }

    public static float[] getRoundRectRadii(float[] cornerRadiusDp, final DisplayMetrics displayMetrics) {
        float[] result = new float[8];
        return getRoundRectRadii(cornerRadiusDp, displayMetrics, result);
    }

    public static float[] getRoundRectRadii(float[] cornerRadiusDp, final DisplayMetrics displayMetrics, float[] result) {
        if (result.length < cornerRadiusDp.length * 2) throw new IllegalArgumentException("result[] is shorter than required. result: " + result.length + ", required: " + cornerRadiusDp.length * 2);
        for (int i = 0; i < cornerRadiusDp.length; i++) {
            result[i * 2] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cornerRadiusDp[i], displayMetrics);
            result[i * 2 + 1] = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, cornerRadiusDp[i], displayMetrics);
        }
        return result;
    }

    public static float getPxFromDp(float dp, Context context) {
        return getPxFromDp(dp, context.getResources().getDisplayMetrics());
    }

    public static float getPxFromDp(float dp, DisplayMetrics displayMetrics) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, displayMetrics);
    }

    public static String toStringSpec(int measureSpec) {
        switch (View.MeasureSpec.getMode(measureSpec)) {
            case View.MeasureSpec.AT_MOST : return "" + View.MeasureSpec.getSize(measureSpec) + ".A";
            case View.MeasureSpec.EXACTLY : return "" + View.MeasureSpec.getSize(measureSpec) + ".E";
            default                  : return "" + View.MeasureSpec.getSize(measureSpec) + ".U";
        }
    }

    public static String toStringSpec(int widthSpec, int heightSpec) {
        return toStringSpec(widthSpec) + "|" + toStringSpec(heightSpec);
    }

    public static String toString(MotionEvent event) {
        StringBuilder sb = new StringBuilder();

        sb.append("action: ");
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN    : sb.append("DOWN"); break;
            case MotionEvent.ACTION_UP      : sb.append("UP  "); break;
            case MotionEvent.ACTION_MOVE    : sb.append("MOVE"); break;
            case MotionEvent.ACTION_CANCEL  : sb.append("CANCEL"); break;
            case MotionEvent.ACTION_SCROLL  : sb.append("SCROLL"); break;
            case MotionEvent.ACTION_POINTER_UP     : {
                sb.append("ACTION_POINTER_UP");
                final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                sb.append(" pointer: ").append(pointerIndex);
                break;
            }
            case MotionEvent.ACTION_POINTER_DOWN   : {
                sb.append("ACTION_POINTER_DOWN");
                final int pointerIndex = (event.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
                sb.append(" pointer: ").append(pointerIndex);
                break;
            }
            default                         : sb.append(event.getAction()); break;
        }
        sb.append(", pts: [");
        for (int i = 0; i < event.getPointerCount(); i++) {
            sb.append(i > 0 ? ", ":"");
            sb.append(i).append(": ").append(String.format("%.1fx%.1f", event.getX(i), event.getY(i)));
        }
        sb.append("]");
        return sb.toString();
    }

}
