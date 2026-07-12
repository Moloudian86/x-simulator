package model;

import javafx.util.Duration;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Post implements Comparable<Post>{

    private int id;
    private User author;
    private String content;
    private Date creationDate;
    private List<Hashtag> hashtags = new ArrayList<>();
    private List<Integer> likeIds = new ArrayList<>();
    private Integer parentPostId;
    private List<Integer> answerPosts = new ArrayList<>();
    private int viewCount=0;
    private int likesCount = 0;
    private boolean blocked = false;
    private List<Integer> viewIds = new ArrayList<>();
    private String mediaPath;
    private Duration totalDuration = Duration.ZERO;
    private boolean edited = false;

    public Post(User author,String content){
        this.author = author;
        this.content = content;
        this.creationDate = new Date();
    }

    public Post(Post other) {
        this.content = other.content;
        this.mediaPath = other.mediaPath;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public List<Integer> getLikeIds() {
        return likeIds;
    }

    public void setLikeIds(List<Integer> likes) {
        this.likeIds = likes;
    }

    public Integer getParentPostId() {
        return parentPostId;
    }

    public void setParentPostId(Integer parentPostId) {
        this.parentPostId = parentPostId;
    }

    public List<Integer> getAnswerPosts() {
        return answerPosts;
    }

    public void setAnswerPosts(List<Integer> answerPosts) {
        this.answerPosts = answerPosts;
    }

    public int getViewCount() {
        return viewCount;
    }

    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }

    public int getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(int likesCount) {
        this.likesCount = likesCount;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public List<Integer> getViewIds() {
        return viewIds;
    }

    public void setViewIds(List<Integer> viewIds) {
        this.viewIds = viewIds;
    }

    public String getMediaPath() {
        return mediaPath;
    }

    public void setMediaPath(String mediaPath) {
        this.mediaPath = mediaPath;
    }

    public List<Hashtag> getHashtags() {
        return hashtags;
    }

    public void setHashtags(List<Hashtag> hashtags) {
        this.hashtags = hashtags;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }


    public Duration getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(Duration totalDuration) {
        this.totalDuration = totalDuration;
    }

    public void like() {
        likesCount++;
    }

    public void disLike(){
        likesCount--;
    }

    public void view(){
        viewCount++;
    }

    @Override
    public int compareTo(Post other) {
        if (!this.getContent().equals(other.getContent())) {
            return 1;
        }
        if (this.getMediaPath() == null && other.getMediaPath() != null) return 1;
        if (this.getMediaPath() != null && other.getMediaPath() == null) return 1;
        if (this.getMediaPath() != null && other.getMediaPath() != null) {
            if (!this.getMediaPath().equals(other.getMediaPath())) {
                return 1;
            }
        }
        return 0;
    }
}
