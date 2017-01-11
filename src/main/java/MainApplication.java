import models.Account;

import java.util.List;


public class MainApplication {

    private static String extractLogin(String[] args) {
        return args[0];
    }

    private static String extractPassword(String[] args) {
        return args[1];
    }

    private static void checkArguments(String[] args) {
        if (args.length != 2)
            throw new RuntimeException("Input Error. Please pass arguments [nik] [password] to log into your account!");
    }

    public static void main(String[] args) {
        try {
            checkArguments(args);
            Scraper scraper = Scraper.initializeWithProxy();
            scraper.performLoadLogInPage();
            scraper.performLogInIntoBankAccount(extractLogin(args), extractPassword(args));
            printAccountDetails(scraper.getAccounts());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void printAccountDetails(List<Account> accounts) {
        for (Account account : accounts)
            System.out.println(account.toString());
    }

}
