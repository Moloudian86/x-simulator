package LogicController;

import interfaces.IRepository;
import model.*;
import repository.UserRepository;

import java.util.List;


public class UserService {
    private static final UserService instance = new UserService();
    private final  UserRepository userRepository = new UserRepository();
    private static User currentUser;

    public static UserService getInstance() {
        return instance;
    }


    public boolean fillAllFields
            (String fullName,String username,String email,String phone, String password,String confirmPassword){
        if (fullName.isEmpty()|| username.isEmpty()|| email.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            return false;
        }
        return true;
    }

    public boolean fillAllFields
            (String username,String password){
        if (username.isEmpty()|| password.isEmpty()){
            return false;
        }
        return true;
    }

    public boolean checkPassword(String password,String confirmPassword){
        if (password.equals(confirmPassword)){
            return true;
        }
        else
            return false;
    }


    public boolean regexFullName(String fullName){
        if (fullName.matches("^[a-zA-Z]{3,50}$")) {
            return true;
        }
        else
            return false;

    }
    public boolean regexUsername(String username){
        if(username.matches("^[A-Za-z][A-za-z0-9]{3,20}$")){
            return true;
        }
        else
            return false;
    }

    public boolean regexEmail(String email){
        if (email.matches("^[A-Za-z0-9_.-]+@[A-Za-z0-9.-]+$")){
            return true;
        }
        else
            return false;
    }

    public boolean regexPhone(String phone){
        if (phone.matches("09[0-9]{9}")){
            return true;
        }
        else
            return false;
    }

    public boolean regexPassword(String password){
        if (password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,20}$")){
            return true;
        }
        else
            return false;
    }




    public boolean signUp(String fullName,String username,String email,String phone, String password){
        List<User> users = userRepository.findAll();
        for (User u : users){
            if (u.getUsername().equals(username) ||u.getEmail().equals(email) ||u.getPhone().equals(phone)){
                return false;
            }
        }
        NormalUser user = new NormalUser(fullName,username,email,phone, password);
        user.setAccType("normal");
        userRepository.add(user);
        currentUser = user;
        return true;
    }

    public boolean AdminLogin(String username, String password){
        Admin admin = Admin.getInstance();
        if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
            return true;
        }
        return false;
    }


    public boolean login(String username, String password){
        User user = userRepository.login(username, password);

        if (user == null) {
            return false;
        }

        if (user.isBlocked()) {
            return false;
        }

        currentUser = user;
        return true;
    }

    public boolean editProfile(String fullName,String username,String email,String phone, String password,User user ,String bio){
        List<User> users = userRepository.findAll();
        for (User u : users){
            //خود کاربرنباشه
            if (u.getId() != user.getId()) {

                if (u.getUsername().equals(username) ||
                        u.getEmail().equals(email) ||
                        u.getPhone().equals(phone)) {

                    return false;
                }
            }
        }

        user.setUsername(username);
        user.setEmail(email);
        user.setFullName(fullName);
        user.setPhone(phone);
        user.setBio(bio);
        user.setPassword(password);
        userRepository.update(user.getId(), user);
        currentUser = user;
        return true;
    }


    public boolean follow(User profileUser){
        if (!UserService.getCurrentUser().getFollowing().contains(profileUser.getId())){
            profileUser.getFollowers().add(UserService.getCurrentUser().getId());
            UserService.getCurrentUser().getFollowing().add(profileUser.getId());
            userRepository.update(profileUser.getId(),profileUser);
            userRepository.update(currentUser.getId(),currentUser);
            return true;
        }else{
            profileUser.getFollowers().remove(Integer.valueOf(UserService.getCurrentUser().getId()));
            UserService.getCurrentUser().getFollowing().remove(Integer.valueOf(profileUser.getId()));
            userRepository.update(profileUser.getId(), profileUser);
            userRepository.update(currentUser.getId(), currentUser);
            return false;
        }
    }

    public User getUserById(int id){
        return userRepository.findById(id);
    }

    //وقتی کاربر یه پستیو لایک میکنه
    public void favoriteHashtagsFromPost(User user, Post post) {
        for (Hashtag hashtag : post.getHashtags()){
            if (!user.getFavoriteHashtags().contains(hashtag.getId())) {
                user.getFavoriteHashtags().add(hashtag.getId());
            }
        }
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(User currentUser) {
        UserService.currentUser = currentUser;
    }
}
