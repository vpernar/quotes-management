package server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import common.*;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HttpRequestHandler extends BaseHttpRequestHandler {
    public static final Type QUOTETYPE = new TypeToken<List<Quote>>() {
    }.getType();
    public static final Type QUOTESINGLETYPE = new TypeToken<Quote>() {
    }.getType();
    public static final String dbFileName = "quotedb.json";

    @Override
    protected HttpResponse handleGet(HttpRequest request) throws Exception {
        if (request.getPath().equals("/quotes")) {
            HttpResponse response = new HttpResponse(200);
            response.append("Content-Type: text/html\r\n");
            response.startBody();
            getQuotes(response);
            return response;
        }
        return super.handleNotFound("Path not found");
    }

    @Override
    protected HttpResponse handlePost(HttpRequest request) throws Exception {
        if (request.getPath().equals("/save-quote")) {
            HttpResponse response = new HttpResponse(303);
            response.append("Location: http://localhost/quotes\r\n");
            response.startBody();
            postSaveQuote(request);
            return response;
        }
        return super.handleNotFound("Path not found");
    }

    private void getQuotes(HttpResponse response) throws IOException {
        List<Quote> quoteList = getQuoteList();
        Quote qotd;
        try {
            qotd = MakeRequest.makeGetRequestForJSON("localhost", 8888, "/qotd", QUOTESINGLETYPE);
        } catch (Exception e) {
            qotd = new Quote("Quote", "could not be loaded");
        }
        response.append("<html><head><title>Quotes</title></head><body>");
        response.append("<form method=\"POST\" action=\"save-quote\" accept-charset=\"utf-8\">");
        response.append("<label>Author: </label><input name=\"author\" type=\"text\"><br><br>");
        response.append("<label>Quote: </label><input name=\"quote\" type=\"text\"><br><br>");
        response.append("<button>Submit</button>");
        response.append("</form>");
        response.append("<h1>Quote of the day:</h1><br>");
        response.append(qotd.toString() + "<br><br>");
        response.append("<h1>Saved Quotes:</h1><br>");
        for (Quote quote : quoteList) {
            response.append(quote.toString() + "<br>");
        }
        response.append("<h1></h1><br>");
        response.append("<body></html>");
    }

    private void postSaveQuote(HttpRequest request) throws IOException {
        List<Quote> quoteList = getQuoteList();
        quoteList.add(new Quote(request.getParamValue("author"), request.getParamValue("quote")));
        Gson gson = new Gson();
        Writer w = new FileWriter(dbFileName);
        gson.toJson(quoteList, w);
        w.flush();
        w.close();
    }

    private List<Quote> getQuoteList() throws IOException {
        Gson gson = new Gson();
        File db = new File(dbFileName);
        if (!db.exists()) {
            db.createNewFile();
        }
        JsonReader reader = new JsonReader(new FileReader(dbFileName));
        List<Quote> quoteList = gson.fromJson(reader, QUOTETYPE);
        if (quoteList == null) {
            return new ArrayList<>();
        }
        return quoteList;
    }

}
