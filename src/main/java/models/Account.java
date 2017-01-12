package models;


public class Account {

    public String name;
    public String balance;

    @Override
    public String toString() {
        return "Account{" +
                "name='" + name + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }

}
