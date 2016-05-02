package com.andreoid.EuAluno;

import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.andreoid.EuAluno.fragment.MainFragment;
import com.andreoid.EuAluno.fragment.ViewPagerFragment;
import com.andreoid.EuAluno.models.ListaDeCursos;
import com.andreoid.EuAluno.models.ServerRequest;
import com.andreoid.EuAluno.models.ServerResponse;
import com.andreoid.EuAluno.models.User;


import java.util.ArrayList;
import java.util.List;

import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.model.HelpLiveo;
import br.liveo.navigationliveo.NavigationLiveo;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ProfileActivity extends NavigationLiveo implements OnItemClickListener {

    //Textview to show currently logged in user
    private TextView textView;
    private HelpLiveo mHelpLiveo;
    private SharedPreferences pref;
    public String name;//= "Andre";

    public String email;//= "andre@asdndsa.sass";
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    Retrofit retrofit;
    private List<ListaDeCursos.Curso> cursos;
    @Override
    public void onInt(Bundle savedInstanceState) {
        //Fetching email from shared preferences
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        retrofit= new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        pref = getSharedPreferences("EuAluno", Context.MODE_PRIVATE);

        // User Information
        this.userName.setText(pref.getString(Constants.NAME, ""));
        this.userEmail.setText(pref.getString(Constants.EMAIL, ""));
        this.userPhoto.setImageResource(R.mipmap.ic_no_user);
        this.userBackground.setImageResource(R.drawable.ic_user_background_first);


        if (pref.getBoolean("novoCadastro", false)) {

            if (Integer.parseInt(pref.getString("tipo", "")) == 0) {
               //showConfigAlunoDialog(pref.getString(Constants.UNIQUE_ID, ""));
                getCursos();
                showConfigAlunoDialog(pref.getString(Constants.UNIQUE_ID, ""));

            }
            if (Integer.parseInt(pref.getString("tipo", "")) == 1) {
                registerProfessor(pref.getString(Constants.UNIQUE_ID, ""));

            }
        }
        // Creating items navigation
        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.add(getString(R.string.inbox), R.mipmap.ic_inbox_black_24dp, 100);
        mHelpLiveo.addSubHeader(getString(R.string.categories)); //Item subHeader
        mHelpLiveo.add(getString(R.string.presence), R.mipmap.ic_star_black_24dp);
        mHelpLiveo.add(getString(R.string.grades), R.mipmap.ic_send_black_24dp);
        mHelpLiveo.add(getString(R.string.disciplines), R.mipmap.ic_drafts_black_24dp);
        mHelpLiveo.addSeparator(); // Item separator
        mHelpLiveo.add(getString(R.string.myProfile), R.mipmap.ic_delete_black_24dp);
        //mHelpLiveo.add(getString(R.string.spam), R.mipmap.ic_report_black_24dp, 120);


        //{optional} - Header Customization - method customHeader
//        View mCustomHeader = getLayoutInflater().inflate(R.layout.custom_header_user, this.getListView(), false);
//        ImageView imageView = (ImageView) mCustomHeader.findViewById(R.id.imageView);

        with(this).startingPosition(2) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())

                        //{optional} - List Customization "If you remove these methods and the list will take his white standard color"
                        //.selectorCheck(R.drawable.selector_check) //Inform the background of the selected item color
                        //.colorItemDefault(R.color.nliveo_blue_colorPrimary) //Inform the standard color name, icon and counter
                        //.colorItemSelected(R.color.nliveo_purple_colorPrimary) //State the name of the color, icon and meter when it is selected
                        //.backgroundList(R.color.nliveo_black_light) //Inform the list of background color
                        //.colorLineSeparator(R.color.nliveo_transparent) //Inform the color of the subheader line

                        //{optional} - SubHeader Customization
                .colorItemSelected(R.color.nliveo_blue_colorPrimary)
                .colorNameSubHeader(R.color.nliveo_blue_colorPrimary)
                        //.colorLineSeparator(R.color.nliveo_blue_colorPrimary)

                .footerItem(R.string.settings, R.mipmap.ic_settings_black_24dp)
                        //.footerSecondItem(R.string.settings, R.mipmap.ic_settings_black_24dp)

                        //{optional} - Header Customization
                        //.customHeader(mCustomHeader)

                        //{optional} - Footer Customization
                        //.footerNameColor(R.color.nliveo_blue_colorPrimary)
                        //.footerIconColor(R.color.nliveo_blue_colorPrimary)
                        //.footerBackground(R.color.nliveo_white)

                .setOnClickUser(onClickPhoto)
                .setOnPrepareOptionsMenu(onPrepare)
                .setOnClickFooter(onClickFooter)
                        //.setOnClickFooterSecond(onClickFooter)
                .build();

        int position = this.getCurrentPosition();
        this.setElevationToolBar(position != 2 ? 15 : 0);
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
       // getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onItemClick(int position) {
        Fragment mFragment;
        //FragmentManager mFragmentManager = getSupportFragmentManager();

        switch (position) {
            case 2:
                mFragment = new ViewPagerFragment();
                break;
            case 6:
                mFragment = new ProfileFragment();
                break;
            case 4:
                mFragment = new DisciplinaFragment();

                break;
            default:
                mFragment = MainFragment.newInstance(position + "");
                break;
        }

        if (mFragment != null) {

            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, mFragment);
            ft.commit();
        }

        setElevationToolBar(position != 2 ? 15 : 0);
    }

    private OnPrepareOptionsMenuLiveo onPrepare = new OnPrepareOptionsMenuLiveo() {
        @Override
        public void onPrepareOptionsMenu(Menu menu, int position, boolean visible) {
        }
    };

    private View.OnClickListener onClickPhoto = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(getApplicationContext(), "ClickNaFoto()", Toast.LENGTH_SHORT).show();
            closeDrawer();
        }
    };

    private View.OnClickListener onClickFooter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
            closeDrawer();
        }
    };


    //Logout function
    private void logout() {
        //Creating an alert dialog to confirm logout
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Você deseja deslogar?");
        alertDialogBuilder.setPositiveButton("Sim",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        /*editor.putBoolean(Constants.IS_LOGGED_IN, false);
                        editor.putString(Constants.EMAIL,"");
                        editor.putString(Constants.NAME,"");
                        editor.putString(Constants.UNIQUE_ID, "");
                        */
                        editor.apply();


                        //Starting login activity
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        finish();
                        startActivity(intent);
                    }
                });

        alertDialogBuilder.setNegativeButton("Não",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

                    }
                });

        //Showing the alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuLogout) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void registerAluno(String uniqueId, String idCurso, String matricula, String ano) {

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setUnique_id(uniqueId);
        user.setIdCurso(idCurso);
        user.setMatricula(matricula);
        user.setAno(ano);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.REGISTER_OPERATION + "Aluno");
        request.setUser(user);

        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                System.out.println(response.body());
                ServerResponse resp = response.body();
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("novoCadastro", false);
                editor.apply();
                //Snackbar.make(this, resp.getMessage(), Snackbar.LENGTH_LONG).show();

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                System.out.println(call.request().body());
                // progress.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG, t.getMessage());
                //Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();


            }
        });
    }

    private void registerProfessor(String uniqueId) {

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);

        User user = new User();
        user.setUnique_id(uniqueId);

        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.REGISTER_OPERATION + "Professor");
        request.setUser(user);

        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                System.out.println(response.body());
                ServerResponse resp = response.body();
                Toast.makeText(ProfileActivity.this, resp.getMessage(), Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = pref.edit();
                editor.putBoolean("novoCadastro", false);
                editor.apply();
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                //System.out.println(call.request().body());
                // progress.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG, t.getMessage());
                Toast.makeText(ProfileActivity.this,  t.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });
    }
    Spinner spinnerCurso;
    public void showConfigAlunoDialog(final String uniqueId) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
        final Spinner spinner = (Spinner) dialogView.findViewById(R.id.spinner);
        spinnerCurso = (Spinner) dialogView.findViewById(R.id.spinner2);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<>();
        categories.add("1");
        categories.add("2");
        categories.add("3");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        //dialogBuilder.setTitle("Cadastro de Aluno");
        //dialogBuilder.setMessage("Enter text below");
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String matricula = edt.getText().toString();
                String ano = spinner.getSelectedItem().toString();
                int cursoId = spinnerCurso.getSelectedItemPosition()+1;
                registerAluno(uniqueId, cursoId+"", matricula, ano);
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void getCursos() {
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("getCursos");
        Call<ListaDeCursos> response = requestInterface.getCursos(request);

        response.enqueue(new Callback<ListaDeCursos>() {

            @Override
            public void onResponse(Call<ListaDeCursos> call, Response<ListaDeCursos> response) {
                //System.out.println(response.body());
                ListaDeCursos listaDeCursos = response.body();
                cursos = listaDeCursos.getCursos();
                for (int i = 0; i < cursos.size(); i++) {
                    System.out.println(cursos.get(i).getNome());
                }
                populateSpinner();

                //populateSpinner();
                //
                // System.out.println(resp.getCurso().getNome());
            }

            @Override
            public void onFailure(Call<ListaDeCursos> call, Throwable t) {


                // progress.setVisibility(View.INVISIBLE);
//                Log.d(Constants.TAG, t.getLocalizedMessage());
                //Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }
    private void populateSpinner() {
        List<String> lables = new ArrayList<>();


        for (int i = 0; i < cursos.size(); i++) {
            lables.add(cursos.get(i).getNome());
        }

        // Creating adapter for spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, lables);

        // Drop down layout style - list view with radio button
        spinnerAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerCurso.setAdapter(spinnerAdapter);
    }
}
