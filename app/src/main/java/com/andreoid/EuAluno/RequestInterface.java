package com.andreoid.EuAluno;


import com.andreoid.EuAluno.models.ServerRequest;
import com.andreoid.EuAluno.models.ServerResponse;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RequestInterface {

    @POST("TestePHP/")
    Call<ServerResponse> operation(@Body ServerRequest request);
    @Multipart
    @POST("TestePHP/")
    Call<ServerResponse> upload(@Part MultipartBody.Part file, @Part("request") ServerRequest request);
}
