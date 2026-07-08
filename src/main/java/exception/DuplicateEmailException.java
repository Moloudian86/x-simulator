package exception;

public class DuplicateEmailException extends AuthenticationException {
    public DuplicateEmailException(){
        super("DuplicateEmailException");
    }
}
