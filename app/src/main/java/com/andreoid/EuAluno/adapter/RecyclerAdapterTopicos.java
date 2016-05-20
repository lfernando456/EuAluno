package com.andreoid.EuAluno.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andreoid.EuAluno.Constants;
import com.andreoid.EuAluno.R;
import com.andreoid.EuAluno.RequestInterface;
import com.andreoid.EuAluno.TopicosFragment;
import com.andreoid.EuAluno.models.CardItemTopicoModel;
import com.andreoid.EuAluno.models.ServerRequest;
import com.andreoid.EuAluno.models.ServerResponse;

import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RecyclerAdapterTopicos extends RecyclerView.Adapter<RecyclerAdapterTopicos.ViewHolder> {

    public List<CardItemTopicoModel> cardItems;
    private AdapterCallback mAdapterCallback;
    private Context cntxt;

    public RecyclerAdapterTopicos(List<CardItemTopicoModel> cardItems, Context context){
        this.cardItems = cardItems;
        cntxt = context;
        try {
            this.mAdapterCallback = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("AdapterCallback não implementado.");
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        TextView professor;
        TextView disciplina;
        private SharedPreferences pref;
        TextView views;
        TextView replies_number;
        Retrofit retrofit;
        public ViewHolder(final View itemView) {
            super(itemView);

            this.title = (TextView)itemView.findViewById(R.id.card_title);
            this.content = (TextView)itemView.findViewById(R.id.card_content);
            this.professor = (TextView)itemView.findViewById(R.id.card_professor);
            this.disciplina = (TextView)itemView.findViewById(R.id.card_disciplina);
            this.views = (TextView)itemView.findViewById(R.id.card_views);
            this.replies_number = (TextView)itemView.findViewById(R.id.card_replies_number);
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();
            retrofit= new Retrofit.Builder()
                    .baseUrl(Constants.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            pref = (cntxt.getSharedPreferences("EuAluno", Context.MODE_PRIVATE));
            itemView.findViewById(R.id.relativeLayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAdapterCallback.onMethodCallback(cardItems.get(getAdapterPosition()).idTopico, cardItems.get(getAdapterPosition()).title);

                    RequestInterface requestInterface = retrofit.create(RequestInterface.class);
                    ServerRequest request = new ServerRequest();
                    request.setOperation("insertView");
                    request.setTopic_cat(cardItems.get(getAdapterPosition()).idTopico);
                    request.setUnique_id(pref.getString(Constants.UNIQUE_ID,""));
                    Call<ServerResponse> response = requestInterface.operation(request);
                    response.enqueue(new Callback<ServerResponse>() {

                        @Override
                        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                            System.out.println(response.body());
                            ServerResponse resp = response.body();
//                Snackbar.make(getView(), resp.getMessage(), Snackbar.LENGTH_LONG).show();

                        }

                        @Override
                        public void onFailure(Call<ServerResponse> call, Throwable t) {

                            System.out.println(call.request().body());

                            Log.d(Constants.TAG, t.getMessage());
                            //             Snackbar.make(getView(), t.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();


                        }
                    });

                }
            });
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.title.setText(cardItems.get(position).title);
        holder.content.setText(cardItems.get(position).content);
        holder.professor.setText(cardItems.get(position).professor);
        holder.disciplina.setText(cardItems.get(position).disciplina);
        holder.views.setText(cardItems.get(position).views);
        holder.replies_number.setText(cardItems.get(position).replies_number);
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CLICADOOO\n"+position+"\n"+holder.author.getText());
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }
    public static interface AdapterCallback {
        void onMethodCallback(String idTopico, String title);
    }
}
