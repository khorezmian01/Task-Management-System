package sfera.tsm.exception;

public class TaskIsNotYourException extends RuntimeException{
    public TaskIsNotYourException(String message){
        super(message);
    }
}
