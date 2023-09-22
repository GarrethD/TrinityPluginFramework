package utilities;


import ru.sbtqa.monte.media.Format;
import ru.sbtqa.monte.media.FormatKeys;
import ru.sbtqa.monte.media.math.Rational;
import ru.sbtqa.monte.screenrecorder.ScreenRecorder;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.ByteOrder;

import static ru.sbtqa.monte.media.AudioFormatKeys.*;
import static ru.sbtqa.monte.media.FormatKeys.EncodingKey;
import static ru.sbtqa.monte.media.FormatKeys.MediaTypeKey;
import static ru.sbtqa.monte.media.VideoFormatKeys.*;

public class VideoRecorder {

    static ScreenRecorder screenRecorder;
    static String currentUsersHomeDir = System.getProperty("user.dir");
    static String videos = currentUsersHomeDir + File.separator + "ExtentReports";

    /**
     * Creates a recording instance for video and audio recording.
     *
     * @param audioFormatSettings a boolean value indicating whether audio recording is allowed or not
     */
    public static void CreateRecordingInstance(Boolean audioFormatSettings) {
        //For video recoding -- Not working in pipeline
        GraphicsConfiguration gc = GraphicsEnvironment
                .getLocalGraphicsEnvironment()
                .getDefaultScreenDevice()
                .getDefaultConfiguration();
        File videoFile = new File(videos);
        Format audioFormat = new Format();
        //If Audio record allowed
        if (audioFormatSettings)
        {
             audioFormat = new Format(
                MediaTypeKey, FormatKeys.MediaType.AUDIO,
                EncodingKey, ENCODING_PCM_SIGNED,
                SampleRateKey, new Rational(44100, 1),
                SampleSizeInBitsKey, 16,
                ChannelsKey, 2,
                FrameRateKey, new Rational(44100, 1),
                ByteOrderKey, ByteOrder.BIG_ENDIAN);
        }
        else
        {
            audioFormat = null;
        }
        try {
            screenRecorder = new ScreenRecorder(gc,
                    null,
                    new Format(MediaTypeKey, MediaType.FILE, MimeTypeKey, MIME_AVI),
                    new Format(MediaTypeKey, MediaType.VIDEO, EncodingKey, ENCODING_AVI_MJPG,
                            CompressorNameKey, ENCODING_AVI_MJPG, DepthKey, 24, FrameRateKey, Rational.valueOf(15)),
                    null, audioFormat, videoFile);
        } catch (IOException | AWTException e) {
            e.printStackTrace();
        }

    }

    /**
     * Starts the recording process.
     *
     * This method starts the recording process using the initialized screenRecorder object.
     * If any IOException occurs during the process, it is caught and printed.
     */

    public static void StartRecording() {
        try {
            screenRecorder.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the recording process.
     *
     * This method stops the recording process using the initialized screenRecorder object.
     * If any IOException occurs during the process, it is caught and printed.
     */
    public static void StopRecording() {
        try {
            screenRecorder.stop();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
