import java.util.List;

/**
 * Created by sgorokh on 05.01.17.
 */
public class MaskedPasswordCreator {
    private MaskedPasswordCreator(){}

    public static String getMaskedPassword(String password, List<Integer> passwordPositions) {
        String passwordLetters = "";
        for (int pos : passwordPositions){
            if (pos - 1 >= password.length()){
                break;
            }
            passwordLetters += password.charAt(pos - 1);
        }
        return passwordLetters;
    }
}
