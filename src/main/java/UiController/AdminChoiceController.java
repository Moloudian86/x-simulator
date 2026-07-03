package UiController;

import LogicController.HashtagService;
import LogicController.PostService;
import LogicController.ReportService;
import LogicController.UserService;
import com.example.twitter.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminChoiceController implements Initializable {
    private boolean userBlock = false;
    @FXML
    private ListView<VBox> listView;

    @FXML
    private Button btnBack;

    @FXML
    private Label titleLabel;


    public void setTitle(String text){
        titleLabel.setText(text);

        if(text.equals("Popular Posts")){
            showPopularPosts();
        }

        if (text.equals("Posts List")){
            showPostsList();
        }

        if (text.equals("Users List")){
            showUsersList();
        }
        if(text.equals("Reports")){
            showReports();
        }
        if(text.equals("Popular Hashtags")){
            showPopularHashtags();
        }

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void showPopularPosts(){
        listView.getItems().clear();
        Post[] popularPosts = PostService.getInstance().getPopularPosts();

        for (Post post : popularPosts) {
            VBox postBox = PostviewBuilder.createPostView(post);
            listView.getItems().add(postBox);
        }
    }

    public void showPopularHashtags(){
        listView.getItems().clear();
        Hashtag[] popularHashtags = HashtagService.getInstance().getPopularHashtags();

        for (Hashtag hashtag : popularHashtags) {
            VBox hashtagBox = createHashtagView(hashtag);
            listView.getItems().add(hashtagBox);
        }
    }

    public void showPostsList(){
        listView.getItems().clear();
        Post posts[] = PostService.getInstance().getَAllPosts();
        for (Post post : posts) {
            VBox postBox = PostviewBuilder.createPostView(post);
            listView.getItems().add(postBox);
        }
    }

    public void showUsersList(){
        listView.getItems().clear();

        for (User user : Database.getInstance().getUsers()) {

            VBox userBox = createUserView(user);

            listView.getItems().add(userBox);
        }
    }


    private VBox createUserView(User user){

        Button username = new Button(user.getUsername());
        username.getStyleClass().add("btnUsername");
        Label fullNameText = new Label("Full Name: ");
        Label fullName = new Label(user.getFullName());
        Label phoneText = new Label("Phone Number: ");
        Label phone = new Label(user.getPhone());
        Label passwordText = new Label("Password: ");
        Label password = new Label(user.getPassword());
        Label emailText = new Label("Email: ");
        Label email = new Label(user.getEmail());
        Label typeText = new Label("Type: ");
        Label type = new Label(user.getClass().getSimpleName());

        HBox fullNameData = new HBox(10,fullNameText, fullName);
        HBox phoneData = new HBox(10,phoneText, phone);
        HBox passwordData = new HBox(10,passwordText,password);
        HBox emailData = new HBox(10,emailText, email);
        HBox typeData = new HBox(10,typeText,type);

        VBox userBox = new VBox(10, username,fullNameData,phoneData,passwordData,emailData,typeData);

        userBox.getStyleClass().add("box");

        return userBox;
    }

    private VBox createHashtagView(Hashtag hashtag) {
        Label titleText = new Label("Hashtag: ");
        Label title = new Label(hashtag.getTitle());

        Label countText = new Label("Used in posts: ");
        Label count = new Label(String.valueOf(hashtag.getPostIds().size()));

        HBox titleBox = new HBox(10, titleText, title);
        HBox countBox = new HBox(10, countText, count);

        VBox hashtagBox = new VBox(10, titleBox, countBox);

        hashtagBox.getStyleClass().add("box");

        return hashtagBox;
    }

    public void showReports(){
        listView.getItems().clear();

        for (Report report : Database.getInstance().getReports()){
            if (report.getStatus() == Status.REJECTED){
                continue;
            }
            VBox reportBox = new VBox();
            Button confirm = new Button("Confirm");
            Button reject = new Button("Reject");
            Button blockUser = new Button("Block User");
            Label reasonText = new Label("reason: ");
            Label reasonLabel = new Label();
            User user = UserService.getInstance().getUserById(report.getReporterId());
            Button repoeterUser = new Button(user.getUsername());
            repoeterUser.setStyle(
                    "-fx-background-color: #36cd9e;"+
                            "-fx-border-color: #6136cd"

            );
            Post reportedPost = PostService.getInstance().getPostById(report.getReportedPostId());
            VBox postVbox = PostviewBuilder.createPostView(reportedPost);
            reasonLabel.setText(report.getReason());
            HBox reason = new HBox(reasonText,reasonLabel);
            HBox status = new HBox(10,confirm,reject,blockUser);
            reportBox.getChildren().addAll(repoeterUser,postVbox,reason,status);
            confirm.setOnAction(e->{
                ReportService.getInstance().confirm(report,reportedPost);
                    showReports();

            });
            reject.setOnAction(e->{
                ReportService.getInstance().reject(report,reportedPost);
                showReports();
            });
            blockUser.setOnAction(e->{
                User reportedUser = UserService.getInstance().getUserById(report.getReportedId());
                if (!reportedUser.isBlocked()){
                    blockUser.setText("Un Block");
                    ReportService.getInstance().blockUser(report);
                }else {
                    blockUser.setText("Block User");
                    ReportService.getInstance().unBlockUser(report);
                }

            });
            listView.getItems().add(reportBox);
        }


    }

    @FXML
    void btnBackAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("admin-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
