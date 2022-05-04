package helper;

public class TooManyOptionsException extends Exception{
    public TooManyOptionsException(String message){
        super(message);
    }
    public TooManyOptionsException(){
        super();
    }
}
