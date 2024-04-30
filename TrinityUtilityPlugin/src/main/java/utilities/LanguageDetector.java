package utilities;

import com.google.common.base.Optional;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;

import java.io.IOException;
import java.util.List;


public class LanguageDetector {
    /**
     * Detect text langauge
     * Ensure Text Quality: Make sure that the text extracted from the page title is clean and representative of the language
     * you expect to detect. If the text is too short, noisy, or contains non-textual characters, it might affect the accuracy
     * of language detection.
     * @param detectedText   This is sent text which we want to detect its language
     */
    public String languageDetector(String detectedText){
        System.out.println("Text sent for language detection " + detectedText);

        // Load all languages:
        List<LanguageProfile> languageProfiles;
        try {
            languageProfiles = new LanguageProfileReader().readAllBuiltIn();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Build language detector:
        com.optimaize.langdetect.LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                .withProfiles(languageProfiles)
                .build();

        // Query language detection directly on the text string
        Optional<LdLocale> lang = languageDetector.detect(detectedText);

        // Print language detection results
        System.out.println("lang.isPresent: " + lang.isPresent());

        if (lang.isPresent()) {
            System.out.println("Language detected: " + lang.get().toString().toUpperCase());
            return lang.get().toString().toUpperCase();

        } else {
            System.out.println("Language detection failed. No language detected.");
            return "Language detection failed. No language detected.";
        }
    }
}
