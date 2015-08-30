package com.caeumc.caeumc;

import android.os.AsyncTask;
import android.util.Log;

import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.model.*;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * An asynchronous task that handles the Google Calendar API call.
 * Placing the API calls in their own task ensures the UI stays responsive.
 */
public class ApiAsyncTask extends AsyncTask<Void, Void, Void> {
    private NavDrawerActivity mActivity;

    /**
     * Constructor.
     * @param activity MainActivity that spawned this task.
     */
    ApiAsyncTask(NavDrawerActivity activity) {
        this.mActivity = activity;
    }

    /**
     * Background task to call Google Calendar API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected Void doInBackground(Void... params) {
        try {
            mActivity.clearResultsText();
            mActivity.updateResultsText(getDataFromApi());

        } catch (final GooglePlayServicesAvailabilityIOException availabilityException) {
            mActivity.showGooglePlayServicesAvailabilityErrorDialog(
                    availabilityException.getConnectionStatusCode());

        } catch (UserRecoverableAuthIOException userRecoverableException) {
            mActivity.startActivityForResult(
                    userRecoverableException.getIntent(),
                    NavDrawerActivity.REQUEST_AUTHORIZATION);

        } catch (Exception e) {
            mActivity.updateStatus("The following error occurred:\n" +
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
        List<String> eventStrings = new ArrayList<String>();
        Events events = mActivity.mService.events().list("primary")
                .setMaxResults(20)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();

        for (Event event : items) {

           String descricao = event.getSummary();
            if (descricao == null) {
                descricao = "(Sem título)";
            }
            long data = event.getStart().getDateTime().getValue();
            data = data / 1000;
            long horaInicio = event.getStart().getDateTime().getValue();
            horaInicio = horaInicio / 1000;
            long horaFinal = event.getEnd().getDateTime().getValue();
            horaFinal = horaFinal / 1000;
            String local = event.getLocation();



            DateTime start = event.getStart().getDateTime();
            if (start == null) {
                // All-day events don't have start times, so just use
                // the start date.
                start = event.getStart().getDate();
                data = event.getStart().getDate().getValue();
                horaInicio = 0;
                horaFinal = 0;
            }
            eventStrings.add(
                    String.format("%s (%s)", event.getSummary(), start));
            EventosListModel eventosListModel = new EventosListModel(descricao, (int)data, (int)horaInicio, (int)horaFinal, local, false);
            eventosListModel.save();
        }

       Date dateDiaMinimo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2015-08-03 00:00:00");
        DateTime diaMinimo = new DateTime(dateDiaMinimo);

        Date dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("2016-01-01 00:00:00");
        DateTime diaMaximo = new DateTime(dateDiaMaximo);

        Events events1 = mActivity.mService.events().list("pt.brazilian#holiday@group.v.calendar.google.com")
                .setTimeMin(diaMinimo)
                .setTimeMax(diaMaximo)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
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
                start = event.getStart().getDate();
                data = event.getStart().getDate().getValue();
                data = data / 1000;
                horaInicio = 0;
                horaFinal = 0;
            } else {

                data = event.getStart().getDateTime().getValue();
                data = data / 1000;
                horaInicio = event.getStart().getDateTime().getValue();
                horaFinal = event.getEnd().getDateTime().getValue();
            }
            eventStrings.add(
                    String.format("%s (%s)", event.getSummary(), start));
            EventosListModel eventosListModel = new EventosListModel(descricao, (int)data, (int)horaInicio, (int)horaFinal, local, true);
            eventosListModel.save();

        }
        List<EventosListModel> eventosList = EventosListModel.findWithQuery(EventosListModel.class, "SELECT * FROM EVENTOS_LIST_MODEL ORDER BY data*1000 ASC");
        return eventosList;
    }

}