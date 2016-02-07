package com.caeumc.caeumc;/*
 * Created by EDNEI on 07/02/2016.
 */

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import java.util.Calendar;
import java.util.List;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(final Context context, Intent intent) {
        // get your stored alarms from your database
        // reregister them with the alarm manager

        Handler handler = new Handler(Looper.getMainLooper());
        final Runnable r = new Runnable() {
            public void run() {


                List<EventosListModel> eventosListModels = EventosListModel.findWithQuery(EventosListModel.class, "SELECT * FROM EVENTOS_LIST_MODEL WHERE feriado = 0 ORDER BY data*1000 ASC");


                for (EventosListModel eventosListModel : eventosListModels) {
                    java.util.Calendar calendar = Calendar.getInstance();
                    long data = eventosListModel.getData()*1000L;
                    long horainicio = eventosListModel.getHoraInicio()*1000L;
                    long horafinal = eventosListModel.getHoraFinal()*1000L;
                    long idEvento = eventosListModel.getId();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.clear();
                    calendar.setTimeInMillis(data);
                    calendar.set(Calendar.HOUR_OF_DAY, 18);
                    calendar.set(Calendar.MINUTE,   20);
                    calendar.set(Calendar.SECOND, 0);
                    Intent intent = new Intent(context, NotifyService.class);
                    intent.putExtra(NotifyService.INTENT_NOTIFY, true);
                    intent.putExtra("Descricao", eventosListModel.getDescricao());
                    intent.putExtra("Data", data);
                    intent.putExtra("HoraInicio", horainicio);
                    intent.putExtra("HoraFinal", horafinal);
                    intent.putExtra("Local", eventosListModel.getLocal());
                    intent.putExtra("idEvento", eventosListModel.getId());
                    PendingIntent pendingIntent = PendingIntent.getService(context, (int)idEvento, intent, 0);

                    // Sets an alarm - note this alarm will be lost if the phone is turned off and on again
                    AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                    am.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);

                }
                //do your stuff here after DELAY sec
            }
        };
        handler.postDelayed(r, 120000);

    }

}
