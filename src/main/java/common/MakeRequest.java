package common;

import com.google.gson.Gson;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Socket;

public class MakeRequest {
    public static <T> T makeGetRequestForJSON(String host, int port, String path, Type typeofT) throws IOException {
        Socket socket = new Socket(host, port);
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        PrintWriter out = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()), true);
        out.println("GET " + path + " HTTP/1.1\r\nHost: " + host + "\r\n\r\n");
        String singleLine;
        do {
            singleLine = in.readLine();
        } while (!singleLine.trim().equals(""));
        StringBuilder payload = new StringBuilder();
        while (in.ready()) {
            payload.append((char) in.read());
        }
        Gson gson = new Gson();
        T target = gson.fromJson(payload.toString(), typeofT);
        return target;
    }
}
