package com.andreoid.EuAluno;

import com.andreoid.EuAluno.models.ServerRequest;
import com.andreoid.EuAluno.models.ServerResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("TestePHP/")
    Call<ServerResponse> operation(@Body ServerRequest request);

}
