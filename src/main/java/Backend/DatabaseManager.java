package Backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DatabaseManager {

    public Connection sqlConnection = null;

    public DatabaseManager(String filename) throws SQLException{
        try {
            sqlConnection = DriverManager.getConnection("jdbc:sqlite:" + filename);
            System.out.println("Connected to " + filename);
            this.createTable();

        }
        catch (SQLException e) {
            System.out.println(e);
            System.out.println("DB not found");
        }
    }

    private void createTable() throws SQLException {
        String table = "CREATE TABLE IF NOT EXISTS passwords(" +
                "account TEXT PRIMARY KEY NOT NULL," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL)";
        try {
            Statement stmt = sqlConnection.createStatement();
            stmt.executeUpdate(table);
            stmt.close();
            System.out.println("passwords table created");
        }
        catch (SQLException e) {
            System.out.println(e);
            System.out.println("Found passwords table");
        }
    }

    public void addNewPassword(Password password) throws SQLException {
        String query = "INSERT INTO passwords (account, username, password) VALUES (?, ?, ?)";
        try (PreparedStatement insert = this.sqlConnection.prepareStatement(query)) {
            insert.setString(1, password.getAccount());
            insert.setString(2, password.getUserName());
            insert.setString(3, password.getPassword());
            insert.executeUpdate();
            System.out.println("Password added");
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    public Password getPasswords(String userName) throws SQLException {

        ResultSet results;

        String query = "SELECT * from passwords where username = ?";

        PreparedStatement getPassword = this.sqlConnection.prepareStatement(query);
        getPassword.setString(1, userName);
        results = getPassword.executeQuery();

        String name = results.getString("USERNAME");
        String password = results.getString("PASSWORD");
        String account = results.getString("ACCOUNT");

        getPassword.closeOnCompletion();
        return new Password(account, userName, password);
    }

    public ArrayList<Password> getPasswords() throws SQLException {
        ArrayList<Password> passwordsArrayList = new ArrayList<>();
        String query = "SELECT * FROM passwords";
        PreparedStatement getPasswords = this.sqlConnection.prepareStatement(query);
        ResultSet results = getPasswords.executeQuery();

        while (results.next()) {
            String account = results.getString("ACCOUNT");
            String name = results.getString("NAME");
            String password = results.getString("PASSWORD");
            Password row = new Password(account,name, password);
            passwordsArrayList.add(row);
        }
        getPasswords.closeOnCompletion();
        return passwordsArrayList;
    }

    public void storePasswords(ArrayList<Password> passwords) throws SQLException {
        PreparedStatement storePasswords;
        String query = "INSERT INTO passwords (account, username,password) VALUES(?, ?, ?)";
        storePasswords = this.sqlConnection.prepareStatement(query);
        for (Password p : passwords) {
            storePasswords.setString(1, p.getAccount());
            storePasswords.setString(2, p.getUserName());
            storePasswords.setString(3, p.getPassword());
            storePasswords.executeUpdate();
        }
        if (storePasswords != null)
            storePasswords.close();
    }

    public void removePassword(String userName) throws SQLException {
        String query = "DELETE FROM passwords where username= ?";
        PreparedStatement removePassword = this.sqlConnection.prepareStatement(query);

        removePassword.setString(1, userName);
        removePassword.executeUpdate();
        removePassword.close();
    }

    public void renamePassword(String oldName, String newName) throws SQLException {
        PreparedStatement renamePassword;
        String query = "UPDATE passwords set username = ? where username = ?";
        renamePassword = this.sqlConnection.prepareStatement(query);
        renamePassword.setString(1, newName);
        renamePassword.setString(2, oldName);
        renamePassword.executeUpdate();
        renamePassword.close();
    }

    public void clearPasswords() throws SQLException {
        PreparedStatement clear = this.sqlConnection.prepareStatement("DELETE FROM passwords");
        clear.executeUpdate();
        clear.close();
    }

    public void updatePassword(Password newP) throws SQLException {
        PreparedStatement update;
        String query = "UPDATE passwords set password = ? where username = ? and account = ?";
        update = this.sqlConnection.prepareStatement(query);
        update.setString(1, newP.getPassword());
        update.setString(2, newP.getUserName());
        update.setString(3, newP.getAccount());

        update.executeUpdate();
    }

    public void closeDatabase() throws SQLException {
        this.sqlConnection.close();
    }

}