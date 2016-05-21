package com.andreoid.EuAluno.models;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luisf on 17/05/2016.
 */
public class ListaDeReplies {
    private List<Replies> replies = new ArrayList<Replies>();

    public List<Replies> getReplies() {
        return replies;
    }
    public class Replies {
        private String idreplies;
        private String reply_content;
        private  String reply_date;
        private String reply_topic;
        private String reply_by;
        private String autorComentario;
        private String unique_id;
        private String anexo;
        public String getAutorComentario() {
            return autorComentario;
        }

        public void setAutorComentario(String autorComentario) {
            this.autorComentario = autorComentario;
        }

        public String getIdreplies() {
            return idreplies;
        }

        public void setIdreplies(String idreplies) {
            this.idreplies = idreplies;
        }

        public String getReply_content() {
            return reply_content;
        }

        public void setReply_content(String reply_content) {
            this.reply_content = reply_content;
        }

        public String getReply_date() {
            return reply_date;
        }

        public void setReply_date(String reply_date) {
            this.reply_date = reply_date;
        }

        public String getReply_topic() {
            return reply_topic;
        }

        public void setReply_topic(String reply_topic) {
            this.reply_topic = reply_topic;
        }

        public String getReply_by() {
            return reply_by;
        }

        public void setReply_by(String reply_by) {
            this.reply_by = reply_by;
        }

        public String getUnique_id() {
            return unique_id;
        }

        public String getAnexo() {
            return anexo;
        }

        public void setAnexo(String anexo) {
            this.anexo = anexo;
        }
    }
}
