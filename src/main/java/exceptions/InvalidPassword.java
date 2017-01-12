package exceptions;


public class InvalidPassword extends RuntimeException {

    public InvalidPassword() {
        super("Entered password is invalid");
    }

}
