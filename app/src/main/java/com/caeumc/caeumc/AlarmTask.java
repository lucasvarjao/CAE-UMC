package com.caeumc.caeumc;/*
 * Created by EDNEI on 06/02/2016.
 */
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

public class AlarmTask implements Runnable{
    // The date selected for the alarm
    private final Calendar date;
    // The android system alarm manager
    private final AlarmManager am;
    // Your context to retrieve the alarm manager from
    private final Context context;

    private final String mDescricao;

    private final long mData;

    private final long mHorainicio;

    private final long mHorafinal;

    private final String mLocal;

    private final long mIdEvento;

    public AlarmTask(Context context, Calendar date, String descricao, long data, long horainicio, long horafinal, String local, long idEvento) {
        this.context = context;
        this.am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        this.date = date;
        this.mDescricao = descricao;
        this.mData =data;
        this.mHorainicio =  horainicio;
        this.mHorafinal = horafinal;
        this.mLocal = local;
        this.mIdEvento = idEvento;
    }

    @Override
    public void run() {
        // Request to start are service when the alarm date is upon us
        // We don't start an activity as we just want to pop up a notification into the system bar not a full activity
        Intent intent = new Intent(context, NotifyService.class);
        intent.putExtra(NotifyService.INTENT_NOTIFY, true);
        intent.putExtra("Descricao", mDescricao);
        intent.putExtra("Data", mData);
        intent.putExtra("HoraInicio", mHorainicio);
        intent.putExtra("HoraFinal", mHorafinal);
        intent.putExtra("Local", mLocal);
        intent.putExtra("idEvento", mIdEvento);
        PendingIntent pendingIntent = PendingIntent.getService(context, (int)mIdEvento, intent, 0);

        // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
        am.set(AlarmManager.RTC_WAKEUP, date.getTimeInMillis(), pendingIntent);
    }
}