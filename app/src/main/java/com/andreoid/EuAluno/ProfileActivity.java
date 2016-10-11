package com.andreoid.EuAluno;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andreoid.EuAluno.adapter.RecyclerAdapterTopicos;
import com.andreoid.EuAluno.broadcast_receivers.NotificationEventReceiver;
import com.andreoid.EuAluno.fragment.MainFragment;
import com.andreoid.EuAluno.models.ServerRequest;
import com.andreoid.EuAluno.models.ServerResponse;
import com.andreoid.EuAluno.models.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoTools;

import br.liveo.interfaces.OnItemClickListener;
import br.liveo.interfaces.OnPrepareOptionsMenuLiveo;
import br.liveo.model.HelpLiveo;
import br.liveo.navigationliveo.NavigationLiveo;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends NavigationLiveo implements AdapterCallback {

    private static final int PERMISSION_REQUEST_CODE = 1;

    private HelpLiveo mHelpLiveo;
    int currentPosition;
    private SharedPreferences pref;
    public String name;
    public String email;
    int a;
    int tipo;
    ImageView C2;

    RequestInterface requestInterface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        NotificationEventReceiver.setupAlarm(getApplicationContext());
        super.onCreate(savedInstanceState);
    }
    @Override
    public NavigationLiveo setOnClickUser(View.OnClickListener listener){
        C2.setOnClickListener(listener);
        return this;
    }
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString("TITLE", getTitle().toString());
        savedInstanceState.putInt("POSITION", currentPosition);

        super.onSaveInstanceState(savedInstanceState);
    }
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setCheckedItemNavigation(savedInstanceState.getInt("POSITION"), true);
    }
    @Override
    public void onInt(Bundle savedInstanceState) {
        //Fetching email from shared preferences

        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        requestInterface = RetroClient.getApiService(1);


        pref = getSharedPreferences("EuAluno", Context.MODE_PRIVATE);
        tipo = Integer.parseInt(pref.getString(Constants.TIPO, ""));
        name=pref.getString(Constants.NAME, "");
        email=pref.getString(Constants.EMAIL, "");
        // User Information
        this.userName.setText(name);
        this.userEmail.setText(email);

        ViewGroup parent = (ViewGroup) this.userPhoto.getParent();
        int index = parent.indexOfChild(this.userPhoto);
        parent.removeView(this.userPhoto);
        View view = getLayoutInflater().inflate(R.layout.imageview, parent, false);
        parent.addView(view, index);

        C2 = (ImageView) view.findViewById(R.id.userPhoto2);



        this.userBackground.setImageResource(R.drawable.ic_user_background_first);
        //this.userBackground.setImageResource(R.color.colorPrimary); Fundo do cabeçalho com a cor primaria


        if (pref.getBoolean("novoCadastro", false)) {

            if (tipo == 0) {
                showConfigAlunoDialogAluno(pref.getString(Constants.UNIQUE_ID, ""));
            }
            if (tipo == 1) {
                showConfigAlunoDialogProfessor(pref.getString(Constants.UNIQUE_ID, ""));
            }
        }
        // Creating items navigation
        mHelpLiveo = new HelpLiveo();
        mHelpLiveo.add(getString(R.string.inbox), R.mipmap.ic_inbox_black_24dp, 100);
        mHelpLiveo.addSubHeader(getString(R.string.categories)); //Item subHeader
        mHelpLiveo.add("Inicio", R.drawable.ic_book_multiple_black_24dp);
        mHelpLiveo.add(getString(R.string.grades), R.mipmap.ic_send_black_24dp);

        mHelpLiveo.add(getString(R.string.disciplines), R.drawable.ic_book_black_24dp);
        mHelpLiveo.add("Cadastrar " + getString(R.string.disciplines), R.drawable.ic_book_plus_black_24dp);
        mHelpLiveo.addSeparator(); // Item separator
        mHelpLiveo.add(getString(R.string.myProfile), R.drawable.ic_account_black_24dp);
        //mHelpLiveo.add(getString(R.string.spam), R.mipmap.ic_report_black_24dp, 120);

        with(onItemClick).startingPosition(2) //Starting position in the list
                .addAllHelpItem(mHelpLiveo.getHelp())

                .colorItemSelected(R.color.nliveo_blue_colorPrimary)
                .colorNameSubHeader(R.color.nliveo_blue_colorPrimary)
                .footerItem(R.string.settings, R.mipmap.ic_settings_black_24dp)
                .setOnClickUser(onClickPhoto)

                .setOnPrepareOptionsMenu(onPrepare)
                .setOnClickFooter(onClickFooter)

                .build();

        //DrawerLayout mList = (DrawerLayout) findViewById(R.id.drawerLayout);
        //mList.setFitsSystemWindows(false);
        this.setElevationToolBar(15);

        verificadorAD(pref.getString(Constants.UNIQUE_ID, ""));
        setUserPhoto();
        if (savedInstanceState != null) {
            setTitle(savedInstanceState.getString("TITLE"));
            currentPosition = savedInstanceState.getInt("POSITION");
            setCheckedItemNavigation(currentPosition,true);
        } else {
            PicassoTools.clearCache(Picasso.with(this));
        }
        if(currentPosition==5)setElevationToolBar(0);
    }

    public void setUserPhoto() {
        Picasso.with(this)
                .load(Constants.BASE_URL+"TestePHP/FotosDePerfil/"+pref.getString(Constants.UNIQUE_ID, "")+".png")
                .placeholder(R.drawable.ic_no_user)
                .transform(new CircleTransform())
                .into(C2);
    }

    private OnItemClickListener onItemClick = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {

            Fragment mFragment;
            //FragmentManager mFragmentManager = getSupportFragmentManager();

            switch (position) {
                case 2:
                    mFragment = TopicosFragment.newInstance("0", "-1");
                    break;
                case 4:
                    mFragment = new DisciplinaFragment();
                    break;
                case 5:
                    mFragment = new CadastrarDisciplinaFragment();
                    break;
                case 7:
                    mFragment = new ProfileFragment();
                    break;
                default:
                    mFragment = MainFragment.newInstance(position + "");

                    break;
            }

            if (mFragment != null) {
                getSupportFragmentManager().popBackStack();
                setTitle(mHelpLiveo.get(position).getName());
                currentPosition = position;
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, mFragment).addToBackStack( getTitle().toString() );
                ft.commit();
            }
            if(position==5)setElevationToolBar(0);
        }
    };

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
                        editor.putString(Constants.EMAIL, email);
                        editor.apply();


                        //Starting login activity
                        Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                        finish();
                        startActivity(intent);

                        NotificationEventReceiver.cancelAlarm(getApplicationContext());
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
    private void registerAluno(String uniqueId, String matricula) {


        User user = new User();
        user.setUnique_id(uniqueId);

        user.setMatricula(matricula);

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

                ///      System.out.println(call.request().body());
                // progress.setVisibility(View.INVISIBLE);
//                Log.d(Constants.TAG, t.getMessage());
                //Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();


            }
        });
    }
    private void registerProfessor(String uniqueId, String siap) {


        User user = new User();
        user.setUnique_id(uniqueId);
        user.setSiap(siap);

        ServerRequest request = new ServerRequest();
        request.setOperation("registerProfessor");
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
                Toast.makeText(ProfileActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });
    }
    public void showConfigAlunoDialogAluno(final String uniqueId) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String matricula = edt.getText().toString();

                registerAluno(uniqueId, matricula);

                Fragment mFragment = new CadastrarDisciplinaFragment();

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.container, mFragment);
                ft.commit();

            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }
    public void showConfigAlunoDialogProfessor(final String uniqueId) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        TextView textV = (TextView)dialogView.findViewById(R.id.textView2);
        textV.setText("Número SIAP");

        dialogBuilder.setView(dialogView);

        dialogBuilder.setCancelable(false);
        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String siap = edt.getText().toString();
                if(siap!=null&&siap!="") {
                    registerProfessor(uniqueId, siap);

                    Fragment mFragment = new CadastrarDisciplinaFragment();

                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.container, mFragment);
                    ft.commit();
                }
                else{
                    showConfigAlunoDialogProfessor(uniqueId);
                }
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void verificadorAD(final String unique_id ){

        ServerRequest request = new ServerRequest();
        request.setOperation("verificadorD");
        request.setUnique_id(unique_id);


        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                //System.out.println(response.body());
                ServerResponse resp = response.body();

                if (resp.isAux()) {
                    a = 1;
                } else {
                    a = 0;
                }
                setTheme(R.style.AppTheme);
                //if (resp.getMessage() != null)
                //Toast.makeText(, resp.getMessage(), Toast.LENGTH_LONG).show();


            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                System.out.println(call.request().body());

                Log.d(Constants.TAG, t.getLocalizedMessage());
                setTheme(R.style.AppTheme);
                //Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();


            }
        });

    }
    @Override
    public void onMethodCallbackTopico(String idTopico, String title) {
        setTitle(title);
        Fragment mFragment = RepliesFragment.newInstance(tipo + "", idTopico);

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.container, mFragment).addToBackStack( title ).commit();

    }
    String filename;
    @Override
    public void onMethodCallbackReply(final String filename) {
        this.filename=filename;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setNegativeButton("Cancelar",null);
        dialogBuilder.setMessage("O que você deseja fazer?");
        dialogBuilder.setPositiveButton("Download aqui", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                downloadFile(filename);
            }
        });
        dialogBuilder.setNeutralButton("Enviar para o email", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();



    }

    public void downloadFile(String filename){

        if(checkPermission()){
            startDownload(filename);
        } else {

            requestPermission();
        }
    }

    private void startDownload(String filename){

        Intent intent = new Intent(this,DownloadService.class);
        intent.putExtra("FILENAME",filename);
        startService(intent);

    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        } else {

            return false;
        }
    }

    private void requestPermission(){

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    startDownload(filename);
                } else {

                    Snackbar.make(findViewById(R.id.fab_coordinator_layout), "Permissão Negada, Permita para continuar!", Snackbar.LENGTH_LONG).show();

                }
                break;
        }
    }
    @Override
    public void onBackPressed(){
        if(!isDrawerOpen()) {
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                if (currentPosition == 0 || currentPosition == 3 || currentPosition == 4 || currentPosition == 5 || currentPosition == 7) {
                    onItemClick.onItemClick(2);
                    setCheckedItemNavigation(2, true);
                    currentPosition=2;

                } else {
                    finish();
                }
            } else {
                super.onBackPressed();
                int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
                setTitle(getSupportFragmentManager().getBackStackEntryAt(index).getName());
            }
        }else super.onBackPressed();
    }
}
