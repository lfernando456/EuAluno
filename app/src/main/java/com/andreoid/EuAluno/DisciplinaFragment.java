package com.andreoid.EuAluno;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.andreoid.EuAluno.models.ListaDeCursos;
import com.andreoid.EuAluno.models.ListaDeDisciplinas;
import com.andreoid.EuAluno.models.ListaDeTopicos;
import com.andreoid.EuAluno.models.ListaDeTurmas;
import com.andreoid.EuAluno.models.ServerRequest;
import com.andreoid.EuAluno.models.ServerResponse;
import com.andreoid.EuAluno.models.User;

import java.util.ArrayList;
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
    private List<ListaDeCursos.Curso> cursos;
    private List<ListaDeTurmas.Turma> turmas;

    Button btn_concluir;
    Button btn_voltar;
    Retrofit retrofit;
    ListView listView ;
    ProgressBar progressBar;
    private SharedPreferences pref;
    TextView textView;

   private View view;
    String[] nomes;
    String[] idCurso;
    String[] idTurma;
    String auxTurma;
    String unique_id;

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
        verificadorAD(unique_id);




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
        //btn_concluir = (Button) view.findViewById(R.id.bSalvar);
        btn_voltar = (Button) view.findViewById(R.id.bVoltar);
        btn_concluir = (Button) view.findViewById(R.id.bSalvar);
        btn_concluir.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {


            List<ListaDeDisciplinas.Disciplina> selectedItems =new ArrayList<>();

            SparseBooleanArray checkedPositions = listView.getCheckedItemPositions();
            for (int i = 0; i < listView.getCount(); i++) {
                if (checkedPositions.get(i)) {
                    selectedItems.add(disciplinas.get(i));
                }
            }

            insertAlunoDisciplina(selectedItems);
            }
        });

    }
    private void getCursos() {

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());
        alertDialogBuilder.setMessage("Você ainda não cadastrou suas disciplinas, por favor selecione-as a seguir");
        alertDialogBuilder.setPositiveButton("Continuar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        request.setOperation("getCursos");
        Call<ListaDeCursos> response = requestInterface.getCursos(request);

        response.enqueue(new Callback<ListaDeCursos>() {

            @Override
            public void onResponse(Call<ListaDeCursos> call, Response<ListaDeCursos> response) {
                //System.out.println(response.body());
                ListaDeCursos listaDeCursos = response.body();
                cursos = listaDeCursos.getCursos();
                nomes = new String[cursos.size()];
                idCurso = new String[cursos.size()];
                for (int i = 0; i < cursos.size(); i++) {
                    nomes[i] = cursos.get(i).getNome();
                    idCurso[i] = cursos.get(i).getIdCurso();
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
                        String itemValue = (String) listView.getItemAtPosition(position);
                        Toast.makeText(getActivity(), "Position: " + position + " ListItem: " + itemValue, Toast.LENGTH_SHORT).show();

                        getTurmas(cursos.get(position).getIdCurso());
                    }

                });

            }

            @Override
            public void onFailure(Call<ListaDeCursos> call, Throwable t) {


                // progress.setVisibility(View.INVISIBLE);
//                Log.d(Constants.TAG, t.getLocalizedMessage());
                //Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });

    }
    private void getTurmas(final String idCurso) {
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();

        btn_voltar.setVisibility(View.VISIBLE);
        request.setOperation("getTurmas");
        request.setIdCurso(idCurso);
        Call<ListaDeTurmas> response = requestInterface.getTurmas(request);
        response.enqueue(new Callback<ListaDeTurmas>() {

            @Override
            public void onResponse(Call<ListaDeTurmas> call, Response<ListaDeTurmas> response) {
                System.out.println(response.body());
                ListaDeTurmas listaDeTurmas = response.body();
                turmas = listaDeTurmas.getTurmas();
                nomes = new String[turmas.size()];
                idTurma = new String[turmas.size()];
                for (int i = 0; i < turmas.size(); i++) {
                    nomes[i] = turmas.get(i).getNome();

                    idTurma[i] = turmas.get(i).getIdTurma();
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
                        String itemValue = (String) listView.getItemAtPosition(position);
                        Toast.makeText(getActivity(), "Position: " + position + " ListItem: " + itemValue, Toast.LENGTH_SHORT).show();
                        for (int i = 0; i < nomes.length; i++) {
                            if( nomes[i].equals(itemValue)){
                                auxTurma = idTurma[i];

                                textView.setText(textView.getText() + "Turma: " + nomes[i]);
                            }
                        }
                        System.out.println("Curso: " + idCurso + " Turma: " + auxTurma);
                        getDisciplinas(auxTurma);
                    }

                });

            }

            @Override
            public void onFailure(Call<ListaDeTurmas> call, Throwable t) {


                // progress.setVisibility(View.INVISIBLE);
//                Log.d(Constants.TAG, t.getLocalizedMessage());
                //Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
        btn_voltar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                textView.setText("");
                getCursos();


            }
        });
    }
    private void getDisciplinas(final String turma) {
        btn_concluir.setVisibility(View.VISIBLE);


        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("getDisciplinas");
        request.setTurma(turma);
        Call<ListaDeDisciplinas> response = requestInterface.getDisciplinas(request);

        response.enqueue(new Callback<ListaDeDisciplinas>() {

            @Override
            public void onResponse(Call<ListaDeDisciplinas> call, Response<ListaDeDisciplinas> response) {
                //System.out.println(response.body());
                ListaDeDisciplinas listaDeDisciplinas = response.body();
                disciplinas = listaDeDisciplinas.getDisciplinas();
                nomes = new String[disciplinas.size()];
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
                        //getTurmas(cursos.get(itemPosition).getIdCurso());

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


        btn_voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getCursos();


            }
        });
    }
    private void insertAlunoDisciplina (List<ListaDeDisciplinas.Disciplina> selectedItems) {


        RequestInterface requestInterface = retrofit.create(RequestInterface.class);



        ListaDeDisciplinas listaDeDisciplinas=new ListaDeDisciplinas();
        listaDeDisciplinas.setDisciplinas(selectedItems);
        ServerRequest request = new ServerRequest();
        request.setOperation("insertAlunoDisciplina");
        request.setUnique_id(unique_id);
        request.setListaDeDisciplinas(listaDeDisciplinas);

        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                System.out.println(response.body());
                ServerResponse resp = response.body();

                Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                System.out.println(call.request().body());
                progressBar.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG, t.getMessage());
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();


            }
        });
    }
    private void verificadorAD(final String unique_id ){

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("verificadorAD");
        request.setUnique_id(unique_id);


        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                System.out.println(response.body());
                ServerResponse resp = response.body();

                if(!resp.isAux())
                {
                    getCursos();
                }

                Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();
                progressBar.setVisibility(View.INVISIBLE);

            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                System.out.println(call.request().body());
                progressBar.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG, t.getMessage());
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();


            }
        });

    }
    {
    /** private void getTopicos (final int topicCat) {

        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        ServerRequest request = new ServerRequest();
        request.setOperation("getTopicos");
        request.setTopicCat(topicCat+"");

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
                        //getTurmas(cursos.get(itemPosition).getIdCurso());

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

    }**/
    }

}
