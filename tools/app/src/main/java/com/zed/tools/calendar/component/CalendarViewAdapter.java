package com.zed.tools.calendar.component;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.zed.tools.calendar.interf.OnAdapterSelectListener;
import com.zed.tools.calendar.interf.IDayRenderer;
import com.zed.tools.calendar.interf.OnSelectDateListener;
import com.zed.tools.calendar.Utils;
import com.zed.tools.calendar.view.MonthPager;
import com.zed.tools.calendar.model.CalendarDate;
import com.zed.tools.calendar.view.Calendar;

import java.util.ArrayList;
import java.util.HashMap;

public class CalendarViewAdapter extends PagerAdapter {

    private static CalendarDate date = new CalendarDate();
    private ArrayList<Calendar> calendars = new ArrayList<>();
    private int currentPosition = MonthPager.CURRENT_DAY_INDEX;
    private int rowCount = 0;
    private CalendarDate seedDate;
    //周排列方式 1：代表周日显示为本周的第一天
    //0:代表周一显示为本周的第一天
    private CalendarAttr.WeekArrayType weekArrayType = CalendarAttr.WeekArrayType.Monday;

    public CalendarViewAdapter(Context context,
                               OnSelectDateListener onSelectDateListener,
                               CalendarAttr.WeekArrayType weekArrayType,
                               IDayRenderer dayView) {
        super();
        this.weekArrayType = weekArrayType;
        init(context, onSelectDateListener);
        setCustomDayRenderer(dayView);
    }

    public static void saveSelectedDate(CalendarDate calendarDate) {
        date = calendarDate;
    }

    public static CalendarDate loadSelectedDate() {
        return date;
    }

    private void init(Context context, OnSelectDateListener onSelectDateListener) {
        saveSelectedDate(new CalendarDate());
        //初始化的种子日期为今天
        seedDate = new CalendarDate();
        for (int i = 0; i < 3; i++) {
            CalendarAttr calendarAttr = new CalendarAttr();
            calendarAttr.setWeekArrayType(weekArrayType);
            Calendar calendar = new Calendar(context, onSelectDateListener, calendarAttr);
            calendar.setOnAdapterSelectListener(new OnAdapterSelectListener() {
                @Override
                public void cancelSelectState() {
                    cancelOtherSelectState();
                }

                @Override
                public void updateSelectState() {
                    invalidateCurrentCalendar();
                }
            });
            calendars.add(calendar);
        }
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Log.e("ldf", "setPrimaryItem");
        super.setPrimaryItem(container, position, object);
        this.currentPosition = position;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Log.e("ldf", "instantiateItem");
        if (position < 2) {
            return null;
        }
        Calendar calendar = calendars.get(position % calendars.size());
        CalendarDate current = seedDate.modifyMonth(position - MonthPager.CURRENT_DAY_INDEX);
        current.setDay(1);//每月的种子日期都是1号
        calendar.showDate(current);
        if (container.getChildCount() == calendars.size()) {
            container.removeView(calendars.get(position % 3));
        }
        if (container.getChildCount() < calendars.size()) {
            container.addView(calendar, 0);
        } else {
            container.addView(calendar, position % 3);
        }
        return calendar;
    }

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(container);
    }

    public ArrayList<Calendar> getPagers() {
        return calendars;
    }

    public CalendarDate getFirstVisibleDate() {
        return calendars.get(currentPosition % 3).getFirstDate();
    }

    public CalendarDate getLastVisibleDate() {
        return calendars.get(currentPosition % 3).getLastDate();
    }

    public void cancelOtherSelectState() {
        for (int i = 0; i < calendars.size(); i++) {
            Calendar calendar = calendars.get(i);
            calendar.cancelSelectState();
        }
    }

    public void invalidateCurrentCalendar() {
        for (int i = 0; i < calendars.size(); i++) {
            Calendar calendar = calendars.get(i);
            calendar.update();
        }
    }

    public void setMarkData(HashMap<String, String> markData) {
        Utils.setMarkData(markData);
        notifyDataChanged();
    }

    public void notifyMonthDataChanged(CalendarDate date) {
        seedDate = date;
        refreshCalendar();
    }

    public void notifyDataChanged(CalendarDate date) {
        seedDate = date;
        saveSelectedDate(date);
        refreshCalendar();
    }

    public void notifyDataChanged() {
        refreshCalendar();
    }

    private void refreshCalendar() {
        MonthPager.CURRENT_DAY_INDEX = currentPosition;

        Calendar v1 = calendars.get(currentPosition % 3);//0
        v1.showDate(seedDate);

        Calendar v2 = calendars.get((currentPosition - 1) % 3);//2
        CalendarDate last = seedDate.modifyMonth(-1);
        last.setDay(1);
        v2.showDate(last);

        Calendar v3 = calendars.get((currentPosition + 1) % 3);//1
        CalendarDate next = seedDate.modifyMonth(1);
        next.setDay(1);
        v3.showDate(next);
    }

    /**
     * 为每一个Calendar实例设置renderer对象
     *
     * @return void
     */
    public void setCustomDayRenderer(IDayRenderer dayRenderer) {
        Calendar c0 = calendars.get(0);
        c0.setDayRenderer(dayRenderer);

        Calendar c1 = calendars.get(1);
        c1.setDayRenderer(dayRenderer.copy());

        Calendar c2 = calendars.get(2);
        c2.setDayRenderer(dayRenderer.copy());
    }

    public CalendarAttr.WeekArrayType getWeekArrayType() {
        return weekArrayType;
    }
}