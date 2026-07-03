package LogicController;

import model.*;

import java.util.List;

public class PostService {
    private static PostService instance;

    private PostService() {
        loadSamplePosts();
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
        Database.getInstance().getPosts().add(post);
    }
    public void removePost(Post post) {
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
        for (int i = Database.getInstance().getPosts().size() - 1; i >= 0; i--) {
            Post p = Database.getInstance().getPosts().get(i);

            if (p.getParentPostId() != null && p.getParentPostId().equals(post.getId())) {
                removePost(p);
            }
        }

        // حذف از likedPosts کاربران
        for (User user : Database.getInstance().getUsers()) {
            if (user.getLikedPosts().contains(post.getId())){
                user.getLikedPosts().remove(Integer.valueOf(post.getId()));
            }

        }

        Database.getInstance().getPosts().remove(post);
    }

    public List<Post> getPosts(){
        return Database.getInstance().getPosts();
    }


    private void loadSamplePosts() {
        NormalUser u1 = new NormalUser("pass", "محمد","pass","pass","pass");
        NormalUser u2 = new NormalUser("pass", "bob","pass","pass","pass");
        NormalUser u3 = new NormalUser("pass", "charlie","pass","pass","pass");
        Database.getInstance().getUsers().add(u1);
        Database.getInstance().getUsers().add(u2);
        Database.getInstance().getUsers().add(u3);
        Post p1 = new Post(u1,"امروز یه اتفاق جالب افتاد");
        Post p2 = new Post(u2,"This is the sample post.");
        Post p3 = new Post(u3,"JavaFX is working.");

        p1.getHashtags().add(HashtagService.getInstance().getART());
        p1.getHashtags().add(HashtagService.getInstance().getGAMING());
        HashtagService.getInstance().getART().getPostIds().add(p1.getId());
        HashtagService.getInstance().getGAMING().getPostIds().add(p1.getId());

        p2.getHashtags().add(HashtagService.getInstance().getNEWS());
        HashtagService.getInstance().getNEWS().getPostIds().add(p2.getId());

        p3.getHashtags().add(HashtagService.getInstance().getTECHNOLOGY());
        HashtagService.getInstance().getTECHNOLOGY().getPostIds().add(p3.getId());

        addPost(p1);
        addPost(p2);
        addPost(p3);
    }

    public Post getPostById(int id){

        for(Post post : Database.getInstance().getPosts()){

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

        Post[] popularPosts = new Post[Database.getInstance().getPosts().size()];

        for (int i = 0; i < Database.getInstance().getPosts().size(); i++) {
            popularPosts[i] = Database.getInstance().getPosts().get(i);
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

        Post[] allPosts = new Post[Database.getInstance().getPosts().size()];

        for (int i = 0; i < Database.getInstance().getPosts().size(); i++) {
            allPosts[i] = Database.getInstance().getPosts().get(i);
        }
        return allPosts;
    }





    public java.util.List<Post> getAllPosts() {
        return Database.getInstance().getPosts();
    }
}
