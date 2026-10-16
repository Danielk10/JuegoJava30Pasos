package com.diamon.curso.diff;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Vista personalizada de mapa de calor (Heatmap) interactivo de sectores de 4KB.
 * Permite visualizar al instante la distribución de diferencias en volcados SPI
 * de 16MB, 32MB o 64MB y saltar con un toque al sector modificado.
 */
public class DiffHeatmapView extends View {

    public interface OnSectorSelectedListener {
        void onSectorSelected(int sectorIndex, long offset);
    }

    private MappedDiffDataSource dataSource;
    private OnSectorSelectedListener listener;

    private final Paint paintNeutral = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintModifiedLow = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintModifiedHigh = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintIndicator = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintSelection = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint paintBorder = new Paint(Paint.ANTI_ALIAS_FLAG);

    private final RectF cellRect = new RectF();
    private int selectedSector = -1;
    private long currentVisibleOffset = 0;

    public DiffHeatmapView(Context context) {
        super(context);
        init();
    }

    public DiffHeatmapView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DiffHeatmapView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paintNeutral.setColor(0xFF263238); // Gris oscuro azulado suave
        paintNeutral.setStyle(Paint.Style.FILL);

        paintModifiedLow.setColor(0xFFFFB300); // Ámbar PRO
        paintModifiedLow.setStyle(Paint.Style.FILL);

        paintModifiedHigh.setColor(0xFFFF5252); // Rojo intenso para alta densidad
        paintModifiedHigh.setStyle(Paint.Style.FILL);

        paintIndicator.setColor(0x8000E5FF); // Cian translúcido de viewport
        paintIndicator.setStyle(Paint.Style.FILL);

        paintSelection.setColor(0xFF00E5FF); // Borde cian de selección
        paintSelection.setStyle(Paint.Style.STROKE);
        paintSelection.setStrokeWidth(3f);

        paintBorder.setColor(0xFF37474F);
        paintBorder.setStyle(Paint.Style.STROKE);
        paintBorder.setStrokeWidth(2f);
    }

    public void setDataSource(MappedDiffDataSource dataSource) {
        this.dataSource = dataSource;
        this.selectedSector = -1;
        invalidate();
    }

    public void setOnSectorSelectedListener(OnSectorSelectedListener listener) {
        this.listener = listener;
    }

    public void setVisibleOffset(long offset) {
        this.currentVisibleOffset = offset;
        invalidate();
    }

    public void setSelectedSector(int sectorIndex) {
        this.selectedSector = sectorIndex;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getWidth();
        int height = getHeight();

        if (width <= 0 || height <= 0) return;

        // Borde exterior
        canvas.drawRect(1, 1, width - 1, height - 1, paintBorder);

        if (dataSource == null || dataSource.getSectorCount() <= 0) {
            canvas.drawRect(2, 2, width - 2, height - 2, paintNeutral);
            return;
        }

        int totalSectors = dataSource.getSectorCount();
        float sectorWidth = (float) (width - 4) / (float) totalSectors;

        // Renderizado de sectores
        for (int i = 0; i < totalSectors; i++) {
            float left = 2 + (i * sectorWidth);
            float right = 2 + ((i + 1) * sectorWidth);
            cellRect.set(left, 2, right, height - 2);

            int diffs = dataSource.getDiffCountInSector(i);
            if (diffs == 0) {
                canvas.drawRect(cellRect, paintNeutral);
            } else {
                float ratio = dataSource.getSectorDiffRatio(i);
                if (ratio > 0.05f) {
                    canvas.drawRect(cellRect, paintModifiedHigh);
                } else {
                    canvas.drawRect(cellRect, paintModifiedLow);
                }
            }
        }

        // Indicador de viewport visible
        int visibleSector = (int) (currentVisibleOffset / MappedDiffDataSource.SECTOR_SIZE);
        if (visibleSector >= 0 && visibleSector < totalSectors) {
            float vLeft = 2 + (visibleSector * sectorWidth);
            float vRight = Math.max(vLeft + 4f, 2 + ((visibleSector + 1) * sectorWidth));
            cellRect.set(vLeft, 2, vRight, height - 2);
            canvas.drawRect(cellRect, paintIndicator);
        }

        // Sector seleccionado actualmente
        if (selectedSector >= 0 && selectedSector < totalSectors) {
            float sLeft = 2 + (selectedSector * sectorWidth);
            float sRight = Math.max(sLeft + 6f, 2 + ((selectedSector + 1) * sectorWidth));
            cellRect.set(sLeft, 2, sRight, height - 2);
            canvas.drawRect(cellRect, paintSelection);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (dataSource == null || dataSource.getSectorCount() <= 0) {
            return super.onTouchEvent(event);
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                int totalSectors = dataSource.getSectorCount();
                float x = Math.max(0, Math.min(event.getX(), getWidth()));
                int sectorIndex = (int) ((x / (float) getWidth()) * totalSectors);
                if (sectorIndex >= totalSectors) sectorIndex = totalSectors - 1;

                selectedSector = sectorIndex;
                invalidate();

                if (event.getAction() == MotionEvent.ACTION_UP && listener != null) {
                    long offset = (long) sectorIndex * MappedDiffDataSource.SECTOR_SIZE;
                    listener.onSectorSelected(sectorIndex, offset);
                }
                return true;
        }

        return super.onTouchEvent(event);
    }
}
