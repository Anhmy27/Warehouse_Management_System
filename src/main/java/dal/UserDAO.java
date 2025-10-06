package dal;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.User;

public class UserDAO extends DBContext {

    public User loginByEmail(String email, String password) {
        String sql = "SELECT * FROM [user] WHERE email=? AND password=? AND status='active'";
        try {
            PreparedStatement st = connection.prepareStatement(sql);
            st.setString(1, email);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            if (rs.next()) {
                User u = new User(
                        rs.getString("uid"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("fullname"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("gender"),
                        rs.getDate("dateofbirth"),
                        rs.getString("address"),
                        rs.getString("image"),
                        rs.getString("status"),
                        rs.getString("rid"),
                        rs.getString("wid")
                );
                return u;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

}
