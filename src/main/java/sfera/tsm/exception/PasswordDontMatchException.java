package sfera.tsm.exception;

public class PasswordDontMatchException extends RuntimeException {
    public PasswordDontMatchException(){
        super("Password don't match");
    }
}
