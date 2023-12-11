package executor;

import Backend.DatabaseManager;
import Backend.Password;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseManager db = new DatabaseManager("userdata");
        System.out.println(db.sqlConnection);

        Password pass = new Password("test", "test", "test");
        db.addNewPassword(pass);

    }
}


