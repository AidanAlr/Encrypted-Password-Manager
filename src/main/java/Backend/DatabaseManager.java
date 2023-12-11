package Backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class DatabaseManager {
    public static String filename = "userdata";

    public static Connection sqlConnection;

    static {
        try {
            sqlConnection = DriverManager.getConnection("jdbc:sqlite:" + filename);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public DatabaseManager() throws SQLException{
        try {
            if (sqlConnection != null){
            System.out.println("Connected to " + filename);
            this.createTable();
            }

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

    public void addNewPassword(Record record) throws SQLException {
        String query = "INSERT INTO passwords (account, username, password) VALUES (?, ?, ?)";
        try (PreparedStatement insert = sqlConnection.prepareStatement(query)) {
            insert.setString(1, record.getAccount());
            insert.setString(2, record.getUserName());
            insert.setString(3, record.getPassword());
            insert.executeUpdate();
            System.out.println("Password added");
        } catch (SQLException e) {
            System.out.println(e);
        }

    }

    public Record getPasswords(String userName) throws SQLException {

        ResultSet results;

        String query = "SELECT * from passwords where username = ?";

        PreparedStatement getPassword = sqlConnection.prepareStatement(query);
        getPassword.setString(1, userName);

        results = getPassword.executeQuery();

        String name = results.getString("USERNAME");
        String password = results.getString("PASSWORD");
        String account = results.getString("ACCOUNT");

        getPassword.closeOnCompletion();
        return new Record(account, userName, password);
    }

    public ArrayList<Record> getAllRecords() throws SQLException {
        ArrayList<Record> passwordsArrayList = new ArrayList<>();
        String query = "SELECT * FROM passwords";
        PreparedStatement getPasswords = sqlConnection.prepareStatement(query);
        ResultSet results = getPasswords.executeQuery();
        System.out.println(results);

        while (results.next()) {
            String account = results.getString("ACCOUNT");
            String name = results.getString("USERNAME");
            String password = results.getString("PASSWORD");
            Record row = new Record(account, name, password);
            System.out.println(row);

            passwordsArrayList.add(row);
        }

        return passwordsArrayList;
    }


    public void storePasswords(ArrayList<Record> records) throws SQLException {
        PreparedStatement storePasswords;
        String query = "INSERT INTO passwords (account, username,password) VALUES(?, ?, ?)";
        storePasswords = sqlConnection.prepareStatement(query);
        for (Record p : records) {
            storePasswords.setString(1, p.getAccount());
            storePasswords.setString(2, p.getUserName());
            storePasswords.setString(3, p.getPassword());
            storePasswords.executeUpdate();
        }
        if (storePasswords != null)
            storePasswords.close();
    }

    public void removePassword(String account) throws SQLException {
        String query = "DELETE FROM passwords where account= ?";
        PreparedStatement removePassword = sqlConnection.prepareStatement(query);

        removePassword.setString(1, account);
        removePassword.executeUpdate();
        removePassword.close();
    }

    public void renameRecord(String oldName, String newName) throws SQLException {
        PreparedStatement renamePassword;
        String query = "UPDATE passwords set account = ? where account = ?";
        renamePassword = sqlConnection.prepareStatement(query);
        renamePassword.setString(1, newName);
        renamePassword.setString(2, oldName);
        renamePassword.executeUpdate();
        renamePassword.close();
    }

    public void clearPasswords() throws SQLException {
        PreparedStatement clear = sqlConnection.prepareStatement("DELETE FROM passwords");
        clear.executeUpdate();
        clear.close();
    }

    public void updatePassword(Record newP) throws SQLException {
        PreparedStatement update;
        String query = "UPDATE passwords set password = ? where username = ? and account = ?";
        update = sqlConnection.prepareStatement(query);
        update.setString(1, newP.getPassword());
        update.setString(2, newP.getUserName());
        update.setString(3, newP.getAccount());

        update.executeUpdate();
    }

    public void closeDatabase() throws SQLException {
        sqlConnection.close();
    }

}