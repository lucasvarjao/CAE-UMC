package com.caeumc.caeumc;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AgendaDetails extends AppCompatActivity {

    private String descricao;
    private long data;
    private long horaInicio;
    private long horaFinal;
    private String local;
    private Boolean feriado;
    private String observacao;
    private int dia;
    private int mes;
    private int ano;
    private int diasemana;
    private String horaInicioFormatada;
    private String horaFinalFormatada;
    private String mesEscrito;
    private String semanaEscrita;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_details);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_agenda_details);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_agenda_details);

        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleTextAppearance(R.style.expandedappbar);

        LinearLayout layoutClock = (LinearLayout) findViewById(R.id.layoutClock);
        LinearLayout layoutLocation = (LinearLayout) findViewById(R.id.layoutLocation);
        LinearLayout layoutObservacao = (LinearLayout) findViewById(R.id.layoutObservacao);

        TextView txtDiaDetails = (TextView) findViewById(R.id.txtDiaDetails);
        TextView txtHoraDetails = (TextView) findViewById(R.id.txtHoraDetails);
        TextView txtLocalDetails = (TextView) findViewById(R.id.txtLocalDetails);
        TextView txtEnderecoDetails = (TextView) findViewById(R.id.txtEnderecoDetails);
        TextView txtObservacoes = (TextView) findViewById(R.id.txtObsDetails);
       // TextView txtDescricao = (TextView) findViewById(R.id.txtDescricaoDetails);

        EventosListModel eventosListModel = EventosListModel.findById(EventosListModel.class, NavDrawerActivity.idEvento);

        descricao = eventosListModel.getDescricao();
        collapsingToolbarLayout.setTitle(descricao);
       // txtDescricao.setText(descricao);
        data = (long) eventosListModel.getData();
        horaInicio = (long) eventosListModel.getHoraInicio();
        horaFinal = (long) eventosListModel.getHoraFinal();
        local = eventosListModel.getLocal();
        feriado = eventosListModel.getFeriado();
        observacao = eventosListModel.getObservacao();
        Date dataEvento = new Date(data * 1000L);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataEvento);

        dia = calendar.get(Calendar.DAY_OF_MONTH);
        mes = calendar.get(Calendar.MONTH) + 1;
        ano = calendar.get(Calendar.YEAR);
        diasemana = calendar.get(Calendar.DAY_OF_WEEK);

        if (eventosListModel.getHoraInicio() != 0) {
            Date horaInicioEvento = new Date(horaInicio * 1000L);
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            horaInicioFormatada = dateFormat.format(horaInicioEvento);
        }

        if (eventosListModel.getHoraFinal() != 0) {
            Date horaFinalEvento = new Date(horaFinal * 1000L);
            DateFormat dateFormat = new SimpleDateFormat("HH:mm");
            horaFinalFormatada = dateFormat.format(horaFinalEvento);
        }

        mesEscrito = getMes(mes-1);
        semanaEscrita = getDiaSemana(diasemana);

        txtDiaDetails.setText(String.format("%s, %s de %s de %s", semanaEscrita, dia, mesEscrito, ano));
        if (eventosListModel.getHoraInicio() == 0 || eventosListModel.getHoraFinal() == 0) {
            txtHoraDetails.setVisibility(View.GONE);
        } else {
            txtHoraDetails.setText(String.format("%s - %s", horaInicioFormatada, horaFinalFormatada));
        }

        if (local == null) {
            layoutLocation.setVisibility(View.GONE);
        } else if (local.trim().equals("")) {
            layoutLocation.setVisibility(View.GONE);
        } else {
            txtLocalDetails.setText(local);
            txtEnderecoDetails.setVisibility(View.GONE);
        }

        if (observacao != null) {
            if (!observacao.trim().equals("")) {

                txtObservacoes.setText(observacao);

            } else {
                layoutObservacao.setVisibility(View.GONE);
            }
        } else {
            layoutObservacao.setVisibility(View.GONE);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            this.finish();
        }

        return super.onOptionsItemSelected(item);
    }
    private String getMes (int nMes) {
        String mesEvento="";
        switch (nMes) {
            case 0:
                mesEvento = "janeiro";
                break;
            case 1:
                mesEvento = "fevereiro";

                break;
            case 2:
                mesEvento = "março";

                break;
            case 3:
                mesEvento = "abril";

                break;
            case 4:
                mesEvento = "maio";

                break;
            case 5:
                mesEvento = "junho";

                break;
            case 6:
                mesEvento = "julho";

                break;
            case 7:
                mesEvento = "agosto";

                break;
            case 8:
                mesEvento = "setembro";

                break;
            case 9:
                mesEvento = "outubro";

                break;
            case 10:
                mesEvento = "novembro";

                break;
            case 11:
                mesEvento = "dezembro";
                break;
        }
        return mesEvento;
    }

    private String getDiaSemana (int nDiaSemana) {
        String diasemanaEvento = "";
        switch (nDiaSemana) {
            case 1:
                diasemanaEvento = "Domingo";
                break;
            case 2:
                diasemanaEvento = "Segunda";
                break;
            case 3:
                diasemanaEvento = "Terça";
                break;
            case 4:
                diasemanaEvento = "Quarta";
                break;
            case 5:
                diasemanaEvento = "Quinta";
                break;
            case 6:
                diasemanaEvento = "Sexta";
                break;
            case 7:
                diasemanaEvento = "Sábado";
                break;
        }
        return diasemanaEvento;
    }

}
