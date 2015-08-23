package com.caeumc.caeumc;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Lucas Varjao on 15/08/2015.
 */
public class ListAdapter extends ArrayAdapter<MateriaListModel> {

    public ListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public ListAdapter(Context context, int resource, List<MateriaListModel> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.materias_listrow, null);
        }

       MateriaListModel p = getItem(position);

        if (p != null) {
            TextView tt1 = (TextView) v.findViewById(R.id.lblExame);
            TextView tt2 = (TextView) v.findViewById(R.id.lblM1);
            TextView tt3 = (TextView) v.findViewById(R.id.lblM2);
            TextView tt4 = (TextView) v.findViewById(R.id.lblMateria);
            TextView tt5 = (TextView) v.findViewById(R.id.lblNotaFinal);
            TextView tt6 = (TextView) v.findViewById(R.id.lblPI);

            if (tt1 != null) {
                if (p.getEX() == -1.0)
                tt1.setText("-");
                else
                    tt1.setText(p.getEX().toString());
                if (p.getEX() < 5) {
                    tt1.setTextColor(Color.parseColor("#F44336"));
                } else if (p.getEX() <= 7) {
                    tt1.setTextColor(Color.parseColor("#2196F3"));
                } else {
                    tt1.setTextColor(Color.parseColor("#1565C0"));
                }
            }

            if (tt2 != null) {
                if (p.getM1() == -1.0)
                    tt2.setText("-");
                else
                    tt2.setText(p.getM1().toString());

                if (p.getM1() < 5) {
                    tt2.setTextColor(Color.parseColor("#F44336"));
                } else if (p.getM1() <= 7) {
                    tt2.setTextColor(Color.parseColor("#2196F3"));
                } else {
                    tt2.setTextColor(Color.parseColor("#1565C0"));
                }
            }

            if (tt3 != null) {
                if (p.getM2() == -1.0)
                    tt3.setText("-");
                else
                    tt3.setText(p.getM2().toString());

                if (p.getM2() < 5) {
                    tt3.setTextColor(Color.parseColor("#F44336"));
                } else if (p.getM2() <= 7) {
                    tt3.setTextColor(Color.parseColor("#2196F3"));
                } else {
                    tt3.setTextColor(Color.parseColor("#1565C0"));
                }
            }

            if (tt4 != null) {
                tt4.setText(p.getNomeMateria());
                tt4.setTextColor(Color.parseColor("#212121"));

            }

            if (tt5 != null) {
                if (p.getNF() == -1.0)
                    tt5.setText("-");
                else
                    tt5.setText(p.getNF().toString());

                if (p.getNF() < 5) {
                    tt5.setTextColor(Color.parseColor("#F44336"));
                } else if (p.getNF() <= 7) {
                    tt5.setTextColor(Color.parseColor("#2196F3"));
                } else {
                    tt5.setTextColor(Color.parseColor("#1565C0"));
                }
            }

            if (tt6 != null) {
                if (p.getPI() == -1.0)
                    tt6.setText("-");
                else
                    tt6.setText(p.getPI().toString());

                if (p.getPI() < 5) {
                    tt6.setTextColor(Color.parseColor("#F44336"));
                } else if (p.getPI() <= 7) {
                    tt6.setTextColor(Color.parseColor("#2196F3"));
                } else {
                    tt6.setTextColor(Color.parseColor("#1565C0"));
                }
            }
        }

        return v;
    }
}
