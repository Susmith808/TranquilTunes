package com.example.tranquiltunes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class VisualizerView extends View {
    private byte[] waveform;
    private Paint paint = new Paint();

    public VisualizerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public VisualizerView(Context context) {
        super(context);
        init();
    }

    private void init() {
        waveform = null;
        paint.setStrokeWidth(2f);
        paint.setAntiAlias(true);
        paint.setColor(0xFF00FF00); // Green color for the waveform
    }

    public void updateVisualizer(byte[] waveform) {
        this.waveform = waveform;
        invalidate(); // Request to redraw the view with the new data
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (waveform == null) return;

        float width = getWidth();
        float height = getHeight();
        for (int i = 0; i < waveform.length - 1; i++) {
            float x1 = width * i / (waveform.length - 1);
            float y1 = height / 2 + ((byte) (waveform[i] + 128)) * (height / 2) / 128;
            float x2 = width * (i + 1) / (waveform.length - 1);
            float y2 = height / 2 + ((byte) (waveform[i + 1] + 128)) * (height / 2) / 128;
            canvas.drawLine(x1, y1, x2, y2, paint);
        }
    }
}
