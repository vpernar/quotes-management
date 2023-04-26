package common;

public class HttpPostParam {
    private String name;
    private String value;

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public HttpPostParam(String name, String value) {
        this.name = name;
        this.value = value;
    }
}
