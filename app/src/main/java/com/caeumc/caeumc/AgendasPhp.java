package com.caeumc.caeumc;

import android.os.AsyncTask;

import com.caeumc.php.Agenda;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by EDNEI on 22/01/2016.
 */
public class AgendasPhp extends AsyncTask<String, Agenda, ArrayList<Agenda>> {

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        NavDrawerActivity.swipeRefreshLayout.setEnabled(false);
        NavDrawerActivity.swipeRefreshLayout.setRefreshing(true);
    }

    @Override
    protected void onPostExecute(ArrayList<Agenda> agendas) {
        NavDrawerActivity.swipeRefreshLayout.setEnabled(true);
        NavDrawerActivity.swipeRefreshLayout.setRefreshing(false);

    }

    /**
     * Background task to call Google Calendar API.
     * @param params no parameters needed for this task.
     */
    @Override
    protected ArrayList<Agenda> doInBackground(String... params) {
        ArrayList<Agenda> agendas = new ArrayList<Agenda>();

        if (params[0].equals("getAgendasIdentificacao")) {
            agendas = getAgendasIdentificacao(params[1]);
        } else if (params[0].equals("getAgendasCompartilhadas")) {
            agendas = getAgendasCompartilhadas();
        }

        return agendas;

    }

    public static ArrayList<Agenda> getAgendas() {
        String mUrl = "http://caeumc.com/admin/getAgendasAndroid.php";


        String json = getJson(mUrl);
        ArrayList<Agenda> agendas = new ArrayList<Agenda>();

        if (json != null) {
            if (!json.trim().isEmpty()) {

                Agenda agenda = new Agenda();
                Gson gson = new Gson();
                JsonParser jsonParser = new JsonParser();
                JsonArray jsonElements = jsonParser.parse(json).getAsJsonArray();

                for (JsonElement element : jsonElements) {
                    agenda = gson.fromJson(element, Agenda.class);
                    agendas.add(agenda);
                }
            }
        }
        return agendas;
    }

    public static ArrayList<Agenda> getAgendasCompartilhadas() {
        String mUrl = "http://caeumc.com/admin/getAgendasAndroid.php?compartilhado=1";
        String json = getJson(mUrl);


        ArrayList<Agenda> agendas = new ArrayList<Agenda>();

        if (json != null) {
            if (!json.trim().isEmpty()) {

                Agenda agenda = new Agenda();
                Gson gson = new Gson();
                JsonParser jsonParser = new JsonParser();
                JsonArray jsonElements = jsonParser.parse(json).getAsJsonArray();

                for (JsonElement element : jsonElements) {
                    agenda = gson.fromJson(element, Agenda.class);
                    agendas.add(agenda);
                }
            }
        }
        return agendas;
    }

    public static ArrayList<Agenda> getAgendasIdentificacao(String identificacao) {
        identificacao = identificacao.toLowerCase().trim();
        identificacao = identificacao.replace(" ","");
        String mUrl = String.format("http://caeumc.com/admin/getAgendasAndroid.php?identificacao=%s", identificacao);
        String json = getJson(mUrl);

        ArrayList<Agenda> agendas = new ArrayList<Agenda>();

        if (json != null) {
            if (!json.trim().isEmpty()) {

                Agenda agenda = new Agenda();
                Gson gson = new Gson();
                JsonParser jsonParser = new JsonParser();
                JsonArray jsonElements = jsonParser.parse(json).getAsJsonArray();

                for (JsonElement element : jsonElements) {
                    agenda = gson.fromJson(element, Agenda.class);
                    agendas.add(agenda);
                }
            }
        }
        return agendas;
    }

    private static String getJson (String mUrl) {
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

}
