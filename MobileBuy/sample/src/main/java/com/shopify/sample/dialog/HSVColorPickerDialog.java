/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Jesper Borgstrup (https://github.com/jesperborgstrup/buzzingandroid/blob/master/src/com/buzzingandroid/ui/HSVColorPickerDialog.java)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.shopify.sample.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

@SuppressWarnings("ResourceType")
public class HSVColorPickerDialog extends AlertDialog {

    private static final int PADDING_DP = 20;

    private static final int CONTROL_SPACING_DP = 20;
    private static final int SELECTED_COLOR_HEIGHT_DP = 50;
    private static final int BORDER_DP = 1;
    private static final int BORDER_COLOR = Color.BLACK;

    private final OnColorSelectedListener listener;
    private int selectedColor;

    public interface OnColorSelectedListener {
        /**
         * @param color The color code selected, or null if no color. No color is only
         * possible if {@link HSVColorPickerDialog#setNoColorButton(int) setNoColorButton()}
         * has been called on the dialog before showing it
         */
        public void colorSelected( Integer color );
    }

    public HSVColorPickerDialog(Context context, int initialColor, final OnColorSelectedListener listener) {
        super(context);
        this.selectedColor = initialColor;
        this.listener = listener;

        colorWheel = new HSVColorWheel( context );
        valueSlider = new HSVValueSlider( context );
        int padding = (int) (context.getResources().getDisplayMetrics().density * PADDING_DP);
        int borderSize = (int) (context.getResources().getDisplayMetrics().density * BORDER_DP);
        RelativeLayout layout = new RelativeLayout( context );

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT );
        lp.bottomMargin = (int) (context.getResources().getDisplayMetrics().density * CONTROL_SPACING_DP);
        colorWheel.setListener( new OnColorSelectedListener() {
            public void colorSelected(Integer color) {
                valueSlider.setColor( color, true );
            }
        } );
        colorWheel.setColor( initialColor );
        colorWheel.setId( 1 );
        layout.addView( colorWheel, lp );

        int selectedColorHeight = (int) (context.getResources().getDisplayMetrics().density * SELECTED_COLOR_HEIGHT_DP);

        FrameLayout valueSliderBorder = new FrameLayout( context );
        valueSliderBorder.setBackgroundColor( BORDER_COLOR );
        valueSliderBorder.setPadding( borderSize, borderSize, borderSize, borderSize );
        valueSliderBorder.setId( 2 );
        lp = new RelativeLayout.LayoutParams( LayoutParams.MATCH_PARENT, selectedColorHeight + 2 * borderSize );
        lp.bottomMargin = (int) (context.getResources().getDisplayMetrics().density * CONTROL_SPACING_DP);
        lp.addRule( RelativeLayout.BELOW, 1 );
        layout.addView( valueSliderBorder, lp );

        valueSlider.setColor( initialColor, false );
        valueSlider.setListener( new OnColorSelectedListener() {
            @Override
            public void colorSelected(Integer color) {
                selectedColor = color;
                selectedColorView.setBackgroundColor( color );
            }
        });
        valueSliderBorder.addView( valueSlider );

        FrameLayout selectedColorborder = new FrameLayout( context );
        selectedColorborder.setBackgroundColor( BORDER_COLOR );
        lp = new RelativeLayout.LayoutParams( LayoutParams.MATCH_PARENT, selectedColorHeight + 2 * borderSize );
        selectedColorborder.setPadding( borderSize, borderSize, borderSize, borderSize );
        lp.addRule( RelativeLayout.BELOW, 2 );
        layout.addView( selectedColorborder, lp );

        selectedColorView = new View( context );
        selectedColorView.setBackgroundColor( selectedColor );
        selectedColorborder.addView( selectedColorView );

        setButton( BUTTON_NEGATIVE, context.getString( android.R.string.cancel ), clickListener );
        setButton( BUTTON_POSITIVE, context.getString( android.R.string.ok ), clickListener );

        setView( layout, padding, padding, padding, padding );
    }

    private OnClickListener clickListener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch ( which ) {
                case BUTTON_NEGATIVE:
                    dialog.dismiss();
                    break;
                case BUTTON_NEUTRAL:
                    dialog.dismiss();
                    listener.colorSelected( -1 );
                    break;
                case BUTTON_POSITIVE:
                    listener.colorSelected( selectedColor );
                    break;
            }
        }
    };

    private HSVColorWheel colorWheel;
    private HSVValueSlider valueSlider;

    private View selectedColorView;

    /**
     * Adds a button to the dialog that allows a user to select "No color",
     * which will call the listener's {@link OnColorSelectedListener#colorSelected(Integer) colorSelected(Integer)} callback
     * with null as its parameter
     * @param res A string resource with the text to be used on this button
     */
    public void setNoColorButton( int res ) {
        setButton( BUTTON_NEUTRAL, getContext().getString( res ), clickListener );
    }

    private static class HSVColorWheel  extends View {

        private static final float SCALE = 2f;
        private static final float FADE_OUT_FRACTION = 0.03f;

        private static final int POINTER_LINE_WIDTH_DP = 2;
        private static final int POINTER_LENGTH_DP = 10;

        private final Context context;

        private OnColorSelectedListener listener;
        public HSVColorWheel(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            this.context = context;
            init();
        }

        public HSVColorWheel(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.context = context;
            init();
        }

        public HSVColorWheel(Context context) {
            super(context);
            this.context = context;
            init();
        }

        private int scale;
        private int pointerLength;
        private int innerPadding;
        private Paint pointerPaint = new Paint();
        private void init() {
            float density = context.getResources().getDisplayMetrics().density;
            scale = (int) (density * SCALE);
            pointerLength = (int) (density * POINTER_LENGTH_DP );
            pointerPaint.setStrokeWidth(  (int) (density * POINTER_LINE_WIDTH_DP ) );
            innerPadding = pointerLength / 2;
        }

        public void setListener( OnColorSelectedListener listener ) {
            this.listener = listener;
        }

        float[] colorHsv = { 0f, 0f, 1f };
        public void setColor( int color ) {
            Color.colorToHSV(color, colorHsv);
            invalidate();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if ( bitmap != null ) {
                canvas.drawBitmap(bitmap, null, rect, null);
                float hueInPiInterval = colorHsv[0] / 180f * (float)Math.PI;

                selectedPoint.x = rect.left + (int) (-Math.cos( hueInPiInterval ) * colorHsv[1] * innerCircleRadius + fullCircleRadius);
                selectedPoint.y = rect.top + (int) (-Math.sin( hueInPiInterval ) * colorHsv[1] * innerCircleRadius + fullCircleRadius);

                canvas.drawLine( selectedPoint.x - pointerLength, selectedPoint.y, selectedPoint.x + pointerLength, selectedPoint.y, pointerPaint );
                canvas.drawLine( selectedPoint.x, selectedPoint.y - pointerLength, selectedPoint.x, selectedPoint.y + pointerLength, pointerPaint );
            }
        }

        private Rect rect;
        private Bitmap bitmap;

        private int[] pixels;
        private float innerCircleRadius;
        private float fullCircleRadius;

        private int scaledWidth;
        private int scaledHeight;
        private int[] scaledPixels;

        private float scaledInnerCircleRadius;
        private float scaledFullCircleRadius;
        private float scaledFadeOutSize;

        private Point selectedPoint = new Point();

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            rect = new Rect( innerPadding, innerPadding, w - innerPadding, h - innerPadding );
            bitmap = Bitmap.createBitmap( w - 2 * innerPadding, h - 2 * innerPadding, Config.ARGB_8888 );

            fullCircleRadius = Math.min( rect.width(), rect.height() ) / 2;
            innerCircleRadius = fullCircleRadius * ( 1 - FADE_OUT_FRACTION );

            scaledWidth = rect.width() / scale;
            scaledHeight = rect.height() / scale;
            scaledFullCircleRadius = Math.min( scaledWidth, scaledHeight ) / 2;
            scaledInnerCircleRadius = scaledFullCircleRadius * ( 1 - FADE_OUT_FRACTION );
            scaledFadeOutSize = scaledFullCircleRadius - scaledInnerCircleRadius;
            scaledPixels = new int[ scaledWidth * scaledHeight ];
            pixels = new int[ rect.width() * rect.height() ];

            createBitmap();
        }

        private void createBitmap() {
            int w = rect.width();
            int h = rect.height();

            float[] hsv = new float[] { 0f, 0f, 1f };
            int alpha = 255;

            int x = (int) -scaledFullCircleRadius, y = (int) -scaledFullCircleRadius;
            for ( int i = 0; i < scaledPixels.length; i++ ) {
                if ( i % scaledWidth == 0 ) {
                    x = (int) -scaledFullCircleRadius;
                    y++;
                } else {
                    x++;
                }

                double centerDist = Math.sqrt( x*x + y*y );
                if ( centerDist <= scaledFullCircleRadius ) {
                    hsv[ 0 ] = (float) (Math.atan2( y, x ) / Math.PI * 180f) + 180;
                    hsv[ 1 ] = (float) (centerDist / scaledInnerCircleRadius);
                    if ( centerDist <= scaledInnerCircleRadius ) {
                        alpha = 255;
                    } else {
                        alpha = 255 - (int) ((centerDist - scaledInnerCircleRadius) / scaledFadeOutSize * 255);
                    }
                    scaledPixels[ i ] = Color.HSVToColor( alpha, hsv );
                } else {
                    scaledPixels[ i ] = 0x00000000;
                }
            }

            int scaledX, scaledY;
            for( x = 0; x < w; x++ ) {
                scaledX = x / scale;
                if ( scaledX >= scaledWidth ) scaledX = scaledWidth - 1;
                for ( y = 0; y < h; y++ ) {
                    scaledY = y / scale;
                    if ( scaledY >= scaledHeight ) scaledY = scaledHeight - 1;
                    pixels[ x * h + y ] = scaledPixels[ scaledX * scaledHeight + scaledY ];
                }
            }

            bitmap.setPixels( pixels, 0, w, 0, 0, w, h );

            invalidate();
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            int maxWidth = MeasureSpec.getSize( widthMeasureSpec );
            int maxHeight = MeasureSpec.getSize( heightMeasureSpec );

            int width, height;
			/*
			 * Make the view quadratic, with height and width equal and as large as possible
			 */
            width = height = Math.min( maxWidth, maxHeight );

            setMeasuredDimension( width, height );
        }

        public int getColorForPoint( int x, int y, float[] hsv ) {
            x -= fullCircleRadius;
            y -= fullCircleRadius;
            double centerDist = Math.sqrt( x*x + y*y );
            hsv[ 0 ] = (float) (Math.atan2( y, x ) / Math.PI * 180f) + 180;
            hsv[ 1 ] = Math.max( 0f, Math.min( 1f, (float) (centerDist / innerCircleRadius) ) );
            return Color.HSVToColor( hsv );
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getActionMasked();
            switch ( action ) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    if ( listener != null ) {
                        listener.colorSelected( getColorForPoint( (int)event.getX(), (int)event.getY(), colorHsv ) );
                    }
                    invalidate();
                    return true;
            }
            return super.onTouchEvent(event);
        }

    }

    private static class HSVValueSlider extends View {

        public HSVValueSlider(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
        }

        public HSVValueSlider(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public HSVValueSlider(Context context) {
            super(context);
        }

        private OnColorSelectedListener listener;
        public void setListener( OnColorSelectedListener listener ) {
            this.listener = listener;
        }

        float[] colorHsv = { 0f, 0f, 1f };
        public void setColor( int color, boolean keepValue ) {
            float oldValue = colorHsv[2];
            Color.colorToHSV(color, colorHsv);
            if ( keepValue ) {
                colorHsv[2] = oldValue;
            }
            if ( listener != null ) {
                listener.colorSelected( Color.HSVToColor( colorHsv ) );
            }

            createBitmap();
        }

        @Override
        protected void onDraw(Canvas canvas) {
            if ( bitmap != null ) {
                canvas.drawBitmap(bitmap, srcRect, dstRect, null);
            }
        }
        private Rect srcRect;
        private Rect dstRect;
        private Bitmap bitmap;
        private int[] pixels;

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            super.onSizeChanged(w, h, oldw, oldh);

            srcRect = new Rect( 0, 0, w, 1 );
            dstRect = new Rect( 0, 0, w, h );
            bitmap = Bitmap.createBitmap( w, 1, Config.ARGB_8888 );
            pixels = new int[ w ];

            createBitmap();
        }

        private void createBitmap() {
            if ( bitmap == null ) {
                return;
            }
            int w = getWidth();

            float[] hsv = new float[] { colorHsv[0], colorHsv[1], 1f };

            int selectedX = (int) (colorHsv[ 2 ] * w);

            float value = 0;
            float valueStep = 1f / w;
            for( int x = 0; x < w; x++ ) {
                value += valueStep;
                if ( x >= selectedX - 1 && x <= selectedX + 1 ) {
                    int intVal = 0xFF - (int)( value * 0xFF );
                    int color = intVal * 0x010101 + 0xFF000000;
                    pixels[x] = color;
                } else {
                    hsv[2] = value;
                    pixels[x] = Color.HSVToColor( hsv );
                }
            }

            bitmap.setPixels( pixels, 0, w, 0, 0, w, 1 );

            invalidate();
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            int action = event.getActionMasked();
            switch ( action ) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_MOVE:
                    int x = Math.max( 0, Math.min( bitmap.getWidth() - 1, (int)event.getX() ) );
                    float value = x / (float)bitmap.getWidth();
                    if ( colorHsv[2] != value ) {
                        colorHsv[2] = value;
                        if ( listener != null ) {
                            listener.colorSelected( Color.HSVToColor( colorHsv ) );
                        }
                        createBitmap();
                        invalidate();
                    }
                    return true;
            }
            return super.onTouchEvent(event);
        }

    }

}