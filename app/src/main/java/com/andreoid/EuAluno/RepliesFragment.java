package com.andreoid.EuAluno;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.andreoid.EuAluno.adapter.RecyclerAdapterReplies;
import com.andreoid.EuAluno.models.CardItemReplyModel;
import com.andreoid.EuAluno.models.ListaDeReplies;
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


        recyclerView = (RecyclerView)view.findViewById(R.id.fab_recycler_view2);

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

        getReplies(getArguments().getString(Constants.IDTOPIC));

        recyclerAdapterReplies= new RecyclerAdapterReplies(cardItems,getContext());
        recyclerView.setAdapter(recyclerAdapterReplies);

    }
    public void addItem(String idReply, String author, String reply_content, String data_reply){
        recyclerAdapterReplies.cardItems.add(new CardItemReplyModel(idReply, author, reply_content, data_reply));
        recyclerAdapterReplies.notifyDataSetChanged();
    }

    public void removeItem(){
        recyclerAdapterReplies.cardItems.remove(recyclerAdapterReplies.cardItems.size() - 1);
        recyclerAdapterReplies.notifyDataSetChanged();
    }
    private void getReplies(final String reply_topic) {


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


                for (int i = 0; i < replies.size(); i++) {
                    addItem(replies.get(i).getIdreplies(), replies.get(i).getAutorComentario(), replies.get(i).getReply_content(), replies.get(i).getReply_date());

                    System.out.println(replies.get(i).getIdreplies());
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


