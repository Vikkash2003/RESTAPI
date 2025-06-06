package project.restapi.models;

public class ErrorResponse {
    private String error;
    private int status;

    public ErrorResponse(String error, int status) {
        this.error = error;
        this.status = status;
    }

    // Getters and setters
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
