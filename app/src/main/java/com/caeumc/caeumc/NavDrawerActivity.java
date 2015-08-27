package com.caeumc.caeumc;

import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.Fragment;
import android.app.SearchManager;
import android.app.usage.UsageEvents;
import android.content.Context;

import android.content.Intent;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Canvas;
import android.graphics.Color;

import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.android.multiselector.ModalMultiSelectorCallback;
import com.bignerdranch.android.multiselector.MultiSelector;
import com.bignerdranch.android.multiselector.MultiSelectorBindingHolder;
import com.bignerdranch.android.multiselector.SingleSelector;
import com.bignerdranch.android.multiselector.SwappingHolder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.calendar.CalendarScopes;

import java.nio.charset.CoderResult;
import java.util.ArrayList;

import java.util.Arrays;
import java.util.List;


public class NavDrawerActivity extends AppCompatActivity{

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
   private static RecyclerView lstMaterias;
    private static RecyclerView lstEventos;
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
    private static MultiSelector mMultiSelector = new MultiSelector();

    static List<Integer> selectedDisciplinas = new ArrayList<>();

    static CoordinatorLayout rootLayout;

    static List<MateriaListModel> disciplinasList = new ArrayList<>();
    static ActionMode.Callback mDeleteMode;
    static ActionMode mActionDeleteMode;

   private static TextView tt1 = null;
    private static TextView tt2= null;
    private static TextView tt3= null;
    private static TextView tt4= null;
    private static TextView tt5= null;
    private static TextView tt6= null;
    private static CheckBox chbDP = null;

    private static TextView tv1 = null;
    private static TextView tv2= null;
    private static TextView tv3= null;
    private static TextView tv4= null;
    private static TextView tv5= null;
    private static CheckBox chbDP2 = null;
    static String[] n = null;
    private static MateriaListModel mDisciplinas;

    static com.google.api.services.calendar.Calendar mService;
    static GoogleAccountCredential credential;
    static final HttpTransport transport = AndroidHttp.newCompatibleTransport();
    static final JsonFactory jsonFactory = GsonFactory.getDefaultInstance();
    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { CalendarScopes.CALENDAR_READONLY };


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
        MateriaListModel.deleteAll(MateriaListModel.class);

        MateriaListModel materia = new MateriaListModel("Resistência dos Materiais III", -1.0, -1.0, -1.0, -1.0, -1.0, false);
        materia.save();

        MateriaListModel materia1 = new MateriaListModel("Estruturas de Concreto I", -1.0, -1.0, -1.0, -1.0, -1.0, false);
        materia1.save();

        MateriaListModel materia2 = new MateriaListModel("Teoria das Estruturas II", -1.0, -1.0, -1.0, -1.0, -1.0, false);
        materia2.save();

        MateriaListModel materia3 = new MateriaListModel("Hidrologia", -1.0, -1.0, -1.0, -1.0, -1.0, false);
        materia3.save();

        MateriaListModel materia4 = new MateriaListModel("Patologia", -1.0, -1.0, -1.0, -1.0, -1.0, false);
        materia4.save();

