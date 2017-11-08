package servlet.registration.exceptions;

public class AlreadyExistException extends Exception {

    public AlreadyExistException(){
        super();
    }

    public AlreadyExistException(String s){
        super(s);
    }
}
