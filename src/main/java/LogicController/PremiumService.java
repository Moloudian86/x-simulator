package LogicController;

import model.*;
import repository.UserRepository;

import java.util.List;


public class PremiumService {
    private static final PremiumService instance = new PremiumService();
    private final UserRepository userRepository = new UserRepository();


    public static PremiumService getInstance() {
        return instance;
    }

    public boolean buyBlueSubscription(User user) {
        if (user instanceof BlueUser || user instanceof GoldUser){
            return false;
        }
        return buySubscription(user, 9, "blue");
    }

    public boolean buyGoldSubscription(User user) {
        if (user instanceof GoldUser){
            return false;
        }
        if (user instanceof BlueUser){
            return buySubscription(user,11,"gold");
        }
        return buySubscription(user, 19, "gold");
    }
    //عدم رعایت اصل دوم : هر بار که نوع جدیدی اضافه شود باید باز instanceof بزنیم
    // راه حل : اسبفاده از چند ریختی

    private boolean buySubscription(User user, int price, String type) {
        if (user == null || user.getCredit() < price ) {
            return false;
        }

        User premiumUser;
        if (type.equals("blue")) {
            premiumUser = new BlueUser(user.getFullName(), user.getUsername(), user.getEmail(), user.getPhone(), user.getPassword());
            premiumUser.setBadgeImagePath("/img/blue_tik.png");
            premiumUser.setAccType("blue");
        } else {
            premiumUser = new GoldUser(user.getFullName(), user.getUsername(), user.getEmail(), user.getPhone(), user.getPassword());
            premiumUser.setBadgeImagePath("/img/gold_tik.png");
            premiumUser.setAccType("gold");

        }
        copyUserData(user, premiumUser);
        premiumUser.setCredit(user.getCredit() - price);
        premiumUser.setToken(user.getToken() + 3000);
        replaceUserInDatabase(user, premiumUser);
        replaceAuthorInPosts(user, premiumUser);
        UserService.setCurrentUser(premiumUser);
        userRepository.update(premiumUser.getId(),premiumUser);
        return true;
    }

    private void copyUserData(User oldUser, User newUser) {
        newUser.setId(oldUser.getId());
        newUser.setJoinDate(oldUser.getJoinDate());
        newUser.setProfileImage(oldUser.getProfileImage());
        newUser.setBio(oldUser.getBio());
        newUser.setBlocked(oldUser.isBlocked());
        newUser.getPosts().addAll(oldUser.getPosts());
        newUser.getFollowers().addAll(oldUser.getFollowers());
        newUser.getFollowing().addAll(oldUser.getFollowing());
        newUser.getLikedPosts().addAll(oldUser.getLikedPosts());
        newUser.getFavoriteHashtags().addAll(oldUser.getFavoriteHashtags());

    }

    private void replaceUserInDatabase(User oldUser, User newUser) {
        List<User> users = userRepository.findAll();
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId() == oldUser.getId()) {
                users.set(i, newUser);
                return;
            }
        }
    }

    private void replaceAuthorInPosts(User oldUser, User newUser) {
        for (Post post : Database.getInstance().getPosts()) {
            if (post.getAuthor().getId() == oldUser.getId()) {
                post.setAuthor(newUser);
            }
        }
    }

    public boolean buyToken(User user, int dollarAmount){
        if (user == null){
            return false;
        }
        if (dollarAmount <= 0){
            return false;
        }
        if (user.getCredit() < dollarAmount){
            return false;
        }
        int addToken = dollarAmount*1000;
        user.setCredit(user.getCredit() - dollarAmount);
        user.setToken(user.getToken()+addToken);
        return true;
    }
}
