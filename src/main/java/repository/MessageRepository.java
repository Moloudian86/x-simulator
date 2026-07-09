package repository;

import connection.ConnectionDB;
import interfaces.IRepository;
import model.ChatMessage;
import model.MessageStatus;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageRepository implements IRepository<ChatMessage> {

    private final UserRepository userRepository = new UserRepository();

    @Override
    public boolean add(ChatMessage message) {
        String sql = "INSERT INTO tblmessages " +
                "(sender_id, receiver_id, content, send_time, status) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, message.getSender().getId());
            ps.setInt(2, message.getReceiver().getId());
            ps.setString(3, message.getContent());
            ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            ps.setString(5, message.getStatus().name());

            int result = ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                message.setId(rs.getInt(1));
            }

            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean remove(int id) {
        String sql = "DELETE FROM tblmessages WHERE id=?";

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
    public boolean update(int id, ChatMessage message) {
        String sql = "UPDATE tblmessages SET " +
                "sender_id=?, receiver_id=?, content=?, status=? WHERE id=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, message.getSender().getId());
            ps.setInt(2, message.getReceiver().getId());
            ps.setString(3, message.getContent());
            ps.setString(4, message.getStatus().name());
            ps.setInt(5, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<ChatMessage> findAll() {
        List<ChatMessage> list = new ArrayList<>();
        String sql = "SELECT * FROM tblmessages";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(map(rs));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public ChatMessage findById(int id) {
        String sql = "SELECT * FROM tblmessages WHERE id=?";
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

    private ChatMessage map(ResultSet rs) throws SQLException {
        User sender = userRepository.findById(rs.getInt("sender_id"));
        User receiver = userRepository.findById(rs.getInt("receiver_id"));
        ChatMessage msg = new ChatMessage(sender, receiver, rs.getString("content"));

        msg.setId(rs.getInt("id"));
        msg.setSendTime(rs.getTimestamp("send_time"));
        msg.setStatus(MessageStatus.valueOf(rs.getString("status")));
        return msg;
    }

    //متد مهم برای چت
    public List<ChatMessage> getConversation(int user1Id, int user2Id) {
        List<ChatMessage> list = new ArrayList<>();

        String sql = "SELECT * FROM tblmessages WHERE " +
                "(sender_id=? AND receiver_id=?) OR (sender_id=? AND receiver_id=?) " +
                "ORDER BY send_time";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, user1Id);
            ps.setInt(2, user2Id);
            ps.setInt(3, user2Id);
            ps.setInt(4, user1Id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(map(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}