package common;

public class Quote {
    private String author;
    private String quote;

    public Quote(String author, String quote) {
        this.author = author;
        this.quote = quote;
    }

    public String toString() {
        return this.author + ": " + '"' + this.quote + '"';
    }
}
