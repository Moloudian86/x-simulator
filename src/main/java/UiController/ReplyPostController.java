package UiController;

import LogicController.HashtagService;
import LogicController.PostService;
import LogicController.PremiumService;
import LogicController.UserService;
import com.example.twitter.Main;
import interfaces.IRepository;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
import javafx.stage.FileChooser;
import model.Database;
import model.Post;
import model.PremiumUser;
import model.User;
import repository.PostRepository;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ReplyPostController{
    private final IRepository<Post> postRepository = new PostRepository();

    private Post parentPost;


    @FXML
    private Button btnAddMedia;

    @FXML
    private Button btnBack;

    @FXML
    private Button btnSend;

    @FXML
    private VBox parentPostBox;


    @FXML
    private Label fileLable;


    @FXML
    private ListView<VBox> listViewReply;


    @FXML
    private ListView<VBox> listView;

    @FXML
    private TextArea textAreaReply;

    private File selectedFile;

    private Post editingReply;

    public void setParentPost(Post post) {

        this.parentPost = post;

        listView.getItems().clear();

        VBox postView = PostviewBuilder.createPostView(post);

        listView.getItems().add(postView);

        loadPosts();
    }

    public void setEditingReply(Post reply) {
        this.editingReply = reply;
        textAreaReply.setText(reply.getContent());

        if (reply.getMediaPath() != null) {
            fileLable.setText(reply.getMediaPath());
        }
    }



    private void loadPosts() {
        List<Post> postsDB = postRepository.findAll();
        listViewReply.getItems().clear();
        List<Post> posts = postsDB;
        for (int i = posts.size() - 1; i >= 0; i--) {
            Post post = posts.get(i);
            if (post.isBlocked()) {
                continue;
            }
            if (post.getParentPostId() == null || post.getParentPostId() != parentPost.getId()) {
                continue;
            }
            VBox postView = PostviewBuilder.createPostView(post);
            listViewReply.getItems().add(postView);
        }
    }

    @FXML
    void btnSendAction(ActionEvent event) {
        User currentUser = UserService.getCurrentUser();
        String text = textAreaReply.getText();

        if (editingReply != null) {
            Post oldPost = new Post(editingReply);
            editingReply.setContent(text);
            HashtagService.getInstance().updateHashtagsForPost(editingReply);

            if (selectedFile != null) {
                editingReply.setMediaPath(selectedFile.getAbsolutePath());
            }
            if (editingReply.compareTo(oldPost) != 0) {
                editingReply.setEdited(true);
            }
            PostService.getInstance().updatePost(editingReply);
            textAreaReply.clear();
            fileLable.setText("");
            selectedFile = null;
            editingReply = null;

            loadPosts();
            return;
        }

        boolean hasMedia;
        if (selectedFile!=null){
            hasMedia = true;
        }else {
            hasMedia = false;
        }
        int cost = PremiumService.getInstance().calculatePostCost(text,hasMedia,currentUser);
        if (currentUser.getToken() < cost) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("You do not have enough tokens.");
            alert.show();
            return;
        }
        currentUser.setToken(currentUser.getToken() - cost);
        Post reply = new Post(UserService.getCurrentUser(), text);
        reply.setParentPostId(parentPost.getId());
        PostService.getInstance().addAnswerPost(parentPost,reply);
        PostService.getInstance().addPost(reply);
        if (selectedFile != null) {
            reply.setMediaPath(selectedFile.getAbsolutePath());
        }
        HashtagService.getInstance().getHashtagFromPost(reply);
        textAreaReply.clear();
        loadPosts();
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


    @FXML
    void btnBackAction(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("home-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TextArea getTextAreaReply() {
        return textAreaReply;
    }

    public void setTextAreaReply(TextArea textAreaReply) {
        this.textAreaReply = textAreaReply;
    }
}
