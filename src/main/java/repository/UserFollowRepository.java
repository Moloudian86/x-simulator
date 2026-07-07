package repository;

import connection.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserFollowRepository {

    public boolean follow(int followerId, int followingId) {
        String sql = "INSERT INTO tbluser_follow(follower_id,following_id) VALUES(?,?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, followerId);
            ps.setInt(2, followingId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean unfollow(int followerId, int followingId) {
        String sql = "DELETE FROM tbluser_follow WHERE follower_id=? AND following_id=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, followerId);
            ps.setInt(2, followingId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<Integer> getFollowingIds(int userId) {
        List<Integer> following = new ArrayList<>();
        String sql = "SELECT following_id FROM tbluser_follow WHERE follower_id=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                following.add(rs.getInt("following_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return following;
    }

    public List<Integer> getFollowerIds(int userId) {
        List<Integer> followers = new ArrayList<>();
        String sql = "SELECT follower_id FROM tbluser_follow WHERE following_id=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                followers.add(rs.getInt("follower_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return followers;
    }
}