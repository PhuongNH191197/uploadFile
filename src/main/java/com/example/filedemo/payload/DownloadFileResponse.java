package com.example.filedemo.payload;


import org.springframework.core.io.Resource;

public class DownloadFileResponse {

    private String id;
    private String nameGame;
    private String urlDownload;
    private String logo;

    public DownloadFileResponse() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNameGame() {
        return nameGame;
    }

    public void setNameGame(String nameGame) {
        this.nameGame = nameGame;
    }

    public String getUrlDownload() {
        return urlDownload;
    }

    public void setUrlDownload(String urlDownload) {
        this.urlDownload = urlDownload;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    @Override
    public String toString() {
        return "DownloadFileResponse{" +
                "id='" + id + '\'' +
                ", nameGame='" + nameGame + '\'' +
                ", urlDownload='" + urlDownload + '\'' +
                ", logo='" + logo + '\'' +
                '}';
    }
}
