package com.caeumc.caeumc;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.caeumc.php.Agenda;
import com.google.android.gms.auth.GooglePlayServicesAvailabilityException;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;
import com.splunk.mint.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.view.View.INVISIBLE;


public class NavDrawerActivity extends AppCompatActivity {

    static final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    static final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    static final int REQUEST_CODE_PICK_ACCOUNT = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;

    private static final int SIGNED_IN = 0;
    private static final int STATE_SIGNING_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;
    private static final int RC_SIGN_IN = 0;
    static final String fileName = "CAEUMC.apk";
    static final String url = "http://caeumc.com/download/CAEUMC.apk";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String TAG = "drive-quickstart";
    private static final String[] SCOPES = {CalendarScopes.CALENDAR_READONLY, Plus.SCOPE_PLUS_LOGIN.toString(), Plus.SCOPE_PLUS_PROFILE.toString()};
    private static final String ACCOUNT_NAMENAV = "accountNameNav";
    private static final String ACCOUNT_EMAIL = "accountEmail";
    private static final String ACCOUNT_PHOTOURL = "accountPhotoURL";
    final static private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    public static int notifyUpdate = 0;
    public static int disciplinaPosition = -99;
    public static int nDisciplinas = 999999999;
    public static long idEvento;
    public static Bitmap imageProfile = null;
    public static ProgressDialog mProgressDialog;
    public static boolean attAvailable;
    static int actionModeStatus = 0;
    static Context context;
    static Context contextFragment;
    static View snackView;
    static View appView;
    static Activity activityDisciplina;
    static Activity mainActivity;
    static Activity fragmentActivity;
    static NavDrawerActivity drawerActivity;
    static List<String> teste = new ArrayList<String>();
    static List<Integer> selectedDisciplinas = new ArrayList<>();
    static CoordinatorLayout rootLayout;
    static ActionMode.Callback mDeleteMode;
    static ActionMode mActionDeleteMode;
    static List<MateriaListModel> materiaListModels;
    static com.google.api.services.calendar.Calendar mService;
    static GoogleApiClient mGoogleApiClient;
    static GoogleAccountCredential credential;
    static String mEmail;
    static int fragmentatual = -1;
    static boolean mudarusuario = false;
    static String emailantigo = "";
    static List<EventosListModel> eventosListModels;
    static long idDeletar;
    static Button btnCarregarAgenda;
    static TextView txtNomeAccount;
    static TextView txtEmailAccount;
    static CircleImageView imgImageProfile;


    public static SwipeRefreshLayout swipeRefreshLayout;

    private static NavigationView mNavigationView;
    private static ListView lstMaterias;
    private static ListView lstEventos;

    private static String ALTERACAO2 = "0";
    public static String accountNameNav = "";
    public static String accountEmail = "";
    public static String accountPhotoURL = "";
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private Toolbar toolbar;


    // Request code to use when launching the resolution activity
    private static final int REQUEST_RESOLVE_ERROR = 1001;
    // Unique tag for the error dialog fragment
    private static final String DIALOG_ERROR = "dialog_error";
    // Bool to track whether the app is already resolving an error
    private boolean mResolvingError = false;

    private static final String STATE_RESOLVING_ERROR = "resolving_error";

    static final int REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR = 1001;

