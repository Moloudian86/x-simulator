package repository;

import connection.ConnectionDB;
import interfaces.IRepository;
import model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PostRepository implements IRepository<Post> {
    private final UserRepository userRepository = new UserRepository();


    @Override
    public boolean add(Post post) {
        String sql = "insert into tblposts" +
                "(author_id, content , parent_post_id, view_count, likes_count, blocked, media_path, total_duration, edited) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS)){

            ps.setInt(1,post.getAuthor().getId());
            ps.setString(2, post.getContent());
            if (post.getParentPostId() != null)
                ps.setInt(3, post.getParentPostId());
            else
                ps.setNull(3, Types.INTEGER);
            ps.setInt(4,post.getViewCount());
            ps.setInt(5,post.getLikesCount());
            ps.setBoolean(6, post.isBlocked());
            ps.setString(7, post.getMediaPath());
            ps.setInt(8, (int) post.getTotalDuration().toSeconds());
            ps.setBoolean(9, post.isEdited());

            int result = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                post.setId(rs.getInt(1));
            }
            return result > 0;

        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean remove(int id) {
        String sql = "DELETE FROM tblposts WHERE id=?";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(int id, Post post) {
        String sql = "update tblposts set " +
                "author_id=?, content=?, parent_post_id=?, view_count=?, likes_count=?, blocked=?, media_path=?, total_duration=?, edited=? " +
                "where id=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1,post.getAuthor().getId());
            ps.setString(2,post.getContent());
            if (post.getParentPostId() != null)
                ps.setInt(3, post.getParentPostId());
            else
                ps.setNull(3, Types.INTEGER);
            ps.setInt(4,post.getViewCount());
            ps.setInt(5,post.getLikesCount());
            ps.setBoolean(6, post.isBlocked());
            ps.setString(7, post.getMediaPath());
            ps.setInt(8, (int) post.getTotalDuration().toSeconds());
            ps.setBoolean(9, post.isEdited());
            ps.setInt(10, id);

            return ps.executeUpdate() > 0;
        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Post> findAll() {
        List<Post> posts = new ArrayList<>();
        String sql = "SELECT * FROM tblposts";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                posts.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return posts;
    }

    @Override
    public Post findById(int id) {
        String sql = "SELECT * FROM tblposts WHERE id=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private Post map(ResultSet rs) throws SQLException {
        Post post = new Post(
                userRepository.findById(rs.getInt("author_id")),
                rs.getString("content")
        );

        post.setId(rs.getInt("id"));
        post.setCreationDate(rs.getTimestamp("creation_date"));

        int parentId = rs.getInt("parent_post_id");
        if (!rs.wasNull())
            post.setParentPostId(parentId);

        post.setViewCount(rs.getInt("view_count"));
        post.setLikesCount(rs.getInt("likes_count"));
        post.setBlocked(rs.getBoolean("blocked"));
        post.setMediaPath(rs.getString("media_path"));
        post.setTotalDuration(javafx.util.Duration.seconds(rs.getInt("total_duration")));
        post.setEdited(rs.getBoolean("edited"));

        return post;
    }
}
