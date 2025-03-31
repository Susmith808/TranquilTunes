package com.example.tranquiltunes;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

public class SpectrumAnalyzerView extends View {
    private byte[] fftData;
    private Paint barPaint;
    private int barCount = 32; // Number of frequency bands

    public SpectrumAnalyzerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        barPaint = new Paint();
        barPaint.setColor(Color.CYAN);
        barPaint.setStyle(Paint.Style.FILL);
    }

    public void updateFFT(byte[] fft) {
        this.fftData = fft;
        invalidate(); // Redraw the spectrum
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (fftData == null) return;

        int width = getWidth();
        int height = getHeight();
        int barWidth = width / barCount;

        for (int i = 0; i < barCount; i++) {
            int index = i * 2;
            if (index >= fftData.length) break;

            int magnitude = (int) Math.sqrt(fftData[index] * fftData[index] + fftData[index + 1] * fftData[index + 1]);
            int barHeight = Math.min(height, magnitude * 2);

            int x = i * barWidth;
            int y = height - barHeight;
            canvas.drawRect(x, y, x + barWidth - 2, height, barPaint);
        }
    }
}
