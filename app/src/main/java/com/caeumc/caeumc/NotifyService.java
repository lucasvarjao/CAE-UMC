package com.caeumc.caeumc;/*
 * Created by EDNEI on 06/02/2016.
 */

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.app.Service;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class NotifyService extends Service {

    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {
        NotifyService getService() {
            return NotifyService.this;
        }
    }

    // Unique id to identify the notification.
    private static final int NOTIFICATION = 123;
    // Name of an intent extra we can use to identify if this service was started to create a notification
    public static final String INTENT_NOTIFY = "com.caeumc.caeumc.INTENT_NOTIFY";
    // The system notification manager
    private NotificationManager mNM;

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");
        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("LocalService", "Received start id " + startId + ": " + intent);

        // If this service was started by out AlarmTask intent then we want to show our notification
        if(intent.getBooleanExtra(INTENT_NOTIFY, false))
            showNotification(intent.getLongExtra("idEvento", 0), intent.getStringExtra("Descricao"), intent.getLongExtra("Data", 0), intent.getLongExtra("HoraInicio", 0),
                    intent.getLongExtra("HoraFinal",0), intent.getStringExtra("Local"));

        // We don't care if this service is stopped as we have already delivered our notification
        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();

    /**
     * Creates a notification and shows it in the OS drag-down status bar
     */
    private void showNotification(long idEventoString, String descricao, long data, long horainicio, long horafinal, String local) {
        long time = System.currentTimeMillis();
        Intent intent = new Intent(this, AgendaDetails.class);
        intent.putExtra("idEvento", idEventoString);
        PendingIntent contentIntent = PendingIntent.getActivity(this, (int)idEventoString, intent, 0);

        String datahora;
        String localnull;

        if (horainicio == 0) {

            datahora = "Hoje";

        } else {
            Date horaInicioEvento = new Date(horainicio);
            @SuppressLint("SimpleDateFormat") DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            String horaInicioFormatada = dateFormat.format(horaInicioEvento);

            Date horaFinalEvento = new Date(horafinal);
            @SuppressLint("SimpleDateFormat") DateFormat dateFormatFinal = new SimpleDateFormat("HH:mm");
            String horaFinalFormatada = dateFormatFinal.format(horaFinalEvento);
            datahora = String.format("%s - %s", horaInicioFormatada, horaFinalFormatada);
        }

        if (local == null) {
            localnull = "";
        } else {
            localnull = local;
        }

        String contenttext = String.format("%s em %s", datahora, localnull);

        NotificationCompat.InboxStyle inboxStyle = new android.support.v4.app.NotificationCompat.InboxStyle();

        inboxStyle.addLine(datahora);
        inboxStyle.addLine(localnull);

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_calendar_grey600_24dp);

        android.support.v4.app.NotificationCompat.Builder notification;

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {

            notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_launcher)
                    .setLargeIcon(bitmap)
                    .setContentTitle(descricao)
                    .setContentText(contenttext)
                    .setContentIntent(contentIntent)
                    .setStyle(inboxStyle)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .setWhen(time);

        } else {

            notification = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launchercae)
                    .setLargeIcon(bitmap)
                    .setContentTitle(descricao)
                    .setContentText(contenttext)
                    .setContentIntent(contentIntent)
                    .setStyle(inboxStyle)
                    .setWhen(time);

        }

        notification.setDefaults(Notification.DEFAULT_ALL);


        // Clear the notification when it is pressed
        int idEvento = (int) idEventoString;
        // Send the notification to the system.
        mNM.notify(idEvento, notification.build());
        // Stop the service when we are finished
        stopSelf();
    }
}