package UiController;

import LogicController.UserService;
import LogicController.PostService;
import com.example.twitter.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import model.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import repository.PostRepository;

import java.io.File;



import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


public class HomeController implements Initializable {
    private final PostRepository postRepository = new PostRepository();


    @FXML
    private Button btnFilterNewPosts;

    @FXML
    private ListView<VBox> listView;

    @FXML
    private Button btnHome;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnPorofile;

    @FXML
    private Button btnSearch;


    @FXML
    private Button btnPremium;

    @FXML
    private Button btnMessages;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Image homeImg = new Image(PostviewBuilder.class.getResourceAsStream("/img/home.png"));
        ImageView homeView = new ImageView(homeImg);
        homeView.setFitWidth(20);
        homeView.setFitHeight(20);
        btnHome.setGraphic(homeView);

        Image searchImg = new Image(PostviewBuilder.class.getResourceAsStream("/img/search.png"));
        ImageView searchView = new ImageView(searchImg);
        searchView.setFitWidth(20);
        searchView.setFitHeight(20);
        btnSearch.setGraphic(searchView);

        Image profileImg = new Image(PostviewBuilder.class.getResourceAsStream("/img/supervisedusercircle.png"));
        ImageView profileView = new ImageView(profileImg);
        profileView.setFitWidth(20);
        profileView.setFitHeight(20);
        btnPorofile.setGraphic(profileView);

        Image newPostImg = new Image(PostviewBuilder.class.getResourceAsStream("/img/edit_graph_report_512px.png"));
        ImageView newPostView = new ImageView(newPostImg);
        newPostView.setFitWidth(20);
        newPostView.setFitHeight(20);
        btnNew.setGraphic(newPostView);

        Image premiumImg = new Image(PostviewBuilder.class.getResourceAsStream("/img/approval_480px.png"));
        ImageView premiumView = new ImageView(premiumImg);
        premiumView.setFitWidth(20);
        premiumView.setFitHeight(20);
        btnPremium.setGraphic(premiumView);

        Image logOutImg = new Image(PostviewBuilder.class.getResourceAsStream("/img/logout_144px.png"));
        ImageView logOutView = new ImageView(logOutImg);
        logOutView.setFitWidth(20);
        logOutView.setFitHeight(20);
        btnLogOut.setGraphic(logOutView);

        PostService.getInstance();
        setActive(btnHome);
        loadPosts();
    }



    private void setActive(Button button){
        btnHome.getStyleClass().remove("mainButton");
        btnSearch.getStyleClass().remove("mainButton");
        btnPorofile.getStyleClass().remove("mainButton");
        btnNew.getStyleClass().remove("mainButton");
        btnLogOut.getStyleClass().remove("mainButton");
        button.getStyleClass().add("mainButton");
    }

    @FXML
    void btnHomeAction(ActionEvent event) {
        setActive(btnHome);
    }

    @FXML
    void btnSearchAction(ActionEvent event) {
        setActive(btnSearch);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("search.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnPorofileAction(ActionEvent event) {
        setActive(btnPorofile);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("profile-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            ProfileController controller = fxmlLoader.getController();
            controller.loadProfile(UserService.getCurrentUser());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnNewAction(ActionEvent event) {
        setActive(btnNew);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("newPost-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnPremiumAction(ActionEvent event) {
        setActive(btnPremium);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("premium-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnMessagesAction(ActionEvent event) {
        setActive(btnMessages);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("message-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void btnLogOutAction(ActionEvent event) {
        try {
            UserService.setCurrentUser(null);
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadPosts() {
        Iterable<Post> postsDB = postRepository.findAll();
        listView.getItems().clear();
        User currentUser = UserService.getCurrentUser();
        List<Post> posts = new ArrayList<>();
        for (Post post : postsDB) {
            posts.add(post);
        }
        for (int i = 0; i < posts.size(); i++) {
            for (int j = i + 1; j < posts.size(); j++) {
                int scoreI = getPostScore(currentUser,posts.get(i));
                int scoreJ = getPostScore(currentUser,posts.get(j));
                if (scoreJ > scoreI) {
                    Post temp = posts.get(i);
                    posts.set(i, posts.get(j));
                    posts.set(j, temp);
                }
            }
        }

        for (Post post : posts) {
            if (post.isBlocked()) {
                continue;
            }
            if (post.getParentPostId() != null) {
                continue;
            }
            VBox postView = PostviewBuilder.createPostView(post);
            listView.getItems().add(postView);
        }
    }

    private int getPostScore(User user,Post post){
        int score = 0;
        for (Hashtag hashtag : post.getHashtags()){
            if (user.getFavoriteHashtags().contains(hashtag.getId())){
                score = score + 2;
            }
        }
        if (user.getFollowing().contains(post.getAuthor().getId())){
            score++;
        }
        return score;
    }

    @FXML
    void btnFilterNewPostsAction(ActionEvent event) {
        List<Post> postsDB = postRepository.findAll();
        listView.getItems().clear();
        List<Post> posts = postsDB;
        for (int i = posts.size() - 1; i >= 0; i--) {
            Post post = posts.get(i);
            if (post.isBlocked()) {
                continue;
            }
            if (post.getParentPostId() != null) {
                continue;
            }
            VBox postView = PostviewBuilder.createPostView(post);
            listView.getItems().add(postView);
        }
    }
}

