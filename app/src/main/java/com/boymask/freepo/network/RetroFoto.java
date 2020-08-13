package com.boymask.freepo.network;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RetroFoto {

 /*   id	"HBgBgaViIbY"
    title	"Busty Tiny Teen With Angel Body Gets Fucked"
    keywords	"Busty Tiny Teen With Angel Body Gets Fucked, students, teens, big tits, for women, hardcore, petite, amateurs, blonde, Eva E"
    views	2349453
    rate	"4.12"
    url	"https://www.eporner.com/hd-porn/HBgBgaViIbY/Busty-Tiny-Teen-With-Angel-Body-Gets-Fucked/"
    added	"2019-03-15 17:22:52"
    length_sec	1759
    length_min	"29:19"*/
    //  	"https://www.eporner.com/embed/HBgBgaViIbY/"


    public void setEmbed(String embed) {
        this.embed = embed;
    }

    @SerializedName("embed")
    private String embed;
    @SerializedName("title")
    private String title;
    @SerializedName("videos")
    private List<?> videos;

    public int getTotal_count() {
        return total_count;
    }

    @SerializedName("total_count")
    private int total_count;


    public RetroFoto(String embed, String title, int total_count, List<?> videos) {
        this.embed = embed;
        this.title = title;
        this.total_count = total_count;
        this.videos = videos;
    }

    public String getEmbed() {
        return embed;
    }

    public List<?> getVideos() {
        return videos;
    }
}