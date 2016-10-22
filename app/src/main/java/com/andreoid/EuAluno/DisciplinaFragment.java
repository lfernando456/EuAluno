package com.andreoid.EuAluno;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.andreoid.EuAluno.adapter.DisciplinasListAdapter;
import com.andreoid.EuAluno.models.Disciplina;
import com.andreoid.EuAluno.models.Topico;
import com.andreoid.EuAluno.models.ServerRequest;
import com.andreoid.EuAluno.models.ServerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisciplinaFragment extends Fragment{
    private List<Disciplina> disciplinas;

    ListView listView ;
    ProgressBar progressBar;
    TextView textView;
    private SharedPreferences pref;

   private View view;
    String[] nomes;
    String[] nomesTurmas;
    String unique_id;
    private RequestInterface requestInterface;
    private DisciplinasListAdapter adapter;


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
        requestInterface = RetroClient.getApiService(1);

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
        textView = (TextView) view.findViewById(R.id.textView3);
        //btn_concluir = (Button) view.findViewById(R.id.bSalvar);


    }

    private void getDisciplinasAP(final String unique_id) {

        loading(true);
        ServerRequest request = new ServerRequest();
        request.setOperation("getDisciplinasAP");
        request.setUnique_id(unique_id);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                //System.out.println(response.body());

                //btn_concluir.setVisibility(View.VISIBLE);

                disciplinas = response.body().getListaDeDisciplinas();
            if(disciplinas != null) {
                nomes = new String[disciplinas.size()];
                nomesTurmas = new String[disciplinas.size()];
                for (int i = 0; i < disciplinas.size(); i++) {
                    nomesTurmas[i] = disciplinas.get(i).getNomeTurma();
                    if (pref.getString("tipo", "").equals("1")) {


                        nomes[i] = disciplinas.get(i).getNome() + " -> " + nomesTurmas[i];
                    } else {
                        nomes[i] = disciplinas.get(i).getNome();
                    }
                }

                adapter = new DisciplinasListAdapter(getActivity(), disciplinas);
                listView.setAdapter(adapter);

            }
                // Assign adapter to ListView

                //asdasdasdasdasd
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // ListView Clicked item index
                        //disciplinas.get(position).getNome();
                        // ListView Clicked item value

                        // Show Alert


                        Fragment mFragment = TopicosFragment.newInstance(pref.getString(Constants.TIPO, ""), disciplinas.get(position).getIdDisciplina());

                        FragmentTransaction ft = getFragmentManager().beginTransaction();
                        ft.replace(R.id.container, mFragment).addToBackStack(getActivity().getTitle().toString()).commit();

                        //getTopicos(disciplinas.get(position).getIdDisciplina());


                    }

                });
                if(response.message().equals("Cant get disciplinas"))textView.setVisibility(View.VISIBLE);

                loading(false);
                //populateSpinner();
                //
                // System.out.println(resp.getCurso().getNome());
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

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
