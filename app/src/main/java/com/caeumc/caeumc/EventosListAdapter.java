package com.caeumc.caeumc;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

/**
 * Created by EDNEI on 29/08/2015.
 */
public class EventosListAdapter extends BaseAdapter {
    List<EventosListModel> data = new ArrayList<>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
    Activity activityDisciplina ;
    static ActionMode mActionDeleteMode;
    static ActionMode.Callback mDeleteMode;
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    String mesAnterior = "";
    LayoutInflater vi;

    public EventosListAdapter(Context context, int textViewResourceId) {

    }

    public EventosListAdapter(Activity a, Context context, int resource, List<EventosListModel> items) {
        activityDisciplina = a;
        data = items;
        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final EventosListModel item) {
        data.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final EventosListModel item) {
        data.add(item);
        sectionHeader.add(data.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    public int getCount() {

        if(data.size()<=0)
            return 0;
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
        ViewHolder holder = null;
        View v = convertView;
        int rowType = getItemViewType(position);

        if (v == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    v = vi.inflate(R.layout.agenda_recyclerview, null);
                    holder.tt1 = (TextView) v.findViewById(R.id.lblMesAno);
                    holder.tt2 = (TextView) v.findViewById(R.id.lblDiaMes);
                    holder.tt3 = (TextView) v.findViewById(R.id.lblDiaSemana);
                    holder.tt4 = (TextView) v.findViewById(R.id.lblDescricaoEvento);
                    holder.tt5 = (TextView) v.findViewById(R.id.lblHoraLocal);
                    break;
                case TYPE_SEPARATOR:
                    break;
            }
            v.setTag(holder);

        } else {
            holder = (ViewHolder) v.getTag();
        }

       EventosListModel p = getItem(position);

        if (p != null) {

            /*TextView tt1 = (TextView) v.findViewById(R.id.lblMesAno);
            TextView tt2 = (TextView) v.findViewById(R.id.lblDiaMes);
            TextView tt3 = (TextView) v.findViewById(R.id.lblDiaSemana);
            TextView  tt4 = (TextView) v.findViewById(R.id.lblDescricaoEvento);
            TextView tt5 = (TextView) v.findViewById(R.id.lblHoraLocal);*/
                String descricao = p.getDescricao();
                long data = (long) p.getData();
                Date dataEvento = new Date(data * 1000L);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(dataEvento);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                int diasemana = calendar.get(Calendar.DAY_OF_WEEK);
                int mes = calendar.get(Calendar.MONTH);
                String mesEvento = "";
                switch (mes) {
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


                int ano = calendar.get(Calendar.YEAR);
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
                    dia = "0" + dia;
                }
                long horaInicio = (long) p.getHoraInicio();
                String horaInicioFormatada = "";
                String horaFinalFormatada = "";

                if (p.getHoraInicio() != 0) {
                    Date horaInicioEvento = new Date(horaInicio * 1000L);
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    horaInicioFormatada = dateFormat.format(horaInicioEvento);
                }
                long horaFinal = (long) p.getHoraFinal();
                if (p.getHoraFinal() != 0) {
                    Date horaFinalEvento = new Date(horaFinal * 1000L);
                    DateFormat dateFormat = new SimpleDateFormat("HH:mm");
                    horaFinalFormatada = dateFormat.format(horaFinalEvento);
                }
                String local = p.getLocal();

                //  teste.add(materialistmodel.getId().toString());


                if (holder.tt1 != null) {
                    if (mesAnterior.equals("")) {
                        holder.tt1.setText(String.format("%s de %s", mesEvento, String.valueOf(ano)));
                        holder.tt1.setVisibility(View.VISIBLE);
                        mesAnterior = mesEvento;
                    } else if (mesAnterior == mesEvento) {
                        holder.tt1.setText(String.format("%s de %s", mesEvento, String.valueOf(ano)));
                        holder.tt1.setVisibility(View.GONE);
                    } else {
                        holder.tt1.setText(String.format("%s de %s", mesEvento, String.valueOf(ano)));
                        holder.tt1.setVisibility(View.VISIBLE);
                        mesAnterior = mesEvento;
                    }

                }

                if (holder.tt2 != null) {

                    holder.tt2.setText(String.valueOf(dia));

                }

                if (holder.tt3 != null) {

                    holder.tt3.setText(diasemanaEvento);

                }

                if (holder.tt4 != null) {

                    holder.tt4.setText(descricao);


                }

                if (holder.tt5 != null) {

                    if (p.getHoraInicio() == 0) {
                        holder.tt5.setVisibility(View.GONE);
                    } else {
                        holder.tt5.setText("19:00-22:00 em UMC");
                        holder.tt5.setText(String.format("%s-%s em %s", horaInicioFormatada, horaFinalFormatada, local));
                        holder.tt5.setVisibility(View.VISIBLE);
                    }


                }
            }

            return v;

    }

    public static class ViewHolder {
        TextView tt1 ;
        TextView tt2 ;
        TextView tt3 ;
        TextView  tt4 ;
        TextView tt5;
    }
}
