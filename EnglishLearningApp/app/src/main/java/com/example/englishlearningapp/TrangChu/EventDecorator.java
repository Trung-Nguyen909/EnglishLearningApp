package com.example.englishlearningapp.TrangChu;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.style.ForegroundColorSpan;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Collection;
import java.util.HashSet;

public class EventDecorator implements DayViewDecorator {

    private final int drawableResId;
    private final HashSet<CalendarDay> dates;
    private final Drawable drawable;

    public EventDecorator(int drawableResId, Collection<CalendarDay> dates, Activity context) {
        this.drawableResId = drawableResId;
        this.dates = new HashSet<>(dates);
        // Load hình tròn (xanh/đỏ) từ drawable
        this.drawable = ContextCompat.getDrawable(context, drawableResId);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        // Chỉ tô màu những ngày có trong danh sách
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        // Set background cho ngày đó
        if (drawable != null) {
            Drawable wrappedDrawable = DrawableCompat.wrap(drawable.getConstantState().newDrawable());
            view.setSelectionDrawable(wrappedDrawable);
        }
        // Đổi màu chữ thành trắng khi được tô màu
        view.addSpan(new ForegroundColorSpan(Color.WHITE));
    }
}

