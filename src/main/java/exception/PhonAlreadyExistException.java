package exception;

public class PhonAlreadyExistException extends AuthenticationException {
    public PhonAlreadyExistException() {
        super("PhonAlreadyExistException");
    }
}
