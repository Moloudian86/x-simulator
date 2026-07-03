package LogicController;

import model.Post;
import model.Report;
import model.User;

public class ReportService {
    private static ReportService instance;
    public static ReportService getInstance() {
        if (instance == null)
            instance = new ReportService();
        return instance;
    }

    public void confirm(Report report, Post reportedPost){
        report.setStatus(model.Status.CONFIRMED);

        if (reportedPost != null) {
            reportedPost.setBlocked(true);
        }

    }

    public void reject(Report report, Post reportedPost){
        report.setStatus(model.Status.REJECTED);
        reportedPost.setBlocked(false);
    }

    public void blockUser(Report report){

        User reportedUser = UserService.getInstance().getUserById(report.getReportedId());

        if (reportedUser != null) {
            reportedUser.setBlocked(true);
        }
    }

    public void unBlockUser(Report report){

        User reportedUser = UserService.getInstance().getUserById(report.getReportedId());

        if (reportedUser != null) {
            reportedUser.setBlocked(false);
        }
    }
}
