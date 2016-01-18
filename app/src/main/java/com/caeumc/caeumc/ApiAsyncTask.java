package com.caeumc.caeumc;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import com.caeumc.javautils.Calendario;
import com.caeumc.javautils.Item;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.util.DateTime;
import com.google.gson.Gson;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

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

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        NavDrawerActivity.swipeRefreshLayout.setEnabled(false);
        NavDrawerActivity.swipeRefreshLayout.setRefreshing(true);
    }

    protected void onPostExecute() {
        NavDrawerActivity.swipeRefreshLayout.setEnabled(true);
        NavDrawerActivity.swipeRefreshLayout.setRefreshing(false);

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


    private Calendario getCalendario(String mUrl) {
        Calendario calendario = new Calendario();
        Gson gson = new Gson();
        String json = getJson(mUrl);


        if (json != null) {
            if (!json.trim().isEmpty()) {

                calendario = gson.fromJson(json, Calendario.class);
            }
        }
        return calendario;
    }

    private String getJson(String mUrl) {
        URL url = null;
        String json=null;
        try {
            url = new URL(mUrl);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader r = new BufferedReader(new InputStreamReader(in));
            StringBuilder total = new StringBuilder();
            String line;
            while ((line = r.readLine()) != null) {
                total.append(line);
            }
            json = total.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
                urlConnection.disconnect();
            }
        return json;
    }



    /**
     * Fetch a list of the next 10 events from the primary calendar.
     * @return List of Strings describing returned events.
     * @throws IOException
     */
    private List<EventosListModel> getDataFromApi() throws IOException, ParseException {



        DateTime now = new DateTime(System.currentTimeMillis());
        Date hoje = new Date(System.currentTimeMillis());
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
                String sDiaMinimo = String.format("%s-01-01T00:00:00", anoatual);
                dateDiaMinimo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(sDiaMinimo);
                diaMinimo = new DateTime(dateDiaMinimo);
            }
            switch (nAlcanceAgenda) {
                case 12:
                   sDiaMaximo = String.format("%s-01-01T00:00:00", anoatual + 1);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
                case 18:
                    sDiaMaximo = String.format("%s-07-31T23:59:59", anoatual + 1);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
                case 24:
                   sDiaMaximo = String.format("%s-01-01T00:00:00", anoatual + 2);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
                default:
                   sDiaMaximo = String.format("%s-07-31T23:59:59", anoatual);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
            }

        } else {

            if (eventosAnteriores) {
                String sDiaMinimo = String.format("%s-08-01T00:00:00", anoatual);
                dateDiaMinimo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(sDiaMinimo);
                diaMinimo = new DateTime(dateDiaMinimo);
            }

            switch (nAlcanceAgenda) {
                case 12:
                    sDiaMaximo = String.format("%s-08-01T00:00:00", anoatual + 1);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
                case 18:
                    sDiaMaximo = String.format("%s-12-31T23:59:59", anoatual + 1);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
                case 24:
                    sDiaMaximo = String.format("%s-08-01T00:00:00", anoatual + 2);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
                default:
                    sDiaMaximo = String.format("%s-12-31T23:59:59", anoatual);
                    dateDiaMaximo = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(sDiaMaximo);
                    diaMaximo = new DateTime(dateDiaMaximo);
                    break;
            }
        }

        long ts = System.currentTimeMillis();
        Date localTime = new Date(ts);

        String idCalendario = "ssqpnnspvp4geaq93m8ubladok@group.calendar.google.com";

        List<Item> items = null;
        if (eventosAnteriores) {
            String urlCalendario = String.format("https://www.googleapis.com/calendar/v3/calendars/%s/events?key=AIzaSyCM-Vc1sw_K18OSHlXFYhxM08VE2YFIRwA&" +
                            "orderby=starttime&" +
                            "singleevents=true&" +
                            "maxResults=2500&" +
                            "ctz=America/Sao_Paulo&" +
                            "timeMin=%s&" +
                            "timeMax=%s",
                    idCalendario, diaMinimo.toStringRfc3339(), diaMaximo.toStringRfc3339());

            Calendario calendario = getCalendario(urlCalendario);
            items = calendario.getItems();
        } else {

            String urlCalendario = String.format("https://www.googleapis.com/calendar/v3/calendars/%s/events?key=AIzaSyCM-Vc1sw_K18OSHlXFYhxM08VE2YFIRwA&" +
                            "orderby=starttime&" +
                            "singleevents=true&" +
                            "maxResults=2500&" +
                            "ctz=America/Sao_Paulo&" +
                            "timeMin=%s&" +
                            "timeMax=%s",
                    idCalendario, now.toStringRfc3339(), diaMaximo.toStringRfc3339());

            Calendario calendario = getCalendario(urlCalendario);
            items = calendario.getItems();

        }

        if (items != null) {
            if (items.size() > 0) {
                for (Item item : items) {

                    String descricao = item.getSummary();
                    if (descricao == null) {
                        descricao = "(Sem título)";
                    }
                    long data;
                    long horaInicio;
                    long horaFinal;
                    String local = item.getLocation();
                    String observacao = item.getDescription();
                    //String start = item.getStart().getDate();
                    //DateTime start = new DateTime(item.getStart().getDate());
                    if (item.getStart().getDate() == null) {
                        // All-day events don't have start times, so just use
                        // the start date.
                        // the start date.

                        Calendar dataCalendario = Calendar.getInstance();
                        dataCalendario.setTimeInMillis(new DateTime(item.getStart().getDatetime()).getValue());
                        long timezonedif = -(dataCalendario.get(Calendar.ZONE_OFFSET)+dataCalendario.get(Calendar.DST_OFFSET));
                        data = new DateTime(item.getStart().getDatetime()).getValue() + TimeZone.getDefault().getOffset(localTime.getTime()) + timezonedif;
                        data = data / 1000;
                        horaInicio = new DateTime(item.getStart().getDatetime()).getValue() + TimeZone.getDefault().getOffset(localTime.getTime()) + timezonedif;
                        horaInicio = horaInicio / 1000;
                        horaFinal = new DateTime(item.getEnd().getDatetime()).getValue() + TimeZone.getDefault().getOffset(localTime.getTime()) + timezonedif;
                        horaFinal = horaFinal / 1000;


                    } else {
                        Calendar dataCalendario = Calendar.getInstance();
                        dataCalendario.setTimeInMillis(new DateTime(item.getStart().getDate()).getValue());
                        long timezonedif = -(dataCalendario.get(Calendar.ZONE_OFFSET)+dataCalendario.get(Calendar.DST_OFFSET));
                        data = new DateTime(item.getStart().getDate()).getValue() + TimeZone.getDefault().getOffset(localTime.getTime()) + timezonedif;;
                        data = data / 1000;
                        horaInicio = 0;
                        horaFinal = 0;
                        // the start date.

                    }
                    EventosListModel eventosListModel = new EventosListModel(descricao, (int)data, (int)horaInicio, (int)horaFinal, local, false, observacao);
                    eventosListModel.save();

                }
            }
        }


       /* List<String> eventStrings = new ArrayList<String>();
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
        }*/

        /*if ((mesatual+1) <=7) {
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
        }*/


        List<Item> itemsFeriado = null;
        if (eventosAnteriores) {
            String urlCalendario = String.format("https://www.googleapis.com/calendar/v3/calendars/%s/events?key=AIzaSyCM-Vc1sw_K18OSHlXFYhxM08VE2YFIRwA&" +
                            "orderby=starttime&" +
                            "singleevents=true&" +
                            "maxResults=2500&" +
                            "timeMin=%s&" +
                            "timeMax=%s",
                    "pt.brazilian%23holiday%40group.v.calendar.google.com", diaMinimo.toStringRfc3339(), diaMaximo.toStringRfc3339());

            Calendario calendario = getCalendario(urlCalendario);
            itemsFeriado = calendario.getItems();
        } else {

            String urlCalendario = String.format("https://www.googleapis.com/calendar/v3/calendars/%s/events?key=AIzaSyCM-Vc1sw_K18OSHlXFYhxM08VE2YFIRwA&" +
                            "orderby=starttime&" +
                            "singleevents=true&" +
                            "maxResults=2500&" +
                            "timeMin=%s&" +
                            "timeMax=%s",
                    "pt.brazilian%23holiday%40group.v.calendar.google.com", now.toStringRfc3339(), diaMaximo.toStringRfc3339());

            Calendario calendario = getCalendario(urlCalendario);
            itemsFeriado = calendario.getItems();

        }

        if (itemsFeriado != null) {
            if (itemsFeriado.size() > 0) {
                for (Item item : itemsFeriado) {

                    String descricao = item.getSummary();
                    if (descricao == null) {
                        descricao = "(Sem título)";
                    }
                    long data;
                    long horaInicio;
                    long horaFinal;
                    String local = item.getLocation();
                    String observacao = item.getDescription();


                   // String start = item.getStart().getDate();
                    if (item.getStart().getDate() == null) {
                        // All-day events don't have start times, so just use
                        // the start date.
                        // the start date.

                        Calendar dataCalendario = Calendar.getInstance();
                        dataCalendario.setTimeInMillis(new DateTime(item.getStart().getDatetime()).getValue());
                        long timezonedif = -(dataCalendario.get(Calendar.ZONE_OFFSET)+dataCalendario.get(Calendar.DST_OFFSET));
                        data = new DateTime(item.getStart().getDatetime()).getValue() + TimeZone.getDefault().getOffset(localTime.getTime());
                        data = data / 1000;
                        horaInicio = new DateTime(item.getStart().getDatetime()).getValue() + TimeZone.getDefault().getOffset(localTime.getTime());
                        horaInicio = horaInicio / 1000;
                        horaFinal = new DateTime(item.getEnd().getDatetime()).getValue() + TimeZone.getDefault().getOffset(localTime.getTime());
                        horaFinal = horaFinal / 1000;


                    } else {
                        Calendar dataCalendario = Calendar.getInstance();
                        dataCalendario.setTimeInMillis(new DateTime(item.getStart().getDate()).getValue());
                        long timezonedif = -(dataCalendario.get(Calendar.ZONE_OFFSET)+dataCalendario.get(Calendar.DST_OFFSET));
                        data = new DateTime(item.getStart().getDate()).getValue() + timezonedif;
                        data = data / 1000;
                        horaInicio = 0;
                        horaFinal = 0;
                        // the start date.

                    }
                    EventosListModel eventosListModel = new EventosListModel(descricao, (int)data, (int)horaInicio, (int)horaFinal, local, true, observacao);
                    eventosListModel.save();

                }
            }
        }

        /*Events events1;
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

        }*/
       List<EventosListModel> eventosListModel = EventosListModel.findWithQuery(EventosListModel.class, "SELECT * FROM EVENTOS_LIST_MODEL ORDER BY data*1000 ASC");
        return eventosListModel;
    }

}