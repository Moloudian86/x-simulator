package UiController;

import com.example.twitter.Main;
import interfaces.IRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import model.*;
import LogicController.*;
import repository.PostRepository;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    private final IRepository<Post> postRepository = new PostRepository();


    @FXML
    private Label bioLable;

    @FXML
    private Button btnEditProfile;

    @FXML
    private Button btnFollow;

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
    private Button btnFollowing;

    @FXML
    private Button btnPremium;

    @FXML
    private Button btnFollowers;

    @FXML
    private Label emailLabel;

    @FXML
    private Label followersLabel;

    @FXML
    private Label followingLable;

    @FXML
    private Label labelPosts;

    @FXML
    private ListView<VBox> listView;

    @FXML
    private Label passwordLabel;

    @FXML
    private VBox privateVbox;

    @FXML
    private Button btnProfileImage;

    @FXML
    private Label usernameLabel;

    @FXML
    private ImageView imageView;

    private User profileUser;

    public void loadProfile(User user){
         User currentUser = UserService.getCurrentUser();
         this.profileUser = user;
         imageView.setFitWidth(40);
         imageView.setFitHeight(40);
         imageView.setPreserveRatio(false);
         Image image;
         if (user.getProfileImage() == null) {
             image = new Image(PostviewBuilder.class.getResourceAsStream("/img/userIcon.png"));
         } else {
             File file = new File(user.getProfileImage());
             image = new Image(file.toURI().toString());
         }
         imageView.setImage(image);


         if (user.getId() == currentUser.getId()){
            loadPosts(user);

            followersLabel.setText(String.valueOf(user.getFollowers().size()));
            followingLable.setText(String.valueOf((user.getFollowing().size())));
            usernameLabel.setText(user.getUsername());
            passwordLabel.setText(user.getPassword());
            emailLabel.setText(user.getEmail());
            bioLable.setText(user.getBio());

            btnFollow.setVisible(false);
            btnFollow.setManaged(false);

            btnProfileImage.setVisible(true);
            btnProfileImage.setManaged(true);

            btnEditProfile.setVisible(true);
            btnEditProfile.setManaged(true);

            privateVbox.setVisible(true);
        }else{
            loadPosts(user);
            followersLabel.setText(String.valueOf(user.getFollowers().size()));
            followingLable.setText(String.valueOf((user.getFollowing().size())));
            usernameLabel.setText(user.getUsername());
            bioLable.setText(user.getBio());
            btnFollow.setVisible(true);
            btnFollow.setManaged(true);

             if (UserService.getCurrentUser().getFollowing().contains(user.getId())) {
                 btnFollow.setText("Un Follow");
                 btnFollow.getStyleClass().remove("follow");
             } else {
                 btnFollow.setText("Follow");
                 if (!btnFollow.getStyleClass().contains("follow")) {
                     btnFollow.getStyleClass().add("follow");
                 }
             }

            btnProfileImage.setVisible(false);
            btnProfileImage.setManaged(false);

            btnEditProfile.setVisible(false);
            btnEditProfile.setManaged(false);

            privateVbox.setVisible(false);
            privateVbox.setManaged(false);
        }

    }


    @FXML
    void btnEditProfileAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("editProfile-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            EditProfileController controller = fxmlLoader.getController();
            controller.loadUser(UserService.getCurrentUser());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    void btnProfileImageAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("addProfileImage-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnFollowAction(ActionEvent event) {

        if (UserService.getInstance().follow(profileUser)){

            btnFollow.setText("Un follow");
            btnFollow.getStyleClass().remove("follow");

            loadProfile(profileUser);
        }else{

            btnFollow.setText("Follow");
            btnFollow.getStyleClass().add("follow");
            loadProfile(profileUser);
        }
    }

    private void loadPosts(User user) {
        List<Post> postsDB = postRepository.findAll();
        listView.getItems().clear();
        List<Post> posts = postsDB;
        for (int i = posts.size() - 1; i >= 0; i--) {
            Post post = posts.get(i);
            if (post.isBlocked()) {
                continue;
            }

            if (post.getAuthor().getId() == user.getId()) {
                if (post.getParentPostId() != null){
                    continue;
                }
                VBox postBox = PostviewBuilder.createPostView(post);
                listView.getItems().add(postBox);
            }
        }
    }

    @FXML
    void btnFollowersAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("followersAndFollowing-view.fxml"));
            Scene scene = new Scene(loader.load());
            FollowersAndFollowing controller = loader.getController();
            controller.showFollowers(profileUser);
            Main.setMainStage(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @FXML
    void btnFollowingAction(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("followersAndFollowing-view.fxml"));
            Scene scene = new Scene(loader.load());
            FollowersAndFollowing controller = loader.getController();
            controller.showFollowing(profileUser);
            Main.setMainStage(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

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
        setActive(btnPorofile);
        btnFollow.setText("Follow");
        btnFollow.getStyleClass().add("follow");
        btnFollow.getStyleClass().add("btnFollow");


    }

    private void setActive(Button button){

        btnHome.getStyleClass().remove("mainButton");
        btnSearch.getStyleClass().remove("mainButton");
        btnPorofile.getStyleClass().remove("mainButton");
        btnNew.getStyleClass().remove("mainButton");


        button.getStyleClass().add("mainButton");

    }

    @FXML
    void btnHomeAction(ActionEvent event) {
        setActive(btnHome);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    }

    @FXML
    void btnNewAction(ActionEvent event) {
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
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("premium-view.fxml"));
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


}

