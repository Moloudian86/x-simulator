package model;

public class Report {
    private int reporterId;
    private int reportedId;
    private int reportedPostId;

    private String reason;
    private Status status;

    public Report(int reporterId,int reportedId,int reportedPostId,String reason){
        this.reporterId = reporterId;
        this.reportedId = reportedId;
        this.reportedPostId = reportedPostId;
        this.reason = reason;
        this.status = Status.WAITING;
    }

    public int getReporterId() {
        return reporterId;
    }

    public void setReporterId(int reporterId) {
        this.reporterId = reporterId;
    }

    public int getReportedId() {
        return reportedId;
    }

    public void setReportedId(int reportedId) {
        this.reportedId = reportedId;
    }

    public int getReportedPostId() {
        return reportedPostId;
    }

    public void setReportedPostId(int reportedPostId) {
        this.reportedPostId = reportedPostId;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
