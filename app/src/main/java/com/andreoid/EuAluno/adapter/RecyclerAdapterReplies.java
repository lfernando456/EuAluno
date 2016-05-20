package com.andreoid.EuAluno.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.andreoid.EuAluno.R;
import com.andreoid.EuAluno.models.CardItemReplyModel;
import com.andreoid.EuAluno.models.CardItemTopicoModel;

import java.util.List;


public class RecyclerAdapterReplies extends RecyclerView.Adapter<RecyclerAdapterReplies.ViewHolder> {

    public List<CardItemReplyModel> cardItems;
    private AdapterCallback mAdapterCallback;

    public RecyclerAdapterReplies(List<CardItemReplyModel> cardItems, Context context){
        this.cardItems = cardItems;


    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView author;
        TextView reply_content;
        TextView data_reply;
        RelativeLayout layDownload;
        public ViewHolder(final View itemView) {
            super(itemView);

            this.author = (TextView)itemView.findViewById(R.id.card_author);
            this.reply_content = (TextView)itemView.findViewById(R.id.card_reply_content);
            this.data_reply = (TextView)itemView.findViewById(R.id.card_data_reply);
            this.layDownload = (RelativeLayout) itemView.findViewById(R.id.layDownload);
            itemView.findViewById(R.id.card_author).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // mAdapterCallback.onMethodCallback(cardItems.get(getAdapterPosition()).idReply);
                    //System.out.println(cardItems.get(getAdapterPosition()).author);

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
        if(position==0){
            holder.layDownload.setVisibility(View.VISIBLE);
        }else holder.layDownload.setVisibility(View.GONE);
        holder.author.setText(cardItems.get(position).author);
        holder.reply_content.setText(cardItems.get(position).reply_content);
        holder.data_reply.setText(cardItems.get(position).data_reply);

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
        void onMethodCallback(String idTopico);
    }
}
