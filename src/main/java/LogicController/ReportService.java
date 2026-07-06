package LogicController;

import model.Post;
import model.Report;
import model.User;
import repository.UserRepository;

public class ReportService {
    private static ReportService instance;
    private final UserRepository userRepository = new UserRepository();

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
        PostService.getInstance().updatePost(reportedPost);
    }

    public void reject(Report report, Post reportedPost){
        report.setStatus(model.Status.REJECTED);
        reportedPost.setBlocked(false);
        PostService.getInstance().updatePost(reportedPost);

    }

    public void blockUser(Report report){

        User reportedUser = UserService.getInstance().getUserById(report.getReportedId());

        if (reportedUser != null) {
            reportedUser.setBlocked(true);
        }
        userRepository.update(reportedUser.getId(),reportedUser);
    }

    public void unBlockUser(Report report){

        User reportedUser = UserService.getInstance().getUserById(report.getReportedId());

        if (reportedUser != null) {
            reportedUser.setBlocked(false);
        }
        userRepository.update(reportedUser.getId(),reportedUser);
    }
}
