package com.andreoid.EuAluno.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.andreoid.EuAluno.AdapterCallback;
import com.andreoid.EuAluno.R;
import com.andreoid.EuAluno.models.CardItemTopicoModel;

import java.util.List;


public class RecyclerAdapterTopicos extends RecyclerView.Adapter<RecyclerAdapterTopicos.ViewHolder> {

    public List<CardItemTopicoModel> cardItems;
    private AdapterCallback mAdapterCallbackTopico;

    public RecyclerAdapterTopicos(List<CardItemTopicoModel> cardItems, Context context){
        this.cardItems = cardItems;


        try {
            this.mAdapterCallbackTopico = ((AdapterCallback) context);
        } catch (ClassCastException e) {
            throw new ClassCastException("AdapterCallback não implementado.");
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        TextView content;
        TextView professor;
        TextView disciplina;
        TextView views;
        TextView replies_number;
        TextView tv_novo;
        public ViewHolder(final View itemView) {
            super(itemView);

            this.title = (TextView)itemView.findViewById(R.id.card_title);
            this.content = (TextView)itemView.findViewById(R.id.card_content);
            this.professor = (TextView)itemView.findViewById(R.id.card_professor);
            this.disciplina = (TextView)itemView.findViewById(R.id.card_disciplina);
            this.views = (TextView)itemView.findViewById(R.id.card_views);
            this.replies_number = (TextView)itemView.findViewById(R.id.card_replies_number);
            this.tv_novo = (TextView)itemView.findViewById(R.id.tv_novo);
            itemView.findViewById(R.id.relativeLayout).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mAdapterCallbackTopico.onMethodCallbackTopico(cardItems.get(getAdapterPosition()).idTopico, cardItems.get(getAdapterPosition()).title);

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
        if(cardItems.get(position).viewed==0)holder.tv_novo.setVisibility(View.VISIBLE);
        else holder.tv_novo.setVisibility(View.GONE);
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

}