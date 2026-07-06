package repository;

import connection.ConnectionDB;
import model.Hashtag;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostHashtagRepository {

    private final HashtagRepository hashtagRepository = new HashtagRepository();
    public boolean add(int postId, int hashtagId) {
        String sql = "INSERT INTO tblpost_hashtags(post_id, hashtag_id) VALUES (?, ?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ps.setInt(2, hashtagId);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public boolean removeByPost(int postId) {
        String sql = "DELETE FROM tblpost_hashtags WHERE post_id=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, postId);

            return ps.executeUpdate() > 0;

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return false;
    }

    public List<Hashtag> getPostHashtags(int postId) {
        List<Hashtag> hashtags = new ArrayList<>();
        String sql = "SELECT hashtag_id FROM tblpost_hashtags WHERE post_id=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, postId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Hashtag hashtag = hashtagRepository.findById(rs.getInt("hashtag_id"));
                if (hashtag != null){
                    hashtags.add(hashtag);

                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return hashtags;
    }

    public void updatePostHashtags(int postId, List<Hashtag> hashtags) {
        removeByPost(postId);
        for (Hashtag hashtag : hashtags) {
            add(postId, hashtag.getId());
        }
    }

}