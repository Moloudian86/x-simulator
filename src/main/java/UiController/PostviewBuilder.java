
package UiController;

import LogicController.PostService;
import LogicController.UserService;
import com.example.twitter.Main;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.media.*;
import javafx.util.Duration;
import model.*;

import java.io.File;
import java.io.IOException;

public class PostviewBuilder {

    public static VBox createPostView(Post post) {
        User currentUser = UserService.getCurrentUser();

        HBox userAndTime = createHeader(post);
        Label content = new Label(post.getContent());
        Label hashtagsPost = createHashtagsLabel(post);
        Node mediaNode = createMediaNode(post);
        HBox buttons = createButtons(post, currentUser);

        VBox postBox = new VBox(10);
        postBox.getChildren().addAll(userAndTime, content);

        if (mediaNode != null) {
            postBox.getChildren().add(mediaNode);
        }
        if (post.isEdited()) {
            Label editLabel = new Label("(edited)");
            postBox.getChildren().add(editLabel);
        }

        postBox.getChildren().addAll(hashtagsPost);

        postBox.getChildren().add(buttons);
        postBox.getStyleClass().add("box");

        return postBox;
    }

    private static HBox createHeader(Post post) {
        Button username = new Button(post.getAuthor().getUsername());
        username.getStyleClass().add("btnUsername");
        username.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("profile-view.fxml"));
                Scene scene = new Scene(loader.load());

                ProfileController controller = loader.getController();
                controller.loadProfile(post.getAuthor());

                Main.setMainStage(scene);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        Label time = new Label(String.valueOf(post.getCreationDate()));

        HBox userAndTime = new HBox(10);

        ImageView profileView = createProfileImageView(post.getAuthor());
        userAndTime.getChildren().addAll(profileView, username, time);

        if (post.getAuthor().getBadgeImagePath() != null) {
            Image badgeImagePath = new Image(PostviewBuilder.class.getResourceAsStream(post.getAuthor().getBadgeImagePath()));
            ImageView badgeImagePathView = new ImageView(badgeImagePath);

            badgeImagePathView.getStyleClass().add("badgeImagePathView");
            badgeImagePathView.setFitWidth(20);
            badgeImagePathView.setFitHeight(20);

            userAndTime.getChildren().add(badgeImagePathView);
        }

