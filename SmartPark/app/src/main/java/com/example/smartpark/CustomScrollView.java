package com.example.smartpark;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView {

    public CustomScrollView(Context context) {
        super(context);
    }

    public CustomScrollView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public CustomScrollView(Context context, AttributeSet attr, int def) {
        super(context, attr, def);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();

        switch (action) {
            case MotionEvent.ACTION_MOVE:
                return false;

            case MotionEvent.ACTION_UP:
                return false;

            case MotionEvent.ACTION_DOWN:

            case MotionEvent.ACTION_CANCEL:
                super.onTouchEvent(motionEvent);
                break;

            default:
                break;
        }

        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {
        super.onTouchEvent(motionEvent);
        return true;
    }
}
