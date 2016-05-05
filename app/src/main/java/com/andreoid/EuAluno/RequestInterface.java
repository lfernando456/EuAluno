package com.andreoid.EuAluno;


import com.andreoid.EuAluno.models.ListaDeCursos;
import com.andreoid.EuAluno.models.ListaDeDisciplinas;
import com.andreoid.EuAluno.models.ListaDeTopicos;
import com.andreoid.EuAluno.models.ListaDeTurmas;
import com.andreoid.EuAluno.models.ServerRequest;
import com.andreoid.EuAluno.models.ServerResponse;


import retrofit2.Call;

import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RequestInterface {

    @POST("TestePHP/")
    Call<ServerResponse> operation(@Body ServerRequest request);
    @POST("TestePHP/")
    Call<ListaDeCursos> getCursos(@Body ServerRequest request);
    @POST("TestePHP/")
    Call<ListaDeTurmas> getTurmas(@Body ServerRequest request);
    @POST("TestePHP/")
    Call<ListaDeDisciplinas> getDisciplinas(@Body ServerRequest request);
    @POST("TestePHP/")
    Call<ListaDeTopicos> getTopicos(@Body ServerRequest request);
   // @POST("TestePHP/")
   // Call<ServerResponse> insertAlunoDisciplina(@Body ServerRequest request);
    @POST("TestePHP/")
    Call<ServerResponse> verificadorAD(@Body ServerRequest request);
}
