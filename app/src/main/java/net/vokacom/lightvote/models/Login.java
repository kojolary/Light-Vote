package net.vokacom.lightvote.models;

public class Login {
    private String message;
    private boolean status;
    private AuthenticatedUser user;

    public String getMessage() {
        return message;
    }

    public Login setMessage(String message) {
        this.message = message;
        return this;
    }

    public boolean isSuccessful() {
        return status;
    }

    public Login setStatus(boolean status) {
        this.status = status;
        return this;
    }

    public AuthenticatedUser getUser() {
        return user;
    }

    public Login setUser(AuthenticatedUser user) {
        this.user = user;
        return this;
    }
}
