package com.caeumc.caeumc;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.app.Fragment;
import android.app.SearchManager;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;

import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;

import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
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
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.AccountPicker;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;

import java.util.ArrayList;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import static android.view.View.INVISIBLE;


public class NavDrawerActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    private static SwipeRefreshLayout swipeRefreshLayout;
    private static CalendarView calendarView;
    private DrawerLayout mDrawerLayout;
    public static int notifyUpdate = 0;
    public static int disciplinaPosition=-99;
    public static int nDisciplinas = 999999999;
    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    static int actionModeStatus=0;
    private NavigationView mNavigationView;
    private Toolbar toolbar;
   private static ListView lstMaterias;
    private static ListView lstEventos;
    private static ListAdapter adapter;
    private static String ALTERACAO2 = "0";
    static Context context;
    static Context contextFragment;
    static View snackView;
    static View appView;
    static Activity activityDisciplina ;
    static Activity mainActivity;
    static Activity fragmentActivity;
    static NavDrawerActivity drawerActivity;
    static List<String> teste = new ArrayList<String>();

    static List<Integer> selectedDisciplinas = new ArrayList<>();

    static CoordinatorLayout rootLayout;

    static List<MateriaListModel> disciplinasList = new ArrayList<>();
    static List<EventosListModel> eventosList = new ArrayList<>();
    static ActionMode.Callback mDeleteMode;
    static ActionMode mActionDeleteMode;
static RelativeLayout layoutLoading;
   private static TextView tt1 = null;
    private static TextView tt2= null;
    private static TextView tt3= null;
    private static TextView tt4= null;
    private static TextView tt5= null;
    private static TextView tt6= null;
    private static CheckBox chbDP = null;

    private static ProgressBar pgbLoading;

    private static TextView tv1 = null;
    private static TextView tv2= null;
    private static TextView tv3= null;
    private static TextView tv4= null;
    private static TextView tv5= null;
    private static CheckBox chbDP2 = null;
    static String[] n = null;
    private static MateriaListModel mDisciplinas;
    private static EventosListModel mEventos;
