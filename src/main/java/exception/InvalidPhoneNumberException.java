package exception;

public class InvalidPhoneNumberException extends AuthenticationException {
    public InvalidPhoneNumberException(){
        super("InvalidPhoneNumberException");
    }
}
