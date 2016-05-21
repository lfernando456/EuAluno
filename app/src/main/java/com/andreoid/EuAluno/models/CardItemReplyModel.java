package com.andreoid.EuAluno.models;

public class CardItemReplyModel {
    public String idReply;

    public String author;
    public String reply_content;
    public String data_reply;
    public String unique_id;
    public String anexo;
    public CardItemReplyModel(String idReply, String author, String reply_content, String data_reply,String unique_id,String anexo) {
        this.idReply = idReply;
        this.author = author;
        this.reply_content = reply_content;
        this.data_reply = data_reply;
        this.unique_id = unique_id;
        this.anexo = anexo;
    }
}