static List<Long> eventosID = new ArrayList<>();
    static com.google.api.services.calendar.Calendar mService;
    static GoogleApiClient mGoogleApiClient;
    static GoogleAccountCredential credential;
    static GoogleAccountCredential accountCredential;
    static final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    static final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String TAG = "drive-quickstart";
    private static final String[] SCOPES = { CalendarScopes.CALENDAR_READONLY };
    static String mEmail;
    static int fragmentatual = -1;
    static boolean mudarusuario = false;
    static String emailantigo;

    public static long idEvento;


    private static void popularListView() {


    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navdrawerlayout);

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


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
               // R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close
        ) {
            public void onDrawerClose(View view) {
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
            public boolean onNavigationItemSelected(MenuItem menuItem) {
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (fragmentatual == 1) {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.calendario_menu, menu);

        }

        return super.onCreateOptionsMenu(menu);
    }

  //* Called whenever we call invalidateOptionsMenu() *//*
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        // If the nav drawer is open, hide action items related to the content view
      //  boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mNavigationView);
//       menu.findItem(R.id.action_websearch).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }



    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(mNavigationView) == true) {
            mDrawerLayout.closeDrawers();
        }

        else if (getFragmentManager().getBackStackEntryCount() > 1) {
            getFragmentManager().popBackStack();

        }
        else {
            super.onBackPressed();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // The action bar home/up action should open or close the drawer.
        // ActionBarDrawerToggle will take care of this.
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        // Handle action buttons
        switch(item.getItemId()) {
            case R.id.alterar_conta:
                mudarusuario = true;
                emailantigo = mEmail;
                getUserAccountWrapper();
                return true;
            case R.id.atualizar:
                swipeRefreshLayout.setEnabled(false);
                swipeRefreshLayout.setRefreshing(true);
                AtualizarCalendario();
                return true;
            case R.id.opcoes_conta:
                Intent intent = new Intent(this, SettingsAgendaActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void selectItem(int position, String titleName) {
        // update the main content by replacing fragments
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.content_frame, fragment).addToBackStack("fragBack").commit();

        // update selected item and title, then close the drawer
      //  mDrawerList.setItemChecked(position, true);
        setTitle(titleName);
       // mDrawerLayout.closeDrawer(mDrawerList);
        mDrawerLayout.closeDrawer(mNavigationView);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }



    /**
     * Fragment that appears in the "content_frame", shows a planet
     */
    public static class PlanetFragment extends Fragment {
        public static final String ARG_PLANET_NUMBER = "planet_number";


       // private MultiSelector mMultiSelector = new SingleSelector();

        public PlanetFragment() {
            // Empty constructor required for fragment subclasses
        }



        @Override
        public void onPause() {
            super.onPause();
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            int n = Integer.parseInt(ALTERACAO2);

            if (i == 2) {

                if (mGoogleApiClient != null) {
                    mGoogleApiClient.disconnect();
                }
            }

        }





        @Override
        public void onResume() {
            super.onResume();

            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            int n = Integer.parseInt(ALTERACAO2);

            if (i == 3) {
                TextView txtWelcome = (TextView) appView.findViewById(R.id.textView);
                SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                txtWelcome.setText(sharedPreferences.getString("alterar_usuario",""));
            }


            if (i == 0) {
                rootLayout = (CoordinatorLayout) appView.findViewById(R.id.content);


                if (notifyUpdate == 1) {
                    teste.clear();
                    List<MateriaListModel> materiaListModels = MateriaListModel.listAll(MateriaListModel.class);
                    ArrayAdapter arrayAdapter = new ListAdapter(activityDisciplina, contextFragment, R.layout.materias_listrow, materiaListModels);
                    lstMaterias.setAdapter(arrayAdapter);

                    notifyUpdate = 0;
                    Snackbar.make(rootLayout, "Disciplina Alterada", Snackbar.LENGTH_SHORT).show();
                } else if (notifyUpdate == 2) {
                    List<MateriaListModel> materiaListModels = MateriaListModel.listAll(MateriaListModel.class);
                    ArrayAdapter arrayAdapter = new ListAdapter(activityDisciplina, contextFragment, R.layout.materias_listrow, materiaListModels);
                    lstMaterias.setAdapter(arrayAdapter);
                    notifyUpdate = 0;
                    Snackbar.make(rootLayout, "Disciplina Adicionada", Snackbar.LENGTH_SHORT).show();
                }


               // int nDisciplinas2 = lstMaterias.getAdapter().getCount();
                ALTERACAO2 = "0";


            }

            if (i == 1) {
                List<EventosListModel> eventosListModels = EventosListModel.findWithQuery(EventosListModel.class, "SELECT * FROM EVENTOS_LIST_MODEL ORDER BY data*1000 ASC");
                if (eventosListModels.size() <= 0 || eventosListModels == null) {
                    if (isGooglePlayServicesAvailable()) {
                        refreshResults();
                    } else {
                        Toast.makeText(contextFragment,"Google Play Services required: " +
                                "after installing, close and relaunch this app.", Toast.LENGTH_LONG).show();
                    }
                } else {
                    if (mudarusuario == true) {
                        if (emailantigo == mEmail) {
                            swipeRefreshLayout.setEnabled(false);
                            swipeRefreshLayout.setRefreshing(true);
                            updateResultsText(eventosListModels);
                        } else {
                            if (isGooglePlayServicesAvailable()) {
                                swipeRefreshLayout.setEnabled(false);
                                swipeRefreshLayout.setRefreshing(true);
                                refreshResults();

                            } else {
                                Toast.makeText(contextFragment, "Google Play Services required: " +
                                        "after installing, close and relaunch this app.", Toast.LENGTH_LONG).show();
                            }
                        }
                        mudarusuario = false;
                    } else {
                        swipeRefreshLayout.setEnabled(false);
                        swipeRefreshLayout.setRefreshing(true);
                        updateResultsText(eventosListModels);
                    }
                }


            }

            if (i == 2) {
                if (mGoogleApiClient == null) {
                    // Create the API client and bind it to an instance variable.
                    // We use this instance as the callback for connection and connection
                    // failures.
                    // Since no account name is passed, the user is prompted to choose.
                    mGoogleApiClient = new GoogleApiClient.Builder(context)
                            .addApi(Drive.API)
                            .addScope(Drive.SCOPE_FILE)
                            .addConnectionCallbacks(drawerActivity)
                            .addOnConnectionFailedListener(drawerActivity)
                            .build();
                }
                // Connect the client. Once connected, the camera is launched.
                mGoogleApiClient.connect();
            }
        }
        private int statusBarColor;
        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            fragmentatual = i;
            final View rootView;
            switch (i)
            {
                case 0:
                    mainActivity.invalidateOptionsMenu();

                    //mDeleteMode = new ModalMultiSelectorCallback(mMultiSelector) {
                    mDeleteMode = new ActionMode.Callback() {

                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
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
                        public void onDestroyActionMode(ActionMode mode) {
                            actionModeStatus = 0;
                            lstMaterias.clearChoices();
                            for (int i = 0; i < lstMaterias.getCount(); i++)
                                lstMaterias.setItemChecked(i, false);
                            List<MateriaListModel> materiaListModels = MateriaListModel.listAll(MateriaListModel.class);

                            if (materiaListModels.size() == 0)
                            {
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
                        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

                            mode.setTitle("Excluir");
                            return false; // Return false if nothing is done
                        }




                        @Override
                        public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                            switch (menuItem.getItemId()) {
                                case R.id.menu_item_delete_disciplina:
                                    // Delete crimes from model
                                    actionModeStatus = 0;


                                    for (int i = lstMaterias.getAdapter().getCount()-1; i >= 0; i--) {
                                        if (lstMaterias.isItemChecked(i)) {
                                            long idDeletar = lstMaterias.getAdapter().getItemId(i);
                                            MateriaListModel materiaListModel = MateriaListModel.findById(MateriaListModel.class, idDeletar);
                                            materiaListModel.delete();

                                        }
                                    }
                                    lstMaterias.clearChoices();
                                    for (int i = 0; i < lstMaterias.getCount(); i++)
                                        lstMaterias.setItemChecked(i, false);
                                    List<MateriaListModel> materiaListModels = MateriaListModel.listAll(MateriaListModel.class);

                                    if (materiaListModels.size() == 0)
                                    {
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



                    final FloatingActionButton fab = (FloatingActionButton) appView.findViewById(R.id.fab);
                    //fab.setVisibility(View.VISIBLE);
                    fab.show();
                   // List<MateriaListModel> materias = MateriaListModel.listAll(MateriaListModel.class);

                    contextFragment = rootView.getContext();


                            fab.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
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

                    List<MateriaListModel> materiaListModels = MateriaListModel.listAll(MateriaListModel.class);
                    ArrayAdapter arrayAdapter = new ListAdapter(activityDisciplina, contextFragment, R.layout.materias_listrow, materiaListModels);
                   // View emptyView = rootView.findViewById(R.id.lblEmptyList);
                    TextView emptyText = (TextView)rootView.findViewById(android.R.id.empty);
                    lstMaterias.setEmptyView(emptyText);
                    lstMaterias.setAdapter(arrayAdapter);
                    if (materiaListModels.size() == 0) {
                        lstMaterias.setAdapter(null);
                    }
                    lstMaterias.setClickable(true);
                    lstMaterias.setLongClickable(true);
                    lstMaterias.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                            if (actionModeStatus == 1) {

                                if (!lstMaterias.isItemChecked(position)) {
                                   // v.setSelected(false);
                                    lstMaterias.setItemChecked(position, false);
                                    v.setBackgroundColor(0);
                                    String sizeSelect = String.valueOf(lstMaterias.getCheckedItemCount());
                                    if (lstMaterias.getCheckedItemCount() <=1)
                                    mActionDeleteMode.setTitle(sizeSelect + " selecionado");
                                    else
                                        mActionDeleteMode.setTitle(sizeSelect + " selecionados");




                                } else {

                                    //v.setSelected(true);
                                    lstMaterias.setItemChecked(position, true);
                                    v.setBackgroundColor(Color.LTGRAY);
                                    String sizeSelect = String.valueOf(lstMaterias.getCheckedItemCount());
                                    if (lstMaterias.getCheckedItemCount() <=1)
                                        mActionDeleteMode.setTitle(sizeSelect + " selecionado");
                                    else
                                        mActionDeleteMode.setTitle(sizeSelect + " selecionados");


                                }

                            }
                            else if (actionModeStatus == 0) {
                                // start an instance of CrimePagerActivity
                                String ids = String.valueOf(id);
                                Intent i = new Intent (contextFragment, DialogActivity.class);
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
                        public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {

                            if (actionModeStatus == 1) {

                                if (lstMaterias.isItemChecked(position)) {

                                   // v.setSelected(false);
                                    lstMaterias.setItemChecked(position, false);
                                    v.setBackgroundColor(0);
                                    String sizeSelect = String.valueOf(lstMaterias.getCheckedItemCount());
                                    if (lstMaterias.getCheckedItemCount() <=1)
                                        mActionDeleteMode.setTitle(sizeSelect + " selecionado");
                                    else
                                        mActionDeleteMode.setTitle(sizeSelect + " selecionados");



                                } else {

                                   //v.setSelected(true);
                                    lstMaterias.setItemChecked(position, true);
                                    v.setBackgroundColor(Color.LTGRAY);
                                    String sizeSelect = String.valueOf(lstMaterias.getCheckedItemCount());
                                    if (lstMaterias.getCheckedItemCount() <=1)
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
                                if (lstMaterias.getCheckedItemCount() <=1)
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
                    pgbLoading = (ProgressBar) rootView.findViewById(R.id.pgbLoading);
                    layoutLoading = (RelativeLayout) rootView.findViewById(R.id.layoutLoading);
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
                            .setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));
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
                        public void onRefresh() {
                            swipeRefreshLayout.setEnabled(false);
                            AtualizarCalendario();
                        }
                    });
                    lstEventos = (ListView) snackView.findViewById(R.id.lstEventos);
                    lstEventos.setClickable(true);
                    lstEventos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            if (lstEventos.getAdapter().getItemViewType(position) == 0) {

                                idEvento = lstEventos.getAdapter().getItemId(position);

                            }

                        }
                    });
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_arquivos, container, false);
                    mainActivity.invalidateOptionsMenu();
                    break;
                case 3:
                    mainActivity.invalidateOptionsMenu();
                    rootView = inflater.inflate(R.layout.fragment_home, container, false);
                    FloatingActionButton fab2 = (FloatingActionButton) appView.findViewById(R.id.fab);
                  //  fab2.setVisibility(INVISIBLE);
                    fab2.hide();
                    ImageButton btnFacebook = (ImageButton) rootView.findViewById(R.id.btnFacebook);
                    TextView txtWelcome = (TextView) rootView.findViewById(R.id.textView);
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
                    txtWelcome.setText(sharedPreferences.getString("alterar_usuario",""));
                    btnFacebook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/CAE-UMC-Villa-Lobos-386902094768273/"));
                            startActivity(facebookIntent);
                        }
                    });
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

    public String getSelectedTitle() {
        String Title = getString(R.string.selected_count, selectedDisciplinas.size());
        return Title;
    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // Called whenever the API client fails to connect.
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }
        // The failure has a resolution. Resolve it.
        // Called typically when the app is not yet authorized, and an
        // authorization
        // dialog is displayed to the user.
        try {
            result.startResolutionForResult(this, 3);
        } catch (IntentSender.SendIntentException e) {
            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }


    @Override
    public void onConnected(Bundle connectionHint) {
        Log.i(TAG, "API client connected.");
    }

    @Override
    public void onConnectionSuspended(int cause) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    @Override
    public void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK /*&& data != null &&
                            data.getExtras() != null*/) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    mEmail = accountName;
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);
                        SharedPreferences settings =
                                fragmentActivity.getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(contextFragment,"Account unspecified.",Toast.LENGTH_LONG).show();
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode != RESULT_OK) {
                    getUserAccountWrapper();
                }
                break;
        }

        //super.onActivityResult(requestCode, resultCode, data);
    }

    static int MY_PERMISSIONS_REQUEST_GET_ACCOUNTS = 1;

    private static void chooseAccount() {
           /* NavDrawerActivity navDrawerActivity = new NavDrawerActivity();
            fragmentActivity.startActivityForResult(
                    credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);*/

        String[] accountTypes = new String[]{"com.google"};
        drawerActivity.startActivityForResult(credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
    }

    private static void refreshResults() {
       if (credential.getSelectedAccountName() == null) {
            getUserAccountWrapper();
        } else {
            if (isDeviceOnline()) {
                String SCOPE = CalendarScopes.CALENDAR_READONLY;
                new ApiAsyncTask(drawerActivity, mEmail, SCOPE).execute();
            } else {
                Toast.makeText(contextFragment,"No network connection available.", Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(true);
            }
        }
    }

    public static void clearResultsText() {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                EventosListModel.deleteAll(EventosListModel.class);

            }
        });
    }

    public static void updateResultsText(final List<EventosListModel> dataStrings) {
        fragmentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dataStrings == null) {
                    Toast.makeText(contextFragment, "Error retrieving data!", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(true);
                } else if (dataStrings.size() == 0) {
                    Toast.makeText(contextFragment, "No data found.", Toast.LENGTH_LONG).show();
                    swipeRefreshLayout.setRefreshing(false);
                    swipeRefreshLayout.setEnabled(true);
                } else {

                    lstEventos = (ListView) snackView.findViewById(R.id.lstEventos);
                    // eventosList = EventosListModel.findWithQuery(EventosListModel.class, "SELECT * FROM EVENTOS_LIST_MODEL ORDER BY datetime(data*1000, 'unixepoch', 'localtime') DESC");
                    EventosListAdapter arrayAdapter = new EventosListAdapter(activityDisciplina, contextFragment);
                    String mesAnterior = "";
                    String semanaAnterior = "";
                    int diaAnterior =0;
                    EventosListModel model = new EventosListModel();
                    EventosListModel diaModel = new EventosListModel();

                    for (int i=0; i < dataStrings.size(); i++) {
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
                    lstEventos.setVisibility(View.VISIBLE);
                    pgbLoading.setVisibility(View.GONE);
                    layoutLoading.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(contextFragment, "Data retrieved using" +
                            " the Google Calendar API:", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    static void AtualizarCalendario() {

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

        return  mesEvento;
    }

    private static int getDiaEvento (long data) {
        Date dataEvento = new Date(data * 1000L);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dataEvento);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return  day;
    }

    private static String getSemana (int dia) {
        String semana="";
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

    public static void updateStatus(final String message) {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(contextFragment, message, Toast.LENGTH_LONG).show();
                swipeRefreshLayout.setRefreshing(false);
                swipeRefreshLayout.setEnabled(true);
            }
        });
    }



    private static boolean isDeviceOnline() {
        NavDrawerActivity navDrawerActivity = new NavDrawerActivity();
        ConnectivityManager connMgr =
                (ConnectivityManager) contextFragment.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    private static boolean isGooglePlayServicesAvailable() {
        //NavDrawerActivity navDrawerActivity = new NavDrawerActivity();
        final int connectionStatusCode =
                GooglePlayServicesUtil.isGooglePlayServicesAvailable(contextFragment);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
            return false;
        } else if (connectionStatusCode != ConnectionResult.SUCCESS ) {
            return false;
        }
        return true;
    }

    static void showGooglePlayServicesAvailabilityErrorDialog(

            final int connectionStatusCode) {
        final NavDrawerActivity navDrawerActivity = new NavDrawerActivity();
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(
                        connectionStatusCode,
                        mainActivity,
                        REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }

    final static private int REQUEST_CODE_ASK_PERMISSIONS = 123;

    private static void getUserAccountWrapper() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(contextFragment, Manifest.permission.GET_ACCOUNTS);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(fragmentActivity,Manifest.permission.GET_ACCOUNTS)) {
                showMessageOKCancel("Você precisa aceitar para ver esse calendário",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(fragmentActivity, new String[] {Manifest.permission.GET_ACCOUNTS},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
           ActivityCompat.requestPermissions(fragmentActivity, new String[] {Manifest.permission.WRITE_CONTACTS},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        chooseAccount();
    }

    private static void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(fragmentActivity)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    getUserAccountWrapper();
                } else {
                    // Permission Denied
                    Toast.makeText(NavDrawerActivity.this, "WRITE_CONTACTS Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}


