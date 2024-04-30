package TestSetupHelper;

public class TutorialPage {
    private String pageHeader;
    private String imageUrl;
    private boolean isVideoPage;
    private String pageText;
    private String question;
    private String[] answers;

    public TutorialPage(String pageHeader, String imageUrl, boolean isVideoPage, String pageText, String question, String[] answers) {
        this.pageHeader = pageHeader;
        this.imageUrl = imageUrl;
        this.isVideoPage = isVideoPage;
        this.pageText = pageText;
        this.question = question;
        this.answers = answers;
    }

    public String getPageHeader() {
        return pageHeader;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean getIsVideoPage() {
        return isVideoPage;
    }

    public String getPageText() {
        return pageText;
    }

    public String getQuestion() {
        return question;
    }

    public String[] getAnswers() {
        return answers;
    }
}
