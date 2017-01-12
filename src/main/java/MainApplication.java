import models.Account;

import java.util.List;


class MainApplication {

    private static String extractLogin(String[] args) {
        return args[0];
    }

    private static String extractPassword(String[] args) {
        return args[1];
    }

    private static String extractProxyHostName(String[] args) {
        return args[2];
    }

    private static int extractProxyPort(String[] args) {
        return Integer.parseInt(args[3]);
    }

    private static void checkArguments(String[] args) {
        if (args.length < 2){
            String msg = "Input Error. Please pass arguments [nik] [password] to log into your account!\n" +
                    "Optionally if you want to set proxy pass [nik] [password] [proxyHostname] [proxyPort]";
            throw new RuntimeException(msg);
        }
    }

    private static void printAccountDetails(List<Account> accounts) {
        for (Account account : accounts)
            System.out.println(account.toString());
    }

    public static void main(String[] args) {
        try {
            checkArguments(args);
            Scraper scraper;
            if (isProxyArgumentsArePassed(args))
                scraper = Scraper.initializeWithProxy(extractProxyHostName(args), extractProxyPort(args));
            else
                scraper = Scraper.initialize();
            scraper.performLoadLogInPage();
            scraper.performLogInIntoBankAccount(extractLogin(args), extractPassword(args));
            printAccountDetails(scraper.getAccounts());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static boolean isProxyArgumentsArePassed(String[] args) {
        return args.length > 3;
    }

}
