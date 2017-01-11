package models;


public class PasswordRequestPostParameters {

    private final String password;
    private final String postUrlStr;
    private final String maskNumberStr;
    private final String maskLengthStr;

    public PasswordRequestPostParameters(String password, String postUrlStr) {
        this.password = password;
        this.postUrlStr = postUrlStr;
        this.maskNumberStr = "";
        this.maskLengthStr = "";
    }

    public PasswordRequestPostParameters(String password, String postUrlStr, String maskNumberStr,
                                         String maskLengthStr) {
        this.password = password;
        this.postUrlStr = postUrlStr;
        this.maskNumberStr = maskNumberStr;
        this.maskLengthStr = maskLengthStr;
    }

    public String getPassword() {
        return password;
    }

    public String getPostUrlStr() {
        return postUrlStr;
    }

    public String getMaskNumberStr() {
        return maskNumberStr;
    }

    public String getMaskLengthStr() {
        return maskLengthStr;
    }

}
