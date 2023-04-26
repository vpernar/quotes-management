package server;

import common.HttpRequest;
import common.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.util.StringTokenizer;

public class ServerThread implements Runnable {

    private Socket client;
    private BufferedReader in;
    private PrintWriter out;
    private HttpRequestHandler requestHandler;

    public ServerThread(Socket sock) {
        this.client = sock;
        this.requestHandler = new HttpRequestHandler();
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() {
        try {
            HttpRequest request = new HttpRequest(this.in);
            HttpResponse response = requestHandler.handleRequest(request);
            out.println(response.toString());
            in.close();
            out.close();
            client.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
