package com.andreoid.EuAluno;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andreoid.EuAluno.ProfileActivity;
import com.andreoid.EuAluno.R;
import com.andreoid.EuAluno.adapter.RecyclerAdapter;
import com.andreoid.EuAluno.models.CardItemModel;
import com.andreoid.EuAluno.models.ListaDeDisciplinas;
import com.andreoid.EuAluno.models.ListaDeTopicos;
import com.andreoid.EuAluno.models.ServerRequest;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class FabFragment extends Fragment {

    private List<CardItemModel> cardItems = new ArrayList<>(30);
    private List<ListaDeTopicos.Topicos> topicos;
    private ProfileActivity mainActivity;
    private SharedPreferences pref;
    private RecyclerView recyclerView;

    private FloatingActionButton floatingActionButton;
    private RecyclerAdapter recyclerAdapter;
    private View dialogView;
    Retrofit retrofit;
    Bundle mBundle = new Bundle();
    //private View thisView;


    public static FabFragment newInstance(String tipo,String text){
        FabFragment mFragment = new FabFragment();
        Bundle mBundle = new Bundle();
            mBundle.putString(Constants.TOPIC_CAT, text);
            mBundle.putString(Constants.TIPO, tipo);


        mFragment.setArguments(mBundle);
        return mFragment;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mainActivity = (ProfileActivity)activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fab, container, false);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();

        pref = getActivity().getSharedPreferences("EuAluno", Context.MODE_PRIVATE);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        if(getArguments().getString(Constants.TIPO, "").equals(Constants.IS_ALUNO)){
            floatingActionButton.setVisibility(View.INVISIBLE);
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabClick(view);
            }
        });
        //floatingActionButton.setVisibility(View.INVISIBLE);
        fixFloatingActionButtonMargin();


            recyclerView = (RecyclerView)view.findViewById(R.id.fab_recycler_view);
        retrofit= new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);


        setupRecyclerView();

        return view;
    }




    private void setupRecyclerView(){
        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        recyclerView.setHasFixedSize(true);

            getTopicos(getArguments().getString(Constants.TOPIC_CAT));

        recyclerAdapter = new RecyclerAdapter(cardItems);
        recyclerView.setAdapter(recyclerAdapter);
    }

    public void getTopicos(final String topic_cat){
        System.out.println(getArguments().getString(Constants.TOPIC_CAT, ""));
        if(getArguments().getString(Constants.TOPIC_CAT, "").equals("-1")){
            RequestInterface requestInterface = retrofit.create(RequestInterface.class);
            ServerRequest request = new ServerRequest();
            request.setOperation("getTopicosIndex");
            request.setUnique_id(pref.getString(Constants.UNIQUE_ID,""));

            Call<ListaDeTopicos> response = requestInterface.getTopicos(request);

            response.enqueue(new Callback<ListaDeTopicos>() {

                @Override
                public void onResponse(Call<ListaDeTopicos> call, Response<ListaDeTopicos> response) {
                    System.out.println(response.body());
                    ListaDeTopicos ListaDeTopicos = response.body();
                    topicos = ListaDeTopicos.getTopicos();
                    String[] nomeTopicos = new String[topicos.size()];
                    String[] nomeProfessor = new String[topicos.size()];
                    System.out.println(topicos.size());
                    for (int i = 0; i < topicos.size(); i++) {
                        nomeProfessor[i]= "Professor(a): "+topicos.get(i).getNomeProfessor();
                        nomeTopicos[i] = topicos.get(i).getTopic_subject();
                        System.out.println(nomeTopicos[i]);
                        System.out.println( nomeProfessor[i]);
                    }



                    final int length = nomeTopicos.length;
                    for (int i = 0; i < length; i++) {
                        addItem(nomeTopicos[i], nomeProfessor[i]);


                    }


                    //populateSpinner();
                    //
                    // System.out.println(resp.getCurso().getNome());
                }

                @Override
                public void onFailure(Call<ListaDeTopicos> call, Throwable t) {

                    // progress.setVisibility(View.INVISIBLE);
//                Log.d(Constants.TAG, t.getLocalizedMessage());
                    //Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                }
            });

        }
        else
            {
            RequestInterface requestInterface = retrofit.create(RequestInterface.class);
            ServerRequest request = new ServerRequest();
            request.setOperation("getTopicos");
            request.setTopic_cat(topic_cat);

        Call<ListaDeTopicos> response = requestInterface.getTopicos(request);

        response.enqueue(new Callback<ListaDeTopicos>() {

                @Override
                public void onResponse(Call<ListaDeTopicos> call, Response<ListaDeTopicos> response) {
                    System.out.println(response.body());
                    ListaDeTopicos ListaDeTopicos = response.body();
                    topicos = ListaDeTopicos.getTopicos();
                    String[] nomeTopicos = new String[topicos.size()];
                    String[] nomeProfessor = new String[topicos.size()];
                    System.out.println(topicos.size());
                    for (int i = 0; i < topicos.size(); i++) {
                        nomeProfessor[i]= "Professor(a): "+topicos.get(i).getNomeProfessor();
                        nomeTopicos[i] = topicos.get(i).getTopic_subject();
                        System.out.println(nomeTopicos[i]);
                        System.out.println( nomeProfessor[i]);
                    }



                    final int length = nomeTopicos.length;
                    for (int i = 0; i < length; i++) {
                        addItem(nomeTopicos[i], nomeProfessor[i]);


                    }


                    //populateSpinner();
                    //
                    // System.out.println(resp.getCurso().getNome());
                }

                @Override
                public void onFailure(Call<ListaDeTopicos> call, Throwable t) {

                    // progress.setVisibility(View.INVISIBLE);
//                Log.d(Constants.TAG, t.getLocalizedMessage());
                    //Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                }
            });

        }}



    public void addItem(String title,String content){
        recyclerAdapter.cardItems.add(new CardItemModel(title, content));
        recyclerAdapter.notifyDataSetChanged();
    }

    public void removeItem(){
        recyclerAdapter.cardItems.remove(recyclerAdapter.cardItems.size() - 1);
        recyclerAdapter.notifyDataSetChanged();
    }

    public void fixFloatingActionButtonMargin(){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams)
                    floatingActionButton.getLayoutParams();
            // get rid of margins since shadow area is now the margin
            p.setMargins(0, 0, dpToPx(getActivity(), 8), 0);
            floatingActionButton.setLayoutParams(p);
        }
    }

    public static int dpToPx(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) ((dp * scale) + 0.5f);
    }
    public void fabClick(View view){

        setupDialog();

        AlertDialog.Builder builder =
                new AlertDialog.Builder(mainActivity, R.style.AppCompatAlertDialogStyle);
        builder.setTitle(getString(R.string.dialog_title));
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!isEmpty()) {

                    String titleText = ((EditText) dialogView.findViewById(R.id.title_text_input))
                            .getText().toString().trim();
                    String contentText = ((EditText) dialogView.findViewById(R.id.content_text_input))
                            .getText().toString().trim();
                    addItem(titleText, contentText);

                }
            }
        });//second parameter used for onclicklistener
        builder.setNegativeButton("Cancel", null);
        //Show dialog and launch keyboard
        builder.show().getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    private void setupDialog(){
        dialogView = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_layout,null,false);

        final TextInputLayout titleInputLayout = (TextInputLayout)dialogView.findViewById(R.id.text_input_title);
        final TextInputLayout contentInputLayout = (TextInputLayout)dialogView.findViewById(R.id.text_input_content);

        titleInputLayout.setErrorEnabled(true);
        contentInputLayout.setErrorEnabled(true);

        EditText titleInput = (EditText)dialogView.findViewById(R.id.title_text_input);
        EditText contentInput = (EditText)dialogView.findViewById(R.id.content_text_input);

        titleInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence==null||charSequence.toString().equals("")){
                    titleInputLayout.setError(getString(R.string.edittext_error));
                }else{
                    titleInputLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        contentInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence==null||charSequence.toString().equals("")){
                    contentInputLayout.setError(getString(R.string.edittext_error));
                }else{
                    contentInputLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private boolean isEmpty(){
        String titleText = ((EditText)dialogView.findViewById(R.id.title_text_input))
                .getText().toString().trim();
        String contentText = ((EditText)dialogView.findViewById(R.id.content_text_input))
                .getText().toString().trim();

        if((titleText==null||titleText.equals(""))&&(contentText==null||contentText.equals(""))){
            Snackbar.make(getView().findViewById(R.id.fab_coordinator_layout),
                    getString(R.string.title_content_error),Snackbar.LENGTH_LONG).show();
            return true;
        }else if(titleText==null||titleText.equals("")){
            Snackbar.make(getView().findViewById(R.id.fab_coordinator_layout),
                    getString(R.string.titletext_error),Snackbar.LENGTH_LONG).show();
            return true;
        }else if(contentText==null||contentText.equals("")){
            Snackbar.make(getView().findViewById(R.id.fab_coordinator_layout),
                    getString(R.string.contenttext_error),Snackbar.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
}


