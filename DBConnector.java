package postgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DBConnector {

    private static String dbHost = "127.0.0.1"; // "localhost";
    private static  String dbPort = "5432";
    private static String dbUserName = "prasad_admin";
    private static String dbPassword = "mbprasad";
    private static String dbName = "banking";


    public DBConnector() {}

    public Connection getConnection() {


        String connectionString = "jdbc:postgresql://"+dbHost+":"+dbPort+"/"+dbName;
        Connection c = null;
        try {
          // Class.forName("org.postgresql.Driver");

            c = DriverManager
                    .getConnection(connectionString,
                            dbUserName, dbPassword);
            PreparedStatement ps=c.prepareStatement("select * from customer");
            ResultSet rs=ps.executeQuery();
            System.out.println(rs.getString("id"));
            System.out.println(rs.getString("user_name"));
            System.out.println(rs.getString("password"));
            System.out.println(rs.getString("email"));
            System.out.println(rs.getString("phone"));
            System.out.println(rs.getString("name"));



        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        //System.out.println("Opened database successfully");
        return c;
    }

}
