package exceptions;


public class InvalidLogin extends RuntimeException {

    public InvalidLogin() {
        super("Entered login is invalid");
    }

}
