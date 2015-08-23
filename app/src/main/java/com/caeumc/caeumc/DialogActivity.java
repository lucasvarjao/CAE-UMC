package com.caeumc.caeumc;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DialogActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private EditText txtDisciplinas;
    private TextView lblCampObrigatorio;
    private TextView lblContador;
    private EditText txtM1;
    private EditText txtPI;
    private EditText txtM2;
    private EditText txtEX;
    private String disciplina2;
    private double M1;
    private double M2;
    private double PI;
    private double EX;
    private double NF;

    private String nDisciplina2;
    private double nM1;
    private double nM2;
    private double nPI;
    private double nEX;
    private double nNF;

    String idDisciplina;
    String editarDisciplina = "0";
    int eDisciplina = 0;
    Context context;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fulldialogtoolbar);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        this.setFinishOnTouchOutside(false);

        toolbar = (Toolbar) findViewById(R.id.tool_bar_dialog);
        setSupportActionBar(toolbar);

        context = getApplicationContext();


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_close_clear_cancel);

        txtDisciplinas = (EditText) findViewById(R.id.editText);
        lblCampObrigatorio = (TextView) findViewById(R.id.lblCampoObrigatorio);
        lblContador = (TextView) findViewById(R.id.lblContador);
        txtM1 = (EditText) findViewById(R.id.txtM1);
        txtPI = (EditText) findViewById(R.id.txtPI);
        txtM2 = (EditText) findViewById(R.id.txtM2);
        txtEX = (EditText) findViewById(R.id.txtEX);
        lblCampObrigatorio.setVisibility(View.INVISIBLE);
        lblContador.setTextColor(Color.GRAY);


        Bundle args = getIntent().getExtras();
        idDisciplina = args.getString("DISCIPLINA_ID");
        editarDisciplina = args.getString("EDITAR_DISCIPLINA");
        eDisciplina = Integer.valueOf(editarDisciplina);

        if (eDisciplina == 1) {
            getSupportActionBar().setTitle("Alterar");
            Log.e("Teste","ENTROU NA CONDICAO");
            Long i = Long.valueOf(idDisciplina);
            Log.e("ID", i.toString() + " " + idDisciplina);
            MateriaListModel materiaListModel = MateriaListModel.findById(MateriaListModel.class, i);
            nDisciplina2 = materiaListModel.getNomeMateria();
            nM1 = materiaListModel.getM1();
            nM2 = materiaListModel.getM2();
            nPI = materiaListModel.getPI();
            nEX = materiaListModel.getEX();
            nNF = materiaListModel.getNF();
            txtDisciplinas.setText(materiaListModel.getNomeMateria());
            lblContador.setText(String.valueOf(txtDisciplinas.getText().length()) + "/35");
            if (materiaListModel.getM1() == -1) {
                txtM1.setText("");

            } else {
                txtM1.setText(materiaListModel.getM1().toString());

            }

            if (materiaListModel.getEX() == -1) {
                txtEX.setText("");

            } else {
                txtEX.setText(materiaListModel.getEX().toString());

            }

            if (materiaListModel.getM2() == -1) {
                txtM2.setText("");

            } else {
                txtM2.setText(materiaListModel.getM2().toString());

            }

            if (materiaListModel.getPI() == -1) {
                txtPI.setText("");

            } else {
                txtPI.setText(materiaListModel.getPI().toString());

            }

        }

        Log.e("ENTROU", idDisciplina + " " + editarDisciplina);

        final TextWatcher txtDisciplinasCount = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                lblContador.setText(String.valueOf(s.length()) + "/35");

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (s.length() == 35) {
                    lblContador.setTextColor(Color.parseColor("#E53935"));
                } else {
                    lblContador.setTextColor(Color.GRAY);
                }
                lblCampObrigatorio.setVisibility(View.INVISIBLE);

            }
        };

        txtDisciplinas.addTextChangedListener(txtDisciplinasCount);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();  // Always call the superclass

        // Stop method tracing that the activity started during onCreate()
        android.os.Debug.stopMethodTracing();
    }

    public double Arredondar(double nota) {
        Log.e("Arredondamento", String.valueOf(nota));
        double iM2 = Math.floor(nota);
        Log.e("Arredondamento", String.valueOf(iM2));
        BigDecimal dM2 = new BigDecimal(nota % 1);
        Log.e("Arredondamento", String.valueOf(dM2));
        dM2 = dM2.setScale(2, RoundingMode.HALF_UP);
        Log.e("Arredondamento", String.valueOf(dM2));
        double ddM2 = dM2.doubleValue();
        Log.e("Arredondamento", String.valueOf(ddM2));
        if (ddM2 < 0.46) {
            nota = iM2;
        } else if (ddM2 >= 0.56) {
            nota = iM2 + 1;
        } else {
            nota = iM2 + 0.5;
        }
        Log.e("Arredondamento", String.valueOf(nota));

        return nota;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save) {

            if (eDisciplina == 1) {

                txtDisciplinas = (EditText) findViewById(R.id.editText);
                lblCampObrigatorio = (TextView) findViewById(R.id.lblCampoObrigatorio);
                lblContador = (TextView) findViewById(R.id.lblContador);
                txtM1 = (EditText) findViewById(R.id.txtM1);
                txtPI = (EditText) findViewById(R.id.txtPI);
                txtM2 = (EditText) findViewById(R.id.txtM2);
                txtEX = (EditText) findViewById(R.id.txtEX);

                if (txtDisciplinas.getText().toString().trim().length() == 0) {
                    lblCampObrigatorio.setVisibility(View.VISIBLE);
                } else {
                    NF = -1.0;
                    disciplina2 = txtDisciplinas.getText().toString().trim();
                    try {
                        M1 = Double.valueOf(txtM1.getText().toString().trim());
                    } catch (NumberFormatException ex) {
                        M1 = -1.0;
                    }
                    try {
                        M2 = Double.valueOf(txtM2.getText().toString().trim());
                    } catch (NumberFormatException ex) {
                        M2 = -1.0;
                    }
                    try {
                        PI = Double.valueOf(txtPI.getText().toString().trim());
                    } catch (NumberFormatException ex) {
                        PI = -1.0;
                    }
                    try {
                        EX = Double.valueOf(txtEX.getText().toString().trim());
                    } catch (NumberFormatException ex) {
                        EX = -1.0;
                    }

                    if (M1 != -1 && PI != -1 && M2 == -1) {
                        M2 = ((3 * 5) - M1 - (0.6*PI)) / 1.4;
                        M2 = Arredondar(M2);

                   //     M2 = M2 * 2;
                  //      M2 = Math.round(M2);
                    //    M2 = M2 * 0.5;
                        NF = (M1 + ((PI*0.3 + M2*0.7)*2))/3;
                        NF = Arredondar(NF);
                     //   NF = NF * 2;
                     //   NF = Math.round(NF);
                      //  NF = NF * 0.5;
                    } else if (M1 != -1 && PI == -1 && M2 == -1) {
                        PI =0;
                        M2 = ((3 * 5) - M1 - (0.6*PI)) / 1.4;
                        M2 = Arredondar(M2);
                        NF = (M1 + ((PI*0.3 + M2*0.7)*2))/3;
                        NF = Arredondar(NF);

                    }   else if (M1 != -1 && PI != -1 && M2 != -1) {
                        NF = (M1 + ((PI*0.3 + M2*0.7)*2))/3;
                       NF = Arredondar(NF);
                        //NF = NF * 2;
                       // NF = Math.round(NF);
                       // NF = NF * 0.5;

                    } else if (M1 != -1 && PI != -1 && M2 != -1 && NF != -1) {
                        if (NF < 5) {

                        }
                    }
                    Long i = Long.valueOf(idDisciplina);

                    MateriaListModel materiaListModel = MateriaListModel.findById(MateriaListModel.class, i);

                    Log.e("Teste", String.valueOf(nNF) + "=" + String.valueOf(NF) + "   " + String.valueOf(nPI) + "=" + String.valueOf(PI));

                    if ((nDisciplina2.equals(disciplina2)) && (nM1 == M1) && (nPI == PI) && (nM2 == M2) && (nEX == EX) && (nNF == NF)) {

                        Log.e("ALteracoes", "Entrou nas alterações");

                    } else {

                        materiaListModel.setNomeMateria(disciplina2);
                        materiaListModel.setM1(M1);
                        materiaListModel.setPI(PI);
                        materiaListModel.setM2(M2);
                        materiaListModel.setEX(EX);
                        materiaListModel.setNF(NF);
                        materiaListModel.save();
                        NavDrawerActivity.notifyUpdate = 1;

                    }


                    DialogActivity.this.finish();

                }

            } else {

                txtDisciplinas = (EditText) findViewById(R.id.editText);
                lblCampObrigatorio = (TextView) findViewById(R.id.lblCampoObrigatorio);
                lblContador = (TextView) findViewById(R.id.lblContador);
                txtM1 = (EditText) findViewById(R.id.txtM1);
                txtPI = (EditText) findViewById(R.id.txtPI);
                txtM2 = (EditText) findViewById(R.id.txtM2);
                txtEX = (EditText) findViewById(R.id.txtEX);

                if (txtDisciplinas.getText().toString().trim().length() == 0) {
                    lblCampObrigatorio.setVisibility(View.VISIBLE);
                } else {
                    NF = -1.0;
                    disciplina2 = txtDisciplinas.getText().toString().trim();
                    try {
                        M1 = Double.valueOf(txtM1.getText().toString().trim());
                    } catch (NumberFormatException ex) {
                        M1 = -1.0;
                    }
                    try {
                        M2 = Double.valueOf(txtM2.getText().toString().trim());
                    } catch (NumberFormatException ex) {
                        M2 = -1.0;
                    }
                    try {
                        PI = Double.valueOf(txtPI.getText().toString().trim());
                    } catch (NumberFormatException ex) {
                        PI = -1.0;
                    }
                    try {
                        EX = Double.valueOf(txtEX.getText().toString().trim());
                    } catch (NumberFormatException ex) {
                        EX = -1.0;
                    }

                    if (M1 != -1 && PI != -1 && M2 == -1) {
                        M2 = ((3 * 5) - M1 - (0.6*PI)) / 1.4;
                        M2 = Arredondar(M2);
                        Log.e("Arredondamento", String.valueOf(M2));
                        //     M2 = M2 * 2;
                        //      M2 = Math.round(M2);
                        //    M2 = M2 * 0.5;
                        NF = (M1 + ((PI*0.3 + M2*0.7)*2))/3;
                        NF = Arredondar(NF);
                        //   NF = NF * 2;
                        //   NF = Math.round(NF);
                        //  NF = NF * 0.5;
                    } else if (M1 != -1 && PI != -1 && M2 != -1) {
                        NF = (M1 + ((PI*0.3 + M2*0.7)*2))/3;
                        NF = Arredondar(NF);
                        //NF = NF * 2;
                        // NF = Math.round(NF);
                        // NF = NF * 0.5;

                    }

                    MateriaListModel materia = new MateriaListModel(disciplina2, M1, M2, PI, EX, NF);
                    materia.save();



                    DialogActivity.this.finish();
                }

                }


                return true;

            }

            if (id == android.R.id.home) {
                txtDisciplinas = (EditText) findViewById(R.id.editText);
                txtM1 = (EditText) findViewById(R.id.txtM1);
                txtPI = (EditText) findViewById(R.id.txtPI);
                txtM2 = (EditText) findViewById(R.id.txtM2);
                txtEX = (EditText) findViewById(R.id.txtEX);

                if (eDisciplina == 1) {


                    NF = -1.0;
                    disciplina2 = txtDisciplinas.getText().toString().trim();
                    try {
                        M1 = Double.valueOf(txtM1.getText().toString().trim());
                    } catch (NumberFormatException ex) {
                        M1 = -1.0;
                    }
                    try {
                        M2 = Double.valueOf(txtM2.getText().toString().trim());
                    } catch (NumberFormatException ex) {
                        M2 = -1.0;
                    }
                    try {
                        PI = Double.valueOf(txtPI.getText().toString().trim());
                    } catch (NumberFormatException ex) {
                        PI = -1.0;
                    }
                    try {
                        EX = Double.valueOf(txtEX.getText().toString().trim());
                    } catch (NumberFormatException ex) {
                        EX = -1.0;
                    }

                    if (M1 != -1 && PI != -1 && M2 == -1) {
                        M2 = ((3 * 5) - M1 - (0.6*PI)) / 1.4;
                        M2 = Arredondar(M2);
                        Log.e("Arredondamento", String.valueOf(M2));
                        //     M2 = M2 * 2;
                        //      M2 = Math.round(M2);
                        //    M2 = M2 * 0.5;
                        NF = (M1 + ((PI*0.3 + M2*0.7)*2))/3;
                        NF = Arredondar(NF);
                        //   NF = NF * 2;
                        //   NF = Math.round(NF);
                        //  NF = NF * 0.5;
                    } else if (M1 != -1 && PI != -1 && M2 != -1) {
                        NF = (M1 + ((PI*0.3 + M2*0.7)*2))/3;
                        NF = Arredondar(NF);
                        //NF = NF * 2;
                        // NF = Math.round(NF);
                        // NF = NF * 0.5;

                    }

                    if ((nDisciplina2.equals(disciplina2)) && (nM1 == M1) && (nPI == PI) && (nM2 == M2) && (nEX == EX) && (nNF == NF)) {

                        this.finish();
                    } else {

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Descartar alterações?");
                        builder.setPositiveButton("Descartar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DialogActivity.this.finish();


                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }



                } else {



                    if ((txtDisciplinas.getText().toString().trim().length() != 0) ||
                            (txtM1.getText().toString().trim().length() != 0) ||
                            (txtPI.getText().toString().trim().length() != 0) ||
                            (txtM2.getText().toString().trim().length() != 0) ||
                            (txtEX.getText().toString().trim().length() != 0)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage("Descartar alterações?");
                        builder.setPositiveButton("Descartar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DialogActivity.this.finish();


                            }
                        });
                        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();

                            }
                        });

                        AlertDialog dialog = builder.create();
                        dialog.show();

                    } else {
                        this.finish();
                    }

                }
                return true;

            }


            return super.onOptionsItemSelected(item);
        }
    }

