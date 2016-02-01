package com.caeumc.caeumc;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class DialogActivity extends AppCompatActivity {

    private EditText txtDisciplinas;

    private TextView lblContador;
    private EditText txtM1;
    private EditText txtPI;
    private EditText txtM2;
    private EditText txtEX;
    private CheckBox chbDP;

    private RelativeLayout tutorialView;
    private RelativeLayout tutorialView2;
    private RelativeLayout tutorialView3;
    private RelativeLayout tutorialView4;
    private int ntutorial = 0;
    private TextView txtTutorial;

    private TextInputLayout txtInputDisciplina;

    private String disciplina2;
    private double M1;
    private double M2;
    private double PI;
    private double EX;
    private double NF;
    private boolean DP;

    private String nDisciplina2;
    private double nM1;
    private double nM2;
    private double nPI;
    private double nEX;
    private double nNF;
    private boolean nDP;

    private String idDisciplina;
    private int eDisciplina = 0;

    private MateriaListModel materiaListModel;

    private String PREF_KEY_TUT_MAIN;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getWindow().setWindowAnimations(android.R.style.Animation_Toast);
        setContentView(R.layout.fulldialogtoolbar);
        //  getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        this.setFinishOnTouchOutside(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar_dialog);
        setSupportActionBar(toolbar);

        Context context = getApplicationContext();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //hold current color of status bar

            //set your gray color
            getWindow().setStatusBarColor(Color.parseColor("#1A237E"));
            getWindow().setNavigationBarColor(Color.parseColor("#1A237E"));
        }
        //noinspection ConstantConditions
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);

        tutorialView = (RelativeLayout) findViewById(R.id.tutorialView);
        tutorialView2 = (RelativeLayout) findViewById(R.id.tutorialView2);
        tutorialView3 = (RelativeLayout) findViewById(R.id.tutorialView3);
        tutorialView4 = (RelativeLayout) findViewById(R.id.tutorialView4);
        Button btnTutorial = (Button) findViewById(R.id.btnTutorialNota);
        txtTutorial = (TextView) findViewById(R.id.txtTutorialNota);


        txtDisciplinas = (EditText) findViewById(R.id.editText);

        lblContador = (TextView) findViewById(R.id.lblContador);
        txtM1 = (EditText) findViewById(R.id.txtM1);
        txtPI = (EditText) findViewById(R.id.txtPI);
        txtM2 = (EditText) findViewById(R.id.txtM2);
        txtEX = (EditText) findViewById(R.id.txtEX);
        chbDP = (CheckBox) findViewById(R.id.chbDP);


        final boolean tutorialShown = PreferenceManager.getDefaultSharedPreferences(DialogActivity.this).getBoolean(PREF_KEY_TUT_MAIN, false);
        if (!tutorialShown) {
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            txtDisciplinas.setEnabled(false);
            txtM1.setEnabled(false);
            txtPI.setEnabled(false);
            txtM2.setEnabled(false);
            txtEX.setEnabled(false);
            chbDP.setEnabled(false);

            btnTutorial.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick (View v) {
                    switch (ntutorial) {
                        case 0:
                            txtTutorial.setText("Insira a nota da M1 e da PI e deixe o campo 'ND' em branco para saber a nota necessária na ND.");
                            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tutorialView4.getLayoutParams();
                            params.addRule(RelativeLayout.ALIGN_RIGHT, R.id.txtInputM1);
                            tutorialView4.setLayoutParams(params);
                            RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) tutorialView2.getLayoutParams();
                            params1.addRule(RelativeLayout.RIGHT_OF, R.id.txtInputPI);
                            tutorialView2.setLayoutParams(params1);
                            ntutorial += 1;
                            break;
                        case 1:
                            txtTutorial.setText("Quantos todas as notas forem inseridas, caso a nota final não seja o suficiente para ser aprovado, o sistema mostrará a nota necessária no exame.");
                            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) tutorialView4.getLayoutParams();
                            params2.addRule(RelativeLayout.ALIGN_RIGHT, R.id.txtInputPI);
                            tutorialView4.setLayoutParams(params2);
                            RelativeLayout.LayoutParams params3 = (RelativeLayout.LayoutParams) tutorialView2.getLayoutParams();
                            params3.addRule(RelativeLayout.RIGHT_OF, R.id.txtInputM2);
                            tutorialView2.setLayoutParams(params3);
                            ntutorial += 1;
                            break;
                        case 2:
                            txtTutorial.setText("Selecione aqui caso a matéria seja DP ou Adaptação, nesse caso a nota da PI não é necessária.");
                            RelativeLayout.LayoutParams params4 = (RelativeLayout.LayoutParams) tutorialView3.getLayoutParams();
                            params4.addRule(RelativeLayout.BELOW, R.id.comboLayout);
                            tutorialView3.setLayoutParams(params4);

                            RelativeLayout.LayoutParams params5 = (RelativeLayout.LayoutParams) tutorialView4.getLayoutParams();
                            params5.addRule(RelativeLayout.ALIGN_TOP, R.id.comboLayout);
                            params5.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.comboLayout);
                            params5.addRule(RelativeLayout.LEFT_OF, R.id.comboLayout);

                            tutorialView4.setLayoutParams(params5);
                            tutorialView4.setVisibility(View.GONE);


                            RelativeLayout.LayoutParams params6 = (RelativeLayout.LayoutParams) tutorialView.getLayoutParams();
                            params6.addRule(RelativeLayout.ABOVE, R.id.comboLayout);
                            tutorialView.setLayoutParams(params6);

                            RelativeLayout.LayoutParams params7 = (RelativeLayout.LayoutParams) tutorialView2.getLayoutParams();
                            params7.addRule(RelativeLayout.ALIGN_TOP, R.id.comboLayout);
                            params7.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.comboLayout);
                            params7.addRule(RelativeLayout.RIGHT_OF, R.id.txtInputM2);
                            tutorialView2.setLayoutParams(params7);
                            ntutorial += 1;
                            break;
                        default:
                            tutorialView.setVisibility(View.GONE);
                            tutorialView2.setVisibility(View.GONE);
                            tutorialView3.setVisibility(View.GONE);
                            tutorialView4.setVisibility(View.GONE);
                            PreferenceManager.getDefaultSharedPreferences(DialogActivity.this).edit().putBoolean(PREF_KEY_TUT_MAIN, true).commit();
                            txtDisciplinas.setEnabled(true);
                            txtM1.setEnabled(true);
                            txtPI.setEnabled(true);
                            txtM2.setEnabled(true);
                            txtEX.setEnabled(true);
                            chbDP.setEnabled(true);
                            txtDisciplinas.requestFocus();
                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                            break;
                    }
                }
            });


        } else {

            tutorialView.setVisibility(View.GONE);
            tutorialView2.setVisibility(View.GONE);
            tutorialView3.setVisibility(View.GONE);
            tutorialView4.setVisibility(View.GONE);
            this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        }


        chbDP.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged (CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        // txtPI.setVisibility(View.GONE);
                        Esconder(txtPI);
                    } else {
                        txtPI.setVisibility(View.GONE);
                    }
                } else {
                    // txtPI.setVisibility(View.VISIBLE);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Revelar(txtPI);
                    } else {
                        txtPI.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        txtInputDisciplina = (TextInputLayout) findViewById(R.id.txtInputDisciplina);
        TextInputLayout txtInputM1 = (TextInputLayout) findViewById(R.id.txtInputM1);
        TextInputLayout txtInputPI = (TextInputLayout) findViewById(R.id.txtInputPI);
        TextInputLayout txtInputM2 = (TextInputLayout) findViewById(R.id.txtInputM2);
        TextInputLayout txtInputEX = (TextInputLayout) findViewById(R.id.txtInputEX);

        txtInputDisciplina.setHint("Disciplina");
        txtInputM1.setHint("M1");
        txtInputPI.setHint("PI");
        txtInputM2.setHint("ND");
        txtInputEX.setHint("EX");


        lblContador.setTextColor(Color.GRAY);


        Bundle args = getIntent().getExtras();
        idDisciplina = args.getString("DISCIPLINA_ID");
        String editarDisciplina = args.getString("EDITAR_DISCIPLINA");
        eDisciplina = Integer.valueOf(editarDisciplina);

        if (eDisciplina == 1) {

            txtM1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange (View v, boolean hasFocus) {
                    if (hasFocus) {
                        txtM1.setText("");
                    }
                }
            });

            txtM2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange (View v, boolean hasFocus) {
                    if (hasFocus) {
                        txtM2.setText("");
                    }
                }
            });

            txtPI.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange (View v, boolean hasFocus) {
                    if (hasFocus) {
                        txtPI.setText("");
                    }
                }
            });

            txtEX.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange (View v, boolean hasFocus) {
                    if (hasFocus) {
                        txtEX.setText("");
                    }
                }
            });

            getSupportActionBar().setTitle("Alterar");
            Log.e("Teste", "ENTROU NA CONDICAO");


            runOnUiThread(new Runnable() {
                @Override
                public void run () {
                    Long i = Long.valueOf(idDisciplina);
                    Log.e("ID", i.toString() + " " + idDisciplina);
                    materiaListModel = MateriaListModel.findById(MateriaListModel.class, i);
                }
            });
            nDisciplina2 = materiaListModel.getNomeMateria();
            nM1 = materiaListModel.getM1();
            nM2 = materiaListModel.getM2();
            nPI = materiaListModel.getPI();
            nEX = materiaListModel.getEX();
            nNF = materiaListModel.getNF();
            nDP = materiaListModel.getDP();
            txtDisciplinas.setText(materiaListModel.getNomeMateria());
            txtDisciplinas.setSelection(txtDisciplinas.getText().length());
            lblContador.setText(String.valueOf(txtDisciplinas.getText().length()) + "/35");

            if (nNF < 5 && nNF >= 0 && nEX == -1) {
                txtEX.setEnabled(true);
            } else if (nEX != -1) {
                txtEX.setEnabled(true);
            } else {
                txtEX.setEnabled(false);

            }

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
            chbDP.setChecked(nDP);

            if (nDP) {
                //txtPI.setVisibility(View.GONE);
                Esconder(txtPI);
            }

        }

        Log.e("ENTROU", idDisciplina + " " + editarDisciplina);

        final TextWatcher txtDisciplinasCount = new TextWatcher() {
            @Override
            public void beforeTextChanged (CharSequence s, int start, int count, int after) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onTextChanged (CharSequence s, int start, int before, int count) {

                lblContador.setText(String.valueOf(s.length()) + "/35");

            }

            @Override
            public void afterTextChanged (Editable s) {

                if (s.length() == 35) {
                    lblContador.setTextColor(Color.parseColor("#E53935"));
                } else {
                    lblContador.setTextColor(Color.GRAY);
                }
                txtInputDisciplina.setErrorEnabled(false);


            }
        };

        txtDisciplinas.addTextChangedListener(txtDisciplinasCount);


    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public void onDestroy () {
        super.onDestroy();  // Always call the superclass

        // Stop method tracing that the activity started during onCreate()
        android.os.Debug.stopMethodTracing();
    }

    private double Arredondar (double nota) {
        Log.e("Arredondamento", String.valueOf(nota));
        double iM2 = Math.floor(nota);
        Log.e("Arredondamento", String.valueOf(iM2));
        BigDecimal dM2 = new BigDecimal(nota % 1);
        Log.e("Arredondamento", String.valueOf(dM2));
        dM2 = dM2.setScale(1, RoundingMode.HALF_UP);
        Log.e("Arredondamento", String.valueOf(dM2));
        double ddM2 = dM2.doubleValue();
        Log.e("Arredondamento", String.valueOf(ddM2));
        nota = iM2 + ddM2;
       /* if (ddM2 < 0.26) {
            nota = iM2;
        } else if (ddM2 >= 0.76) {
            nota = iM2 + 1;
        } else {
            nota = iM2 + 0.5;
        }*/
        Log.e("Arredondamento", String.valueOf(nota));

        return nota;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save) {

            if (eDisciplina == 1) {

                txtDisciplinas = (EditText) findViewById(R.id.editText);

                lblContador = (TextView) findViewById(R.id.lblContador);
                txtM1 = (EditText) findViewById(R.id.txtM1);
                txtPI = (EditText) findViewById(R.id.txtPI);
                txtM2 = (EditText) findViewById(R.id.txtM2);
                txtEX = (EditText) findViewById(R.id.txtEX);
                chbDP = (CheckBox) findViewById(R.id.chbDP);

                if (txtDisciplinas.getText().toString().trim().length() == 0) {
                    txtInputDisciplina.setError("Nome da disciplina obrigatório");
                    txtInputDisciplina.setErrorEnabled(true);
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

                    DP = chbDP.isChecked();
                    if (chbDP.isChecked()) {

                        if (M1 != -1 && M2 != -1 && NF != -1) {
                            if (NF < 5) {
                                if (M1 != -1 && M2 != -1 && NF != -1 && EX != -1) {

                                    NF = NF + (EX - 5);
                                } else {
                                    EX = 5 + (5 - NF);
                                }


                            } else {
                                NF = (M1 + (M2 * 2)) / 3;
                                NF = Arredondar(NF);
                            }

                        } else if (M1 != -1 && M2 == -1) {
                            M2 = ((3 * 5) - M1) / 2;
                            M2 = Arredondar(M2);
                            NF = (M1 + (M2 * 2)) / 3;
                            NF = Arredondar(NF);

                        } else if (M1 != -1 && M2 != -1) {
                            NF = (M1 + (M2 * 2)) / 3;
                            NF = Arredondar(NF);

                            if (NF < 5) {
                                if (M1 != -1 && M2 != -1 && NF != -1 && EX != -1) {

                                    NF = NF + (EX - 5);
                                } else {
                                    EX = 5 + (5 - NF);
                                }


                            }


                        } else if (M1 != -1 && M2 == -1) {
                            M2 = ((3 * 5) - M1) / 2;
                            M2 = Arredondar(M2);

                            NF = (M1 + (M2 * 2)) / 3;
                            NF = Arredondar(NF);
                        }

                    } else {
                        if (M1 != -1 && PI != -1 && M2 != -1 && NF != -1) {
                            if (NF < 5) {
                                if (M1 != -1 && PI != -1 && M2 != -1 && NF != -1 && EX != -1) {

                                    NF = NF + (EX - 5);
                                } else {
                                    EX = 5 + (5 - NF);
                                }


                            } else {
                                NF = (M1 + ((PI * 0.3 + M2 * 0.7) * 2)) / 3;
                                NF = Arredondar(NF);
                            }

                        } else if (M1 != -1 && PI == -1 && M2 == -1) {
                            PI = -1;
                            M2 = ((3 * 5) - M1 - (0.6 * 0)) / 1.4;
                            M2 = Arredondar(M2);
                            NF = (M1 + ((0 * 0.3 + M2 * 0.7) * 2)) / 3;
                            NF = Arredondar(NF);

                        } else if (M1 != -1 && PI == -1 && M2 != -1) {
                            PI = -1;
                            M2 = Arredondar(M2);
                            NF = (M1 + ((0 * 0.3 + M2 * 0.7) * 2)) / 3;
                            NF = Arredondar(NF);

                        } else if (M1 != -1 && PI != -1 && M2 != -1) {
                            NF = (M1 + ((PI * 0.3 + M2 * 0.7) * 2)) / 3;
                            NF = Arredondar(NF);

                            if (NF < 5) {
                                if (M1 != -1 && PI != -1 && M2 != -1 && NF != -1 && EX != -1) {

                                    NF = NF + (EX - 5);
                                } else {
                                    EX = 5 + (5 - NF);
                                }


                            }


                        } else if (M1 != -1 && PI != -1 && M2 == -1) {
                            M2 = ((3 * 5) - M1 - (0.6 * PI)) / 1.4;
                            M2 = Arredondar(M2);

                            NF = (M1 + ((PI * 0.3 + M2 * 0.7) * 2)) / 3;
                            NF = Arredondar(NF);
                        }
                    }


                    runOnUiThread(new Runnable() {
                        @Override
                        public void run () {
                            Long i = Long.valueOf(idDisciplina);
                            materiaListModel = MateriaListModel.findById(MateriaListModel.class, i);
                        }
                    });

                    Log.e("Teste", String.valueOf(nNF) + "=" + String.valueOf(NF) + "   " + String.valueOf(nPI) + "=" + String.valueOf(PI));

                    if ((nDisciplina2.equals(disciplina2)) && (nM1 == M1) && (nPI == PI) && (nM2 == M2) && (nEX == EX) && (nNF == NF) && (DP == nDP)) {

                        Log.e("ALteracoes", "Entrou nas alterações");

                    } else {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run () {
                                materiaListModel.setNomeMateria(disciplina2);
                                materiaListModel.setM1(M1);
                                materiaListModel.setPI(PI);
                                materiaListModel.setM2(M2);
                                materiaListModel.setEX(EX);
                                materiaListModel.setNF(NF);
                                materiaListModel.setDP(DP);
                                materiaListModel.save();
                                NavDrawerActivity.notifyUpdate = 1;
                            }
                        });


                    }


                    DialogActivity.this.finish();

                }

            } else {

                txtDisciplinas = (EditText) findViewById(R.id.editText);

                lblContador = (TextView) findViewById(R.id.lblContador);
                txtM1 = (EditText) findViewById(R.id.txtM1);
                txtPI = (EditText) findViewById(R.id.txtPI);
                txtM2 = (EditText) findViewById(R.id.txtM2);
                txtEX = (EditText) findViewById(R.id.txtEX);
                chbDP = (CheckBox) findViewById(R.id.chbDP);

                if (txtDisciplinas.getText().toString().trim().length() == 0) {
                    txtInputDisciplina.setError("Nome da disciplina obrigatório");
                    txtInputDisciplina.setErrorEnabled(true);
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

                    DP = chbDP.isChecked();
                    if (chbDP.isChecked()) {

                        if (M1 != -1 && M2 != -1 && NF != -1) {
                            if (NF < 5) {
                                if (M1 != -1 && M2 != -1 && NF != -1 && EX != -1) {

                                    NF = NF + (EX - 5);
                                } else {
                                    EX = 5 + (5 - NF);
                                }


                            } else {
                                NF = (M1 + (M2 * 2)) / 3;
                                NF = Arredondar(NF);
                            }

                        } else if (M1 != -1 && M2 == -1) {
                            M2 = ((3 * 5) - M1) / 2;
                            M2 = Arredondar(M2);
                            NF = (M1 + (M2 * 2)) / 3;
                            NF = Arredondar(NF);

                        } else if (M1 != -1 && M2 != -1) {
                            NF = (M1 + (M2 * 2)) / 3;
                            NF = Arredondar(NF);

                            if (NF < 5) {
                                if (M1 != -1 && M2 != -1 && NF != -1 && EX != -1) {

                                    NF = NF + (EX - 5);
                                } else {
                                    EX = 5 + (5 - NF);
                                }


                            }


                        } else if (M1 != -1 && M2 == -1) {
                            M2 = ((3 * 5) - M1) / 2;
                            M2 = Arredondar(M2);

                            NF = (M1 + (M2 * 2)) / 3;
                            NF = Arredondar(NF);
                        }

                    } else {
                        if (M1 != -1 && PI != -1 && M2 == -1) {
                            M2 = ((3 * 5) - M1 - (0.6 * PI)) / 1.4;
                            M2 = Arredondar(M2);

                            NF = (M1 + ((PI * 0.3 + M2 * 0.7) * 2)) / 3;
                            NF = Arredondar(NF);

                        } else if (M1 != -1 && PI == -1 && M2 == -1) {
                            PI = 0;
                            M2 = ((3 * 5) - M1 - (0.6 * PI)) / 1.4;
                            M2 = Arredondar(M2);
                            NF = (M1 + ((PI * 0.3 + M2 * 0.7) * 2)) / 3;
                            NF = Arredondar(NF);

                        } else if (M1 != -1 && PI == -1 && M2 != -1) {
                            PI = 0;
                            M2 = Arredondar(M2);
                            NF = (M1 + ((PI * 0.3 + M2 * 0.7) * 2)) / 3;
                            NF = Arredondar(NF);

                        } else if (M1 != -1 && PI != -1 && M2 != -1) {
                            NF = (M1 + ((PI * 0.3 + M2 * 0.7) * 2)) / 3;
                            NF = Arredondar(NF);

                            if (NF < 5) {
                                if (M1 != -1 && PI != -1 && M2 != -1 && NF != -1 && EX != -1) {

                                    NF = NF + (EX - 5);
                                } else {
                                    EX = 5 + (5 - NF);
                                }
                            }


                        } else if (M1 != -1 && PI != -1 && M2 != -1 && NF != -1) {
                            if (NF < 5) {
                                if (M1 != -1 && PI != -1 && M2 != -1 && NF != -1 && EX != -1) {

                                    NF = NF + (EX - 5);
                                } else {
                                    EX = 5 + (5 - NF);
                                }

                            }
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run () {
                            MateriaListModel materia = new MateriaListModel(disciplina2, M1, M2, PI, EX, NF, DP);
                            materia.save();
                        }
                    });


                    NavDrawerActivity.notifyUpdate = 2;

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
            chbDP = (CheckBox) findViewById(R.id.chbDP);

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

                DP = chbDP.isChecked();

                if (chbDP.isChecked()) {

                    if (M1 != -1 && M2 != -1 && NF != -1) {
                        if (NF < 5) {
                            if (M1 != -1 && M2 != -1 && NF != -1 && EX != -1) {

                                NF = NF + (EX - 5);
                            } else {
                                EX = 5 + (5 - NF);
                            }


                        } else {
                            NF = (M1 + (M2 * 2)) / 3;
                            NF = Arredondar(NF);
                        }

                    } else if (M1 != -1 && M2 == -1) {
                        M2 = ((3 * 5) - M1) / 2;
                        M2 = Arredondar(M2);
                        NF = (M1 + (M2 * 2)) / 3;
                        NF = Arredondar(NF);

                    } else if (M1 != -1 && M2 != -1) {
                        NF = (M1 + (M2 * 2)) / 3;
                        NF = Arredondar(NF);

                        if (NF < 5) {
                            if (M1 != -1 && M2 != -1 && NF != -1 && EX != -1) {

                                NF = NF + (EX - 5);
                            } else {
                                EX = 5 + (5 - NF);
                            }


                        }


                    } else if (M1 != -1 && M2 == -1) {
                        M2 = ((3 * 5) - M1) / 2;
                        M2 = Arredondar(M2);

                        NF = (M1 + (M2 * 2)) / 3;
                        NF = Arredondar(NF);
                    }

                } else {

                    if (M1 != -1 && PI != -1 && M2 == -1) {
                        M2 = ((3 * 5) - M1 - (0.6 * PI)) / 1.4;
                        M2 = Arredondar(M2);

                        NF = (M1 + ((PI * 0.3 + M2 * 0.7) * 2)) / 3;
                        NF = Arredondar(NF);

                    } else if (M1 != -1 && PI == -1 && M2 == -1) {
                        PI = 0;
                        M2 = ((3 * 5) - M1 - (0.6 * PI)) / 1.4;
                        M2 = Arredondar(M2);
                        NF = (M1 + ((PI * 0.3 + M2 * 0.7) * 2)) / 3;
                        NF = Arredondar(NF);

                    } else if (M1 != -1 && PI == -1 && M2 != -1) {
                        PI = 0;
                        M2 = Arredondar(M2);
                        NF = (M1 + ((PI * 0.3 + M2 * 0.7) * 2)) / 3;
                        NF = Arredondar(NF);

                    } else if (M1 != -1 && PI != -1 && M2 != -1) {
                        NF = (M1 + ((PI * 0.3 + M2 * 0.7) * 2)) / 3;
                        NF = Arredondar(NF);


                    } else if (M1 != -1 && PI != -1 && M2 != -1 && NF != -1) {
                        if (NF < 5) {
                            if (M1 != -1 && PI != -1 && M2 != -1 && NF != -1 && EX != -1) {

                                NF = NF + (EX - 5);
                            } else {
                                EX = 5 + (5 - NF);
                            }

                        }
                    }
                }
                if ((nDisciplina2.equals(disciplina2)) && (nM1 == M1) && (nPI == PI) && (nM2 == M2) && (nEX == EX) && (nNF == NF) && (nDP == DP)) {

                    this.finish();
                } else {

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage("Descartar alterações?");
                    builder.setPositiveButton("Descartar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick (DialogInterface dialog, int which) {
                            DialogActivity.this.finish();


                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick (DialogInterface dialog, int which) {
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
                        public void onClick (DialogInterface dialog, int which) {
                            DialogActivity.this.finish();


                        }
                    });
                    builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick (DialogInterface dialog, int which) {
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

    private static void Revelar (View myView) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            int cx = myView.getWidth() / 2;
            int cy = myView.getHeight() / 2;

// get the final radius for the clipping circle
            int finalRadius = Math.max(myView.getWidth(), myView.getHeight());

// create the animator for this view (the start radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0, finalRadius);

// make the view visible and start the animation
            myView.setVisibility(View.VISIBLE);
            anim.start();
        }

    }

    private static void Esconder (final View myView) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

// get the center for the clipping circle
            int cx = myView.getWidth() / 2;
            int cy = myView.getHeight() / 2;

// get the initial radius for the clipping circle
            int initialRadius = myView.getWidth();

// create the animation (the final radius is zero)
            Animator anim =
                    ViewAnimationUtils.createCircularReveal(myView, cx, cy, initialRadius, 0);

// make the view invisible when the animation is done
            anim.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd (Animator animation) {
                    super.onAnimationEnd(animation);
                    myView.setVisibility(View.GONE);
                }
            });

// start the animation
            anim.start();
        }
    }

}

