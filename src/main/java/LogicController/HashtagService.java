package LogicController;

import model.Database;
import model.Hashtag;
import model.Post;
import repository.HashtagRepository;
import repository.PostHashtagRepository;
import repository.PostRepository;

import java.util.ArrayList;
import java.util.List;

public class HashtagService {
        private final HashtagRepository hashtagRepository = new HashtagRepository();
        private final PostHashtagRepository postHashtagRepository = new PostHashtagRepository();

        private Hashtag NEWS = new Hashtag("#News");
        private Hashtag SPORTS = new Hashtag("#Sports");
        private Hashtag EDUCATION = new Hashtag("#Education");
        private Hashtag GAMING = new Hashtag("#Gaming");
        private Hashtag ART = new Hashtag("#Art");
        private Hashtag TECHNOLOGY = new Hashtag("#Technology");
        private static HashtagService instance;
        public static HashtagService getInstance() {
                if (instance == null)
                        instance = new HashtagService();
                return instance;
        }
        private HashtagService(){
                addHashtag(NEWS);
                addHashtag(SPORTS);
                addHashtag(EDUCATION);
                addHashtag(GAMING);
                addHashtag(ART);
                addHashtag(TECHNOLOGY);
        }

        public void addHashtag(Hashtag hashtag){
                List<Hashtag> hashtags = hashtagRepository.findAll();
                for (Hashtag h : hashtags){
                        if (h.getTitle().equalsIgnoreCase(hashtag.getTitle())){
                                hashtag.setId(h.getId());
                                return;
                        }
                }
                hashtagRepository.add(hashtag);
        }

        public void getHashtagFromPost(Post post){
                if (post == null || post.getContent() == null) {
                        return;
                }

                String[] words = post.getContent().split(" ");
                for (String word : words){
                          if (word.startsWith("#")){
                                Hashtag hashtag = getHashtagByTitle(word);
                                if (hashtag == null){
                                        hashtag = new Hashtag(word);
                                        addHashtag(hashtag);
                                }
                                if (!post.getHashtags().contains(hashtag)) {
                                        post.getHashtags().add(hashtag);
                                }

                                if (!hashtag.getPostIds().contains(post.getId())) {
                                        hashtag.getPostIds().add(post.getId());
                                }
                        }
                }

        }

        public void updateHashtagsForPost(Post post) {
                for (Hashtag hashtag : post.getHashtags()) {
                        hashtag.getPostIds().remove(Integer.valueOf(post.getId()));
                }
                post.getHashtags().clear();
                getHashtagFromPost(post);
        }

        public Hashtag[] getPopularHashtags(){
                List<Hashtag> hashtags = hashtagRepository.findAll();
                Hashtag[] popularHashtags = new Hashtag[hashtags.size()];
                for (int i = 0; i< hashtags.size();i++) {
                        popularHashtags[i] = hashtags.get(i);
                }
                for (int i = 0; i < popularHashtags.length; i++) {
                        for (int j = i + 1; j < popularHashtags.length; j++) {
                                if (popularHashtags[j].getPostIds().size() > popularHashtags[i].getPostIds().size()) {
                                        Hashtag temp = popularHashtags[i];
                                        popularHashtags[i] = popularHashtags[j];
                                        popularHashtags[j] = temp;
                                }
                        }
                }
                return popularHashtags;
        }


        public Hashtag getHashtagById(int id){
               return hashtagRepository.findById(id);
        }

        public Hashtag getHashtagByTitle(String title) {
               return hashtagRepository.findByTitle(title);
        }

        public Hashtag getNEWS() {
                return NEWS;
        }


        public Hashtag getSPORTS() {
                return SPORTS;
        }


        public Hashtag getEDUCATION() {
                return EDUCATION;
        }


        public Hashtag getGAMING() {
                return GAMING;
        }

        public Hashtag getART() {
                return ART;
        }


        public Hashtag getTECHNOLOGY() {
                return TECHNOLOGY;
        }

}
