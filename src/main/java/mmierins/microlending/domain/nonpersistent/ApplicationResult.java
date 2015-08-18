package mmierins.microlending.domain.nonpersistent;

public class ApplicationResult {

    private ApplicationStatus status;
    private String message;
    private String description;
    private int code;

    public ApplicationResult(ApplicationStatus status, String message, String description, int code) {
        this.status = status;
        this.message = message;
        this.description = description;
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

}
