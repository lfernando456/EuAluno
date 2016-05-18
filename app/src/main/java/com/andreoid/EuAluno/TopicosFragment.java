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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;

import com.andreoid.EuAluno.adapter.RecyclerAdapter;
import com.andreoid.EuAluno.models.CardItemModel;
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

        pref = getActivity().getSharedPreferences("EuAluno", Context.MODE_PRIVATE);

        floatingActionButton = (FloatingActionButton) view.findViewById(R.id.fab);
        if(getArguments().getString(Constants.TIPO, "").equals(Constants.IS_ALUNO)){
            floatingActionButton.setVisibility(View.INVISIBLE);
        }
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        //floatingActionButton.setVisibility(View.INVISIBLE);


        recyclerView = (RecyclerView)view.findViewById(R.id.fab_recycler_view);

        retrofit= new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);




        return view;
    }

}


