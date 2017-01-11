package models;


public class Account {

    public final String name;
    public final String balance;

    public Account(String name, String balance) {
        this.name = name;
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }

}
