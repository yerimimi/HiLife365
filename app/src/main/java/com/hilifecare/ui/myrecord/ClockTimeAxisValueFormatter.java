package com.hilifecare.ui.myrecord;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by imcreator on 2017. 6. 10..
 */

public class ClockTimeAxisValueFormatter implements IAxisValueFormatter {
    protected String[] clockTime = new String[]{
            "오전 12:00", "정오", "오후 11:59"
    };

    private BarLineChartBase<?> chart;

    public ClockTimeAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return clockTime[((int) value) % clockTime.length];
    }
}
