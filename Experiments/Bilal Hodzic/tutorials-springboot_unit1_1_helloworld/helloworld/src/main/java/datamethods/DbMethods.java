package datamethods;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbMethods {
    public Connection getConnection() throws SQLException {
        Connection conn;
        String username = "bhodzic309";
        String password = "snail123";
        String dbServer = "jdbc:mysql://localhost:3306/309Testing";
        conn = DriverManager.getConnection(dbServer,username,password);
        System.out.println("Connection established");
        return conn;
    }

    public List<Person> GetAllPersons() throws SQLException{
        Connection conn = getConnection();
        String sql = "CALL p_get_persons()";
        Statement statement = conn.createStatement();
        ResultSet rs = statement.executeQuery(sql);
        List<Person> ret = new ArrayList<>();
        while(rs.next()) {
            ret.add(new Person(rs.getString("First_name"),
                    rs.getString("Last_name"), rs.getString("person_id")));
        }
        return ret;
    }
    public void InsertNewPerson(Person newPerson) throws SQLException{
        Connection conn = getConnection();
        String sql =String.format("CALL p_insert_person('%s', '%s', '%s')",
                newPerson.First_Name, newPerson.Last_Name, newPerson.Person_Id );
        Statement statement = conn.createStatement();
        statement.execute(sql);
    }
}
