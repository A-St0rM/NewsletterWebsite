package app.models;

import java.time.LocalDate;

public class Subscriber {
    private String email;
    private LocalDate signUp_date;

    public Subscriber(String email, LocalDate signUp_date) {
        this.email = email;
        this.signUp_date = signUp_date;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getSignUp_date() {
        return signUp_date;
    }

    public void setSignUp_date(LocalDate signUp_date) {
        this.signUp_date = signUp_date;
    }
}
