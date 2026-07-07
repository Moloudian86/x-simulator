package repository;

import connection.ConnectionDB;
import interfaces.IRepository;
import model.Report;
import model.Status;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReportRepository implements IRepository<Report> {

    @Override
    public boolean add(Report report) {
        String sql = "INSERT INTO tblreports " +
                "(reporter_id, reported_id, reported_post_id, reason, status) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, report.getReporterId());
            ps.setInt(2, report.getReportedId());
            ps.setInt(3, report.getReportedPostId());
            ps.setString(4, report.getReason());
            ps.setString(5, report.getStatus().name());

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean remove(int id) {
        String sql = "DELETE FROM tblreports WHERE reported_post_id=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(int id, Report report) {
        String sql = "UPDATE tblreports SET " +
                "reporter_id=?, reported_id=?, reported_post_id=?, reason=?, status=? " +
                "WHERE reported_post_id=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, report.getReporterId());
            ps.setInt(2, report.getReportedId());
            ps.setInt(3, report.getReportedPostId());
            ps.setString(4, report.getReason());
            ps.setString(5, report.getStatus().name());
            ps.setInt(6, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public List<Report> findAll() {
        List<Report> reports = new ArrayList<>();
        String sql = "SELECT * FROM tblreports";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                reports.add(map(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reports;
    }

    @Override
    public Report findById(int id) {
        String sql = "SELECT * FROM tblreports WHERE reported_post_id=?";
        try (Connection conn = ConnectionDB.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return map(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Report map(ResultSet rs) throws SQLException {
        Report report = new Report(
                rs.getInt("reporter_id"),
                rs.getInt("reported_id"),
                rs.getInt("reported_post_id"),
                rs.getString("reason")
        );
        report.setStatus(Status.valueOf(rs.getString("status")));
        return report;
    }
}