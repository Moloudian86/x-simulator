package exception;

public class UsernameAlreadyExistsException extends AuthenticationException {
    public UsernameAlreadyExistsException() {
        super("UsernameAlreadyExistsException");
    }
}
