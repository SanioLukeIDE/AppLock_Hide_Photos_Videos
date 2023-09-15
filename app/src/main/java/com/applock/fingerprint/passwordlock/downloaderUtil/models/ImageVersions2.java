package com.applock.fingerprint.passwordlock.downloaderUtil.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ImageVersions2 implements Serializable {
    @SerializedName("candidates")
    private List<Candidate> candidates;


    public ImageVersions2() {
    }

    @SerializedName("candidates")
    public List<Candidate> getCandidates() {
        return candidates;
    }

    @SerializedName("candidates")
    public void setCandidates(List<Candidate> value) {
        this.candidates = value;
    }
}
