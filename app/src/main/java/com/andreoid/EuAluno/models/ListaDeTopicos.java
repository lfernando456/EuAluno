package com.andreoid.EuAluno.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luisf on 25/04/2016.
 */
public class ListaDeTopicos {
    private List<Topicos> topicos = new ArrayList<Topicos>();

    public List<Topicos> getTopicos() {
        return topicos;
    }
    public class Topicos {
        private String idTopics;
        private String topic_subject;
        private String topic_date;
        private String topic_cat;
        private String topic_by;

        public String getIdTopics() {
            return idTopics;
        }

        public void setIdTopics(String idTopics) {
            this.idTopics = idTopics;
        }

        public String getTopic_subject() {
            return topic_subject;
        }

        public void setTopic_subject(String topic_subject) {
            this.topic_subject = topic_subject;
        }

        public String getTopic_date() {
            return topic_date;
        }

        public void setTopic_date(String topic_date) {
            this.topic_date = topic_date;
        }

        public String getTopic_cat() {
            return topic_cat;
        }

        public void setTopic_cat(String topic_cat) {
            this.topic_cat = topic_cat;
        }

        public String getTopic_by() {
            return topic_by;
        }

        public void setTopic_by(String topic_by) {
            this.topic_by = topic_by;
        }
    }

}
