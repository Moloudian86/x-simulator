package model;

import java.util.*;

import java.util.List;

public abstract class User extends Account{
    private int credit = 20;
    private int token = 150;
    private String bio = "";
    private String accType;
    private List<Integer> posts = new ArrayList<>();
    private List<Integer> Followers = new ArrayList<>();
    private List<Integer> Following = new ArrayList<>();
    private List<Integer> likedPosts = new ArrayList<>();
    private String badgeImagePath;
    private String profileImage;
    private boolean blocked = false;
    private List<Integer> favoriteHashtags = new ArrayList<>();

    public User(String fullName,String username,String email,String phone, String password){
        super(fullName,username,email,phone, password);


    }
    public List<Integer> getPosts() {
        return posts;
    }

    public List<Integer> getFollowers() {
        return Followers;
    }

    public List<Integer> getFollowing() {
        return Following;
    }

    public List<Integer> getLikedPosts() {
        return likedPosts;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getToken() {
        return token;
    }

    public int getCredit() {
        return credit;
    }

    public void setBadgeImagePath(String badgeImagePath) {
        this.badgeImagePath = badgeImagePath;
    }

    public String getBadgeImagePath() {
        return badgeImagePath;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public List<Integer> getFavoriteHashtags() {
        return favoriteHashtags;
    }

    public String getBio() {
        return bio;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public abstract int calculatePostCost(String text, boolean hasMedia);

    public String getAccType() {
        return accType;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }
}
