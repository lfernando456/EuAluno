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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.andreoid.EuAluno.adapter.RecyclerAdapterTopicos;
import com.andreoid.EuAluno.models.CardItemTopicoModel;
import com.andreoid.EuAluno.models.ListaDeReplies;
import com.andreoid.EuAluno.models.ListaDeTopicos;
import com.andreoid.EuAluno.models.ServerRequest;
import com.andreoid.EuAluno.models.ServerResponse;


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
public class TopicosFragment extends Fragment {

    private List<CardItemTopicoModel> cardItems = new ArrayList();
    private List<ListaDeTopicos.Topicos> topicos;
    private List<ListaDeReplies.Replies> replies;
    private ProfileActivity mainActivity;
    private SharedPreferences pref;
    private RecyclerView recyclerView;

    private FloatingActionButton floatingActionButton;
    private RecyclerAdapterTopicos recyclerAdapterTopicos;
    private View dialogView;
    Retrofit retrofit;
    ProgressBar progressBar;
    private SwipeRefreshLayout swipeContainer;


    public static TopicosFragment newInstance(String tipo,String text){
        TopicosFragment mFragment = new TopicosFragment();
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
        View view = inflater.inflate(R.layout.fragment_topicos, container, false);
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

        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        recyclerView = (RecyclerView)view.findViewById(R.id.fab_recycler_view);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        retrofit= new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        getTopicos(getArguments().getString(Constants.TOPIC_CAT));
        setupRecyclerView();
        setupSwipeRefresh();
setHasOptionsMenu(true);

        return view;
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {


            menu.findItem(R.id.refresh).setVisible(true);


        super.onPrepareOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh:
                getTopicos(getArguments().getString(Constants.TOPIC_CAT));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSwipeRefresh(){

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                getTopicos(getArguments().getString(Constants.TOPIC_CAT));
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary);

    }
    private void setupRecyclerView(){

        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        recyclerView.setHasFixedSize(true);



        recyclerAdapterTopicos = new RecyclerAdapterTopicos(cardItems,getContext());
        recyclerView.setAdapter(recyclerAdapterTopicos);

    }

    public void getTopicos(final String topic_cat){
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
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
                    recyclerAdapterTopicos.cardItems.clear();
                    recyclerAdapterTopicos.notifyDataSetChanged();
                    for (int i = 0; i < topicos.size(); i++) {
                        addItem(
                                topicos.get(i).getIdTopics(),
                                topicos.get(i).getTopic_subject(),
                                topicos.get(i).getContent(),
                                "Professor(a): "+topicos.get(i).getNomeProfessor(),
                                topicos.get(i).getNomeDisciplina(),
                                topicos.get(i).getTopics_view_number(),
                                topicos.get(i).getTopic_replies_number()
                        );
                    }
                    progressBar.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);
                    recyclerView.setVisibility(View.VISIBLE);
                    setupRecyclerView();

                }

                @Override
                public void onFailure(Call<ListaDeTopicos> call, Throwable t) {

                    progressBar.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);
                    recyclerView.setVisibility(View.VISIBLE);
                    setupRecyclerView();
                    Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                }
            });

        }
    public void addItem(String idTopico,String title, String content,String professor,String disciplina,String views,String replies_number){
        recyclerAdapterTopicos.cardItems.add(new CardItemTopicoModel(idTopico,title, content, professor, disciplina, views,replies_number));
        recyclerAdapterTopicos.notifyDataSetChanged();
    }
    public void removeItem(){
        recyclerAdapterTopicos.cardItems.remove(recyclerAdapterTopicos.cardItems.size() - 1);
        recyclerAdapterTopicos.notifyDataSetChanged();
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
                            Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_SHORT).show();
                            getTopicos(getArguments().getString(Constants.TOPIC_CAT));

                        }


                        @Override
                        public void onFailure(Call<ServerResponse> call, Throwable t) {

                            System.out.println(call.request().body());

                            Log.d(Constants.TAG, t.getMessage());


                        }
                    });




                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        //Show dialog and launch keyboard
        builder.show().getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }
    private void setupDialog(){
        dialogView = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_layout_replies,null,false);

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


