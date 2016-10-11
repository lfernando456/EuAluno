package com.andreoid.EuAluno;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andreoid.EuAluno.adapter.RecyclerAdapterTopicos;
import com.andreoid.EuAluno.models.CardItemTopicoModel;
import com.andreoid.EuAluno.models.ServerRequest;
import com.andreoid.EuAluno.models.ServerResponse;
import com.andreoid.EuAluno.models.Topico;
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
public class TopicosFragment extends Fragment {

    private List<CardItemTopicoModel> cardItems = new ArrayList();
    private List<Topico> topicos;
    private ProfileActivity mainActivity;
    private SharedPreferences pref;
    private RecyclerView recyclerView;

    private FloatingActionButton floatingActionButton;
    private RecyclerAdapterTopicos recyclerAdapterTopicos;
    private View dialogView;
    ProgressBar progressBar;
    private SwipeRefreshLayout swipeContainer;
    private RequestInterface requestInterface;
    private int hasAnexo=0;
    private Button buttonSendDocs;
    private TextView path_tv;
    private Uri uri;
    TextView tv_naopossui;
    TextView tv_erro;

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
        requestInterface = RetroClient.getApiService(1);
        pref = getActivity().getSharedPreferences("EuAluno", Context.MODE_PRIVATE);
        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        if(getArguments().getString(Constants.TIPO, "").equals(Constants.IS_ALUNO)){
            floatingActionButton.setVisibility(View.GONE);
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

        tv_naopossui = (TextView) view.findViewById(R.id.tv_naopossui);
        tv_erro = (TextView) view.findViewById(R.id.tv_erro);

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
        tv_naopossui.setVisibility(View.GONE);
        tv_erro.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.GONE);
        System.out.println(getArguments().getString(Constants.TOPIC_CAT, ""));
        ServerRequest request = new ServerRequest();

        if(getArguments().getString(Constants.TOPIC_CAT, "").equals("-1")){
            request.setOperation("getTopicosIndex");
            request.setUnique_id(pref.getString(Constants.UNIQUE_ID,""));
        }
        else {
            request.setOperation("getTopicos");
            request.setTopic_cat(topic_cat);
            request.setUnique_id(pref.getString(Constants.UNIQUE_ID,""));
        }

        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                topicos = response.body().getListaDeTopicos();
                if(topicos!=null){
                    cardItems.clear();
                    for (int i = 0; i < topicos.size(); i++) {

                        cardItems.add(new CardItemTopicoModel(
                                topicos.get(i).getIdTopics(),
                                topicos.get(i).getTopic_subject(),
                                topicos.get(i).getContent(),
                                "Professor(a): " + topicos.get(i).getNomeProfessor(),
                                topicos.get(i).getNomeDisciplina(),
                                topicos.get(i).getTopics_view_number(),
                                topicos.get(i).getTopic_replies_number(),
                                topicos.get(i).getTopic_viewed()
                        ));
                    }
                    recyclerAdapterTopicos.notifyDataSetChanged();
                }else {
                    tv_naopossui.setVisibility(View.VISIBLE);
                }
                if(!getArguments().getString(Constants.TIPO, "").equals(Constants.IS_ALUNO)){
                    floatingActionButton.setVisibility(View.VISIBLE);
                }

                progressBar.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);
                recyclerView.setVisibility(View.VISIBLE);
                //setupRecyclerView();

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                tv_erro.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                swipeContainer.setRefreshing(false);
                recyclerView.setVisibility(View.GONE);
                Snackbar.make(recyclerView, t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

        }
    public void addItem(String idTopico,String title, String content,String professor,String disciplina,String views,String replies_number,int viewed){
        recyclerAdapterTopicos.cardItems.add(new CardItemTopicoModel(idTopico, title, content, professor, disciplina, views, replies_number,viewed));
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
                    if (hasAnexo==1) {
                        uploadAnexo();
                    } else {
                        insertTopico(null);
                    }
                }
            }
        });
        builder.setNegativeButton("Cancel", null);
        //Show dialog and launch keyboard
        builder.show().getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_VISIBLE);

    }

    private void insertTopico(String nome) {
        ServerRequest request = new ServerRequest();

        EditText titleText = ((EditText) dialogView.findViewById(R.id.title_text_input));
        EditText contentText = ((EditText) dialogView.findViewById(R.id.content_text_input));
        //EditText contentText = ((EditText) dialogView.findViewById(R.id.content_text_input))
        request.setOperation("insertTopicos");
        request.setTopic_cat(getArguments().getString(Constants.TOPIC_CAT, ""));

        request.setTopic_subject(titleText.getText().toString().trim());
        request.setUnique_id(pref.getString(Constants.UNIQUE_ID, ""));
        request.setReply_content(contentText.getText().toString().trim());
        if(nome!=null)request.setAnexo(nome);
        Call<ServerResponse> response = requestInterface.operation(request);
        response.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                System.out.println(response.body());
                ServerResponse resp = response.body();

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

    private void uploadAnexo() {
        final ProgressDialog progressDialog;
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Enviando anexo...");
        progressDialog.setCancelable(false);
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
                if(response.isSuccessful()) {
                    if (response.body().getResult().equals("success")) {
                        Toast.makeText(getActivity(), "Upload feito com sucesso", Toast.LENGTH_LONG).show();
                        insertTopico(response.body().getName());
                    }
                    else
                        Toast.makeText(getActivity(), "Falha no upload", Toast.LENGTH_LONG).show();

                } else {
                    Toast.makeText(getActivity(),"Falha no upload", Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                progressDialog.dismiss();
            }
        });
    }


    private void setupDialog(){
        dialogView = LayoutInflater.from(mainActivity).inflate(R.layout.dialog_layout,null,false);

        final TextInputLayout titleInputLayout = (TextInputLayout)dialogView.findViewById(R.id.text_input_title);
        final TextInputLayout contentInputLayout = (TextInputLayout)dialogView.findViewById(R.id.text_input_content);
        buttonSendDocs = (Button)dialogView.findViewById(R.id.buttonSendDoc);
        path_tv = (TextView)dialogView.findViewById(R.id.path_tv);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            uri = data.getData();
            path_tv.setText(uri.getPath()+"");
            hasAnexo=1;

        }
    }
}


