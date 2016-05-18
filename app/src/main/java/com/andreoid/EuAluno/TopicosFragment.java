package com.andreoid.EuAluno;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.andreoid.EuAluno.adapter.RecyclerAdapter;
import com.andreoid.EuAluno.models.CardItemModel;
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
public class TopicosFragment extends Fragment {

    private List<CardItemModel> cardItems = new ArrayList();


    private ProfileActivity mainActivity;
    private SharedPreferences pref;
    private RecyclerView recyclerView;
    ListView listView ;
    private FloatingActionButton floatingActionButton;
    private List<ListaDeReplies.Replies> replies;
    private RecyclerAdapter recyclerAdapter;
    private View dialogView;
    Retrofit retrofit;
    String [] comentario;
    String [] conteudo;
    String[] feitoPor;


    public static TopicosFragment newInstance(String tipo){
        TopicosFragment mFragment = new TopicosFragment();
        Bundle mBundle = new Bundle();

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

        listView = (ListView) view.findViewById(R.id.listView2);
        recyclerView = (RecyclerView)view.findViewById(R.id.fab_recycler_view);

        retrofit= new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        getReplies("30");



        return view;
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
                comentario = new String[replies.size()];
                conteudo =new String[replies.size()];
                feitoPor = new String[replies.size()];
                for (int i = 0; i < replies.size(); i++) {
                    conteudo [i] = replies.get(i).getReply_content();
                    feitoPor [i] = replies.get(i).getAutorComentario();
                        comentario[i] = "Autor: "+replies.get(i).getAutorComentario()+ " Comentário: " +replies.get(i).getReply_content();;
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, comentario);

                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }

                });
                listView.setVisibility(View.VISIBLE);
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


