package engine;

public class srvResponse {
    boolean success;
    String feedback;

    srvResponse(boolean good) {
        if (good) {
            this.success = true;
            this.feedback = "Congratulations, you're right!";
        }
        else {
            this.success = false;
            this.feedback = "Wrong answer! Please, try again.";
        }
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    @Override
    public String toString() {
        return "srvResponse{" +
                "success=" + success +
                ", feedback='" + feedback + '\'' +
                '}';
    }
}
