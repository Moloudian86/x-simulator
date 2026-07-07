package repository;

import connection.ConnectionDB;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostLikeRepository {

    public boolean add(int postId, int userId) {
        String sql = "INSERT INTO tblpost_likes(post_id,user_id) VALUES(?,?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean remove(int postId, int userId) {
        String sql = "DELETE FROM tblpost_likes WHERE post_id=? AND user_id=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ps.setInt(2, userId);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public List<Integer> getLikeIds(int postId) {
        List<Integer> likeIds = new ArrayList<>();
        String sql = "SELECT user_id FROM tblpost_likes WHERE post_id=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                likeIds.add(rs.getInt("user_id"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return likeIds;
    }
}