    private static void chooseAccount () {

        /*Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        drawerActivity.startActivityForResult(signInIntent, RC_SIGN_IN);*/

        String[] accountTypes = new String[]{"com.google"};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null, accountTypes, false, null, null, null, null);
        drawerActivity.startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);

    }

    private static void AlterarConta () {

        //drawerActivity.startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_RESOLVING_ERROR, mResolvingError);
    }

    private static void refreshResults () {
            if (isDeviceOnline()) {
                new ApiAsyncTask(drawerActivity, mEmail).execute();

            } else {
                Toast.makeText(contextFragment, "Sem conexão com a internet.", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
                btnCarregarAgenda.setEnabled(true);
                swipeRefreshLayout.setEnabled(true);
            }
    }

    public static void clearResultsText () {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run () {
                EventosListModel.deleteAll(EventosListModel.class);

            }
        });
    }

    public static void updateResultsText (final List<EventosListModel> dataStrings) {
        fragmentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run () {
                lstEventos = (ListView) snackView.findViewById(R.id.lstEventos);
                if (dataStrings == null) {
                    Toast.makeText(contextFragment, "Erro recuperando dados!", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(true);
                } else if (dataStrings.size() == 0) {
                    Toast.makeText(contextFragment, "Nenhum dado encontrado.", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(true);
                } else {
                    // eventosList = EventosListModel.findWithQuery(EventosListModel.class, "SELECT * FROM EVENTOS_LIST_MODEL ORDER BY datetime(data*1000, 'unixepoch', 'localtime') DESC");
                    EventosListAdapter arrayAdapter = new EventosListAdapter(drawerActivity, contextFragment);
                    String mesAnterior = "";
                    String semanaAnterior = "";
                    int diaAnterior = 0;
                    EventosListModel model = new EventosListModel();
                    EventosListModel diaModel = new EventosListModel();

                    for (int i = 0; i < dataStrings.size(); i++) {
                        model = dataStrings.get(i);
                        diaModel = dataStrings.get(i);
                        String mesEvento = getMesEvento(dataStrings.get(i).getData());
                        int diaEvento = getDiaEvento(dataStrings.get(i).getData());
                        String semana = getSemana(diaEvento);
                        if (mesAnterior.equals("")) {
                            model = dataStrings.get(i);
                            model.setMes(mesEvento);
                            model.setSemana(String.format("%s de %s", semana, mesEvento));
                            arrayAdapter.addSectionHeaderItem(model);
                            arrayAdapter.addSemanaItem(model);
                            arrayAdapter.addItem(dataStrings.get(i));
                            mesAnterior = mesEvento;
                            diaAnterior = diaEvento;
                            semanaAnterior = semana;
                        } else if (mesAnterior == mesEvento) {
                            if (diaEvento == diaAnterior) {
                                diaModel = dataStrings.get(i);
                                diaModel.setDiaEvento(0);
                                diaModel.setDiaSemanaEvento("");
                                arrayAdapter.addItem(diaModel);
                            } else {
                                if (!semana.equals(semanaAnterior)) {
                                    model.setSemana(String.format("%s de %s", semana, mesEvento));
                                    arrayAdapter.addSemanaItem(model);
                                }
                                arrayAdapter.addItem(model);
                            }
                            diaAnterior = diaEvento;
                            semanaAnterior = semana;

                        } else {
                            model = dataStrings.get(i);
                            model.setMes(mesEvento);
                            arrayAdapter.addSectionHeaderItem(model);
                            if (diaEvento == diaAnterior) {
                                diaModel = dataStrings.get(i);
                                diaModel.setDiaEvento(0);
                                diaModel.setDiaSemanaEvento("");
                                arrayAdapter.addItem(diaModel);
                            } else {
                                if (!semana.equals(semanaAnterior)) {
                                    model.setSemana(String.format("%s de %s", semana, mesEvento));
                                    arrayAdapter.addSemanaItem(model);
                                }
                                arrayAdapter.addItem(model);
                            }
                            mesAnterior = mesEvento;
                            diaAnterior = diaEvento;
                            semanaAnterior = semana;
                        }
                    }
                    lstEventos.setDividerHeight(0);
                    lstEventos.setDivider(null);
                    lstEventos.setClickable(false);
                    lstEventos.setAdapter(arrayAdapter);
                    swipeRefreshLayout.setEnabled(true);
                    swipeRefreshLayout.setRefreshing(false);
                    /*Toast.makeText(contextFragment, "Data retrieved using" +
                            " the Google Calendar API:", Toast.LENGTH_LONG).show();*/

                }
            }
        });
    }

    static void AtualizarCalendario () {

        refreshResults();

    }

    private static String getMesEvento (long data) {
        Date dataEvento = new Date(data * 1000L);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataEvento);
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

        return mesEvento;
    }

    private static int getDiaEvento (long data) {
        Date dataEvento = new Date(data * 1000L);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataEvento);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return day;
    }

    private static String getSemana (int dia) {
        String semana = "";
        TreeSet<Integer> semana1 = new TreeSet<Integer>();
        semana1.add(1);
        semana1.add(2);
        semana1.add(3);
        semana1.add(4);
        semana1.add(5);
        semana1.add(6);
        TreeSet<Integer> semana2 = new TreeSet<Integer>();
        semana2.add(7);
        semana2.add(8);
        semana2.add(9);
        semana2.add(10);
        semana2.add(11);
        semana2.add(12);
        TreeSet<Integer> semana3 = new TreeSet<Integer>();
        semana3.add(13);
        semana3.add(14);
        semana3.add(15);
        semana3.add(16);
        semana3.add(17);
        semana3.add(18);
        TreeSet<Integer> semana4 = new TreeSet<Integer>();
        semana4.add(19);
        semana4.add(20);
        semana4.add(21);
        semana4.add(22);
        semana4.add(23);
        semana4.add(24);
        TreeSet<Integer> semana5 = new TreeSet<Integer>();
        semana5.add(25);
        semana5.add(26);
        semana5.add(27);
        semana5.add(28);
        semana5.add(29);
        semana5.add(30);
        semana5.add(31);
        if (semana1.contains(dia)) {
            semana = String.format("%s-%s", semana1.first(), semana1.last());
        } else if (semana2.contains(dia)) {
            semana = String.format("%s-%s", semana2.first(), semana2.last());
        } else if (semana3.contains(dia)) {
            semana = String.format("%s-%s", semana3.first(), semana3.last());
        } else if (semana4.contains(dia)) {
            semana = String.format("%s-%s", semana4.first(), semana4.last());
        } else if (semana5.contains(dia)) {
            semana = String.format("%s-%s", semana5.first(), semana5.last());
        }
        return semana;


    }


    public static void handleException (final Exception e) {
        // Because this call comes from the AsyncTask, we must ensure that the following
        // code instead executes on the UI thread.
        drawerActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (e instanceof GooglePlayServicesAvailabilityException) {
                    // The Google Play services APK is old, disabled, or not present.
                    // Show a dialog created by Google Play services that allows
                    // the user to update the APK
                    int statusCode = ((GooglePlayServicesAvailabilityException)e)
                            .getConnectionStatusCode();
                    GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
                    Dialog dialog = googleApiAvailability.getErrorDialog(
                            drawerActivity,
                            statusCode,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                    dialog.show();
                } else if (e instanceof UserRecoverableAuthException) {
                    // Unable to authenticate, such as when the user has not yet granted
                    // the app access to the account, but the user can fix this.
                    // Forward the user to an activity in Google Play services.
                    Intent intent = ((UserRecoverableAuthException)e).getIntent();
                    drawerActivity.startActivityForResult(intent,
                            REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR);
                }
            }
        });
    }



    public static void updateStatus (final String message) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run () {
                Toast.makeText(contextFragment, message, Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(true);
            }
        });
    }

    private static boolean isDeviceOnline () {
        ConnectivityManager connMgr =
                (ConnectivityManager) contextFragment.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private static boolean isGooglePlayServicesAvailable () {
        //NavDrawerActivity navDrawerActivity = new NavDrawerActivity();
        GoogleApiAvailability googleApiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                googleApiAvailability.isGooglePlayServicesAvailable(context);
        if (googleApiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS) {
            return false;
        }
        return true;
    }

    static void showGooglePlayServicesAvailabilityErrorDialog (

            final int connectionStatusCode) {
        final NavDrawerActivity navDrawerActivity = new NavDrawerActivity();
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run () {
                GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
                Dialog dialog = apiAvailability.getErrorDialog(
                        drawerActivity,
                        connectionStatusCode,
                        REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }

    private static void getUserAccountWrapper () {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(contextFragment, Manifest.permission.GET_ACCOUNTS);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(fragmentActivity, Manifest.permission.GET_ACCOUNTS)) {
                showMessageOKCancel("Você precisa aceitar para vizualizar a agenda",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick (DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(fragmentActivity, new String[]{Manifest.permission.GET_ACCOUNTS},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(fragmentActivity, new String[]{Manifest.permission.GET_ACCOUNTS},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        } else {
            chooseAccount();
        }
    }

    private static void showMessageOKCancel (String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(fragmentActivity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialog, int which) {
                        swipeRefreshLayout.setEnabled(true);
                        swipeRefreshLayout.setRefreshing(false);
                        btnCarregarAgenda.setEnabled(true);
                    }
                })
                .create()
                .show();
    }

    public static String readFileAsString (String fileName) {
        // Context context = App.instance.getApplicationContext();
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(new File(context.getCacheDir(), fileName)));
            while ((line = in.readLine()) != null) stringBuilder.append(line);

        } catch (FileNotFoundException e) {
            Logger.logError(e.getMessage());
        } catch (IOException e) {
            Logger.logError(e.getMessage());
        }

        return stringBuilder.toString();
    }

    private static void BaixarAtualizacao () {
        ValidateInternetConnection internetConnection = new ValidateInternetConnection(context);
        int status = internetConnection.isConnected();

        if (status != -1) {
            if (status == ConnectivityManager.TYPE_MOBILE) {
                new AlertDialog.Builder(fragmentActivity)
                        .setMessage("Você está usando uma conexão de dados móveis, continuar consumirá seu pacote de dados")
                        .setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick (DialogInterface dialog, int which) {

                                File path = new File(context.getCacheDir(), fileName);
                                final DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask(path, fileName, context, false, url);
                                try {
                                    downloadAsyncTask.execute();
                                    mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel (DialogInterface dialog) {
                                            downloadAsyncTask.cancel(true);
                                        }
                                    });
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                            }
                        })
                        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick (DialogInterface dialog, int which) {

                                dialog.dismiss();

                            }
                        })
                        .create()
                        .show();

            } else if (status == ConnectivityManager.TYPE_WIFI) {

                new AlertDialog.Builder(fragmentActivity)
                        .setMessage("Gostaria de atualizar agora?")
                        .setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick (DialogInterface dialog, int which) {
                                //String fileName = "CAEUMC.apk";
                                //String fileName = "app-debug.apk";
                                //String url = "http://caeumc.com/download/CAEUMC.apk";
                                //String url = "http://caeumc.com/download/app-debug.apk";
                                File path = new File(context.getCacheDir(), fileName);
                                final DownloadAsyncTask downloadAsyncTask = new DownloadAsyncTask(path, fileName, context, false, url);
                                try {
                                    downloadAsyncTask.execute();
                                    mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                        @Override
                                        public void onCancel (DialogInterface dialog) {
                                            downloadAsyncTask.cancel(true);
                                        }
                                    });
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }
                            }
                        })
                        .setNegativeButton("Depois", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick (DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .create()
                        .show();

            }

        } else {

            Toast.makeText(contextFragment, "Sem conexão com a internet", Toast.LENGTH_LONG).show();

        }
    }


    @Override
    protected void onStart () {
        super.onStart();

    }

    @Override
    protected void onStop () {
        super.onStop();


    }

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navdrawerlayout);


        // Parse.initialize(this, "eU6KU5piZYvdIrCnTD2CmOZoLvmESO7G4f9fT7vr", "i9CjuOJzVAYx4eaC7qiwwFE5La5CUj9ju89v1OsT");
        // ParseAnalytics.trackAppOpenedInBackground(getIntent());


        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        appView = this.getWindow().getDecorView().getRootView();


        mainActivity = this;
        drawerActivity = this;


        mTitle = mDrawerTitle = getSupportActionBar().getTitle();

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout2);

        mNavigationView = (NavigationView) findViewById(R.id.navigation);


        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

        context = getApplicationContext();

        mResolvingError = savedInstanceState != null
                && savedInstanceState.getBoolean(STATE_RESOLVING_ERROR, false);

      /*  GoogleSignInOptions gso =
                new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestScopes(new Scope("email"), new Scope(CalendarScopes.CALENDAR_READONLY))
                        .build();
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addConnectionCallbacks(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addOnConnectionFailedListener(this)
                .addScope(new Scope("email"))
                .build();*/

        ValidateInternetConnection internetConnection = new ValidateInternetConnection(this);
        int i = internetConnection.isConnected();

        if (i != -1) {
            attAvailable = checkUpdate();
        }

        mProgressDialog = new ProgressDialog(NavDrawerActivity.this);
        mProgressDialog.setMessage("Baixando atualização...");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                // R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClose (View view) {
                //  getSupportActionBar().setTitle(mTitle);

                invalidateOptionsMenu();
                //   getSupportActionBar().show();
            }

            public void onDrawerOpened (View drawerView) {
                //  getSupportActionBar().setTitle(mDrawerTitle);

                invalidateOptionsMenu();
                //     getSupportActionBar().show();
            }
        };
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (savedInstanceState == null) {
            selectItem(3, "CAE UMC");
        }

        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected (MenuItem menuItem) {
                mDrawerLayout.closeDrawers();
                menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.nav_home:
                        selectItem(3, "CAE UMC");
                        break;
                    case R.id.nav_notas:
                        selectItem(0, "Notas");
                        break;
                    case R.id.nav_calendario:
                        selectItem(1, "Calendário Acadêmico");
                        break;
                    case R.id.nav_arquivos:
                        selectItem(2, "Arquivos");
                        break;
                    // TODO - Handle other items
                }
                return true;
            }
        });

        View headerLayout = mNavigationView.getHeaderView(0);


        txtNomeAccount = (TextView) headerLayout.findViewById(R.id.nome_account);
        txtEmailAccount = (TextView) headerLayout.findViewById(R.id.email_account);
        imgImageProfile = (CircleImageView) headerLayout.findViewById(R.id.image_profile);

        SharedPreferences sharedPreferences = drawerActivity.getPreferences(MODE_PRIVATE);
        accountNameNav = sharedPreferences.getString(ACCOUNT_NAMENAV, "");
        accountEmail = sharedPreferences.getString(ACCOUNT_EMAIL, "");
        accountPhotoURL = sharedPreferences.getString(ACCOUNT_PHOTOURL, "");

        if (accountNameNav != null) {
            if (accountNameNav.isEmpty() && accountEmail.isEmpty()) {
                txtNomeAccount.setText("Centro Acadêmico de Engenharia");
                txtNomeAccount.setVisibility(View.VISIBLE);
            } else if (accountNameNav.isEmpty()) {
                txtNomeAccount.setVisibility(INVISIBLE);
            } else {
                txtNomeAccount.setText(accountNameNav);
                txtNomeAccount.setVisibility(View.VISIBLE);
            }
        } else if (accountEmail.isEmpty()) {
            txtNomeAccount.setText("Centro Acadêmico de Engenharia");
            txtNomeAccount.setVisibility(View.VISIBLE);
        } else {
            txtNomeAccount.setVisibility(INVISIBLE);
        }

        if (accountEmail != null) {
            if (accountEmail.isEmpty()) {
                txtEmailAccount.setText("UMC - Villa Lobos");
            } else {
                txtEmailAccount.setText(accountEmail);
                txtEmailAccount.setVisibility(View.VISIBLE);
            }
        }

        if (accountPhotoURL != null) {
            if (!accountPhotoURL.isEmpty()) {
                Uri uri = Uri.parse(accountPhotoURL);
                try {
                    imageProfile = new ImageAsyncTask(drawerActivity, context, uri.toString(), imgImageProfile).execute(uri.toString()).get();
                    if (imageProfile != null) {
                        imgImageProfile.setImageBitmap(imageProfile);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                imgImageProfile.setImageResource(R.drawable.logoandroid);
            }
        } else {
            imgImageProfile.setImageResource(R.drawable.logoandroid);
        }


    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        if (fragmentatual == 1) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.calendario_menu, menu);
        } else if (fragmentatual == 3) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.home_menu, menu);
        }

        return super.onCreateOptionsMenu(menu);
    }

    //* Called whenever we call invalidateOptionsMenu() *//*
    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
        //  boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mNavigationView);
