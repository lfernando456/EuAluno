package com.andreoid.EuAluno.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreoid.EuAluno.CircleTransform;
import com.andreoid.EuAluno.Constants;
import com.andreoid.EuAluno.R;
import com.andreoid.EuAluno.models.Disciplina;
import com.andreoid.EuAluno.models.User;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by André on 22/05/2016.
 */
public class UserViewedListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<User> users;

    public UserViewedListAdapter(Activity activity, List<User> users) {
        this.activity = activity;
        this.users = users;
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public User getItem(int location) {
        return users.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.user_viewed, null);

        //convertView.se
        TextView tv_nome = (TextView) convertView.findViewById(R.id.tv_nome);
        ImageView iv_user = (ImageView) convertView.findViewById(R.id.iv_user);


        User m = users.get(position);

        tv_nome.setText(m.getName());
        Picasso.with(convertView.getContext())
                .load(Constants.BASE_URL+"TestePHP/FotosDePerfil/"+m.getUnique_id()+".png")
                .placeholder(R.drawable.ic_no_user)
                .transform(new CircleTransform())
                .into(iv_user);


        return convertView;
    }
}
