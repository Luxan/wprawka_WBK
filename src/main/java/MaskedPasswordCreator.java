import java.util.List;


public class MaskedPasswordCreator {

    public static String getMaskedPassword(String password, List<Integer> passwordPositions) {
        StringBuilder builder = new StringBuilder();
        passwordPositions.stream()
                .filter(p -> p - 1 < password.length())
                .map(p -> password.charAt(p - 1))
                .forEach(builder::append);
        return builder.toString();
    }

}
