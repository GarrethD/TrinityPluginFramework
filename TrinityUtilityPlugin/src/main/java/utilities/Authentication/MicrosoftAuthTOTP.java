package utilities.Authentication;

import org.jboss.aerogear.security.otp.Totp;
public class MicrosoftAuthTOTP {
    private String secretKey;

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String generateTotp() {
        Totp totp = new Totp(this.secretKey);
        return totp.now(); // Generates a TOTP using the current time
    }

    // You can also validate a TOTP
    public boolean verifyTotp(String totpCode) {
        Totp totp = new Totp(this.secretKey);
        return totp.verify(totpCode); // Validates the provided TOTP code
    }
}
