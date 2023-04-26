package qotdservice;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import common.*;
import common.Quote;

import java.io.*;
import java.lang.reflect.Type;

public class HttpRequestHandler extends BaseHttpRequestHandler {
    private Quote qotd;
    public static final Type QOTDTYPE = new TypeToken<QOTDSchema>() {}.getType();

    @Override
    protected HttpResponse handleGet(HttpRequest request) throws Exception {
        if (request.getPath().equals("/qotd")) {
            HttpResponse response = new HttpResponse(200);
            response.append("Content-Type: application/json\r\n");
            response.startBody();
            if (qotd == null) {
                getQOTD();
            }
            Gson gson = new Gson();
            response.append(gson.toJson(this.qotd));
            return response;
        }
        return super.handleNotFound("Path not found");
    }

    private void getQOTD() throws IOException {
        QOTDSchema qotdjson = MakeRequest.makeGetRequestForJSON("quotes.rest", 80, "/qod.json", QOTDTYPE);
        qotdservice.Quote quote;
        try {
            quote = qotdjson.getContents().getQuotes().get(0);
        } catch (NullPointerException e) {
            this.qotd = new Quote("Albert Einstein", "API throttling will be the end of us all");
            return;
        }
        this.qotd = new Quote(quote.getAuthor(), quote.getQuote());
    }

}
