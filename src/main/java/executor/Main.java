package executor;

import Backend.DatabaseManager;
import Backend.Record;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseManager db = new DatabaseManager("src/main/resources/userdata");
        System.out.println(db.sqlConnection);

        Record pass = new Record("test", "test", "test");
        db.addNewPassword(pass);

    }
}


