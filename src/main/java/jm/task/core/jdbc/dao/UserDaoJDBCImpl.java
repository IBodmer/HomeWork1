package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import javax.transaction.Transactional;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
@Transactional
public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Statement st = Util.getConnection().createStatement()) {
            st.executeUpdate("CREATE TABLE IF NOT EXISTS users " +
                    "(id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY, " +
                    "user_name VARCHAR(50),last_name VARCHAR(50),age INT )");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void dropUsersTable() {
        try (Statement st = Util.getConnection().createStatement()) {
            st.executeUpdate("DROP TABLE IF EXISTS users");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement pst =
                     Util.getConnection().prepareStatement("INSERT INTO users (user_name,last_name,age)" +
                             " VALUES (?,?,?)")) {
            pst.setString(1, name);
            pst.setString(2, lastName);
            pst.setByte(3, age);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement pst = Util.getConnection().prepareStatement("DELETE FROM users WHERE id = ?")) {
            pst.setLong(1, id);
            pst.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public List<User> getAllUsers() {
        List<User> myList = new ArrayList<>();
        try (ResultSet rs = Util.getConnection().createStatement().executeQuery("SELECT * FROM users")) {
            while (rs.next()) {
                User user = new User(rs.getString("user_name"),
                        rs.getString("last_name"), rs.getByte("age"));
                myList.add(user);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return myList;
    }

    public void cleanUsersTable() {
        try(Statement st = Util.getConnection().createStatement()) {
            st.executeUpdate("TRUNCATE users");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
