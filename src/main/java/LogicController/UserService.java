package LogicController;

import model.*;


public class UserService {
    private static final UserService instance = new UserService();
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
        for (User u : Database.getInstance().getUsers()){
            if (u.getUsername().equals(username) ||u.getEmail().equals(email) ||u.getPhone().equals(phone)){
                return false;
            }
        }
        NormalUser user = new NormalUser(fullName,username,email,phone, password);
        Database.getInstance().getUsers().add(user);
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


    public boolean Login (String username, String password){
        for (User u : Database.getInstance().getUsers()) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                if (u.isBlocked()){
                    return false;
                }
                currentUser = u;
                return true;
            }
        }
        return false;
    }

    public boolean editProfile(String fullName,String username,String email,String phone, String password,User user ,String bio){

        for (User u : Database.getInstance().getUsers()){
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
        currentUser = user;
        return true;
    }


    public boolean follow(User profileUser){
        if (!UserService.getCurrentUser().getFollowing().contains(profileUser.getId())){
            profileUser.getFollowers().add(UserService.getCurrentUser().getId());
            UserService.getCurrentUser().getFollowing().add(profileUser.getId());
            return true;
        }else{
            profileUser.getFollowers().remove(Integer.valueOf(UserService.getCurrentUser().getId()));
            UserService.getCurrentUser().getFollowing().remove(Integer.valueOf(profileUser.getId()));
            return false;
        }
    }

    public User getUserById(int id){
        for(User user : Database.getInstance().getUsers()){
            if (user.getId() == id){
                return user;
            }
        }
        return null;
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
