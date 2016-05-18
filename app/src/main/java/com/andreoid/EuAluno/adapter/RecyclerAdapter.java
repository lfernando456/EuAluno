package com.andreoid.EuAluno.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.andreoid.EuAluno.Constants;
import com.andreoid.EuAluno.FabFragment;
import com.andreoid.EuAluno.ProfileActivity;
import com.andreoid.EuAluno.R;

import com.andreoid.EuAluno.TopicosFragment;
import com.andreoid.EuAluno.models.CardItemModel;

import java.util.List;


public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context mContext;
    public List<CardItemModel> cardItems;

    public RecyclerAdapter(List<CardItemModel> cardItems,Context context){
        this.cardItems = cardItems;
        this.mContext=context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;
        TextView content;
        TextView professor;
        TextView disciplina;
        TextView views;
        public ViewHolder(View itemView) {
            super(itemView);
            this.title = (TextView)itemView.findViewById(R.id.card_title);
            this.content = (TextView)itemView.findViewById(R.id.card_content);
            this.professor = (TextView)itemView.findViewById(R.id.card_professor);
            this.disciplina = (TextView)itemView.findViewById(R.id.card_disciplina);
            this.views = (TextView)itemView.findViewById(R.id.card_views);
            itemView.findViewById(R.id.relativeLayout).setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            Fragment mFragment = FabFragment.newInstance("0","-1");


            FragmentTransaction ft = ((ProfileActivity)mContext).getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, mFragment);
            ft.commit();
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
        /*holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("CLICADOOO\n"+position+"\n"+holder.title.getText());
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return cardItems.size();
    }
}
