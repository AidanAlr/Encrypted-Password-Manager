/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Backend;

import java.util.ArrayList;
import java.util.Random;
import java.util.Objects;


public class Record {

    private static final char[] ALPHABET
            = {'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I',
            'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
            'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', 'a',
            'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's',
            't', 'u', 'v', 'w', 'x', 'y', 'z', '0', '1',
            '2', '3', '4', '5', '6', '7', '8', '9'};

    private String userName;
    private String password;
    private String account;

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password){
        if(password.contains(" ") || password.isEmpty()){
        throw new IllegalArgumentException("Password cannot contain whitespace");}
        this.password = password;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName){
        this.userName = userName;
    }

    public String getAccount(){
        return this.account;
    }
    public void setAccount(String account){
        this.account = account;
    }

    public void setPasswordString(String pass) throws IllegalArgumentException {

        this.password = pass;
    }

    public void setPasswordName(String name) throws IllegalArgumentException {
        if (name.isEmpty())
            throw new IllegalArgumentException("Password name cannot be empty");
        if(name.contains(" "))
            throw new IllegalArgumentException("Password name cannot contain whitespace");
        this.userName = name;
    }

    public Record(String userName, String password, String account) throws IllegalArgumentException{
        if (userName.isEmpty() || password.isEmpty() || account.isEmpty()) {
            throw new IllegalArgumentException("Blank fields are not allowed");
        }
        if(userName.contains(" ") || password.contains(" ")){
            throw new IllegalArgumentException("Password name cannot contain whitespace");}
        this.userName = userName;
        this.password = password;
        this.account = account;
    }

    public Record generate(int size, String userName, String account) throws IllegalArgumentException {
        // Check for invalid inputs
        if (userName.isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (size < 5) {
            throw new IllegalArgumentException("Password size must be greater than 5 characters.");
        }

        // Arraylist to store temp password
        ArrayList<Character> temp = new ArrayList<Character>(size);
        // Randomly select a character from the alphabet array and add it to the temp arraylist
        Random rand = new Random();
        for( int i = 0; i < size; i++ )
            temp.add(ALPHABET[rand.nextInt(ALPHABET.length)]);

        this.account = account;
        this.userName = userName;
        this.password = String.valueOf(temp);;
        return this;
    }

    public Record createPassword(String pass, String name) throws IllegalArgumentException {
        name = name.trim();
        pass = pass.trim();
        if (name.isEmpty())
            throw new IllegalArgumentException("Password name cannot be empty");
        if(pass.isEmpty())
            throw new IllegalArgumentException("Password cannot be empty");
        if(name.contains(" "))
            throw new IllegalArgumentException("Password name cannot contain whitespace");
        if(pass.contains(" "))
            throw new IllegalArgumentException("Password cannot contain whitespace");
        this.password = pass;
        this.userName = name;
        return this;
    }

    @Override
    public String toString() {
        return "Account: " + this.getAccount() +", UserName: " + this.getUserName() + ", Password: " + this.getPassword();
    }

    public boolean equals(Record p) {
        return p.password.equals(this.password) && p.userName.equals(this.userName) && p.account.equals(this.account);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 53 * hash + Objects.hashCode(this.userName);
        hash = 53 * hash + Objects.hashCode(this.password);
        return hash;
    }

}
