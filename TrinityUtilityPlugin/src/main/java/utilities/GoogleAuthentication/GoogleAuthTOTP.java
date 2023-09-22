package utilities.GoogleAuthentication;

import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorConfig;

public class GoogleAuthTOTP {
    private final GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder configBuilder
            = new GoogleAuthenticatorConfig.GoogleAuthenticatorConfigBuilder();
    private String secretKey = null;

    /**
     * Set the number of digits to expect as the returned TOTP. Can be 6, 7 or 8
     *
     * @param numberOfDigits The number of digits. Defaults to 6
     */
    public void setNumberOfDigits(int numberOfDigits) {
        if (numberOfDigits < 6 || numberOfDigits > 8) {
            System.out.println("TOTP length needs to be > 6 and < 8");
        }
        configBuilder.setCodeDigits(numberOfDigits);
    }

    /**
     * Set the secret to generate the code for
     *
     * @param secretKey The key to use, as string
     */
    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    /**
     * Generate a OTP based on the secret that was set before
     *
     * @return OTP as 6-8 digit String
     */
    public String getTotp() {
        return getTotpForSecret(secretKey);
    }

    /**
     * Generate a OTP based on a given secret key
     *
     * @param secretKey The secret key to generate OTP for
     * @return OTP as 6-8 digit String
     */
    public String getTotpForSecret(String secretKey){
            if (null == secretKey)
            {
                System.out.println("The secret key string is empty");
            }
        GoogleAuthenticatorConfig gAuthConfig = configBuilder.build();
        GoogleAuthenticator gAuth = new GoogleAuthenticator(gAuthConfig);
        int totp = gAuth.getTotpPassword(secretKey);
        /**
         * GoogleAuthenticator returns code as an int and so can't have leading zeros. This means that the generated code
         * can have a length shorter than the configured length (defaults to 6). This is solved by adding leading zero's
         */
        return padCodeWithZerosToNumberOfDigits(totp, gAuthConfig.getCodeDigits());
    }
    public static String padCodeWithZerosToNumberOfDigits(int code, int numberOfDigits) {
        String formatSpec = "%0" + numberOfDigits + "d";
        return String.format(formatSpec, code);
    }
}
