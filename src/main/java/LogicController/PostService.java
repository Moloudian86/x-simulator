package LogicController;

import model.*;
import repository.PostHashtagRepository;
import repository.PostRepository;
import repository.UserRepository;

import java.util.List;

public class PostService {
    private static PostService instance;
    private final UserRepository userRepository = new UserRepository();
    private final PostRepository postRepository = new PostRepository();
    private final PostHashtagRepository postHashtagRepository = new PostHashtagRepository();


    private PostService() {

    }

    public static PostService getInstance() {
        if (instance == null)
            instance = new PostService();
        return instance;
    }


    public Post createPost(User user,String text){
        Post post = new Post(user, text);
        return post;
    }


    public void addPost(Post post){
        postRepository.add(post);
        for(Hashtag hashtag : post.getHashtags()){
            postHashtagRepository.add(post.getId(), hashtag.getId());
        }
    }
    public void removePost(Post post) {
        List<Post> posts = postRepository.findAll();

        // حذف آیدی پست از هشتگ‌ها
        for (Hashtag hashtag : post.getHashtags()) {
            hashtag.getPostIds().remove(Integer.valueOf(post.getId()));
        }

        // اگر این پست ریپلای است از لیست جواب‌های پست پدر حذف بشه
        if (post.getParentPostId() != null) {
            Post parent = getPostById(post.getParentPostId());
            if (parent != null) {
                parent.getAnswerPosts().remove(Integer.valueOf(post.getId()));
            }
        }

        // اگر این پست اصلی است، ریپلای هاش هم حذف بشه
        for (int i = posts.size() - 1; i >= 0; i--) {
            Post p = posts.get(i);

            if (p.getParentPostId() != null && p.getParentPostId().equals(post.getId())) {
                removePost(p);
            }
        }

        // حذف از likedPosts کاربران
        List<User> users = userRepository.findAll();
        for (User user : users) {
            if (user.getLikedPosts().contains(post.getId())){
                user.getLikedPosts().remove(Integer.valueOf(post.getId()));
            }

        }

        postRepository.remove(post.getId());
    }

    public void updatePost(Post post) {
        postRepository.update(post.getId(), post);
        postHashtagRepository.updatePostHashtags(post.getId(), post.getHashtags());
    }

    public List<Post> getPosts(){
        List<Post> posts = postRepository.findAll();
        return posts;
    }




    public Post getPostById(int id){
        List<Post> posts = postRepository.findAll();

        for(Post post : posts){

            if(post.getId() == id){
                return post;
            }

        }

        return null;
    }


    public void addAnswerPost(Post parentPost,Post post){
        parentPost.getAnswerPosts().add(post.getId());
    }

    public void removeAnswerPost(Post parentPost,Post post){
        parentPost.getAnswerPosts().remove(Integer.valueOf(post.getId()));
    }


    public Post[] getPopularPosts() {
        List<Post> posts = postRepository.findAll();


        Post[] popularPosts = new Post[posts.size()];

        for (int i = 0; i < posts.size(); i++) {
            popularPosts[i] = posts.get(i);
        }
        for (int i = 0; i < popularPosts.length; i++) {
            for (int j = i + 1; j < popularPosts.length; j++) {
                if (popularPosts[j].getLikesCount() > popularPosts[i].getLikesCount()) {
                    Post temp = popularPosts[i];
                    popularPosts[i] = popularPosts[j];
                    popularPosts[j] = temp;
                }
            }
        }
        return popularPosts;
    }

    public Post[] getَAllPosts() {
        List<Post> posts = postRepository.findAll();

        Post[] allPosts = new Post[posts.size()];

        for (int i = 0; i < posts.size(); i++) {
            allPosts[i] = posts.get(i);
        }
        return allPosts;
    }
}
