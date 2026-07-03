package UiController;

import LogicController.UserService;
import LogicController.HashtagService;
import com.example.twitter.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

import java.io.IOException;

public class HashtagController {

    private boolean art = false;
    private boolean gaming = false;
    private boolean eduction = false;
    private boolean news = false;
    private boolean sports = false;
    private boolean technology = false;
    private int hashtagCount = 0;
    @FXML
    private Button artHashtag;

    @FXML
    private Button confirm;

    @FXML
    private Button eductionHashtag;

    @FXML
    private Button gamingHashtag;

    @FXML
    private Button newsHashtag;

    @FXML
    private Button sportsHashtag;

    @FXML
    private Button technologyHashtag;

    private void setColor(Button button){
        button.getStyleClass().add("clicked");
    }

    private void clearColor(Button button){
        button.getStyleClass().remove("clicked");
    }

    @FXML
    void artHashtagActio(ActionEvent event) {
        if (hashtagCount < 4){
            if (!art){
                UserService.getCurrentUser().getFavoriteHashtags().add(HashtagService.getInstance().getART().getId());
                ++hashtagCount;
                art = true;
                setColor(artHashtag);
                return;
            }
        }
       if(art){
           UserService.getCurrentUser().getFavoriteHashtags().remove(Integer.valueOf(HashtagService.getInstance().getART().getId()));

           --hashtagCount;
           art = false;
           clearColor(artHashtag);
       }

    }

    @FXML
    void eductionHashtagAction(ActionEvent event) {
        if (hashtagCount <4){
            if (!eduction){
                UserService.getCurrentUser().getFavoriteHashtags().add(HashtagService.getInstance().getEDUCATION().getId());
                ++hashtagCount;
                eduction = true;
                setColor(eductionHashtag);
                return;
            }
        }

        if (eduction){
            UserService.getCurrentUser().getFavoriteHashtags().remove(Integer.valueOf(HashtagService.getInstance().getEDUCATION().getId()));
            --hashtagCount;
            eduction = false;
            clearColor(eductionHashtag);
        }

    }

    @FXML
    void gamingHashtagAction(ActionEvent event) {
        if (hashtagCount<4){
            if (!gaming){
                UserService.getCurrentUser().getFavoriteHashtags().add(HashtagService.getInstance().getGAMING().getId());
                ++hashtagCount;
                gaming = true;
                setColor(gamingHashtag);
                return;
            }
        }
        if (gaming){
            UserService.getCurrentUser().getFavoriteHashtags().remove(Integer.valueOf(HashtagService.getInstance().getGAMING().getId()));
            --hashtagCount;
            gaming = false;
            clearColor(gamingHashtag);
        }

    }

    @FXML
    void newsHashtagAction(ActionEvent event) {
        if (hashtagCount<4){
            if (!news){
                UserService.getCurrentUser().getFavoriteHashtags().add(HashtagService.getInstance().getNEWS().getId());
                ++hashtagCount;
                news = true;
                setColor(newsHashtag);
                return;
            }
        }
        if (news){
            UserService.getCurrentUser().getFavoriteHashtags().remove(Integer.valueOf(HashtagService.getInstance().getNEWS().getId()));
            --hashtagCount;
            news = false;
            clearColor(newsHashtag);
        }

    }

    @FXML
    void sportsHashtagAction(ActionEvent event) {
        if (hashtagCount < 4){

            if (!sports){
                UserService.getCurrentUser().getFavoriteHashtags().add(HashtagService.getInstance().getSPORTS().getId());
                ++hashtagCount;
                sports = true;
                setColor(sportsHashtag);
                return;

            }
        }
        if (sports){
            UserService.getCurrentUser().getFavoriteHashtags().remove(Integer.valueOf(HashtagService.getInstance().getSPORTS().getId()));
            --hashtagCount;
            sports = false;
            clearColor(sportsHashtag);
        }
    }

    @FXML
    void technologyHashtagAction(ActionEvent event) {
        if (hashtagCount < 4){
            if (!technology){
                UserService.getCurrentUser().getFavoriteHashtags().add(HashtagService.getInstance().getTECHNOLOGY().getId());
                ++hashtagCount;
                technology = true;
                setColor(technologyHashtag);
                return;

            }
        }
        if (technology){
            UserService.getCurrentUser().getFavoriteHashtags().remove(Integer.valueOf(HashtagService.getInstance().getTECHNOLOGY().getId()));
            --hashtagCount;
            technology = false;
            clearColor(technologyHashtag);
        }

    }

    @FXML
    void confirmAction(ActionEvent event) {
        if (hashtagCount == 0){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Error");
            alert.setContentText("Please select hashtags");
            alert.show();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("home-view.fxml"));
            Scene scene = new Scene(loader.load());
            Main.setMainStage(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}