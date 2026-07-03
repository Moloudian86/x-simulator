package UiController;

import LogicController.PostService;
import LogicController.SearchService;
import LogicController.UserService;
import com.example.twitter.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class SearchController implements Initializable {

    @FXML
    private Button btnHome;

    @FXML
    private Button btnLogOut;

    @FXML
    private Button btnNew;

    @FXML
    private Button btnPorofile;

    @FXML
    private Button btnPremium;

    @FXML
    private Button btnSearch;

    @FXML
    private ComboBox<String> comboSearchType;

    @FXML
    private ListView<VBox> listView;

    @FXML
    private TextField searchField;

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

    @FXML
    void searchFieldAction(ActionEvent event) {
        String searchType = comboSearchType.getValue();
        String keyword = searchField.getText();
        listView.getItems().clear();
        if (keyword == null ) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Please enter search text");
            alert.show();
        }
        if (searchType.equals("Users")) {
            List<User> users = SearchService.getInstance().searchUsers(keyword);
            for (User user : users) {
                VBox userBox = createUserView(user);
                listView.getItems().add(userBox);
            }
        }
        if (searchType.equals("Posts")){
            List<Post> posts= SearchService.getInstance().searchPosts(keyword);
            for (Post post : posts){
                VBox postBox = PostviewBuilder.createPostView(post);
                listView.getItems().add(postBox);
            }
        }
        if (searchType.equals("Hashtags")){
            List<Hashtag> hashtags= SearchService.getInstance().searchHashtags(keyword);
            for (Hashtag hashtag : hashtags){
                VBox postBox = createHashtagView(hashtag);
                listView.getItems().add(postBox);
            }
        }
    }
    private VBox createUserView(User user) {
        Button username = new Button(user.getUsername());
        username.getStyleClass().add("btnUsername");
        Label fullNameText = new Label("Full Name: ");
        Label fullName = new Label(user.getFullName());
        Label bioText = new Label("Bio: ");
        Label bio = new Label(user.getBio());

        HBox fullNameData = new HBox(10,fullNameText, fullName);
        HBox bioData = new HBox(10,bioText, bio);
        username.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("profile-view.fxml"));
                Scene scene = new Scene(loader.load());

                ProfileController controller = loader.getController();
                controller.loadProfile(user);

                Main.setMainStage(scene);

            } catch (IOException ex) {
                ex.printStackTrace();
            }

        });
        VBox userVbox = new VBox(10,username,fullNameData,bioData);
        userVbox.getStyleClass().add("box");
        return userVbox;

    }

    private VBox createHashtagView(Hashtag hashtag) {
        Label hashtagTitle = new Label(hashtag.getTitle());
        hashtagTitle.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label postCount = new Label("Posts Count: " + hashtag.getPostIds().size());

        VBox hashtagBox = new VBox(10);
        hashtagBox.getStyleClass().add("box");

        hashtagBox.getChildren().addAll(hashtagTitle, postCount);

        for (Integer postId : hashtag.getPostIds()) {
            Post post = PostService.getInstance().getPostById(postId);

            if (post == null) {
                continue;
            }

            if (post.isBlocked()) {
                continue;
            }

            VBox postBox = PostviewBuilder.createPostView(post);
            hashtagBox.getChildren().add(postBox);
        }


        return hashtagBox;
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

        setActive(btnSearch);
        comboSearchType.getItems().addAll("Users", "Posts", "Hashtags");
        comboSearchType.setValue("Users");
    }
}

