package com.andreoid.EuAluno;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
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
import android.widget.ListView;
import android.widget.ProgressBar;

import com.andreoid.EuAluno.adapter.RecyclerAdapterReplies;
import com.andreoid.EuAluno.models.CardItemReplyModel;
import com.andreoid.EuAluno.models.ListaDeReplies;
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
public class RepliesFragment extends Fragment {

    private List<CardItemReplyModel> cardItems = new ArrayList();


    private ProfileActivity mainActivity;
    private SharedPreferences pref;
    private RecyclerView recyclerView;
    ListView listView ;
    private FloatingActionButton floatingActionButton;
    private List<ListaDeReplies.Replies> replies;
    private RecyclerAdapterReplies recyclerAdapterReplies;
    private View dialogView;

    Retrofit retrofit;
    String [] comentario;
    String [] conteudo;
    String[] feitoPor;
    ProgressBar progressBar;

    public static RepliesFragment newInstance(String tipo, String idTopico){
        RepliesFragment mFragment = new RepliesFragment();
        Bundle mBundle = new Bundle();

        mBundle.putString(Constants.TIPO, tipo);
        mBundle.putString(Constants.IDTOPIC,idTopico);

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
        retrofit= new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        pref = getActivity().getSharedPreferences("EuAluno", Context.MODE_PRIVATE);
        recyclerView = (RecyclerView)view.findViewById(R.id.fab_recycler_view2);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabClick(view);
            }
        });
        getReplies(getArguments().getString(Constants.IDTOPIC));
        setupRecyclerView();
setHasOptionsMenu(true);
        return view;
    }
    private void setupRecyclerView(){

        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        recyclerView.setHasFixedSize(true);



        recyclerAdapterReplies= new RecyclerAdapterReplies(cardItems,getContext());
        recyclerView.setAdapter(recyclerAdapterReplies);

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
                getReplies(getArguments().getString(Constants.IDTOPIC));
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    public void addItem(String idReply, String author, String reply_content, String data_reply){
        recyclerAdapterReplies.cardItems.add(new CardItemReplyModel(idReply, author, reply_content, data_reply));
        recyclerAdapterReplies.notifyDataSetChanged();
    }

    public void removeItem(){
        recyclerAdapterReplies.cardItems.remove(recyclerAdapterReplies.cardItems.size() - 1);
        recyclerAdapterReplies.notifyDataSetChanged();
    }
    public void fabClick(View view){

        setupDialog();

        AlertDialog.Builder builder =
                new AlertDialog.Builder(mainActivity, R.style.AppCompatAlertDialogStyle);
        builder.setTitle("Adicionar novo comentário");
        builder.setView(dialogView);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!isEmpty()) {
                    RequestInterface requestInterface = retrofit.create(RequestInterface.class);
                    ServerRequest request = new ServerRequest();


                    EditText contentText = ((EditText) dialogView.findViewById(R.id.content_text_input));
                    //EditText contentText = ((EditText) dialogView.findViewById(R.id.content_text_input))
                    request.setOperation("insertReplies");
                    request.setReply_topic(getArguments().getString(Constants.IDTOPIC, ""));
                    request.setReply_content(contentText.getText().toString().trim());
                    request.setUnique_id(pref.getString(Constants.UNIQUE_ID, ""));
                    Call<ServerResponse> response = requestInterface.operation(request);
                    response.enqueue(new Callback<ServerResponse>() {

                        @Override
                        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                            System.out.println(response.body());
                            ServerResponse resp = response.body();
//
                            Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_SHORT).show();
                            getReplies(getArguments().getString(Constants.IDTOPIC));

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
        dialogView = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_layout_replies,null,false);


        final TextInputLayout contentInputLayout = (TextInputLayout)dialogView.findViewById(R.id.text_input_content);


        contentInputLayout.setErrorEnabled(true);


        EditText contentInput = (EditText)dialogView.findViewById(R.id.content_text_input);


        contentInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence == null || charSequence.toString().equals("")) {
                    contentInputLayout.setError(getString(R.string.edittext_error));
                } else {
                    contentInputLayout.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private boolean isEmpty(){

        String contentText = ((EditText)dialogView.findViewById(R.id.content_text_input))
                .getText().toString().trim();

        if((contentText==null||contentText.equals(""))){
            Snackbar.make(getView().findViewById(R.id.fab_coordinator_layout),
                    getString(R.string.title_content_error), Snackbar.LENGTH_LONG).show();
            return true;
        }else
        if(contentText==null||contentText.equals("")){
            Snackbar.make(getView().findViewById(R.id.fab_coordinator_layout),
                    getString(R.string.contenttext_error),Snackbar.LENGTH_LONG).show();
            return true;
        }
        return false;
    }
    private void getReplies(final String reply_topic) {

        progressBar.setVisibility(View.VISIBLE);
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        final ServerRequest request = new ServerRequest();
        request.setOperation("getReplies");
        request.setReply_topic(reply_topic);
        Call<ListaDeReplies> response = requestInterface.getReplies(request);

        response.enqueue(new Callback<ListaDeReplies>() {

            @Override
            public void onResponse(Call<ListaDeReplies> call, Response<ListaDeReplies> response) {

                ListaDeReplies ListaDeReplies = response.body();
                replies = ListaDeReplies.getReplies();

                recyclerAdapterReplies.cardItems = new ArrayList<>();
                for (int i = 0; i < replies.size(); i++) {
                    addItem(replies.get(i).getIdreplies(), replies.get(i).getAutorComentario(), replies.get(i).getReply_content(), replies.get(i).getReply_date());

                    System.out.println(replies.get(i).getIdreplies());
                    progressBar.setVisibility(View.GONE);
                }


            }

            @Override
            public void onFailure(Call<ListaDeReplies> call, Throwable t) {


                // progress.setVisibility(View.INVISIBLE);
//                Log.d(Constants.TAG, t.getLocalizedMessage());
                //Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });


    }

}


