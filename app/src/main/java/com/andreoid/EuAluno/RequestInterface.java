package com.andreoid.EuAluno;

import com.andreoid.EuAluno.models.Curso;
import com.andreoid.EuAluno.models.ListaDeCursos;
import com.andreoid.EuAluno.models.ServerRequest;
import com.andreoid.EuAluno.models.ServerResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("TestePHP/")
    Call<ServerResponse> operation(@Body ServerRequest request);
    @POST("TestePHP/")
    Call<ListaDeCursos> getCursos(@Body ServerRequest request);


}
