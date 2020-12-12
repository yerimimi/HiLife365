package com.hilifecare.ui.myrecord;

import com.github.mikephil.charting.charts.BarLineChartBase;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by imcreator on 2017. 6. 10..
 */

public class DayOfWeekAxisValueFormatter implements IAxisValueFormatter {
    protected String[] mDayOfWeeks = new String[]{
            "월", "화", "수", "목", "금", "토", "일"
    };

    private BarLineChartBase<?> chart;

    public DayOfWeekAxisValueFormatter(BarLineChartBase<?> chart) {
        this.chart = chart;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        return mDayOfWeeks[((int) value) % mDayOfWeeks.length];
    }
}
