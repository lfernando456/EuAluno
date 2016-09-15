package com.andreoid.EuAluno;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import com.andreoid.EuAluno.models.ServerRequest;
import com.andreoid.EuAluno.models.ServerResponse;
import com.andreoid.EuAluno.models.User;

import retrofit2.Call;
import retrofit2.Callback;

public class RegisterFragment extends Fragment  implements View.OnClickListener{

    private AppCompatButton btn_register;
    private EditText et_email,et_password,et_name;
    private RadioButton radioAluno,radioProf;
    private TextView tv_login;
    private ProgressBar progress;
    private String tipoUsuario="0";
    private RequestInterface requestInterface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_register,container,false);
        initViews(view);
        requestInterface = RetroClient.getApiService(1);

        return view;
    }

    private void initViews(View view){

        btn_register = (AppCompatButton)view.findViewById(R.id.btn_register);
        tv_login = (TextView)view.findViewById(R.id.tv_login);
        et_name = (EditText)view.findViewById(R.id.et_name);
        et_email = (EditText)view.findViewById(R.id.et_email);
        et_password = (EditText)view.findViewById(R.id.et_password);
        radioAluno = (RadioButton)view.findViewById(R.id.radioAluno);
        radioAluno.setChecked(true);
        radioProf = (RadioButton)view.findViewById(R.id.radioProf);

        progress = (ProgressBar)view.findViewById(R.id.progress);

        btn_register.setOnClickListener(this);
        tv_login.setOnClickListener(this);
        radioAluno.setOnClickListener(this);
        radioProf.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()){

            case R.id.radioAluno:
                if (radioAluno.isChecked())
                    tipoUsuario="0";
                break;

            case R.id.radioProf:
                if (radioProf.isChecked())
                    tipoUsuario="1";
                break;

            case R.id.tv_login:
                goToLogin();
                break;

            case R.id.btn_register:

                String name = et_name.getText().toString();
                String email = et_email.getText().toString();
                String password = et_password.getText().toString();


                if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty()) {

                    progress.setVisibility(View.VISIBLE);
                    registerProcess(name,email,password,tipoUsuario);

                } else {

                    Snackbar.make(getView(), "Fields are empty !", Snackbar.LENGTH_LONG).show();
                }
                break;

        }


    }

    private void registerProcess(String name, String email,String password,String tipoUsuario){

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setTipo(tipoUsuario);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.REGISTER_OPERATION);
        request.setUser(user);
        System.out.println(tipoUsuario);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {
                System.out.println(response.body());
                ServerResponse resp = response.body();

                Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();
                progress.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                System.out.println(call.request().body());
                progress.setVisibility(View.INVISIBLE);
                Log.d(Constants.TAG,t.getMessage());
                Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();


            }
        });
    }

    private void goToLogin(){

        Fragment login = new LoginFragment();
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.fragment_frame,login);
        ft.commit();
    }
    public void onRadioButtonClicked(View view) {
        // Is the button now checked?

    }
}