        MateriaListModel materia5 = new MateriaListModel("Tecnologia do Concreto", -1.0, -1.0, -1.0, -1.0, -1.0, false);
        materia5.save();

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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }*/

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

        else if (getSupportFragmentManager().findFragmentByTag("fragBack") != null) {

        }
        else {
            super.onBackPressed();
            return;
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
            case R.id.action_websearch:
                // create intent to perform web search for this planet
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, getActionBar().getTitle());
                // catch event that there's no activity to handle intent
                if (intent.resolveActivity(getPackageManager()) != null) {
                    startActivity(intent);
                } else {
                    Toast.makeText(this, R.string.app_not_available, Toast.LENGTH_LONG).show();
                }
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
        public void onResume() {
            super.onResume();

            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            int n = Integer.parseInt(ALTERACAO2);


            if (i == 0) {
                rootLayout = (CoordinatorLayout) appView.findViewById(R.id.content);


                if (notifyUpdate == 1) {
                    teste.clear();
                    lstMaterias.setAdapter(new DisciplinaAdapter());

                    notifyUpdate = 0;
                    Snackbar.make(rootLayout, "Disciplina Alterada", Snackbar.LENGTH_SHORT).show();
                }


                int nDisciplinas2 = lstMaterias.getAdapter().getItemCount();
                ALTERACAO2 = "0";

                if (nDisciplinas2 > nDisciplinas) {

                    nDisciplinas = nDisciplinas2;
                    Snackbar.make(rootLayout, "Disciplina Adicionada", Snackbar.LENGTH_SHORT).show();


                }

            }

            if (i == 1) {
                if (isGooglePlayServicesAvailable()) {
                    refreshResults();
                } else {
                    Toast.makeText(contextFragment,"Google Play Services required: " +
                            "after installing, close and relaunch this app.", Toast.LENGTH_LONG).show();
                }
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                                 Bundle savedInstanceState) {
            int i = getArguments().getInt(ARG_PLANET_NUMBER);
            final View rootView;
            switch (i)
            {
                case 0:


                    //mDeleteMode = new ModalMultiSelectorCallback(mMultiSelector) {
                    mDeleteMode = new ModalMultiSelectorCallback(mMultiSelector) {

                        @Override
                        public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {

                            getActivity().getMenuInflater().inflate(R.menu.delete_disciplina_menu, menu);
                            return true;
                        }

                        @Override
                        public void onDestroyActionMode(ActionMode mode) {
                            actionModeStatus = 0;
                            mMultiSelector.clearSelections();
                            selectedDisciplinas.clear();
                            lstMaterias.setAdapter(new DisciplinaAdapter());

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


                                    for (int i = lstMaterias.getAdapter().getItemCount()-1; i >= 0; i--) {
                                        if (mMultiSelector.isSelected(i, 0)) {
                                            MateriaListModel materiaListModel = MateriaListModel.findById(MateriaListModel.class, Long.valueOf(teste.get(i)));
                                            teste.remove(i);
                                            materiaListModel.delete();


                                        }
                                    }

                                    teste.clear();
                                    lstMaterias.setAdapter(new DisciplinaAdapter());
                                    mMultiSelector.clearSelections();
                                    selectedDisciplinas.clear();
                                    actionMode.finish();
                                    return true;
                                default:
                                    break;
                            }
                            return false;
                        }
                    };

                    rootView = inflater.inflate(R.layout.fragment_notas, container, false);
                    lstMaterias = (RecyclerView) rootView.findViewById(R.id.lstMaterias);
                    LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
                    layoutManager.supportsPredictiveItemAnimations();
                    lstMaterias.setLayoutManager(layoutManager);
                    lstMaterias.setItemAnimator(new DefaultItemAnimator());
                    lstMaterias.setHasFixedSize(true);
                    lstMaterias.addItemDecoration(new DividerItemDecoration(getActivity(), null));


                    activityDisciplina = getActivity();



                    FloatingActionButton fab = (FloatingActionButton) appView.findViewById(R.id.fab);
                    fab.setVisibility(View.VISIBLE);
                    List<MateriaListModel> materias = MateriaListModel.listAll(MateriaListModel.class);

                    contextFragment = rootView.getContext();


                            fab.setOnClickListener(new View.OnClickListener() {
                        @Override public void onClick(View v) {
                           // disciplinaDialogFragment.show(getFragmentManager(), "Teste");
                            DialogActivity dialogActivity = new DialogActivity();


                            nDisciplinas = lstMaterias.getAdapter().getItemCount();

                            Intent intent = new Intent(rootView.getContext(), DialogActivity.class);
                            Bundle args = new Bundle();
                            args.putString("DISCIPLINA_ID", "");
                            args.putString("EDITAR_DISCIPLINA", "0");
                            intent.putExtras(args);
                            startActivity(intent);
                        }
                    });


                    adapter = new ListAdapter(rootView.getContext(), R.layout.materias_listrow, materias);

                    DisciplinaAdapter disciplinaAdapter = new DisciplinaAdapter();
                    disciplinaAdapter.setHasStableIds(true);
                    lstMaterias.setAdapter(disciplinaAdapter);

                  //  ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
                  //  itemTouchHelper.attachToRecyclerView(lstMaterias);
                    snackView = rootView;

                    break;
                case 1:
                    rootView = inflater.inflate(R.layout.fragment_calendario, container, false);
                    lstEventos = (RecyclerView) rootView.findViewById(R.id.lstEventos);

                    contextFragment = rootView.getContext();
                    SharedPreferences settings = getActivity().getPreferences(Context.MODE_PRIVATE);
                    credential = GoogleAccountCredential.usingOAuth2(
                            contextFragment.getApplicationContext(), Arrays.asList(SCOPES))
                            .setBackOff(new ExponentialBackOff())
                            .setSelectedAccountName(settings.getString(PREF_ACCOUNT_NAME, null));

                    mService = new com.google.api.services.calendar.Calendar.Builder(
                            transport, jsonFactory, credential)
                            .setApplicationName("CAE UMC")
                            .build();

                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.fragment_arquivos, container, false);
                    break;
                case 3:
                    rootView = inflater.inflate(R.layout.fragment_home, container, false);
                    FloatingActionButton fab2 = (FloatingActionButton) appView.findViewById(R.id.fab);
                    fab2.setVisibility(View.INVISIBLE);
                    ImageButton btnFacebook = (ImageButton) rootView.findViewById(R.id.btnFacebook);
                    btnFacebook.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/"));
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

    private static class DisciplinasHolder extends SwappingHolder implements View.OnClickListener, View.OnLongClickListener {

        private MateriaListModel mDisciplinas;
        LinearLayout swipeableContent;


        public DisciplinasHolder(View itemView) {
            super(itemView, mMultiSelector);

            tt1 = (TextView) itemView.findViewById(R.id.lblExame);
            tt2 = (TextView) itemView.findViewById(R.id.lblM1);
            tt3 = (TextView) itemView.findViewById(R.id.lblM2);
            tt4 = (TextView) itemView.findViewById(R.id.lblMateria);
            tt5 = (TextView) itemView.findViewById(R.id.lblNotaFinal);
            tt6 = (TextView) itemView.findViewById(R.id.lblPI);
            tv1 = (TextView) itemView.findViewById(R.id.textView3);
            tv2 = (TextView) itemView.findViewById(R.id.textView5);
            tv3 = (TextView) itemView.findViewById(R.id.textView7);
            tv4 = (TextView) itemView.findViewById(R.id.textView9);
            tv5 = (TextView) itemView.findViewById(R.id.textView11);


            itemView.setOnClickListener(this);
            itemView.setClickable(true);
            itemView.setLongClickable(true);
            itemView.setOnLongClickListener(this);

            ColorDrawable selectionColor = new ColorDrawable();
            selectionColor.setColor(Color.LTGRAY);
            setSelectionModeBackgroundDrawable(selectionColor);

        }

        @Override
        public void onClick(View v) {
            if (mDisciplinas == null) {
                return;
            } else if (actionModeStatus == 1) {

                if (mMultiSelector.isSelected(lstMaterias.getChildAdapterPosition(v), 0)) {

                    v.setBackgroundColor(0);
                    mMultiSelector.setSelected(this, false);
                    selectedDisciplinas = mMultiSelector.getSelectedPositions();
                    String sizeSelect = String.valueOf(selectedDisciplinas.size());
                    mActionDeleteMode.setTitle(sizeSelect + " selecionado");
                  //  lstMaterias.getAdapter().notifyDataSetChanged();




                } else {
                    v.setBackgroundColor(Color.LTGRAY);
                    mMultiSelector.setSelected(this, true);
                    selectedDisciplinas = mMultiSelector.getSelectedPositions();
                    String sizeSelect = String.valueOf(selectedDisciplinas.size());
                    mActionDeleteMode.setTitle(sizeSelect + " selecionado");
                    //lstMaterias.getAdapter().notifyDataSetChanged();




                }



            }
            else if (!mMultiSelector.tapSelection(this) && actionModeStatus == 0) {
                // start an instance of CrimePagerActivity
                String id = teste.get(lstMaterias.getChildAdapterPosition(v));
               Intent i = new Intent (contextFragment, DialogActivity.class);
                Bundle args = new Bundle();
                args.putString("DISCIPLINA_ID", id);
                args.putString("EDITAR_DISCIPLINA", "1");


                nDisciplinas = lstMaterias.getAdapter().getItemCount();
                disciplinaPosition = lstMaterias.getChildAdapterPosition(v);

                i.putExtras(args);
                contextFragment.startActivity(i);
            }
        }

        @Override
        public boolean onLongClick(View v) {
            AppCompatActivity activity = (AppCompatActivity)activityDisciplina;
           // activity.startSupportActionMode(mDeleteMode);
            mActionDeleteMode = activity.startSupportActionMode(mDeleteMode);
            actionModeStatus = 1;
            v.setBackgroundColor(Color.LTGRAY);
            mMultiSelector.setSelected(this, true);
            selectedDisciplinas = mMultiSelector.getSelectedPositions();
            String sizeSelect = String.valueOf(selectedDisciplinas.size());
            mActionDeleteMode.setTitle(sizeSelect + " selecionado");
          //  lstMaterias.getAdapter().notifyDataSetChanged();


         ;

            return true;
        }



        public void bindCrime(MateriaListModel materialistmodel) {
            mDisciplinas = materialistmodel;

            teste.add(materialistmodel.getId().toString());




            if (tt1 != null) {


                if (materialistmodel.getEX() == -1.0)
                    tt1.setText("-");
                else
                    tt1.setText(materialistmodel.getEX().toString());

                if (materialistmodel.getEX() >= 9) {
                    tt1.setTextColor(Color.parseColor("#0D47A1"));
                } else if (materialistmodel.getEX() >= 7) {
                    tt1.setTextColor(Color.parseColor("#1976D2"));
                } else if (materialistmodel.getEX() >= 5) {
                    tt1.setTextColor(Color.parseColor("#2196F3"));
                } else if (materialistmodel.getEX() >= 3) {
                    tt1.setTextColor(Color.parseColor("#F44336"));
                } else if (materialistmodel.getEX() >= 0) {
                    tt1.setTextColor(Color.parseColor("#D32F2F"));
                } else  {
                    tt1.setTextColor(Color.LTGRAY);
                }
            }

            if (tt2 != null) {
                if (materialistmodel.getM1() == -1.0)
                    tt2.setText("-");
                else
                    tt2.setText(materialistmodel.getM1().toString());

                if (materialistmodel.getM1() >= 9) {
                    tt2.setTextColor(Color.parseColor("#0D47A1"));
                } else if (materialistmodel.getM1() >= 7) {
                    tt2.setTextColor(Color.parseColor("#1976D2"));
                } else if (materialistmodel.getM1() >= 5) {
                    tt2.setTextColor(Color.parseColor("#2196F3"));
                } else if (materialistmodel.getM1() >= 3) {
                    tt2.setTextColor(Color.parseColor("#F44336"));
                } else if (materialistmodel.getM1() >= 0) {
                    tt2.setTextColor(Color.parseColor("#D32F2F"));
                } else  {
                    tt1.setTextColor(Color.LTGRAY);
                }
            }

            if (tt3 != null) {
                if (materialistmodel.getM2() == -1.0)
                    tt3.setText("-");
                else
                    tt3.setText(materialistmodel.getM2().toString());

                if (materialistmodel.getM2() >= 9) {
                    tt3.setTextColor(Color.parseColor("#0D47A1"));
                } else if (materialistmodel.getM2() >= 7) {
                    tt3.setTextColor(Color.parseColor("#1976D2"));
                } else if (materialistmodel.getM2() >= 5) {
                    tt3.setTextColor(Color.parseColor("#2196F3"));
                } else if (materialistmodel.getM2() >= 3) {
                    tt3.setTextColor(Color.parseColor("#F44336"));
                } else if (materialistmodel.getM2() >= 0) {
                    tt3.setTextColor(Color.parseColor("#D32F2F"));
                } else  {
                    tt1.setTextColor(Color.LTGRAY);
                }
            }

            if (tt4 != null) {

                if (materialistmodel.getDP() == true) {
                    tt4.setText("DP - "+materialistmodel.getNomeMateria());

                } else {
                    tt4.setText(materialistmodel.getNomeMateria());
                }



            }

            if (tt5 != null) {
                if (materialistmodel.getNF() == -1.0)
                    tt5.setText("-");
                else
                    tt5.setText(materialistmodel.getNF().toString());

                if (materialistmodel.getNF() >= 9) {
                    tt5.setTextColor(Color.parseColor("#0D47A1"));
                } else if (materialistmodel.getNF() >= 7) {
                    tt5.setTextColor(Color.parseColor("#1976D2"));
                } else if (materialistmodel.getNF() >= 5) {
                    tt5.setTextColor(Color.parseColor("#2196F3"));
                } else if (materialistmodel.getNF() >= 3) {
                    tt5.setTextColor(Color.parseColor("#F44336"));
                } else if (materialistmodel.getNF() >= 0) {
                    tt5.setTextColor(Color.parseColor("#D32F2F"));
                } else  {
                    tt1.setTextColor(Color.LTGRAY);
                }
            }

            if (tt6 != null) {
                if (materialistmodel.getPI() == -1.0)
                    tt6.setText("-");
                else
                    tt6.setText(materialistmodel.getPI().toString());

                if (materialistmodel.getPI() >= 9) {
                    tt6.setTextColor(Color.parseColor("#0D47A1"));
                } else if (materialistmodel.getPI() >= 7) {
                    tt6.setTextColor(Color.parseColor("#1976D2"));
                } else if (materialistmodel.getPI() >= 5) {
                    tt6.setTextColor(Color.parseColor("#2196F3"));
                } else if (materialistmodel.getPI() >= 3) {
                    tt6.setTextColor(Color.parseColor("#F44336"));
                } else if (materialistmodel.getPI() >= 0) {
                    tt6.setTextColor(Color.parseColor("#D32F2F"));
                } else  {
                    tt1.setTextColor(Color.LTGRAY);
                }
            }

        }


    }

    private static class DisciplinaAdapter
            extends RecyclerView.Adapter<DisciplinasHolder> {
        @Override
        public DisciplinasHolder onCreateViewHolder(ViewGroup parent, int pos) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.materias_listrow, parent, false);




            return new DisciplinasHolder(view);
        }



        @Override
        public void onBindViewHolder(DisciplinasHolder disciplinasHolder, int i) {
            disciplinasList = MateriaListModel.listAll(MateriaListModel.class);
            MateriaListModel materiaListModel = disciplinasList.get(i);

            disciplinasHolder.bindCrime(materiaListModel);

        //    disciplinasHolder.itemView.setSelected(selectedDisciplinas.contains(i));

           if (selectedDisciplinas.contains(i)) {
                disciplinasHolder.itemView.setBackgroundColor(Color.LTGRAY);
            }

        }

        @Override
        public int getItemCount() {
            List<MateriaListModel> materias = MateriaListModel.listAll(MateriaListModel.class);
            return materias.size();
        }



    }

    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    isGooglePlayServicesAvailable();
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        credential.setSelectedAccountName(accountName);
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.commit();
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(contextFragment,"Account unspecified.",Toast.LENGTH_LONG).show();
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode != RESULT_OK) {
                    chooseAccount();
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private static void refreshResults() {
        if (credential.getSelectedAccountName() == null) {
            chooseAccount();
        } else {
            if (isDeviceOnline()) {
                new ApiAsyncTask(drawerActivity).execute();
            } else {
                Toast.makeText(contextFragment,"No network connection available.", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void clearResultsText() {
        mainActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {


            }
        });
    }

    public void updateResultsText(final List<String> dataStrings) {
        fragmentActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (dataStrings == null) {
                    Toast.makeText(contextFragment, "Error retrieving data!", Toast.LENGTH_LONG).show();
                } else if (dataStrings.size() == 0) {
                    Toast.makeText(contextFragment, "No data found.", Toast.LENGTH_LONG).show();
                } else {

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(contextFragment, android.R.layout.simple_list_item_1, dataStrings);


                    Toast.makeText(contextFragment, "Data retrieved using" +
                            " the Google Calendar API:", Toast.LENGTH_LONG).show();

                }
            }
        });
    }

    public void updateStatus(final String message) {
       mainActivity.runOnUiThread(new Runnable() {
           @Override
           public void run() {
               Toast.makeText(contextFragment, message, Toast.LENGTH_LONG).show();
           }
       });
    }

    private static void chooseAccount() {
        NavDrawerActivity navDrawerActivity = new NavDrawerActivity();
        mainActivity.startActivityForResult(
                credential.newChooseAccountIntent(), REQUEST_ACCOUNT_PICKER);
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


}


