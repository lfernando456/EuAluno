package com.andreoid.EuAluno;

/**
 * Created by André on 20/05/2016.
 */
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;



public class RetroClient {


    public RetroClient() {

    }


    private static Retrofit getRetroClient(int debug) {
        OkHttpClient client;
        //if(debug==1) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
        //}else {
           // client = new OkHttpClient.Builder().build();
       // }

        return new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

    }

    public static RequestInterface getApiService(int debug) {
        return getRetroClient(debug).create(RequestInterface.class);
    }
}