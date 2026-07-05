package model;

import java.util.*;


public class Database {
    private static Database instance;

    private List<Post> posts = new ArrayList<>();
    private List<Report> reports = new ArrayList<>();
    private List<Hashtag> hashtags = new ArrayList<>();


    public static Database getInstance(){
        if(instance == null)
            instance = new Database();
        return instance;
    }

    public static void setInstance(Database instance) {
        Database.instance = instance;
    }



    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public List<Report> getReports() {
        return reports;
    }

    public void setReports(List<Report> reports) {
        this.reports = reports;
    }

    public List<Hashtag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }
}
