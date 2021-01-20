package uk.ac.gre.aa5119a.timelearn.web.response;

import uk.ac.gre.aa5119a.timelearn.model.User;

public class LoginResponse {

    private User user;
    private String message;

    public LoginResponse() {
//        this.user = user;
//        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
