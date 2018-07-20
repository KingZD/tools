package com.zed.tools.calendar;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.zed.tools.R;
import com.zed.tools.calendar.component.CalendarAttr;
import com.zed.tools.calendar.component.CalendarViewAdapter;
import com.zed.tools.calendar.datapicker.time.HourAndMinutePicker;
import com.zed.tools.calendar.interf.OnSelectDateListener;
import com.zed.tools.calendar.model.CalendarDate;
import com.zed.tools.calendar.view.Calendar;
import com.zed.tools.calendar.view.CustomDayView;
import com.zed.tools.calendar.view.MonthPager;

import java.util.ArrayList;

public class CalendarView extends RelativeLayout implements View.OnClickListener {
    MonthPager monthPager;
    HourAndMinutePicker flTime;
    LinearLayout llDate;
    CalendarDate currentDate;
    RadioButton btYear, btTime;
    private Button btSure;
    private int mCurrentPage = MonthPager.CURRENT_DAY_INDEX;
    CalendarViewAdapter calendarAdapter;
    private ArrayList<Calendar> currentCalendars = new ArrayList<>();

    public CalendarView(@NonNull Context context) {
        super(context);
        initView();
    }

    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public CalendarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.calendar_view, this, true);
        monthPager = findViewById(R.id.calendar_view);
        flTime = findViewById(R.id.flTime);
        llDate = findViewById(R.id.llDate);
        btYear = findViewById(R.id.btYear);
        btYear.setOnClickListener(this);

        btSure = findViewById(R.id.btSure);
        btSure.setOnClickListener(this);

        btTime = findViewById(R.id.btTime);
        btTime.setOnClickListener(this);


        flTime.setOnTimeSelectedListener(new HourAndMinutePicker.OnTimeSelectedListener() {
            @Override
            public void onTimeSelected(int hour, int minute) {
                btTime.setText(flTime.getTime());
            }
        });

        //初始化日历
        initCalendar();
        initCurrentDate();
    }

    void initCalendar() {
        CustomDayView customDayView = new CustomDayView(getContext(), R.layout.custom_day);
        calendarAdapter = new CalendarViewAdapter(
                getContext(),
                new OnSelectDateListener() {
                    @Override
                    public void onSelectDate(CalendarDate date) {
                        refreshClickDate(date);
                        llDate.setVisibility(GONE);
                        flTime.setVisibility(VISIBLE);
                        btTime.setChecked(true);
                    }

                    @Override
                    public void onSelectOtherMonth(int offset) {
                        //偏移量 -1表示刷新成上一个月数据 ， 1表示刷新成下一个月数据
//                        monthPager.selectOtherMonth(offset);
                    }
                },
                CalendarAttr.WeekArrayType.Monday,
                customDayView);
        monthPager.setAdapter(calendarAdapter);
        monthPager.setCurrentItem(mCurrentPage);
        monthPager.setPageTransformer(false, new ViewPager.PageTransformer() {
            @Override
            public void transformPage(View page, float position) {
                position = (float) Math.sqrt(1 - Math.abs(position));
                page.setAlpha(position);
            }
        });
        monthPager.addOnPageChangeListener(new MonthPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mCurrentPage = position;
                currentCalendars = calendarAdapter.getPagers();
                if (currentCalendars.get(position % currentCalendars.size()) != null) {
                    CalendarDate date = currentCalendars.get(position % currentCalendars.size()).getSeedDate();
                    currentDate = date;
                    refreshClickDate(date);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }


    /**
     * 初始化currentDate
     *
     * @return void
     */
    private void initCurrentDate() {
        currentDate = new CalendarDate();
        btYear.setText(currentDate.toString());
    }

    private void refreshClickDate(CalendarDate date) {
        currentDate = date;
        btYear.setText(currentDate.toString());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btYear:
                btYear.setChecked(true);
                llDate.setVisibility(VISIBLE);
                flTime.setVisibility(GONE);
                break;
            case R.id.btTime:
                btTime.setChecked(true);
                llDate.setVisibility(GONE);
                flTime.setVisibility(VISIBLE);
                break;
            case R.id.btSure:
                Toast.makeText(getContext(), currentDate.toString().concat(" ").concat(flTime.getTime()), Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
