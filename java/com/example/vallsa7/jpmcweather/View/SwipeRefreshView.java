package com.example.vallsa7.jpmcweather.View;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Custom SwipeRefreshLayout for the weather fragment
 */

public class SwipeRefreshView extends SwipeRefreshLayout  {

        private int scaledTouchSlop;
        private float prevX;
        private boolean declined;

        public SwipeRefreshView(Context context, AttributeSet attrs) {
            super( context, attrs );
            scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        }

        @Override
        public boolean onInterceptTouchEvent( MotionEvent event ) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    prevX = MotionEvent.obtain( event ).getX();
                    declined = false; // New action
                    break;
                case MotionEvent.ACTION_MOVE:
                    final float eventX = event.getX();
                    float xDiff = Math.abs( eventX - prevX );
                    if( declined || xDiff > scaledTouchSlop ){
                        declined = true; // Memorize
                        return false;
                    }
                    break;
            }
            return super.onInterceptTouchEvent( event );
        }
    }

