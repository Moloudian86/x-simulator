package repository;

import connection.ConnectionDB;
import interfaces.IRepository;
import model.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class UserRepository implements IRepository<User> {
    @Override
    public boolean add(User user) {
        String sql = "INSERT INTO tblusers" +
                "(username, password, fullName, email, phone, bio, followers_count, following_count, profileImage, credit, token, blocked, joinDate,accType)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getBio());
            ps.setInt(7, user.getFollowers().size());
            ps.setInt(8, user.getFollowing().size());
            ps.setString(9, user.getProfileImage());
            ps.setInt(10, user.getCredit());
            ps.setInt(11, user.getToken());
            ps.setBoolean(12, user.isBlocked());

            ps.setTimestamp(13, new java.sql.Timestamp(user.getJoinDate().getTime()));
            ps.setString(14,user.getAccType());

            return ps.executeUpdate() > 0;

        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean remove(int id) {
        String sql = "DELETE FROM tblusers WHERE id = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(int id, User user) {
        String sql = "UPDATE tblusers SET " +
                "username=?, password=?, fullName=?, email=?, phone=?, bio=?, " +
                "profileImage=?, accType=?, followers_count=?, following_count=?, credit=?, token=?, blocked=? " +
                "WHERE id=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1,user.getUsername());
            ps.setString(2,user.getPassword());
            ps.setString(3,user.getFullName());
            ps.setString(4,user.getEmail());
            ps.setString(5,user.getPhone());
            ps.setString(6,user.getBio());
            ps.setString(7,user.getProfileImage());
            ps.setString(8,user.getAccType());
            ps.setInt(9, user.getFollowers().size());
            ps.setInt(10, user.getFollowing().size());
            ps.setInt(11, user.getCredit());
            ps.setInt(12, user.getToken());
            ps.setBoolean(13, user.isBlocked());
            ps.setInt(14, id);

            return ps.executeUpdate() > 0;

        }catch (SQLException ex){
            ex.printStackTrace();
        }

        return false;
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        String sql = "select * from tblusers";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                users.add(map(rs));
            }

        }catch (SQLException ex){
            ex.printStackTrace();
        }

        return users;
    }

    @Override
    public User findById(int id) {
        String sql = "SELECT * FROM tblusers WHERE id=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public User login(String username, String password) {
        String sql = "SELECT * FROM tblusers WHERE username = ? AND password = ?";
        try (java.sql.Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    private User map(ResultSet rs) throws SQLException {
        String type = rs.getString("accType");
        User user;

        if ("gold".equals(type)) {
            user = new GoldUser(
                    rs.getString("fullName"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("password")
            );
        }
        else if ("blue".equals(type)){
            user = new BlueUser(rs.getString("fullName"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("password"));
        }
        else {
            user = new NormalUser(
                    rs.getString("fullName"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("password")
            );
        }

        user.setId(rs.getInt("id"));
        user.setBio(rs.getString("bio"));
        user.setCredit(rs.getInt("credit"));
        user.setToken(rs.getInt("token"));
        user.setBlocked(rs.getBoolean("blocked"));
        user.setProfileImage(rs.getString("profileImage"));
        user.setAccType(type);
        user.setJoinDate(rs.getTimestamp("joinDate"));


        return user;
    }

}
