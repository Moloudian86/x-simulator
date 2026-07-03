package LogicController;

import model.*;

import java.util.ArrayList;
import java.util.List;

public class SearchService {
    private static final SearchService instance = new SearchService();

    public static SearchService getInstance() {
        return instance;
    }

    public List<User> searchUsers(String keyword) {
        List<User> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (User user : Database.getInstance().getUsers()) {
            if (user.isBlocked()) {
                continue;
            }
            if (user.getUsername().toLowerCase().contains(lowerKeyword)) {
                result.add(user);
            }
        }
        return result;
    }

    public List<Post> searchPosts(String keyword){
        List<Post> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Post post : Database.getInstance().getPosts()){
            if (post.isBlocked()) {
                continue;
            }
            if (post.getParentPostId() != null) {
                continue;
            }
            if (post.getContent().toLowerCase().contains(lowerKeyword)){
                result.add(post);
            }
        }
        return result;
    }

    public List<Hashtag> searchHashtags(String keyword){
        List<Hashtag> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Hashtag hashtag : Database.getInstance().getHashtags()){
            if (hashtag.getTitle().toLowerCase().contains(lowerKeyword)){
                result.add(hashtag);
            }
        }
        return result;
    }
}
