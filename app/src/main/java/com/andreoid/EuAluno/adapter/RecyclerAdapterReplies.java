package com.andreoid.EuAluno.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andreoid.EuAluno.AdapterCallback;
import com.andreoid.EuAluno.CircleTransform;
import com.andreoid.EuAluno.Constants;
import com.andreoid.EuAluno.R;
import com.andreoid.EuAluno.models.CardItemReplyModel;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecyclerAdapterReplies extends RecyclerView.Adapter<RecyclerAdapterReplies.ViewHolder> {

    public List<CardItemReplyModel> cardItems;
    private AdapterCallback mAdapterCallback;
    private Context mcontext;
    public RecyclerAdapterReplies(List<CardItemReplyModel> cardItems, Context context){
        this.cardItems = cardItems;
        this.mcontext = context;
        try {
            this.mAdapterCallback = ((AdapterCallback) mcontext);
        } catch (ClassCastException e) {
            throw new ClassCastException("AdapterCallback não implementado.");
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView reply_content;
        TextView data_reply;
        TextView nomeAnexo;
        RelativeLayout layDownload;
        ImageView userPhoto;

        public ViewHolder(final View itemView) {
            super(itemView);

            this.author = (TextView)itemView.findViewById(R.id.card_author);
            this.reply_content = (TextView)itemView.findViewById(R.id.card_reply_content);
            this.data_reply = (TextView)itemView.findViewById(R.id.card_data_reply);
            this.layDownload = (RelativeLayout) itemView.findViewById(R.id.layDownload);
            this.userPhoto = (ImageView) itemView.findViewById(R.id.userPhoto);
            this.nomeAnexo = (TextView) itemView.findViewById(R.id.nomeAnexo);
            itemView.findViewById(R.id.bt_anexo).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    System.out.println(cardItems.get(getAdapterPosition()).anexo);
                     mAdapterCallback.onMethodCallbackReply(cardItems.get(getAdapterPosition()).anexo);
                }
            });

        }


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reply_item,parent,false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        System.out.println(position);
        if(cardItems.get(position).anexo!=null){
            holder.layDownload.setVisibility(View.VISIBLE);
            holder.nomeAnexo.setText(cardItems.get(position).anexo);
        }else holder.layDownload.setVisibility(View.GONE);
        holder.author.setText(cardItems.get(position).author);
        holder.reply_content.setText(cardItems.get(position).reply_content);
        holder.data_reply.setText(cardItems.get(position).data_reply);


        Picasso.with(mcontext)
                .load(Constants.BASE_URL+"TestePHP/FotosDePerfil/"+cardItems.get(position).unique_id+".png")
                        //.load("https://lh3.googleusercontent.com/-CopaXw6seSA/AAAAAAAAAAI/AAAAAAAAAAA/ADhl2ypN6037ye-uMPrcOGvePLklwoWz5Q/s96-c-mo/photo.jpg")

                .placeholder(R.drawable.ic_no_user)
                .transform(new CircleTransform())

                .into(holder.userPhoto);

    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }

}
