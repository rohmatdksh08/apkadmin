package com.jatmika.admin_e_complaintrangkasbitung.Model;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;

public class PercentFormatter implements IValueFormatter {

    private DecimalFormat mFormat;

    public PercentFormatter(){
        mFormat = new DecimalFormat("###,###,##0.#");
    }

    public PercentFormatter(DecimalFormat format){
        this.mFormat = format;
    }

    @Override
    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler){
        if (value == 0.0f)
            return "";
            return mFormat.format(value) + "%";
    }
}
