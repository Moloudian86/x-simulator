package UiController;

import LogicController.HashtagService;
import LogicController.UserService;
import LogicController.PostService;
import com.example.twitter.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.MediaView;
import javafx.stage.FileChooser;

import java.io.File;

import model.*;
import repository.PostRepository;
import repository.UserRepository;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class NewPostController implements Initializable {
    private final PostRepository postRepository = new PostRepository();

    @FXML
    private Button btnPremium;

    @FXML
    private Button btnAddMedia;

    @FXML
    private Button btnCreatePost;

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
    private TextArea textArea;

    @FXML
    private Label fileLable;

    private File selectedFile;

    private Post editingPost;
    private final UserRepository userRepository = new UserRepository();


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

        btnCreatePost.getStyleClass().add("btnCreatePost");
        setActive(btnNew);
    }

    public void setEditingPost(Post post) {
        this.editingPost = post;
        textArea.setText(post.getContent());
        if (post.getMediaPath() != null) {
            fileLable.setText(post.getMediaPath());
        }
    }

    @FXML
    void btnCreatePostAction(ActionEvent event) {
        User currentUser = UserService.getCurrentUser();
        String text = textArea.getText();

        if (editingPost != null) {
            editingPost.setContent(text);
            editingPost.setEdited(true);
            HashtagService.getInstance().updateHashtagsForPost(editingPost);
            if (selectedFile != null) {
                editingPost.setMediaPath(selectedFile.getAbsolutePath());
            }
            PostService.getInstance().updatePost(editingPost);
            editingPost = null;
            try {
                FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home-view.fxml"));
                Scene scene = new Scene(fxmlLoader.load());
                Main.setMainStage(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return;
        }
        boolean hasMedia;
        if (selectedFile!=null){
            hasMedia = true;
        }else {
            hasMedia = false;
        }
        int cost = currentUser.calculatePostCost(text,hasMedia);
        if (currentUser.getToken() < cost) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("You do not have enough tokens.");
            alert.show();
            return;
        }
        currentUser.setToken(currentUser.getToken() - cost);
        userRepository.update(currentUser.getId(),currentUser);
        Post post = PostService.getInstance().createPost(UserService.getCurrentUser(),text);
        PostService.getInstance().addPost(post);
        if (selectedFile != null) {
            post.setMediaPath(selectedFile.getAbsolutePath());
        }
        HashtagService.getInstance().getHashtagFromPost(post);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home-view.fxml"));
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
    void btnAddMediaAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image or Video");

        //فایل هایی که میتونه انتخاب کنه
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Media Files", "*.png", "*.jpg", "*.jpeg", "*.mp4", "*.mov"));

        //انتخاب فایل
        selectedFile = fileChooser.showOpenDialog(btnAddMedia.getScene().getWindow());
        if(selectedFile != null){
            fileLable.setText(selectedFile.getAbsolutePath());
        }
    }


    public void setPostText(String text){
        textArea.setText(text);
    }

    public void setFileLable(String text){fileLable.setText(text);}
}
