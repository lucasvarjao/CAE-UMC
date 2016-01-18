package com.caeumc.caeumc;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.Events;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * An asynchronous task that handles the Google Calendar API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */
public class ApiAsyncTask extends AsyncTask<Void, Void, Void> {
    private NavDrawerActivity mActivity;
    String mEmail;

    /**
     * Constructor.
     * @param activity MainActivity that spawned this task.
     */
    ApiAsyncTask(NavDrawerActivity activity, String name) {
        this.mActivity = activity;
        this.mEmail = name;
    }

    /**
     * Background task to call Google Calendar API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {
            NavDrawerActivity.clearResultsText();
            NavDrawerActivity.updateResultsText(getDataFromApi());

        } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
            NavDrawerActivity.showGooglePlayServicesAvailabilityErrorDialog(
                    availabilityException.getConnectionStatusCode());

        } catch (UserRecoverableAuthIOException userRecoverableException) {
            mActivity.startActivityForResult(
                    userRecoverableException.getIntent(),
                    NavDrawerActivity.REQUEST_AUTHORIZATION);

        } catch (Exception e) {
            NavDrawerActivity.updateStatus("O seguinte erro ocorreu:\n" +
                    e.getMessage());
        }
        return null;
    }

    /**
     * Fetch a list of the next 10 events from the primary calendar.
     * @return List of Strings describing returned events.
     * @throws IOException
     */
    private List<EventosListModel> getDataFromApi() throws IOException, ParseException {
        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Date hoje = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        java.util.Calendar calendar = Calendar.getInstance();
        calendar.setTime(hoje);
        int mesatual = calendar.get(Calendar.MONTH);
        int anoatual = calendar.get(Calendar.YEAR);
        Date dateDiaMinimo;
        DateTime diaMinimo = null;
        Date dateDiaMaximo;
        DateTime diaMaximo;
        String sDiaMaximo;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mActivity);
        boolean eventosAnteriores = sharedPreferences.getBoolean("agenda_switch", false);
        String alcanceagenda = sharedPreferences.getString("alcance_agenda", "6");
        int nAlcanceAgenda = Integer.valueOf(alcanceagenda);

