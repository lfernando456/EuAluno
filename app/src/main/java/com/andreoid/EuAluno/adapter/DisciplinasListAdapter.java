package com.andreoid.EuAluno.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.andreoid.EuAluno.R;
import com.andreoid.EuAluno.models.Disciplina;

import java.util.List;

/**
 * Created by André on 22/05/2016.
 */
public class DisciplinasListAdapter extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Disciplina> disciplinas;

    public DisciplinasListAdapter(Activity activity, List<Disciplina> disciplinas) {
        this.activity = activity;
        this.disciplinas = disciplinas;
    }

    @Override
    public int getCount() {
        return disciplinas.size();
    }

    @Override
    public Disciplina getItem(int location) {
        return disciplinas.get(location);
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
            convertView = inflater.inflate(R.layout.disciplina_list_item, null);

        //convertView.se
        TextView tv_disciplina = (TextView) convertView.findViewById(R.id.tv_disciplina);
        TextView tv_turma = (TextView) convertView.findViewById(R.id.tv_turma);
        TextView tv_professor = (TextView) convertView.findViewById(R.id.tv_professor);
        TextView tv_qtdtopicos = (TextView) convertView.findViewById(R.id.tv_qtdtopicos);

        Disciplina m = disciplinas.get(position);

        tv_disciplina.setText(m.getNome());
        tv_turma.setText(m.getNomeTurma());
        tv_professor.setText(m.getNomeProfessor());
        tv_qtdtopicos.setText("Qtd. Tópicos: " + m.getQtdTopicos());

        return convertView;
    }
}
