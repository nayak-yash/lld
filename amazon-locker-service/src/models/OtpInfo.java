package models;

import java.time.LocalDate;

public class OtpInfo {
    private final String otp;
    private final LocalDate expiryTime;

    public OtpInfo(String otp, LocalDate expiryTime) {
        this.otp = otp;
        this.expiryTime = expiryTime;
    }

    public String getOtp() { return otp; }
    public LocalDate getExpiryTime() { return expiryTime; }
}