//       menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onBackPressed () {

        if (mDrawerLayout.isDrawerOpen(mNavigationView) == true) {
            mDrawerLayout.closeDrawers();
        } else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();

        } else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch (item.getItemId()) {
            case R.id.atualizar:
                swipeRefreshLayout.setEnabled(false);
                swipeRefreshLayout.setRefreshing(true);
                AtualizarCalendario();
                return true;
            case R.id.opcoes_conta:
                Intent intent = new Intent(this, SettingsAgendaActivity.class);
                startActivity(intent);
                return true;
            case R.id.checkUpdate:
                if (checkUpdate()) {
                    new AlertDialog.Builder(fragmentActivity)
                            .setMessage("Uma nova versão está disponível, gostaria de atualizar agora?")
                            .setPositiveButton("Atualizar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface dialog, int which) {
                                    BaixarAtualizacao();
                                }
                            })
                            .setNegativeButton("Depois", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create()
                            .show();
                } else {
                    new AlertDialog.Builder(fragmentActivity).setMessage("Nenhuma atualização disponível.")
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick (DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .create()
                            .show();
                }
                return true;
            case R.id.addagenda:
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(NavDrawerActivity.this);
                    alertDialog.setTitle("Adicionar Agenda");
                    alertDialog.setMessage("Identificação da agenda");


                final EditText input = new EditText(NavDrawerActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                InputFilter filter = new InputFilter() {
                    public CharSequence filter(CharSequence source, int start, int end,
                                               Spanned dest, int dstart, int dend) {
                        for (int i = start; i < end; i++) {
                            if ( !Character.isLetterOrDigit(source.charAt(i)) && !Character.toString(source.charAt(i)).equals("-")) {
                                return "";
                            }
                        }
                        return null;
                    }
                };
                input.setFilters(new InputFilter[]{filter});
                input.setHint("Apenas letras, números e hífens permitidos");
                alertDialog.setView(input);

                alertDialog.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialog, int which) {

                        String identificacao = input.getText().toString();
                        if (identificacao != null) {
                            if (!identificacao.trim().equals("")) {

                                ArrayList<Agenda> agendas = new ArrayList<Agenda>();
                                String[] strings = new String[2];
                                strings[0] = "getAgendasIdentificacao";
                                strings[1] = identificacao;
                                try {
                                    agendas = new AgendasPhp().execute(strings).get();
                                } catch (InterruptedException e) {
                                    Toast.makeText(NavDrawerActivity.this, "Agenda não existe", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    Toast.makeText(NavDrawerActivity.this, "Agenda não existe", Toast.LENGTH_LONG).show();
                                    e.printStackTrace();
                                }
                                if (agendas != null) {

                                    if (agendas.size() == 1) {
                                        final ArrayList<Agenda> finalAgendas = agendas;
                                        new AlertDialog.Builder(NavDrawerActivity.this)
                                                .setMessage("Uma agenda encontrada com essa identificação, deseja adicioná-lá?")
                                                .setTitle("Agenda Encontrada")
                                                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick (DialogInterface dialog, int which) {
                                                        dialog.dismiss();
                                                    }
                                                })
                                                .setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick (DialogInterface dialog, int which) {
                                                        String idAgenda = finalAgendas.get(0).getIdAgenda().toString();
                                                        List<AgendasListModel> agendasListModelList = AgendasListModel.find(AgendasListModel.class, "id_agenda = ?", idAgenda);

                                                        if (agendasListModelList != null) {
                                                            if (agendasListModelList.size() > 0) {
                                                                AgendasListModel agendasListModel = AgendasListModel.findById(AgendasListModel.class, agendasListModelList.get(0).getId());
                                                                agendasListModel.setIdAgenda(finalAgendas.get(0).getIdAgenda());
                                                                agendasListModel.setIdentificacao(finalAgendas.get(0).getIdentificacao());
                                                                agendasListModel.setEndereco(finalAgendas.get(0).getEndereco());
                                                                agendasListModel.setCompartilhado(finalAgendas.get(0).getCompartilhado());
                                                                agendasListModel.setIdUsuario(finalAgendas.get(0).getIdUsuario());
                                                                agendasListModel.save();
                                                            } else {

                                                                AgendasListModel agendasListModel = new AgendasListModel(finalAgendas.get(0).getIdAgenda(), finalAgendas.get(0).getIdentificacao(),
                                                                        finalAgendas.get(0).getEndereco(), finalAgendas.get(0).getCompartilhado(), finalAgendas.get(0).getIdUsuario());
                                                                agendasListModel.save();

                                                            }
                                                        } else {
                                                            AgendasListModel agendasListModel = new AgendasListModel(finalAgendas.get(0).getIdAgenda(), finalAgendas.get(0).getIdentificacao(),
                                                                    finalAgendas.get(0).getEndereco(), finalAgendas.get(0).getCompartilhado(), finalAgendas.get(0).getIdUsuario());
                                                            agendasListModel.save();
                                                        }
                                                    }
                                                })
                                                .show();
                                    }

                                } else {
                                    Toast.makeText(NavDrawerActivity.this, "Agenda não existe", Toast.LENGTH_LONG).show();
                                }
                            }


                        }
                    }
                });

                alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick (DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                alertDialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void selectItem (int position, String titleName) {
        // update the main content by replacing fragments
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.content_frame, fragment).addToBackStack("fragBack").commit();
        setTitle(titleName);
        // mDrawerLayout.closeDrawer(mDrawerList);
        mDrawerLayout.closeDrawer(mNavigationView);
    }

    @Override
    public void setTitle (CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate (Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged (Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    public String getSelectedTitle () {
        String Title = getString(R.string.selected_count, selectedDisciplinas.size());
        return Title;
    }

   /* @Override
    public void onConnectionFailed (ConnectionResult result) {


        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.

            }
        } else {
            // Show dialog using GoogleApiAvailability.getErrorDialog()
            showErrorDialog(result.getErrorCode());
            mResolvingError = true;
        }




        // Called whenever the API client fails to connect.
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());


       *//* if (mSignInProgress != STATE_IN_PROGRESS) {
            mSignInIntent = result.getResolution();
            if (mSignInProgress == STATE_SIGNING_IN) {
                resolveSignInError();
            }
        }
        // Will implement shortly
        onSignedOut();*//*


    }

    private void showErrorDialog(int errorCode) {
        // Create a fragment for the error dialog
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getSupportFragmentManager(), "errordialog");
    }

    public void onDialogDismissed() {
        mResolvingError = false;
    }

    public static class ErrorDialogFragment extends DialogFragment {
        public ErrorDialogFragment() { }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Get the error code and retrieve the appropriate dialog
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            ((NavDrawerActivity) getActivity()).onDialogDismissed();
        }
    }


    private void onSignedOut() {
        // Update the UI to reflect that the user is signed out.
        btnCarregarAgenda.setEnabled(true);
        swipeRefreshLayout.setRefreshing(false);
        swipeRefreshLayout.setEnabled(true);
    }



    @Override
    public void onConnected (Bundle connectionHint) {

        mSignInProgress = SIGNED_IN;

      *//*  try {

            Intent intent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
            GoogleSignInResult signInResult = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);

            if (signInResult.isSuccess()) {
                GoogleSignInAccount signInAccount = signInResult.getSignInAccount();
                mEmail = signInAccount.getEmail();
                credential.setSelectedAccountName(mEmail);
            }

        }
        catch(Exception ex){
            Log.v(TAG, "Deu ruim");
        }*//*

        Log.i(TAG, "API client connected.");
    }

    @Override
    public void onConnectionSuspended (int cause) {
        mGoogleApiClient.connect();
        Log.i(TAG, "GoogleApiClient connection suspended");
    }*/

    private void getUsername() {
        if (mEmail == null) {
            getUserAccountWrapper();
        } else {
            if (isDeviceOnline()) {
                new GetUsernameTask(NavDrawerActivity.this, mEmail, "oauth2:"+CalendarScopes.CALENDAR_READONLY+ " "+ Plus.SCOPE_PLUS_PROFILE).execute();
            } else {
                Toast.makeText(this, "Sem conexão com a internet", Toast.LENGTH_LONG).show();
            }
        }
    }





    @Override
    public void onActivityResult (
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_RESOLVE_ERROR:
                mResolvingError = false;
                if (resultCode == RESULT_OK) {

                }
                break;

            case REQUEST_CODE_PICK_ACCOUNT:
                if (resultCode == RESULT_OK) {
                    //GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                    //if (result.isSuccess()) {
                        //GoogleSignInAccount acct = result.getSignInAccount();
                        String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                        mEmail = accountName;
                       // accountNameNav = acct.getDisplayName();
                        //accountEmail = acct.getEmail();
                        //Uri uri;
                        //uri = acct.getPhotoUrl();
                        //if (uri != null) {
                        //    accountPhotoURL = uri.toString();
                       // }
                        if (accountName != null) {
                            credential.setSelectedAccountName(accountName);
                            getUsername();
                            SharedPreferences settings =
                                    fragmentActivity.getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = settings.edit();
                            editor.putString(PREF_ACCOUNT_NAME, accountName);
                            editor.putString(ACCOUNT_NAMENAV, accountNameNav);
                            editor.putString(ACCOUNT_EMAIL, accountEmail);
                            editor.putString(ACCOUNT_PHOTOURL, accountPhotoURL);
                            editor.apply();





                            if (accountNameNav != null) {
                                if (accountNameNav.isEmpty() && accountEmail.isEmpty()) {
                                    txtNomeAccount.setText("Não logado");
                                    txtNomeAccount.setVisibility(View.VISIBLE);
                                } else if (accountNameNav.isEmpty()) {
                                    txtNomeAccount.setVisibility(INVISIBLE);
                                } else {
                                    txtNomeAccount.setText(accountNameNav);
                                    txtNomeAccount.setVisibility(View.VISIBLE);
                                }
                            } else if (accountEmail.isEmpty()) {
                                txtNomeAccount.setText("Não logado");
                                txtNomeAccount.setVisibility(View.VISIBLE);
                            } else {
                                txtNomeAccount.setVisibility(INVISIBLE);
                            }

                            if (accountEmail != null) {
                                if (accountEmail.isEmpty()) {
                                    txtEmailAccount.setVisibility(View.INVISIBLE);
                                } else {
                                    txtEmailAccount.setText(accountEmail);
                                    txtEmailAccount.setVisibility(View.VISIBLE);
                                }
                            }

                            if (accountPhotoURL != null) {
                                if (!accountPhotoURL.isEmpty()) {
                                    Uri uri2 = Uri.parse(accountPhotoURL);
                                    try {
                                        imageProfile = new ImageAsyncTask(drawerActivity, context, uri2.toString(), imgImageProfile).execute(uri2.toString()).get();
                                        if (imageProfile != null) {
                                            imgImageProfile.setImageBitmap(imageProfile);
                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    imgImageProfile.setImageResource(R.drawable.dummyavatar);
                                }
                            } else {
                                imgImageProfile.setImageResource(R.drawable.dummyavatar);
                            }

                        }
                    /*} else {

                        Toast.makeText(contextFragment, "Conta não especificada.", Toast.LENGTH_LONG).show();
                        swipeRefreshLayout.setEnabled(false);
                        swipeRefreshLayout.setRefreshing(false);
                        btnCarregarAgenda.setEnabled(true);
                        mudarusuario = false;
                    }*/
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(contextFragment, "Conta não especificada.", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setEnabled(true);
                    swipeRefreshLayout.setRefreshing(false);
                    btnCarregarAgenda.setEnabled(true);
                    mudarusuario = false;
                } else if (requestCode == REQUEST_CODE_RECOVER_FROM_PLAY_SERVICES_ERROR && resultCode == RESULT_OK) {

                    //getUsername();

                }
                break;

        }

        //super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult (int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    getUserAccountWrapper();
                } else {
                    // Permission Denied
                    swipeRefreshLayout.setEnabled(true);
                    swipeRefreshLayout.setRefreshing(false);
                    btnCarregarAgenda.setEnabled(true);
                    Toast.makeText(NavDrawerActivity.this, "Permissão negada, vá para as opções e ative as permissões para esse aplicativo", Toast.LENGTH_LONG)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    private boolean checkUpdate () {

        final String PREFS_NAME = "com.caeumc.caeumc";
        final int DOESNT_EXIST = -1;


        // Get current version code
        int currentVersionCode = 0;
        try {
            currentVersionCode = BuildConfig.VERSION_CODE;
        } catch (Exception e) {
            // handle exception
            e.printStackTrace();
        }

        int savedVersionCode = 0;
        String fileName = "VersionApplication.txt";
        File versionApplication = new File(context.getCacheDir(), fileName);
        String url = "http://caeumc.com/download/VersionApplication.txt";
        //boolean statusFTPDownload = DownloadFilesFTP(versionApplication, fileName);
            new DownloadAsyncTask(versionApplication, fileName, context, true, url).execute();

        // if (statusFTPDownload) {
        String versionServer = readFileAsString(fileName);
        if (versionServer != null) {
            if (!versionServer.isEmpty()) {
                savedVersionCode = Integer.parseInt(versionServer);
            } else {
                savedVersionCode = -1;
            }
        } else {
            savedVersionCode = -1;
        }

        //} else {
        // Log.e("Download arquivo versão", "Deu ruim");
        //  savedVersionCode = -1;
        // }

        // Get saved version code

        // Check for first run or upgrade
        if (currentVersionCode == savedVersionCode) {

            return false;

        } else if (savedVersionCode == DOESNT_EXIST) {

            return false;


        } else if (currentVersionCode < savedVersionCode) {

            return true;

        } else {
            return false;
        }
        // Update the shared preferences with the current version code
    }

    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";


        // private MultiSelector mMultiSelector = new SingleSelector();
        private int statusBarColor;

        public PlanetFragment () {
            // Empty constructor required for fragment subclasses
        }

        @Override
        public void onStart () {
            super.onStart();
            int i = getArguments().getInt(ARG_PLANET_NUMBER);

        }

        @Override
        public void onStop () {
            super.onStop();
            int i = getArguments().getInt(ARG_PLANET_NUMBER);

        }

        @Override
        public void onPause () {
            super.onPause();
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            int n = Integer.parseInt(ALTERACAO2);



        }

        @Override
        public void onResume () {
            super.onResume();

            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            int n = Integer.parseInt(ALTERACAO2);

            if (i == 3) {
                mNavigationView.getMenu().getItem(0).setChecked(true);
                //TextView txtWelcome = (TextView) appView.findViewById(R.id.textView);
                // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                //txtWelcome.setText(sharedPreferences.getString("alterar_usuario",""));
            }


            if (i == 0) {
                mNavigationView.getMenu().getItem(1).setChecked(true);
                rootLayout = (CoordinatorLayout) appView.findViewById(R.id.content);


                if (notifyUpdate == 1) {
                    teste.clear();

                    activityDisciplina.runOnUiThread(new Runnable() {

                        @Override
                        public void run () {
                            materiaListModels = MateriaListModel.listAll(MateriaListModel.class);
                        }
                    });
                    if (materiaListModels != null) {
                        ArrayAdapter arrayAdapter = new ListAdapter(activityDisciplina, contextFragment, R.layout.materias_listrow, materiaListModels);
                        lstMaterias.setAdapter(arrayAdapter);
                    }

                    notifyUpdate = 0;
                    Snackbar.make(rootLayout, "Disciplina Alterada", Snackbar.LENGTH_SHORT).show();
                } else if (notifyUpdate == 2) {
                    activityDisciplina.runOnUiThread(new Runnable() {

                        @Override
                        public void run () {
                            materiaListModels = MateriaListModel.listAll(MateriaListModel.class);
                        }
                    });
                    if (materiaListModels != null) {
                        ArrayAdapter arrayAdapter = new ListAdapter(activityDisciplina, contextFragment, R.layout.materias_listrow, materiaListModels);
                        lstMaterias.setAdapter(arrayAdapter);
                        notifyUpdate = 0;
                        Snackbar.make(rootLayout, "Disciplina Adicionada", Snackbar.LENGTH_SHORT).show();

                    }

                }


                // int nDisciplinas2 = lstMaterias.getAdapter().getCount();
                ALTERACAO2 = "0";


            }

            if (i == 1) {
                mNavigationView.getMenu().getItem(2).setChecked(true);
                activityDisciplina.runOnUiThread(new Runnable() {
                    @Override
                    public void run () {
                        eventosListModels = EventosListModel.findWithQuery(EventosListModel.class, "SELECT * FROM EVENTOS_LIST_MODEL ORDER BY data*1000 ASC");
                    }
                });
                if (eventosListModels == null || eventosListModels.size() == 0) {


                }

                if (mudarusuario == true) {
                    if (mEmail != null) {
                        if (emailantigo.equals(mEmail)) {
                            swipeRefreshLayout.setEnabled(false);
                            swipeRefreshLayout.setRefreshing(true);
                            updateResultsText(eventosListModels);
                        } else {
                            if (isGooglePlayServicesAvailable()) {
                                Log.e("EmailAntigo", "Entrou no email antigo é diferente do email atual");
                                swipeRefreshLayout.setEnabled(false);
                                swipeRefreshLayout.setRefreshing(true);
                                refreshResults();

                            } else {
                                Toast.makeText(contextFragment, "Google Play Services é necessário: " +
                                        "após instalar, reinicie o aplicativo.", Toast.LENGTH_LONG).show();
                            }
                        }
                        Log.e("Email Antigo", emailantigo);
                        Log.e("Email Novo", mEmail);
                        mudarusuario = false;
                    } else {
                        if (isGooglePlayServicesAvailable()) {
                            Log.e("EmailAntigo", "Entrou no email antigo é diferente do email atual");
                            swipeRefreshLayout.setEnabled(false);
                            swipeRefreshLayout.setRefreshing(true);
                            refreshResults();

                        } else {
                            Toast.makeText(contextFragment, "Google Play Services é necessário: " +
                                    "após instalar, reinicie o aplicativo.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        }

        @Override
        public View onCreateView (LayoutInflater inflater, final ViewGroup container,
                                  Bundle savedInstanceState) {
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            fragmentatual = i;
            final View rootView;
            switch (i) {
                case 0:
                    mainActivity.invalidateOptionsMenu();

                    //mDeleteMode = new ModalMultiSelectorCallback(mMultiSelector) {
                    mDeleteMode = new ActionMode.Callback() {

                        @Override
                        public boolean onCreateActionMode (ActionMode actionMode, Menu menu) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                //hold current color of status bar

                                statusBarColor = drawerActivity.getWindow().getStatusBarColor();
                                //set your gray color
                                drawerActivity.getWindow().setStatusBarColor(Color.parseColor("#1A237E"));
                            }
                            getActivity().getMenuInflater().inflate(R.menu.delete_disciplina_menu, menu);
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode (ActionMode mode) {
                            actionModeStatus = 0;
                            lstMaterias.clearChoices();
                            for (int i = 0; i < lstMaterias.getCount(); i++)
                                lstMaterias.setItemChecked(i, false);
                            activityDisciplina.runOnUiThread(new Runnable() {
                                @Override
                                public void run () {
                                    materiaListModels = MateriaListModel.listAll(MateriaListModel.class);
                                }
                            });


                            if (materiaListModels.size() == 0 || materiaListModels == null) {
                                lstMaterias.setAdapter(null);
                            } else {
                                ArrayAdapter arrayAdapter = new ListAdapter(activityDisciplina, contextFragment, R.layout.materias_listrow, materiaListModels);
                                lstMaterias.setAdapter(arrayAdapter);
                            }
                            final FloatingActionButton fab = (FloatingActionButton) appView.findViewById(R.id.fab);
                            // fab.setVisibility(View.VISIBLE);
                            fab.show();
                            //lstMaterias.getAdapter().notifyDataSetChanged();
                        }

                        @Override
                        public boolean onPrepareActionMode (ActionMode mode, Menu menu) {

                            mode.setTitle("Excluir");
                            return false; // Return false if nothing is done
                        }


                        @Override
                        public boolean onActionItemClicked (ActionMode actionMode, MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.menu_item_delete_disciplina:
                                    // Delete crimes from model
                                    actionModeStatus = 0;


                                    for (int i = lstMaterias.getAdapter().getCount() - 1; i >= 0; i--) {
                                        if (lstMaterias.isItemChecked(i)) {
                                            idDeletar = lstMaterias.getAdapter().getItemId(i);
                                            activityDisciplina.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run () {
                                                    MateriaListModel materiaListModel = MateriaListModel.findById(MateriaListModel.class, idDeletar);
                                                    materiaListModel.delete();
                                                }
                                            });


                                        }
                                    }
                                    lstMaterias.clearChoices();
                                    for (int i = 0; i < lstMaterias.getCount(); i++)
                                        lstMaterias.setItemChecked(i, false);
                                    activityDisciplina.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run () {
                                            materiaListModels = MateriaListModel.listAll(MateriaListModel.class);
                                        }
                                    });


                                    if (materiaListModels.size() == 0 || materiaListModels == null) {
                                        lstMaterias.setAdapter(null);
                                    } else {
                                        ArrayAdapter arrayAdapter = new ListAdapter(activityDisciplina, contextFragment, R.layout.materias_listrow, materiaListModels);
                                        lstMaterias.setAdapter(arrayAdapter);
                                    }
                                    final FloatingActionButton fab = (FloatingActionButton) appView.findViewById(R.id.fab);
                                    //fab.setVisibility(View.VISIBLE);
                                    fab.show();
                                    actionMode.finish();
                                    return true;
                                default:
                                    break;
                            }
                            return false;
                        }
                    };

                    rootView = inflater.inflate(R.layout.fragment_notas, container, false);
                    lstMaterias = (ListView) rootView.findViewById(android.R.id.list);
                    activityDisciplina = getActivity();

                    mNavigationView.getMenu().getItem(1).setChecked(true);

                    final FloatingActionButton fab = (FloatingActionButton) appView.findViewById(R.id.fab);
                    //fab.setVisibility(View.VISIBLE);
                    fab.show();
                    // List<MateriaListModel> materias = MateriaListModel.listAll(MateriaListModel.class);

                    contextFragment = rootView.getContext();


                    fab.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick (View v) {
                            // disciplinaDialogFragment.show(getFragmentManager(), "Teste");
                            DialogActivity dialogActivity = new DialogActivity();

                            if (lstMaterias.getAdapter() == null)
                                nDisciplinas = 0;
                            else
                                nDisciplinas = lstMaterias.getAdapter().getCount();

                            Intent intent = new Intent(rootView.getContext(), DialogActivity.class);
                            Bundle args = new Bundle();
                            args.putString("DISCIPLINA_ID", "");
                            args.putString("EDITAR_DISCIPLINA", "0");
                            intent.putExtras(args);
                            startActivity(intent);
                        }
                    });

                    activityDisciplina.runOnUiThread(new Runnable() {
                        @Override
                        public void run () {
                            materiaListModels = MateriaListModel.listAll(MateriaListModel.class);
                        }
                    });
                    ArrayAdapter arrayAdapter = new ListAdapter(activityDisciplina, contextFragment, R.layout.materias_listrow, materiaListModels);
                    // View emptyView = rootView.findViewById(R.id.lblEmptyList);
                    // TextView emptyText = (TextView)rootView.findViewById(android.R.id.empty);
                    RelativeLayout emptyLayoutNotas = (RelativeLayout) rootView.findViewById(R.id.empty_layout_notas);
                    // emptyText.setText("Adicione matérias para começar a controlar suas notas.");
                    lstMaterias.setEmptyView(emptyLayoutNotas);
                    lstMaterias.setAdapter(arrayAdapter);
                    if (materiaListModels.size() == 0) {
                        lstMaterias.setAdapter(null);
                    }
                    lstMaterias.setClickable(true);
                    lstMaterias.setLongClickable(true);
                    lstMaterias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick (AdapterView<?> parent, View v, int position, long id) {
                            if (actionModeStatus == 1) {

                                if (!lstMaterias.isItemChecked(position)) {
                                    // v.setSelected(false);
                                    lstMaterias.setItemChecked(position, false);
                                    v.setBackgroundColor(0);
                                    String sizeSelect = String.valueOf(lstMaterias.getCheckedItemCount());
                                    if (lstMaterias.getCheckedItemCount() <= 1)
                                        mActionDeleteMode.setTitle(sizeSelect + " selecionado");
                                    else
                                        mActionDeleteMode.setTitle(sizeSelect + " selecionados");


                                } else {

                                    //v.setSelected(true);
                                    lstMaterias.setItemChecked(position, true);
                                    v.setBackgroundColor(Color.LTGRAY);
                                    String sizeSelect = String.valueOf(lstMaterias.getCheckedItemCount());
                                    if (lstMaterias.getCheckedItemCount() <= 1)
                                        mActionDeleteMode.setTitle(sizeSelect + " selecionado");
                                    else
                                        mActionDeleteMode.setTitle(sizeSelect + " selecionados");


                                }

                            } else if (actionModeStatus == 0) {
                                // start an instance of CrimePagerActivity
                                String ids = String.valueOf(id);
                                Intent i = new Intent(contextFragment, DialogActivity.class);
                                Bundle args = new Bundle();
                                args.putString("DISCIPLINA_ID", ids);
                                args.putString("EDITAR_DISCIPLINA", "1");


                                nDisciplinas = parent.getCount();
                                disciplinaPosition = position;

                                i.putExtras(args);
                                contextFragment.startActivity(i);
                            }
                        }
                    });

                    lstMaterias.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick (AdapterView<?> parent, View v, int position, long id) {

                            if (actionModeStatus == 1) {

                                if (lstMaterias.isItemChecked(position)) {

                                    // v.setSelected(false);
                                    lstMaterias.setItemChecked(position, false);
                                    v.setBackgroundColor(0);
                                    String sizeSelect = String.valueOf(lstMaterias.getCheckedItemCount());
                                    if (lstMaterias.getCheckedItemCount() <= 1)
                                        mActionDeleteMode.setTitle(sizeSelect + " selecionado");
                                    else
                                        mActionDeleteMode.setTitle(sizeSelect + " selecionados");


                                } else {

                                    //v.setSelected(true);
                                    lstMaterias.setItemChecked(position, true);
                                    v.setBackgroundColor(Color.LTGRAY);
                                    String sizeSelect = String.valueOf(lstMaterias.getCheckedItemCount());
                                    if (lstMaterias.getCheckedItemCount() <= 1)
                                        mActionDeleteMode.setTitle(sizeSelect + " selecionado");
                                    else
                                        mActionDeleteMode.setTitle(sizeSelect + " selecionados");


                                }
                            } else {
                                AppCompatActivity activity = (AppCompatActivity) activityDisciplina;
                                // fab.setVisibility(INVISIBLE);
                                fab.hide();
                                // activity.startSupportActionMode(mDeleteMode);
                                mActionDeleteMode = activity.startSupportActionMode(mDeleteMode);
                                actionModeStatus = 1;
                                lstMaterias.clearChoices();
                                for (int i = 0; i < lstMaterias.getCount(); i++)
                                    lstMaterias.setItemChecked(i, false);
                                //v.setSelected(true);
                                lstMaterias.setItemChecked(position, true);
                                v.setBackgroundColor(Color.LTGRAY);
                                String sizeSelect = String.valueOf(lstMaterias.getCheckedItemCount());
                                if (lstMaterias.getCheckedItemCount() <= 1)
                                    mActionDeleteMode.setTitle(sizeSelect + " selecionado");
                                else
                                    mActionDeleteMode.setTitle(sizeSelect + " selecionados");
                                //  lstMaterias.getAdapter().notifyDataSetChanged();

                            }

                            return true;
                        }
                    });

                    //  ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
                    //  itemTouchHelper.attachToRecyclerView(lstMaterias);
                    snackView = rootView;

                    break;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_calendario, container, false);
                    mNavigationView.getMenu().getItem(2).setChecked(true);
                    FloatingActionButton fab3 = (FloatingActionButton) appView.findViewById(R.id.fab);
                    //fab3.setVisibility(INVISIBLE);
                    fab3.hide();
                    snackView = rootView;
                    activityDisciplina = getActivity();
                    contextFragment = rootView.getContext();
                    mainActivity.invalidateOptionsMenu();


                    SharedPreferences settings = fragmentActivity.getPreferences(Context.MODE_PRIVATE);
                    credential = GoogleAccountCredential.usingOAuth2(
                            contextFragment.getApplicationContext(), Arrays.asList(SCOPES))
                            .setBackOff(new ExponentialBackOff())
                            .setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, ""));
                    mEmail = credential.getSelectedAccountName();



                    mService = new com.google.api.services.calendar.Calendar.Builder(
                            transport, jsonFactory, credential)
                            .setApplicationName("CAE UMC")
                            .build();


                    swipeRefreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.activity_main_swipe_refresh_layout);
                    swipeRefreshLayout.setColorSchemeResources(R.color.accent, R.color.primary);
                    swipeRefreshLayout.setEnabled(false);
                    swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                        @Override
                        public void onRefresh () {
                            swipeRefreshLayout.setEnabled(false);
                            AtualizarCalendario();
                        }
                    });
                    lstEventos = (ListView) snackView.findViewById(R.id.lstEventos);
                    TextView emptyTextAgenda = (TextView) rootView.findViewById(android.R.id.empty);
                    emptyTextAgenda.setText("Faça login para vizualizar sua agenda.");
                    RelativeLayout emptyAgendaLayout = (RelativeLayout) rootView.findViewById(R.id.emptyAgendaLayout);
                    lstEventos.setEmptyView(emptyAgendaLayout);
                    lstEventos.setClickable(true);
                    lstEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick (AdapterView<?> parent, View view, int position, long id) {
                            if (lstEventos.getAdapter().getItemViewType(position) == 0) {

                                idEvento = lstEventos.getAdapter().getItemId(position);
                                Intent intent = new Intent(contextFragment, AgendaDetails.class);
                                contextFragment.startActivity(intent);

                            }

                        }
                    });

                    activityDisciplina.runOnUiThread(new Runnable() {
                        @Override
                        public void run () {
                            eventosListModels = EventosListModel.findWithQuery(EventosListModel.class, "SELECT * FROM EVENTOS_LIST_MODEL ORDER BY data*1000 ASC");
                        }
                    });

                    Log.e("On Create Agenda", "Passou por aqui");
                   /* if (eventosListModels.size() <= 0 || eventosListModels == null) {
                        if (isGooglePlayServicesAvailable()) {
                            swipeRefreshLayout.setEnabled(false);
                            swipeRefreshLayout.setRefreshing(true);
                            refreshResults();
                        } else {
                            Toast.makeText(contextFragment,"Google Play Services é necessário: " + "após instalar, reinicie o aplicativo.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                            swipeRefreshLayout.setEnabled(false);
                            swipeRefreshLayout.setRefreshing(true);
                            updateResultsText(eventosListModels);
                    }*/

                    btnCarregarAgenda = (Button) rootView.findViewById(R.id.btnCarregarAgenda);
                    btnCarregarAgenda.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick (View v) {
                            btnCarregarAgenda.setEnabled(false);
                            if (isGooglePlayServicesAvailable()) {
                                swipeRefreshLayout.setEnabled(false);
                                swipeRefreshLayout.setRefreshing(true);
                                refreshResults();
                            } else {
                                Toast.makeText(contextFragment, "Google Play Services é necessário: " + "após instalar, reinicie o aplicativo.", Toast.LENGTH_LONG).show();
                                btnCarregarAgenda.setEnabled(true);
                            }
                        }
                    });

                    if (eventosListModels.size() > 0 && eventosListModels != null) {
                        if (isGooglePlayServicesAvailable()) {
                            swipeRefreshLayout.setEnabled(false);
                            swipeRefreshLayout.setRefreshing(true);
                            updateResultsText(eventosListModels);
                        } else {
                            Toast.makeText(contextFragment, "Google Play Services é necessário: " + "após instalar, reinicie o aplicativo.", Toast.LENGTH_LONG).show();
                            btnCarregarAgenda.setEnabled(true);
                        }
                    }

                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_arquivos, container, false);
                    mNavigationView.getMenu().getItem(3).setChecked(true);
                    mainActivity.invalidateOptionsMenu();
                    break;
                case 3:
                    mainActivity.invalidateOptionsMenu();
                    rootView = inflater.inflate(R.layout.fragment_home, container, false);
                    mNavigationView.getMenu().getItem(0).setChecked(true);
                    FloatingActionButton fab2 = (FloatingActionButton) appView.findViewById(R.id.fab);
                    fab2.hide();

                    if (attAvailable) {
                        LinearLayout layoutAtualizacao = (LinearLayout) rootView.findViewById(R.id.layout_att_disponivel);
                        Button btnAtualizarApp = (Button) rootView.findViewById(R.id.btnAtualizarApp);
                        btnAtualizarApp.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick (View v) {

                                BaixarAtualizacao();

                            }
                        });
                        layoutAtualizacao.setVisibility(View.VISIBLE);
                    }

                    CardView card_view_notas = (CardView) rootView.findViewById(R.id.card_view_notas);
                    CardView card_view_calendario = (CardView) rootView.findViewById(R.id.card_view_calendario);
                    CardView card_view_arquivos = (CardView) rootView.findViewById(R.id.card_view_arquivos);
                    CardView card_view_facebook = (CardView) rootView.findViewById(R.id.card_view_facebook);
                    CardView card_view_sitecae = (CardView) rootView.findViewById(R.id.card_view_sitecae);


                    card_view_notas.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick (View v) {

                            Fragment fragment = new PlanetFragment();
                            Bundle args = new Bundle();
                            args.putInt(PlanetFragment.ARG_PLANET_NUMBER, 0);
                            fragment.setArguments(args);
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.content_frame, fragment).addToBackStack("fragBack").commit();
                            //setTitle(titleName);

                        }
                    });

                    card_view_calendario.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick (View v) {

                            Fragment fragment = new PlanetFragment();
                            Bundle args = new Bundle();
                            args.putInt(PlanetFragment.ARG_PLANET_NUMBER, 1);
                            fragment.setArguments(args);
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.content_frame, fragment).addToBackStack("fragBack").commit();
                            //setTitle(titleName);

                        }
                    });

                    card_view_arquivos.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick (View v) {

                            Fragment fragment = new PlanetFragment();
                            Bundle args = new Bundle();
                            args.putInt(PlanetFragment.ARG_PLANET_NUMBER, 2);
                            fragment.setArguments(args);
                            FragmentManager fragmentManager = getFragmentManager();
                            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.content_frame, fragment).addToBackStack("fragBack").commit();
                            //setTitle(titleName);

                        }
                    });

                    card_view_facebook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick (View v) {

                            Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/CAE-Hip%C3%B3lito-Gustavo-Pujol-Junior-386902094768273/"));
                            startActivity(facebookIntent);

                        }
                    });

                    card_view_sitecae.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick (View v) {
                            Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://caeumc.wix.com/villa-lobos"));
                            startActivity(facebookIntent);


                        }
                    });


                    //    ImageButton btnFacebook = (ImageButton) rootView.findViewById(R.id.btnFacebook);
                    //TextView txtWelcome = (TextView) rootView.findViewById(R.id.textView);
                    // SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    //  txtWelcome.setText(sharedPreferences.getString("alterar_usuario",""));
                /*    btnFacebook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/CAE-UMC-Villa-Lobos-386902094768273/"));
                            startActivity(facebookIntent);
                        }
                    });*/
                    break;
                default:
                    rootView = inflater.inflate(R.layout.fragment_home, container, false);
                    break;

            }

            String planet = getResources().getStringArray(R.array.nav_options)[i];

            //int imageId = getResources().getIdentifier(planet.toLowerCase(Locale.getDefault()),
            //         "drawable", getActivity().getPackageName());
            // ((ImageView) rootView.findViewById(R.id.image)).setImageResource(imageId);
            getActivity().setTitle(planet);
            fragmentActivity = getActivity();
            return rootView;
        }


    }


}


