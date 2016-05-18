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
import android.util.Log;
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
import com.andreoid.EuAluno.models.ListaDeReplies;
import com.andreoid.EuAluno.models.ListaDeTopicos;
import com.andreoid.EuAluno.models.ServerRequest;
import com.andreoid.EuAluno.models.ServerResponse;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

    private List<CardItemModel> cardItems = new ArrayList();
    private List<ListaDeTopicos.Topicos> topicos;
    private List<ListaDeReplies.Replies> replies;
    private ProfileActivity mainActivity;
    private SharedPreferences pref;
    private RecyclerView recyclerView;

    private FloatingActionButton floatingActionButton;
    private RecyclerAdapter recyclerAdapter;
    private View dialogView;
    Retrofit retrofit;


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

        recyclerAdapter = new RecyclerAdapter(cardItems,getContext());
        recyclerView.setAdapter(recyclerAdapter);

    }

    public void getTopicos(final String topic_cat){
        System.out.println(getArguments().getString(Constants.TOPIC_CAT, ""));
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();

        if(getArguments().getString(Constants.TOPIC_CAT, "").equals("-1")){
            request.setOperation("getTopicosIndex");
            request.setUnique_id(pref.getString(Constants.UNIQUE_ID,""));
        }
        else {
            request.setOperation("getTopicos");
            request.setTopic_cat(topic_cat);
        }

        Call<ListaDeTopicos> response = requestInterface.getTopicos(request);

        response.enqueue(new Callback<ListaDeTopicos>() {

                @Override
                public void onResponse(Call<ListaDeTopicos> call, Response<ListaDeTopicos> response) {

                    ListaDeTopicos ListaDeTopicos = response.body();
                    topicos = ListaDeTopicos.getTopicos();
                    System.out.println(topicos.size());
                    for (int i = 0; i < topicos.size(); i++) {
                        addItem(
                                topicos.get(i).getIdTopics(),
                                topicos.get(i).getTopic_subject(),
                                topicos.get(i).getContent(),
                                "Professor(a): "+topicos.get(i).getNomeProfessor(),
                                topicos.get(i).getNomeDisciplina(),
                                "1.486 Views"
                        );
                    }

                }

                @Override
                public void onFailure(Call<ListaDeTopicos> call, Throwable t) {

                    // progress.setVisibility(View.INVISIBLE);
//                Log.d(Constants.TAG, t.getLocalizedMessage());
                    //Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                }
            });

        }



    public void addItem(String idTopico,String title, String content,String professor,String disciplina,String views){
        recyclerAdapter.cardItems.add(new CardItemModel(idTopico,title, content, professor, disciplina, views));
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
        builder.setTitle("Adicionar novo tópico");
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!isEmpty()) {
                    RequestInterface requestInterface = retrofit.create(RequestInterface.class);
                    ServerRequest request = new ServerRequest();

                    EditText titleText = ((EditText) dialogView.findViewById(R.id.title_text_input));
                    EditText contentText = ((EditText) dialogView.findViewById(R.id.content_text_input));
                    //EditText contentText = ((EditText) dialogView.findViewById(R.id.content_text_input))
                    request.setOperation("insertTopicos");
                    request.setTopic_cat(getArguments().getString(Constants.TOPIC_CAT, ""));

                    request.setTopic_subject(titleText.getText().toString().trim());
                    request.setUnique_id(pref.getString(Constants.UNIQUE_ID, ""));
                    request.setReply_content(contentText.getText().toString().trim());
                    Call<ServerResponse> response = requestInterface.operation(request);
                    response.enqueue(new Callback<ServerResponse>() {

                        @Override
                        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                            System.out.println(response.body());
                            ServerResponse resp = response.body();
//


                        }


                        @Override
                        public void onFailure(Call<ServerResponse> call, Throwable t) {

                            System.out.println(call.request().body());

                            Log.d(Constants.TAG, t.getMessage());


                        }
                    });




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


