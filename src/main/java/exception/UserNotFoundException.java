package exception;

public class UserNotFoundException extends AuthenticationException {
    public UserNotFoundException(){
        super("UserNotFoundException");
    }
}
