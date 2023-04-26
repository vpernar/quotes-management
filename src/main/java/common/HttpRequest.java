package common;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class HttpRequest {
    private String method;
    private String path;
    private ArrayList<HttpPostParam> postParams;

    public HttpRequest(BufferedReader in) {
        try {
            String firstLine = in.readLine();
            StringTokenizer stringTok = new StringTokenizer(firstLine);
            this.method = stringTok.nextToken();
            this.path = stringTok.nextToken();

            if (this.isMethod(HttpMethod.POST)) {

                this.handlePost(in);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getPath() {
        return path;
    }

    public String getParamValue(String paramName) {
        for (HttpPostParam postParam : this.postParams) {
            if (postParam.getName().equals(paramName)) {
                return postParam.getValue();
            }
        }
        return null;
    }

    public boolean isMethod(HttpMethod method) {
        return this.method.equals(method.toString());
    }

    private void handlePost(BufferedReader in) throws IOException {
        String singleLine;

        this.postParams = new ArrayList<>();
        do {
            singleLine = in.readLine();
        } while (!singleLine.trim().equals(""));
        StringBuilder payload = new StringBuilder();
        while (in.ready()) {
            payload.append((char) in.read());
        }
        String urlDecodedPayload = URLDecoder.decode(payload.toString(), String.valueOf(StandardCharsets.UTF_8));
        String[] fields = urlDecodedPayload.split("&");
        for (String field : fields) {
            String[] params = field.split("=");
            postParams.add(new HttpPostParam(params[0], params[1]));
        }
    }
}
