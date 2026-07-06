package LogicController;

import model.*;
import repository.HashtagRepository;
import repository.PostRepository;
import repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

public class SearchService {
    private static final SearchService instance = new SearchService();
    private final UserRepository userRepository = new UserRepository();
    private final PostRepository postRepository = new PostRepository();
    private final HashtagRepository hashtagRepository = new HashtagRepository();




    public static SearchService getInstance() {
        return instance;
    }

    public List<User> searchUsers(String keyword) {
        List<User> users = userRepository.findAll();
        List<User> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (User user : users) {
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
        List<Post> posts = postRepository.findAll();
        List<Post> result = new ArrayList<>();
        String lowerKeyword = keyword.toLowerCase();
        for (Post post : posts){
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
        List<Hashtag> hashtags = hashtagRepository.findAll();
        String lowerKeyword = keyword.toLowerCase();
        for (Hashtag hashtag : hashtags){
            if (hashtag.getTitle().toLowerCase().contains(lowerKeyword)){
                result.add(hashtag);
            }
        }
        return result;
    }
}
