package en.Core.enums;

public enum URLS {
    Example_page("https://testpages.eviltester.com/styled/validation/input-validation.html");


    private String url;

    URLS(String url) {
        this.url = url;
    }
    public String getURL() {
        return this.url;

    }
}
