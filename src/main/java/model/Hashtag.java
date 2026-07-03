package model;

import java.util.ArrayList;
import java.util.List;

public class Hashtag {
    private static int idCounter = 0;
    private int id;
    private String title;
    private List<Integer> postIds = new ArrayList<>();


    public Hashtag(String title){
        this.id = ++idCounter;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public int getId() {
        return id;
    }

    public List<Integer> getPostIds() {
        return postIds;
    }

    public void setPostIds(List<Integer> postIds) {
        this.postIds = postIds;
    }
}
