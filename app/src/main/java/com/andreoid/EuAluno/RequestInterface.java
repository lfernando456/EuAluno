package com.andreoid.EuAluno;


import com.andreoid.EuAluno.models.ServerRequest;
import com.andreoid.EuAluno.models.ServerResponse;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface RequestInterface {

    @POST("TestePHP/")
    Call<ServerResponse> operation(@Body ServerRequest request);
    @Multipart
    @POST("TestePHP/")
    Call<ServerResponse> upload(@Part MultipartBody.Part file, @Part("request") ServerRequest request);
    @GET("TestePHP/Anexos/{filename}")
    @Streaming
    Call<ResponseBody> download(@Path("filename") String filename);
}