        return userAndTime;
    }

    private static Label createHashtagsLabel(Post post) {
        StringBuilder hashtags = new StringBuilder();

        for (Hashtag h : post.getHashtags()) {
            if (h != null) {
                hashtags.append(h.getTitle());
                hashtags.append(" ");
            }
        }

        Label hashtagsPost = new Label(String.valueOf(hashtags));
        hashtagsPost.getStyleClass().add("hashtag");

        return hashtagsPost;
    }

    private static Node createMediaNode(Post post) {
        String mediaPath = post.getMediaPath();

        if (mediaPath == null) {
            return null;
        }

        File file = new File(mediaPath);

        if (isImage(mediaPath)) {
            return createImageNode(file);
        }

        if (isVideo(mediaPath)) {
            return createVideoNode(file);
        }

        return null;
    }

    private static ImageView createImageNode(File file) {
        Image image = new Image(file.toURI().toString());
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);

        return imageView;
    }

    private static VBox createVideoNode(File file) {
        Media media = new Media(file.toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        MediaView mediaView = new MediaView(mediaPlayer);

        mediaView.setFitWidth(300);
        mediaView.setPreserveRatio(true);

        Slider videoSlider = new Slider();
        Label timeLabel = new Label();

        mediaPlayer.setOnReady(() -> {
            videoSlider.setMax(mediaPlayer.getTotalDuration().toSeconds());
        });

        videoSlider.setOnMouseReleased(e -> {
            mediaPlayer.seek(Duration.seconds(videoSlider.getValue()));
        });

        mediaPlayer.currentTimeProperty().addListener((obs, oldTime, newTime) -> {
            videoSlider.setValue(newTime.toSeconds());

            int current = (int) newTime.toSeconds();
            int total = (int) mediaPlayer.getTotalDuration().toSeconds();

            timeLabel.setText(formatTime(current) + " / " + formatTime(total));
        });

        Button playPause = createPlayPauseButton(mediaPlayer);
        MenuButton qualityMenu = createQualityMenu();

        HBox videoControls = new HBox(10, playPause, videoSlider, timeLabel, qualityMenu);

        return new VBox(10, mediaView, videoControls);
    }

    private static Button createPlayPauseButton(MediaPlayer mediaPlayer) {
        Button playPause = new Button("Play");

        playPause.setOnAction(e -> {
            if (mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING) {
                mediaPlayer.pause();
                playPause.setText("Play");
            } else {
                mediaPlayer.play();
                playPause.setText("Pause");
            }
        });

        return playPause;
    }

    private static MenuButton createQualityMenu() {
        MenuButton qualityMenu = new MenuButton("Quality");

        MenuItem q240 = new MenuItem("240p");
        MenuItem q480 = new MenuItem("480p");
        MenuItem q720 = new MenuItem("720p");
        MenuItem q1080 = new MenuItem("1080p");

        qualityMenu.getItems().addAll(q240, q480, q720, q1080);

        return qualityMenu;
    }

    private static HBox createButtons(Post post, User currentUser) {
        Label likesLabel = new Label(String.valueOf(post.getLikesCount()));
        Label viewsLabel = new Label(String.valueOf(post.getViewCount()));

        Button likeButton = createLikeButton(post, currentUser, likesLabel);
        Button commentButton = createCommentButton(post, currentUser);
        Button share = createShareButton(post);

        HBox buttons = new HBox(10, likeButton, likesLabel, commentButton, viewsLabel, share);

        if (currentUser != null && post.getAuthor().getId() == currentUser.getId()) {
            Button deleteButton = createDeleteButton(post);
            buttons.getChildren().add(deleteButton);

            if (post.getAuthor() instanceof PremiumUser) {
                Button editButton = createEditButton(post);
                buttons.getChildren().add(editButton);
            }

        } else {
            Button report = createReportButton(post, currentUser);
            buttons.getChildren().add(report);
        }

        return buttons;
    }

    private static Button createLikeButton(Post post, User currentUser, Label likesLabel) {
        Image heartImg = new Image(PostviewBuilder.class.getResourceAsStream("/img/favorite_480px.png"));
        ImageView heartView = new ImageView(heartImg);

        heartView.setFitWidth(20);
        heartView.setFitHeight(20);

        Button likeButton = new Button();
        likeButton.setGraphic(heartView);

        if (currentUser != null && post.getLikeIds().contains(currentUser.getId())) {
            likeButton.getStyleClass().add("like");
        }

        likeButton.setOnAction(e -> {
            if (currentUser == null) {
                return;
            }

            if (post.getLikeIds().contains(currentUser.getId())) {
                post.disLike();
                post.getLikeIds().remove(Integer.valueOf(currentUser.getId()));
                currentUser.getLikedPosts().remove(Integer.valueOf(post.getId()));
                likeButton.getStyleClass().remove("like");
                PostService.getInstance().updatePost(post);
            } else {
                post.like();
                post.getLikeIds().add(currentUser.getId());
                currentUser.getLikedPosts().add(post.getId());
                UserService.getInstance().favoriteHashtagsFromPost(currentUser, post);
                likeButton.getStyleClass().add("like");
                PostService.getInstance().updatePost(post);
            }

            likesLabel.setText(String.valueOf(post.getLikesCount()));
        });

        return likeButton;
    }

    private static Button createCommentButton(Post post, User currentUser) {
        Image commentImg = new Image(PostviewBuilder.class.getResourceAsStream("/img/eye_96px.png"));
        ImageView commentView = new ImageView(commentImg);
        commentView.setFitWidth(20);
        commentView.setFitHeight(20);
        Button commentButton = new Button();
        commentButton.setGraphic(commentView);
        commentButton.setOnAction(e -> {
            try {
                if (currentUser != null && !post.getViewIds().contains(currentUser.getId())) {
                    post.view();
                    post.getViewIds().add(currentUser.getId());
                    PostService.getInstance().updatePost(post);
                }
                FXMLLoader loader = new FXMLLoader(Main.class.getResource("replyPost-view.fxml"));
                Scene scene = new Scene(loader.load());

                ReplyPostController controller = loader.getController();
                controller.setParentPost(post);

                Main.setMainStage(scene);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });

        return commentButton;
    }

    private static Button createShareButton(Post post) {
        Image shareImg = new Image(PostviewBuilder.class.getResourceAsStream("/img/share_480px.png"));
        ImageView shareView = new ImageView(shareImg);
        shareView.setFitWidth(20);
        shareView.setFitHeight(20);
        Button share = new Button();
        share.setGraphic(shareView);
        share.setOnAction(e -> {
            String link = "https://x.com/Posts/" + post.getId();

            TextField linkField = new TextField(link);
            VBox shareBox = new VBox(10, new Label("Post link:"), linkField);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Share Post");
            alert.setHeaderText("Share this post");
            alert.getDialogPane().setContent(shareBox);
            alert.showAndWait();
        });

        return share;
    }

    private static Button createReportButton(Post post, User currentUser) {
        Image reportImg = new Image(PostviewBuilder.class.getResourceAsStream("/img/report.png"));
        ImageView reportView = new ImageView(reportImg);
        reportView.setFitWidth(20);
        reportView.setFitHeight(20);
        Button report = new Button("report");
        report.setGraphic(reportView);
        report.setOnAction(e -> {
            if (currentUser == null) {
                return;
            }
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Report Post");
            dialog.setHeaderText("Report this post");
            dialog.setContentText("Reason:");

            dialog.showAndWait().ifPresent(reason -> {
                Report newReport = new Report(currentUser.getId(), post.getAuthor().getId(), post.getId(), reason);

                Database.getInstance().getReports().add(newReport);
            });
        });

        return report;
    }

    private static Button createDeleteButton(Post post) {
        Image deleteImg = new Image(PostviewBuilder.class.getResourceAsStream("/img/delet2.png"));
        ImageView deleteView = new ImageView(deleteImg);
        deleteView.setFitWidth(20);
        deleteView.setFitHeight(20);
        Button deleteButton = new Button("delete");
        deleteButton.setGraphic(deleteView);
        deleteButton.setOnAction(e -> {
            PostService.getInstance().removePost(post);

            if (post.getParentPostId() == null) {
                openHomePage();
            } else {
                openReplyPage(post);
            }
        });

        return deleteButton;
    }

    private static Button createEditButton(Post post) {
        Image editImg = new Image(PostviewBuilder.class.getResourceAsStream("/img/edit_graph_report_512px.png"));
        ImageView editView = new ImageView(editImg);
        editView.setFitWidth(20);
        editView.setFitHeight(20);
        Button editButton = new Button("edit");
        editButton.setGraphic(editView);

        editButton.setOnAction(e -> {
            if (post.getParentPostId() == null) {
                editPost(post);
            } else {
                editReplyPost(post);
            }

            post.setEdited(true);
        });

        return editButton;
    }


    private static void openHomePage() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("home-view.fxml"));
            Scene scene = new Scene(loader.load());

            Main.setMainStage(scene);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void openReplyPage(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("replyPost-view.fxml"));
            Scene scene = new Scene(loader.load());

            Post parent = PostService.getInstance().getPostById(post.getParentPostId());

            ReplyPostController controller = loader.getController();
            controller.setParentPost(parent);

            PostService.getInstance().removeAnswerPost(parent, post);

            Main.setMainStage(scene);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static ImageView createProfileImageView(User user) {
        ImageView imageView = new ImageView();

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

        return imageView;
    }

    private static boolean isImage(String path) {
        String lower = path.toLowerCase();
        return lower.endsWith(".png") || lower.endsWith(".jpg") || lower.endsWith(".jpeg");
    }

    private static boolean isVideo(String path) {
        String lower = path.toLowerCase();
        return lower.endsWith(".mp4") || lower.endsWith(".mov");
    }

    private static String formatTime(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static void editPost(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("newPost-view.fxml"));
            Scene scene = new Scene(loader.load());
            NewPostController controller = loader.getController();
            controller.setEditingPost(post);
            Main.setMainStage(scene);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void editReplyPost(Post post) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("replyPost-view.fxml"));
            Scene scene = new Scene(loader.load());

            Post parent = PostService.getInstance().getPostById(post.getParentPostId());

            ReplyPostController controller = loader.getController();
            controller.setParentPost(parent);
            controller.setEditingReply(post);

            Main.setMainStage(scene);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