        if ((mesatual+1) <=7) {
            if (eventosAnteriores) {
                String sDiaMinimo = String.format("%s-01-01 00:00:00", anoatual);
                dateDiaMinimo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sDiaMinimo);
                diaMinimo = new DateTime(dateDiaMinimo);
            }
            switch (nAlcanceAgenda) {
                case 12:
                   sDiaMaximo = String.format("%s-01-01 00:00:00", anoatual + 1);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
                case 18:
                    sDiaMaximo = String.format("%s-07-31 23:59:59", anoatual + 1);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
                case 24:
                   sDiaMaximo = String.format("%s-01-01 00:00:00", anoatual + 2);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
                default:
                   sDiaMaximo = String.format("%s-07-31 23:59:59", anoatual);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
            }

        } else {

            if (eventosAnteriores) {
                String sDiaMinimo = String.format("%s-08-01 00:00:00", anoatual);
                dateDiaMinimo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(sDiaMinimo);
                diaMinimo = new DateTime(dateDiaMinimo);
            }

            switch (nAlcanceAgenda) {
                case 12:
                    sDiaMaximo = String.format("%s-08-01 00:00:00", anoatual + 1);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
                case 18:
                    sDiaMaximo = String.format("%s-12-31 23:59:59", anoatual + 1);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
                case 24:
                    sDiaMaximo = String.format("%s-08-01 00:00:00", anoatual + 2);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
                default:
                    sDiaMaximo = String.format("%s-12-31 23:59:59", anoatual);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
            }
        }
        List<String> eventStrings = new ArrayList<String>();
        Events events;
        if (eventosAnteriores) {
            events = NavDrawerActivity.mService.events().list("ssqpnnspvp4geaq93m8ubladok@group.calendar.google.com")
                    .setTimeMin(diaMinimo)
                    .setTimeMax(diaMaximo)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();

        } else {
            events = NavDrawerActivity.mService.events().list("ssqpnnspvp4geaq93m8ubladok@group.calendar.google.com")
                    .setTimeMin(now)
                    .setTimeMax(diaMaximo)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
        }
        List<Event> items = events.getItems();

        for (Event event : items) {

           String descricao = event.getSummary();
            if (descricao == null) {
                descricao = "(Sem título)";
            }
            long data;
            long horaInicio;
            long horaFinal;
            String local = event.getLocation();
            String observacao = event.getDescription();


            DateTime start = event.getStart().getDateTime();
            if (start == null) {
                // All-day events don't have start times, so just use
                // the start date.
                // the start date.
                Calendar dataCalendario = Calendar.getInstance();
                dataCalendario.setTimeInMillis(event.getStart().getDate().getValue());
                long timezonedif = -(dataCalendario.get(Calendar.ZONE_OFFSET)+dataCalendario.get(Calendar.DST_OFFSET));
                data = event.getStart().getDate().getValue() + timezonedif;
                data = data / 1000;
                horaInicio = 0;
                horaFinal = 0;

            } else {
                // the start date.
                Calendar dataCalendario = Calendar.getInstance();
                dataCalendario.setTimeInMillis(event.getStart().getDateTime().getValue());
                long timezonedif = -(dataCalendario.get(Calendar.ZONE_OFFSET)+dataCalendario.get(Calendar.DST_OFFSET));
                data = event.getStart().getDateTime().getValue() + timezonedif;
                data = data / 1000;
                horaInicio = event.getStart().getDateTime().getValue();
                horaInicio = horaInicio / 1000;
                horaFinal = event.getEnd().getDateTime().getValue();
                horaFinal = horaFinal / 1000;
            }
            eventStrings.add(
                    String.format("%s (%s)", event.getSummary(), start));
            EventosListModel eventosListModel = new EventosListModel(descricao, (int)data, (int)horaInicio, (int)horaFinal, local, false, observacao);
            eventosListModel.save();
        }

        if ((mesatual+1) <=7) {
            if (eventosAnteriores) {
                String sDiaMinimo = String.format("%s-01-01 00:00:00", anoatual);
                dateDiaMinimo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(sDiaMinimo);
                diaMinimo = new DateTime(dateDiaMinimo);
            }
            switch (nAlcanceAgenda) {
                case 12:
                    sDiaMaximo = String.format("%s-01-01 00:00:00", anoatual + 1);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
                case 18:
                    sDiaMaximo = String.format("%s-07-31 23:59:59", anoatual + 1);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
                case 24:
                    sDiaMaximo = String.format("%s-01-01 00:00:00", anoatual + 2);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
                default:
                    sDiaMaximo = String.format("%s-07-31 23:59:59", anoatual);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
            }

        } else {

            if (eventosAnteriores) {
                String sDiaMinimo = String.format("%s-08-01 00:00:00", anoatual);
                dateDiaMinimo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(sDiaMinimo);
                diaMinimo = new DateTime(dateDiaMinimo);
            }

            switch (nAlcanceAgenda) {
                case 12:
                    sDiaMaximo = String.format("%s-08-01 00:00:00", anoatual + 1);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
                case 18:
                    sDiaMaximo = String.format("%s-12-31 23:59:59", anoatual + 1);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
                case 24:
                    sDiaMaximo = String.format("%s-08-01 00:00:00", anoatual + 2);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
                default:
                    sDiaMaximo = String.format("%s-12-31 23:59:59", anoatual);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
            }
        }
        Events events1;
        if (eventosAnteriores) {
            events1 = NavDrawerActivity.mService.events().list("pt.brazilian#holiday@group.v.calendar.google.com")
                    .setTimeMin(diaMinimo)
                    .setTimeMax(diaMaximo)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
        } else {
            events1 = NavDrawerActivity.mService.events().list("pt.brazilian#holiday@group.v.calendar.google.com")
                    .setTimeMin(now)
                    .setTimeMax(diaMaximo)
                    .setOrderBy("startTime")
                    .setSingleEvents(true)
                    .execute();
        }
        List<Event> items2 = events1.getItems();

        for (Event event : items2) {
            String descricao = event.getSummary();
           long data;
            long horaInicio;
            long horaFinal;
            String local = event.getLocation();
            DateTime start = event.getStart().getDateTime();
            if (start == null) {
                // All-day events don't have start times, so just use
                // the start date.
                Calendar dataCalendario = Calendar.getInstance();
                dataCalendario.setTimeInMillis(event.getStart().getDate().getValue());
                long timezonedif = -(dataCalendario.get(Calendar.ZONE_OFFSET)+dataCalendario.get(Calendar.DST_OFFSET));
                data = event.getStart().getDate().getValue() + timezonedif;
                data = data / 1000;
                horaInicio = 0;
                horaFinal = 0;
            } else {

                // the start date.
                Calendar dataCalendario = Calendar.getInstance();
                dataCalendario.setTimeInMillis(event.getStart().getDateTime().getValue());
                long timezonedif = -(dataCalendario.get(Calendar.ZONE_OFFSET)+dataCalendario.get(Calendar.DST_OFFSET));
                data = event.getStart().getDateTime().getValue() + timezonedif;
                data = data / 1000;
                horaInicio = event.getStart().getDateTime().getValue();
                horaFinal = event.getEnd().getDateTime().getValue();
            }
            eventStrings.add(
                    String.format("%s (%s)", event.getSummary(), start));
            EventosListModel eventosListModel = new EventosListModel(descricao, (int)data, (int)horaInicio, (int)horaFinal, local, true, "");
            eventosListModel.save();

        }
       List<EventosListModel> eventosListModel = EventosListModel.findWithQuery(EventosListModel.class, "SELECT * FROM EVENTOS_LIST_MODEL ORDER BY data*1000 ASC");
        return eventosListModel;
    }

}