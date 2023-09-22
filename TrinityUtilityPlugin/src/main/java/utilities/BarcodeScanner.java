package utilities;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class BarcodeScanner {

    /**
     * Generates a subimage of the specified element from the provided screenshot image.
     *
     * @param element the web element representing the area to be extracted from the screenshot
     * @param screenshot the file object representing the screenshot image
     * @return the generated subimage as a BufferedImage
     */
    public static BufferedImage generateImage(WebElement element, File screenshot) {
        BufferedImage fullImage;
        BufferedImage qrCodeImage = null;
        try {
            fullImage = ImageIO.read(screenshot);
            Point imageLocation = element.getLocation();
            int qrCodeImageWidth = element.getSize().getWidth();
            int qrCodeImageHeight = element.getSize().getHeight();
            int pointXPosition = imageLocation.getX();
            int pointYPosition = imageLocation.getY();

            qrCodeImage = fullImage.getSubimage(pointXPosition, pointYPosition, qrCodeImageWidth, qrCodeImageHeight);
            ImageIO.write(qrCodeImage, "png", screenshot);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return qrCodeImage;

    }

    /**
     * Decodes the QR code image represented by the provided BufferedImage object.
     *
     * @param qrCodeImage the BufferedImage object representing the QR code image to be decoded
     * @return the decoded text from the QR code as a String
     */
    public static String decodeQRCode(BufferedImage qrCodeImage){
        Result result = null;
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(qrCodeImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            result = new MultiFormatReader().decode(bitmap);
        } catch (com.google.zxing.NotFoundException e) {
            e.printStackTrace();
        }
        return result.getText();
    }
}
