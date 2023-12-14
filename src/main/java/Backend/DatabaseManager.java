package Backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseManager {
    // File name for the SQLite database
    public static String filename = "userdata";

    // Connection object for the database
    public static Connection sqlConnection;

    static {
        try {
            sqlConnection = DriverManager.getConnection("jdbc:sqlite:" + filename);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    ;

    public DatabaseManager() throws SQLException {
        try {
            // Establishes a connection to the database
            // If connection is successful, create the table
            if (sqlConnection != null && !passwordsTableExists()) {
                this.createTable();
            }
        } catch (SQLException e) {
            System.out.println(e);
            System.out.println("Database not found");
        }
    }

    public boolean passwordsTableExists() throws SQLException {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='passwords'";
        Statement stmt = sqlConnection.createStatement();
        ResultSet results = stmt.executeQuery(query);
        return results.next();
    }

    // Method to create the passwords table if it doesn't exist
    private void createTable() throws SQLException {
        // SQL statement for creating a new table
        String table = "CREATE TABLE IF NOT EXISTS passwords (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "account TEXT NOT NULL," +
                "username TEXT NOT NULL," +
                "password TEXT NOT NULL)";
        try {
            Statement stmt = sqlConnection.createStatement();
            stmt.executeUpdate(table);
            stmt.close();
            System.out.println("Created table");
        } catch (SQLException e) {
            System.out.println(e);
        }
    }

    // Method to add a new record to the database
    public void addNewRecord(Record record) throws SQLException {
        // SQL query for inserting a new record
        String query = "INSERT INTO passwords (account, username, password) VALUES (?, ?, ?)";
        try (PreparedStatement insert = sqlConnection.prepareStatement(query)) {
            insert.setString(1, record.getAccount());
            insert.setString(2, record.getUserName());
            insert.setString(3, EncryptionHelper.encrypt(record.getPassword()));
            insert.executeUpdate();
            System.out.println("Added new Record");
        } catch (SQLException e) {
            System.out.println(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // Method to fetch a specific record based on account and username
    public Record getRecord(String account, String userName) throws Exception {
        String query = "SELECT * from passwords where account = ? and username = ? LIMIT 1";
        PreparedStatement stmt = sqlConnection.prepareStatement(query);
        stmt.setString(1, account);
        stmt.setString(2, userName);

        ResultSet results = stmt.executeQuery();

        if (results.next()) {
            String name = results.getString("USERNAME");
            String password = EncryptionHelper.decrypt(results.getString("PASSWORD"));
            account = results.getString("ACCOUNT");

            stmt.closeOnCompletion();
            System.out.println("Found queried record");
            return new Record(account, userName, password);
        } else {
            stmt.closeOnCompletion();
            return null; // Return null if no record is found
        }
    }

    // Method to fetch all records from the database
    public ArrayList<Record> getAllRecords() throws Exception {
        ArrayList<Record> passwordsArrayList = new ArrayList<>();
        String query = "SELECT * FROM passwords";
        PreparedStatement getPasswords = sqlConnection.prepareStatement(query);
        ResultSet results = getPasswords.executeQuery();

        while (results.next()) {
            String account = results.getString("ACCOUNT");
            String name = results.getString("USERNAME");
            String password = results.getString("PASSWORD");
            Record row = new Record(account, name, password);
            passwordsArrayList.add(row);
        }

        System.out.println("Collected All Records");
        return passwordsArrayList;
    }

    public void storeRecords(ArrayList<Record> records) throws SQLException {
        PreparedStatement stmt;
        String query = "INSERT INTO passwords (account, username,password) VALUES(?, ?, ?)";
        stmt = sqlConnection.prepareStatement(query);
        for (Record p : records) {
            stmt.setString(1, p.getAccount());
            stmt.setString(2, p.getUserName());
            stmt.setString(3, p.getPassword());
            stmt.executeUpdate();
        }
        if (stmt != null)
            stmt.close();
    }

    public void removeRecord(String account) throws SQLException {
        String query = "DELETE FROM passwords where account= ?";
        PreparedStatement removePassword = sqlConnection.prepareStatement(query);
        removePassword.setString(1, account);
        removePassword.executeUpdate();
        removePassword.close();
        System.out.println("removed record with account: " + account);
    }

    public void updateAccount(String oldName, String newName) throws SQLException {
        PreparedStatement renamePassword;
        String query = "UPDATE passwords set account = ? where account = ?";
        renamePassword = sqlConnection.prepareStatement(query);
        renamePassword.setString(1, newName);
        renamePassword.setString(2, oldName);
        renamePassword.executeUpdate();
        renamePassword.close();
        System.out.println("Updated account");
    }

    public void clearRecords() throws SQLException {
        PreparedStatement clear = sqlConnection.prepareStatement("DELETE FROM passwords");
        clear.executeUpdate();
        clear.close();
    }

    public void updatePassword(String account, String newP) throws SQLException {
        PreparedStatement update;
        String query = "UPDATE passwords set password = ? where account = ?";
        update = sqlConnection.prepareStatement(query);
        update.setString(1, newP);
        update.setString(2, account);
        update.executeUpdate();
        System.out.println("Updated password");
    }

    public void updateUsername(String account, String username) throws SQLException {
        PreparedStatement update;
        String query = "UPDATE passwords set username = ? where account = ?";
        update = sqlConnection.prepareStatement(query);
        update.setString(1, username);
        update.setString(2, account);
        update.executeUpdate();
        System.out.println("Updated username");
    }

    public void closeDatabase() throws SQLException {
        sqlConnection.close();
    }

}