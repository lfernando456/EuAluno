package com.andreoid.EuAluno;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.andreoid.EuAluno.models.ServerRequest;
import com.andreoid.EuAluno.models.ServerResponse;
import com.andreoid.EuAluno.models.User;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.PicassoTools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements View.OnClickListener {

    private TextView tv_name,tv_email,tv_uid,tv_message;
    private SharedPreferences pref;
    private Button btn_change_password,btn_logout,btn_savePhoto,btn_cancel;
    private RelativeLayout saveRelativeLayout;
    private EditText et_old_password,et_new_password;
    private AlertDialog dialog;
    private ProgressBar progress;

    private ImageView ivImage;
    private RequestInterface requestInterface;
    private Uri uri;
    private String imagePath;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);
        initViews(view);
        super.onCreate(savedInstanceState);
        pref = getActivity().getSharedPreferences("EuAluno", Context.MODE_PRIVATE);
        ivImage = (ImageView)view.findViewById(R.id.userPhoto);
        requestInterface = RetroClient.getApiService(1);
        context = getContext();


        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent();

                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Seleciona uma imagem"), 1);

            }
        });
        setCurrentPhoto();
        return view;
    }

    private void setCurrentPhoto() {
        Picasso.with(getActivity())
                .load(Constants.BASE_URL+"TestePHP/FotosDePerfil/"+pref.getString(Constants.UNIQUE_ID, "")+".png")
                .placeholder(R.drawable.ic_no_user)
                .transform(new CircleTransform())
                .into(ivImage);

        Picasso.with(getActivity())
                .load(Constants.BASE_URL+"TestePHP/FotosDePerfil/"+pref.getString(Constants.UNIQUE_ID, "")+".png")
                .placeholder(R.drawable.ic_no_user)
                .transform(new CircleTransform())
                .into((ImageView) getActivity().findViewById(R.id.userPhoto2));
    }

    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data) {

        if (reqCode == 1 && resCode == -1 && data != null && data.getData() != null) {

            uri = data.getData();
            System.out.println(uri);
            //try {
                //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri);

                Picasso.with(getActivity()).load(uri).transform(new CircleTransform()).into(ivImage);
            saveRelativeLayout.setVisibility(View.VISIBLE);
                //ivImage.setImageBitmap(CircleTransform.transformStatic(bitmap));
            //} catch (IOException e) {
                //e.printStackTrace();
            //}
        }
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {


        tv_name.setText(pref.getString(Constants.NAME,""));
        tv_email.setText(pref.getString(Constants.EMAIL,""));
        tv_uid.setText(pref.getString(Constants.UNIQUE_ID, ""));




    }

    private void initViews(View view){

        tv_name = (TextView)view.findViewById(R.id.tv_name);
        tv_email = (TextView)view.findViewById(R.id.tv_email);
        tv_uid = (TextView)view.findViewById(R.id.tv_uid);
        saveRelativeLayout = (RelativeLayout)view.findViewById(R.id.savePhotoLayout);
        btn_change_password = (AppCompatButton)view.findViewById(R.id.btn_chg_password);
        btn_logout = (AppCompatButton)view.findViewById(R.id.btn_logout);
        btn_savePhoto = (Button) view.findViewById(R.id.savePhotoButton);
        btn_cancel = (Button) view.findViewById(R.id.cancelSavePhotoButton);
        btn_savePhoto.setOnClickListener(this);
        btn_cancel.setOnClickListener(this);
        btn_change_password.setOnClickListener(this);
        btn_logout.setOnClickListener(this);

    }

    private void showDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.dialog_change_password, null);
        et_old_password = (EditText)view.findViewById(R.id.et_old_password);
        et_new_password = (EditText)view.findViewById(R.id.et_new_password);
        tv_message = (TextView)view.findViewById(R.id.tv_message);
        progress = (ProgressBar)view.findViewById(R.id.progress);
        builder.setView(view);
        builder.setTitle("Alterar Senha");
        builder.setPositiveButton("Alterar Senha", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog = builder.create();
        dialog.show();
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String old_password = et_old_password.getText().toString();
                String new_password = et_new_password.getText().toString();
                if (!old_password.isEmpty() && !new_password.isEmpty()) {

                    progress.setVisibility(View.VISIBLE);
                    changePasswordProcess(pref.getString(Constants.EMAIL, ""), old_password, new_password);

                } else {

                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText("Campos vazios!");
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_chg_password:
                showDialog();
                break;
            case R.id.btn_logout:
                //getActivity();
                break;
            case R.id.savePhotoButton:
                try {
                    uploadPhoto(uri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.cancelSavePhotoButton:
                saveRelativeLayout.setVisibility(View.GONE);
                setCurrentPhoto();
                break;

        }
    }


    private void changePasswordProcess(String email,String old_password,String new_password){

        User user = new User();
        user.setEmail(email);
        user.setOld_password(old_password);
        user.setNew_password(new_password);
        ServerRequest request = new ServerRequest();
        request.setOperation(Constants.CHANGE_PASSWORD_OPERATION);
        request.setUser(user);
        Call<ServerResponse> response = requestInterface.operation(request);

        response.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, retrofit2.Response<ServerResponse> response) {

                ServerResponse resp = response.body();
                if(resp.getResult().equals(Constants.SUCCESS)){
                    progress.setVisibility(View.GONE);
                    tv_message.setVisibility(View.GONE);
                    dialog.dismiss();
                    Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                }else {
                    progress.setVisibility(View.GONE);
                    tv_message.setVisibility(View.VISIBLE);
                    tv_message.setText(resp.getMessage());

                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {

                Log.d(Constants.TAG,"failed");
                progress.setVisibility(View.GONE);
                tv_message.setVisibility(View.VISIBLE);
                tv_message.setText(t.getLocalizedMessage());


            }
        });
    }
    private void uploadPhoto(Uri imagePath) throws IOException {

        //if (imagePath!=null||imagePath!="") {

            final ProgressDialog progressDialog;
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Enviando imagem...");
            progressDialog.setCancelable(false);
            progressDialog.show();

            File file = new File(getActivity().getCacheDir(), pref.getString(Constants.UNIQUE_ID,""));
            file.createNewFile();

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imagePath);
            //bitmap.setHeight(50);
            //bitmap.setWidth(50);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 , bos);
            byte[] bitmapdata = bos.toByteArray();

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();

            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);

            // MultipartBody.Part is used to send also the actual file name
            MultipartBody.Part body =
                    MultipartBody.Part.createFormData("uploaded_profile_picture", pref.getString(Constants.UNIQUE_ID, "")+".png", requestFile);
            ServerRequest request = new ServerRequest();
            request.setOperation("uploadPhoto");

            Call<ServerResponse> resultCall = requestInterface.upload(body, request);

            resultCall.enqueue(new Callback<ServerResponse>() {
                @Override
                public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                    progressDialog.dismiss();
                    System.out.println(response.body().getResult());
                    if(response.isSuccessful()) {
                        if (response.body().getResult().equals("success")) {
                            Toast.makeText(getActivity(), "Upload feito com sucesso", Toast.LENGTH_LONG).show();
                            saveRelativeLayout.setVisibility(View.GONE);

                            PicassoTools.clearCache(Picasso.with(getActivity()));
                            setCurrentPhoto();

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
        //}
    }

}
