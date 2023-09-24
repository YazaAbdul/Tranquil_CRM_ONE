/*
 * Copyright 2015 Blanyal D'Souza.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package crm.tranquil_sales_steer.ui.activities.todo_list;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;
import java.util.List;

import crm.tranquil_sales_steer.domain.database.TaskDataBase;


public class _ToDoBootReceiver extends BroadcastReceiver {

    private String mTitle;
    private String mTime;
    private String mDate;
    private String mRepeatType;
    private String[] mDateSplit;
    private String[] mTimeSplit;

    private int mYear, mMonth, mHour, mMinute, mDay, mReceivedID;
    private long mRepeatTime;


    private Calendar mCalendar;
    private _ToDoAlarmReceiver m_ToDoAlarmReceiver;

    // Constant values in milliseconds
    private static final long milMinute = 60000L;
    private static final long milHour = 3600000L;
    private static final long milDay = 86400000L;
    private static final long milWeek = 604800000L;
    private static final long milMonth = 2592000000L;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            TaskDataBase rb = new TaskDataBase(context);
            mCalendar = Calendar.getInstance();
            m_ToDoAlarmReceiver = new _ToDoAlarmReceiver();

            List<TaskDataBase.TasksResponse> reminders = rb.getAllTasks();

            for (TaskDataBase.TasksResponse rm : reminders) {


                mReceivedID = rm.getTask_id();
                mRepeatType = rm.getTask_repeat();
                mDate = rm.getTask_date();
                mTime = rm.getTask_time();
                mDateSplit = mDate.split("/");
                mTimeSplit = mTime.split(":");

                mDay = Integer.parseInt(mDateSplit[0]);
                mMonth = Integer.parseInt(mDateSplit[1]);
                mYear = Integer.parseInt(mDateSplit[2]);
                mHour = Integer.parseInt(mTimeSplit[0]);
                mMinute = Integer.parseInt(mTimeSplit[1]);

                mCalendar.set(Calendar.MONTH, --mMonth);
                mCalendar.set(Calendar.YEAR, mYear);
                mCalendar.set(Calendar.DAY_OF_MONTH, mDay);
                mCalendar.set(Calendar.HOUR_OF_DAY, mHour);
                mCalendar.set(Calendar.MINUTE, mMinute);
                mCalendar.set(Calendar.SECOND, 0);

                // Cancel existing notification of the reminder by using its ID
                // m_ToDoAlarmReceiver.cancelAlarm(context, mReceivedID);

                // Check repeat type
                if (mRepeatType.equals("Minute")) {
                    mRepeatTime = Integer.parseInt("1") * milMinute;
                } else if (mRepeatType.equals("Hour")) {
                    mRepeatTime = Integer.parseInt("1") * milHour;
                } else if (mRepeatType.equals("Day")) {
                    mRepeatTime = Integer.parseInt("1") * milDay;
                } else if (mRepeatType.equals("Week")) {
                    mRepeatTime = Integer.parseInt("1") * milWeek;
                } else if (mRepeatType.equals("Month")) {
                    mRepeatTime = Integer.parseInt("1") * milMonth;
                }

                // Create a new notification

                m_ToDoAlarmReceiver.setRepeatAlarm(context, mCalendar, mReceivedID, mRepeatTime);

            }
        }
    }
}