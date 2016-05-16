package com.andreoid.EuAluno;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

public class DisciplinaFragment extends Fragment{
    private List<ListaDeDisciplinas.Disciplina> disciplinas;
    private List<ListaDeTopicos.Topicos> topicos;

    Retrofit retrofit;
    ListView listView ;
    ProgressBar progressBar;
    private SharedPreferences pref;

   private View view;
    String[] nomes;
    String[] nomesTurmas;
    String unique_id;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        /*switch (item.getItemId()) {
            case R.id.voltar:

                textView.setText("");

                btn_concluir.setVisibility(View.GONE);
                textView.setVisibility(View.GONE);
                relativeLay.setVisibility(View.GONE);
                break;
        }*/
        return super.onOptionsItemSelected(item);
    }
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

        view = inflater.inflate(R.layout.fragment_disciplina, container, false);
        initViews(view);
        unique_id = pref.getString(Constants.UNIQUE_ID, "");


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        pref = getActivity().getSharedPreferences("EuAluno", Context.MODE_PRIVATE);
        getDisciplinasAP(unique_id);
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
        //btn_concluir = (Button) view.findViewById(R.id.bSalvar);


    }

    private void getDisciplinasAP(final String unique_id) {

        loading(true);
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("getDisciplinasAP");
        request.setUnique_id(unique_id);
        Call<ListaDeDisciplinas> response = requestInterface.getDisciplinas(request);

        response.enqueue(new Callback<ListaDeDisciplinas>() {

            @Override
            public void onResponse(Call<ListaDeDisciplinas> call, Response<ListaDeDisciplinas> response) {
                //System.out.println(response.body());
                //btn_concluir.setVisibility(View.VISIBLE);
                ListaDeDisciplinas listaDeDisciplinas = response.body();
                disciplinas = listaDeDisciplinas.getDisciplinas();
                nomes = new String[disciplinas.size()];
                nomesTurmas =new String[disciplinas.size()];
                for (int i = 0; i < disciplinas.size(); i++) {
                    nomesTurmas [i] = disciplinas.get(i).getNomeTurma();
                    if( pref.getString("tipo","").equals("1")) {


                        nomes[i] = disciplinas.get(i).getNome() + " -> " + nomesTurmas[i];
                    }else{
                        nomes[i] = disciplinas.get(i).getNome(); 
                    }
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_list_item_1, android.R.id.text1, nomes);
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
                        getActivity().setTitle(itemValue);
                        getTopicos(disciplinas.get(position).getIdDisciplina());
                        //getTurmas(cursos.get(itemPosition).getIdCurso());

                    }

                });


                loading(false);
                //populateSpinner();
                //
                // System.out.println(resp.getCurso().getNome());
            }

            @Override
            public void onFailure(Call<ListaDeDisciplinas> call, Throwable t) {

                loading(false);
                // progress.setVisibility(View.INVISIBLE);
//                Log.d(Constants.TAG, t.getLocalizedMessage());
                //Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });


    }


    private void getTopicos (final String topic_cat) {
        loading(true);
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("getTopicos");
        request.setTopic_cat(topic_cat);

        Call<ListaDeTopicos> response = requestInterface.getTopicos(request);

        response.enqueue(new Callback<ListaDeTopicos>() {

            @Override
            public void onResponse(Call<ListaDeTopicos> call, Response<ListaDeTopicos> response) {
                //System.out.println(response.body());
                ListaDeTopicos ListaDeTopicos = response.body();
                topicos = ListaDeTopicos.getTopicos();
                String[] nomeTopicos = new String[ topicos.size()];
                System.out.println(topicos.size());
                for (int i = 0; i <  topicos.size(); i++) {
                    nomeTopicos [i]=  topicos.get(i).getTopic_subject();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_list_item_1, android.R.id.text1,nomeTopicos);
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
                        //getTurmas(cursos.get(itemPosition).getIdCurso());

                    }

                });
                loading(false);

                //populateSpinner();
                //
                // System.out.println(resp.getCurso().getNome());
            }

            @Override
            public void onFailure(Call<ListaDeTopicos> call, Throwable t) {

                loading(false);
                // progress.setVisibility(View.INVISIBLE);
//                Log.d(Constants.TAG, t.getLocalizedMessage());
                //Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }
    private void loading(boolean isLoading){
        if(isLoading){
            progressBar.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            listView.setVisibility(View.VISIBLE);
        }
    }
}
