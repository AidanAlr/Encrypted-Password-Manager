/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Backend;

import java.util.Objects;


public class Record {

    public static final char[] ALPHABET
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


    public Record(String account, String userName, String password){
        if (userName.isEmpty() || password.isEmpty() || account.isEmpty()) {
            throw new IllegalArgumentException("Blank fields are not allowed");
        }
        if(userName.contains(" ") || password.contains(" ")){
            throw new IllegalArgumentException("Password name cannot contain whitespace");}
        this.userName = userName;
        this.password = password;
        this.account = account;
    }

    @Override
    public String toString() {
        return "Record with Account: " + this.getAccount();
    }

    public boolean equals(Record p) {
        return p.password.equals(this.password) && p.userName.equals(this.userName) && p.account.equals(this.account);
    }

}
