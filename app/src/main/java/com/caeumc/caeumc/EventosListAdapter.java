package com.caeumc.caeumc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.view.ActionMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by EDNEI on 29/08/2015.
 */
public class EventosListAdapter extends ArrayAdapter<EventosListModel> {
    List<EventosListModel> data = new ArrayList<>();
    Activity activityDisciplina ;
    static ActionMode mActionDeleteMode;
    static ActionMode.Callback mDeleteMode;

    public EventosListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public EventosListAdapter(Activity a, Context context, int resource, List<EventosListModel> items) {
        super(context, resource, items);
        activityDisciplina = a;
        data = items;
    }

    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public EventosListModel getItem(int position) {
        EventosListModel listModel = data.get(position);
        return listModel;
    }

    public long getItemId(int position) {

        EventosListModel eventosListModel = data.get(position);
        long id = eventosListModel.getId();
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.agenda_recyclerview, null);
        }

       EventosListModel p = getItem(position);

        if (p != null) {

            TextView tt1 = (TextView) v.findViewById(R.id.lblMesAno);
            TextView tt2 = (TextView) v.findViewById(R.id.lblDiaMes);
            TextView tt3 = (TextView) v.findViewById(R.id.lblDiaSemana);
            TextView  tt4 = (TextView) v.findViewById(R.id.lblDescricaoEvento);
            TextView tt5 = (TextView) v.findViewById(R.id.lblHoraLocal);
            String descricao = p.getDescricao();
            long data = (long) p.getData();
            Date dataEvento = new Date(data*1000L);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dataEvento);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            int diasemana = calendar.get(Calendar.DAY_OF_WEEK);
            String diasemanaEvento = "";
            switch (diasemana) {
                case 1:
                    diasemanaEvento = "dom";
                    break;
                case 2:
                    diasemanaEvento = "seg";
                    break;
                case 3:
                    diasemanaEvento = "ter";
                    break;
                case 4:
                    diasemanaEvento = "qua";
                    break;
                case 5:
                    diasemanaEvento = "qui";
                    break;
                case 6:
                    diasemanaEvento = "sex";
                    break;
                case 7:
                    diasemanaEvento = "sáb";
                    break;
            }
            Log.e("Dia", String.valueOf(diasemana));
            String dia = String.valueOf(day);
            if (dia.length() == 1) {
                dia = "0"+dia;
            }
            long horaInicio = (long) p.getHoraInicio();
            String horaInicioFormatada = "";
            String horaFinalFormatada = "";

            if (p.getHoraInicio() != 0) {
                Date horaInicioEvento = new Date(horaInicio*1000L);
                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                horaInicioFormatada = dateFormat.format(horaInicioEvento);
            }
            long horaFinal = (long)p.getHoraFinal();
            if (p.getHoraFinal() != 0) {
                Date horaFinalEvento = new Date(horaFinal*1000L);
                DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                horaFinalFormatada = dateFormat.format(horaFinalEvento);
            }
            String local = p.getLocal();

            //  teste.add(materialistmodel.getId().toString());




            if (tt1 != null) {

                tt1.setText("Mes Teste");

            }

            if (tt2 != null) {

                tt2.setText(String.valueOf(dia));

            }

            if (tt3 != null) {

                tt3.setText(diasemanaEvento);

            }

            if (tt4 != null) {

                tt4.setText(descricao);


            }

            if (tt5 != null) {

                if (p.getHoraInicio() == 0) {
                    tt5.setVisibility(View.GONE);
                } else {
                    tt5.setText("19:00-22:00 em UMC");
                    tt5.setText(String.format("%s-%s em %s", horaInicioFormatada, horaFinalFormatada, local));
                    tt5.setVisibility(View.VISIBLE);
                }



            }
        }

        return v;
    }
}
