package com.example.homebabysit;

import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.List;

public class MyEventDecorator implements DayViewDecorator {

    private final List<CalendarDay> dates;
    private final int color;

    public MyEventDecorator(List<CalendarDay> dates, int color) {
        this.dates = dates;
        this.color = color;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        // Add multiple DotSpans to create a larger dot effect
        view.addSpan(new DotSpan(10, color)); // Larger dot size
        view.addSpan(new DotSpan(12, color)); // Add another layer to make it appear bigger
        view.addSpan(new DotSpan(14, color)); // Optional: add one more layer
    }
}
