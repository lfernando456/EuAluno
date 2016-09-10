package com.andreoid.EuAluno.models;

/**
 * Created by luisf on 25/04/2016.
 */
public class Topico {

        private String idtopics;
        private String topic_subject;
        private String topic_date;
        private String topic_cat;
        private String topic_by;
        private String nomeProfessor;
        private String nomeDisciplina;
        private String content;
        private String topic_replies_number;
        private String topics_view_number;
        private int viewed;

    public String getTopics_view_number() {
            return topics_view_number;
        }

        public void setTopics_view_number(String topics_view_number) {
            this.topics_view_number = topics_view_number;
        }

        public String getNomeProfessor() {
            return nomeProfessor;
        }
        public String getNomeDisciplina() {
            return nomeDisciplina;
        }
        public String getContent() {
            return content;
        }

        public String getTopic_replies_number() {
            return topic_replies_number;
        }

        public void setNomeProfessor(String nomeProfessor) {
            this.nomeProfessor = nomeProfessor;
        }

        public String getIdTopics() {
            return idtopics;
        }

        public void setIdTopics(String idTopics) {
            this.idtopics = idTopics;
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

      public int getTopic_viewed() {
            return viewed;
        }


}


