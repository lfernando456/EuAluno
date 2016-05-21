package com.andreoid.EuAluno.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by André on 20/05/2016.
 */
public class Result {

    @SerializedName("result")
    @Expose
    private String result;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @SerializedName("value")
    @Expose
    private String value;

    /**
     * @return The result
     */
    public String getResult() {
        return result;
    }

    /**
     * @param result The result
     */
    public void setResult(String result) {
        this.result = result;
    }

}