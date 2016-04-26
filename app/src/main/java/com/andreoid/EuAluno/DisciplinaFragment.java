package com.andreoid.EuAluno;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andreoid.EuAluno.models.ListaDeCursos;
import com.andreoid.EuAluno.models.ListaDeDisciplinas;
import com.andreoid.EuAluno.models.ListaDeTopicos;
import com.andreoid.EuAluno.models.ServerRequest;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DisciplinaFragment extends Fragment implements View.OnClickListener {
    private List<ListaDeDisciplinas.Disciplina> disciplinas;
    private List<ListaDeTopicos.Topicos> topicos;
    private List<ListaDeCursos.Curso> cursos;
    private SharedPreferences pref;
    Retrofit retrofit;
    ListView listView ;
    ProgressBar progressBar;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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

        View view = inflater.inflate(R.layout.fragment_disciplina,container,false);
        initViews(view);
        getCursos();


        // ListView Item Click Listener

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        pref = getActivity().getSharedPreferences("EuAluno", Context.MODE_PRIVATE);
        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewPager);
        //mViewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(), List<String> l ));
        /*tv_name.setText("Bem-Vindo : "+pref.getString(Constants.NAME,""));
        tv_email.setText(pref.getString(Constants.EMAIL, ""));
        tv_uid.setText(pref.getString(Constants.UNIQUE_ID, ""));*/

    }

    private void initViews(View view){

        /*tv_name = (TextView)view.findViewById(R.id.tv_name);
        tv_email = (TextView)view.findViewById(R.id.tv_email);
        tv_uid = (TextView)view.findViewById(R.id.tv_uid);
        btn_change_password = (AppCompatButton)view.findViewById(R.id.btn_chg_password);
        btn_logout = (AppCompatButton)view.findViewById(R.id.btn_logout);
        btn_change_password.setOnClickListener(this);
        btn_logout.setOnClickListener(this);*/
        listView = (ListView) view.findViewById(R.id.listView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        textView = (TextView) view.findViewById(R.id.textView5);

    }

    @Override
    public void onClick(View view) {

    }
    private void getCursos() {
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("getCursos");
        Call<ListaDeCursos> response = requestInterface.getCursos(request);

        response.enqueue(new Callback<ListaDeCursos>() {

            @Override
            public void onResponse(Call<ListaDeCursos> call, Response<ListaDeCursos> response) {
                //System.out.println(response.body());
                ListaDeCursos listaDeCursos = response.body();
                cursos = listaDeCursos.getCursos();
                String[] nomes = new String[cursos.size()];
                for (int i = 0; i < cursos.size(); i++) {
                    nomes [i] = cursos.get(i).getNome();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, nomes);
                // Assign adapter to ListView
                listView.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // ListView Clicked item index
                        //disciplinas.get(position).getNome();
                        // ListView Clicked item value
                        String itemValue = (String) listView.getItemAtPosition(position);
                        // Show Alert
                        Toast.makeText(getActivity(),
                                "Position: " + position + " ListItem: " + itemValue, Toast.LENGTH_SHORT)
                                .show();
                        //progressBar.setVisibility(View.VISIBLE);
                        //listView.setVisibility(View.GONE);
                        getAnos(cursos.get(position).getIdCurso());
                    }

                });
                //populateSpinner();
                //
                // System.out.println(resp.getCurso().getNome());
            }

            @Override
            public void onFailure(Call<ListaDeCursos> call, Throwable t) {


                // progress.setVisibility(View.INVISIBLE);
//                Log.d(Constants.TAG, t.getLocalizedMessage());
                //Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }
    private void getAnos(final String numCurso) {
        textView.setText(cursos.get(Integer.parseInt(numCurso)).getNome());
        final String[] anos = {"Ano 1","Ano 2","Ano 3"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, android.R.id.text1, anos);
        // Assign adapter to ListView
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ListView Clicked item index
                //disciplinas.get(position).getNome();
                // ListView Clicked item value
                String itemValue = (String) listView.getItemAtPosition(position);
                // Show Alert
                Toast.makeText(getActivity(),
                        "Position: " + position + " ListItem: " + itemValue, Toast.LENGTH_SHORT)
                        .show();
                int ano = position + 1;
                progressBar.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
                //textView.setText("Curso " + cursos.get(Integer.parseInt(numCurso)).getNome()+" --> "+anos[ano]);
                getDisciplinas(numCurso, ano + "");
            }

        });
    }

    private void getDisciplinas(final String numCurso, final String ano) {
        textView.setText(textView.getText()+" --> Ano "+ano);
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("getDisciplinas");
        request.setCurso(numCurso);
        request.setAno(ano);
        Call<ListaDeDisciplinas> response = requestInterface.getDisciplinas(request);

        response.enqueue(new Callback<ListaDeDisciplinas>() {

            @Override
            public void onResponse(Call<ListaDeDisciplinas> call, Response<ListaDeDisciplinas> response) {
                //System.out.println(response.body());
                ListaDeDisciplinas listaDeDisciplinas = response.body();
                disciplinas = listaDeDisciplinas.getDisciplinas();
                String[] nomes = new String[disciplinas.size()];
                for (int i = 0; i < disciplinas.size(); i++) {
                    nomes[i] = disciplinas.get(i).getNome();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_list_item_multiple_choice, android.R.id.text1, nomes);
                // Assign adapter to ListView
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // ListView Clicked item index
                        //disciplinas.get(position).getNome();
                        // ListView Clicked item value
                        String itemValue = (String) listView.getItemAtPosition(position);
                        // Show Alert
                        Toast.makeText(getActivity(),
                                "Position: " + position + " ListItem: " + itemValue, Toast.LENGTH_SHORT)
                                .show();
                        //getAnos(cursos.get(itemPosition).getIdCurso());
                        getTopicos(position+1);
                    }

                });
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                //populateSpinner();
                //
                // System.out.println(resp.getCurso().getNome());
            }

            @Override
            public void onFailure(Call<ListaDeDisciplinas> call, Throwable t) {


                // progress.setVisibility(View.INVISIBLE);
//                Log.d(Constants.TAG, t.getLocalizedMessage());
                //Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }
    private void getTopicos(final int topicCat) {

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("getTopicos");
        request.setTopicCat(topicCat);

        Call<ListaDeTopicos> response = requestInterface.getTopicos(request);

        response.enqueue(new Callback<ListaDeTopicos>() {

            @Override
            public void onResponse(Call<ListaDeTopicos> call, Response<ListaDeTopicos> response) {
                //System.out.println(response.body());
                ListaDeTopicos ListaDeTopicos = response.body();
                topicos = ListaDeTopicos.getTopicos();
                String[] disciplinas = new String[ topicos.size()];
                System.out.println(topicos.size());
                for (int i = 0; i <  topicos.size(); i++) {
                    disciplinas [i]=  topicos.get(i).getTopic_subject();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_list_item_multiple_choice, android.R.id.text1,disciplinas);
                // Assign adapter to ListView
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // ListView Clicked item index
                        //disciplinas.get(position).getNome();
                        // ListView Clicked item value
                        String itemValue = (String) listView.getItemAtPosition(position);
                        // Show Alert
                        Toast.makeText(getActivity(),
                                "Position: " + position + " ListItem: " + itemValue, Toast.LENGTH_SHORT)
                                .show();
                        //getAnos(cursos.get(itemPosition).getIdCurso());

                    }

                });
                progressBar.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
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
}
