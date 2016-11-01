package com.andreoid.EuAluno;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andreoid.EuAluno.adapter.RecyclerAdapterReplies;
import com.andreoid.EuAluno.adapter.UserViewedListAdapter;
import com.andreoid.EuAluno.models.CardItemReplyModel;
import com.andreoid.EuAluno.models.Reply;
import com.andreoid.EuAluno.models.ServerRequest;
import com.andreoid.EuAluno.models.ServerResponse;
import com.andreoid.EuAluno.models.User;
import com.nononsenseapps.filepicker.FilePickerActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class RepliesFragment extends Fragment {


    private List<CardItemReplyModel> cardItems = new ArrayList();


    private ProfileActivity mainActivity;
    private SharedPreferences pref;
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private List<Reply> replies;
    private List<User> user_viewed;
    private RecyclerAdapterReplies recyclerAdapterReplies;
    private View dialogView;
    Button buttonSendDocs;
    TextView path_tv;

    ProgressBar progressBar;
    private SwipeRefreshLayout swipeContainer;
    private RequestInterface requestInterface;
    private int hasAnexo = 0;
    private Uri uri;
    TextView tv_naopossui;
    TextView tv_erro;
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
        View view = inflater.inflate(R.layout.fragment_replies, container, false);
        requestInterface = RetroClient.getApiService(1);

        pref = getActivity().getSharedPreferences("EuAluno", Context.MODE_PRIVATE);
        recyclerView = (RecyclerView)view.findViewById(R.id.fab_recycler_view2);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabClick(view);
            }
        });

        tv_naopossui = (TextView) view.findViewById(R.id.tv_naopossui);
        tv_erro = (TextView) view.findViewById(R.id.tv_erro);
        getReplies(getArguments().getString(Constants.IDTOPIC));
        setupRecyclerView();
        setupSwipeRefresh();
        setHasOptionsMenu(true);
        return view;
    }
    private void setupRecyclerView(){

        recyclerView.setLayoutManager(new LinearLayoutManager(mainActivity));
        recyclerView.setHasFixedSize(true);



        recyclerAdapterReplies= new RecyclerAdapterReplies(cardItems,getContext());
        recyclerView.setAdapter(recyclerAdapterReplies);

    }
    private void setupSwipeRefresh(){

        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
                getReplies(getArguments().getString(Constants.IDTOPIC));
            }
        });
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(R.color.colorPrimary);

    }
    @Override
    public void onPrepareOptionsMenu(Menu menu) {


        menu.findItem(R.id.refresh).setVisible(true);
        menu.findItem(R.id.info).setVisible(true);

        super.onPrepareOptionsMenu(menu);

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh:
                getReplies(getArguments().getString(Constants.IDTOPIC));
                break;
            case R.id.info:
                getUserViews(getArguments().getString(Constants.IDTOPIC));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void getUserViews(String IDTOPIC) {


        new AlertDialog.Builder(getContext())

                .setTitle("Lista de Usuários que visualizaram")
                .setAdapter(
                        new UserViewedListAdapter(getActivity(),user_viewed),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })

                .show();
    }

    public void addItem(String idReply, String author, String reply_content, String data_reply, String unique_id, String anexo){
        recyclerAdapterReplies.cardItems.add(new CardItemReplyModel(idReply, author, reply_content, data_reply, unique_id, anexo));
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
                    if (hasAnexo == 1) {
                        uploadAnexo();
                    } else {
                        insertReply(null);
                    }
                }
            }
        });//second parameter used for onclicklistener
        builder.setNegativeButton("Cancel", null);
        //Show dialog and launch keyboard
        builder.show().getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    private void insertReply(String nome) {
        ServerRequest request = new ServerRequest();


        EditText contentText = ((EditText) dialogView.findViewById(R.id.content_text_input));
        //EditText contentText = ((EditText) dialogView.findViewById(R.id.content_text_input))
        request.setOperation("insertReplies");
        request.setReply_topic(getArguments().getString(Constants.IDTOPIC, ""));
        request.setReply_content(contentText.getText().toString().trim());
        request.setUnique_id(pref.getString(Constants.UNIQUE_ID, ""));
        if(nome!=null)request.setAnexo(nome);
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

    private void uploadAnexo() {

        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Enviando anexo...");
        progressDialog.show();
        File file = new File(uri.getPath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("uploaded_anexo", file.getName(), requestFile);
        ServerRequest request = new ServerRequest();
        request.setOperation("uploadAnexo");

        Call<ServerResponse> resultCall = requestInterface.upload(body, request);

        resultCall.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                progressDialog.dismiss();
                System.out.println(response.body().getResult());
                if (response.isSuccessful()) {
                    if (response.body().getResult().equals("success")) {
                        Toast.makeText(getActivity(), "Upload feito com sucesso", Toast.LENGTH_LONG).show();
                        insertReply(response.body().getName());
                    } else
                        Toast.makeText(getActivity(), "Falha no upload", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getActivity(), "Falha no upload", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }

    private void setupDialog(){
        dialogView = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_layout_replies,null,false);


        final TextInputLayout contentInputLayout = (TextInputLayout)dialogView.findViewById(R.id.text_input_content);
        buttonSendDocs = (Button)dialogView.findViewById(R.id.buttonSendDoc);
        path_tv = (TextView)dialogView.findViewById(R.id.path_tv);
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

        buttonSendDocs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), FilePickerActivity.class);

                i.putExtra(FilePickerActivity.EXTRA_ALLOW_MULTIPLE, false);
                i.putExtra(FilePickerActivity.EXTRA_ALLOW_CREATE_DIR, false);
                i.putExtra(FilePickerActivity.EXTRA_MODE, FilePickerActivity.MODE_FILE);


                i.putExtra(FilePickerActivity.EXTRA_START_PATH, Environment.getExternalStorageDirectory().getPath());

                startActivityForResult(i, 0);
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
        recyclerView.setVisibility(View.GONE);
        tv_naopossui.setVisibility(View.GONE);
        tv_erro.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        final ServerRequest request = new ServerRequest();
        request.setOperation("getReplies");
        request.setReply_topic(reply_topic);
        request.setUnique_id(pref.getString(Constants.UNIQUE_ID, ""));
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                replies = response.body().getListaDeReplies();
                user_viewed = response.body().getListaDeUser_viewed();
                if (replies != null){
                    recyclerAdapterReplies.cardItems.clear();
                    recyclerAdapterReplies.notifyDataSetChanged();
                    for (int i = 0; i < replies.size(); i++) {
                        addItem(replies.get(i).getIdreplies(),
                                replies.get(i).getAutorComentario(),
                                replies.get(i).getReply_content(),
                                replies.get(i).getReply_date(),
                                replies.get(i).getUnique_id(),
                                replies.get(i).getAnexo());

                        //System.out.println(replies.get(i).getIdreplies());

                    }
                }else{
                    tv_naopossui.setVisibility(View.VISIBLE);
                }
                floatingActionButton.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);
                recyclerView.setVisibility(View.VISIBLE);


            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                tv_erro.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);
                recyclerView.setVisibility(View.GONE);
                // progress.setVisibility(View.INVISIBLE);
//                Log.d(Constants.TAG, t.getLocalizedMessage());
                Snackbar.make(recyclerView, t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            path_tv.setText(uri.getPath()+"");
            hasAnexo=1;

        }
    }




}

