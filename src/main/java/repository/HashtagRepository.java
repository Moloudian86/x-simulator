package repository;

import connection.ConnectionDB;
import interfaces.IRepository;
import model.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HashtagRepository implements IRepository<Hashtag> {
    @Override
    public boolean add(Hashtag hashtag) {
        String sql = "INSERT INTO tblhashtag(content) VALUES (?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql,java.sql.Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, hashtag.getTitle());

            int result = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                hashtag.setId(rs.getInt(1));
            }
            return result > 0;

        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean remove(int id) {
        String sql = "DELETE FROM tblhashtag WHERE id = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1,id);

            return ps.executeUpdate() > 0;

        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(int id, Hashtag hashtag) {
        String sql = "UPDATE tblhashtag SET content = ? WHERE id = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1, hashtag.getTitle());
            ps.setInt(2,id);

            return ps.executeUpdate() > 0;

        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Hashtag> findAll() {
        List<Hashtag> hashtags = new ArrayList<>();
        String sql = "SELECT * FROM tblhashtag";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){
            while (rs.next()) {
                hashtags.add(map(rs));
            }

        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return hashtags;
    }

    @Override
    public Hashtag findById(int id) {
        String sql = "SELECT * FROM tblhashtag WHERE id = ?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1,id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }

        }catch (SQLException ex){
            ex.printStackTrace();
        }
        return null;
    }

    public Hashtag findByTitle(String title){

        String sql = "SELECT * FROM tblhashtag WHERE content=?";

        try(Connection conn = ConnectionDB.getConnection();
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setString(1,title);

            ResultSet rs = ps.executeQuery();

            if(rs.next()){
                return map(rs);
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        return null;
    }


    private Hashtag map(ResultSet rs) throws SQLException {

        Hashtag hashtag = new Hashtag(rs.getString("content"));

        hashtag.setId(rs.getInt("id"));

        return hashtag;
    }
}
