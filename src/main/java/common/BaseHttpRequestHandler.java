package common;

public class BaseHttpRequestHandler {
    public HttpResponse handleRequest(HttpRequest request) throws Exception {
        if (request.isMethod(HttpMethod.GET)) {
            return handleGet(request);
        } else if (request.isMethod(HttpMethod.POST)) {
            return handlePost(request);
        } else {
            return handleNotFound("Method not found");
        }
    }

    protected HttpResponse handleNotFound(String message) throws Exception {
        HttpResponse response = new HttpResponse(404);
        response.startBody();
        response.append("<html>" +
                "<head><title>404 Not Found</title></head>" +
                "<body bgcolor=\"white\">" +
                "<center><h1>404 Not Found</h1><h2>Message: "
                + message +
                "</h2></center>" +
                "</body></html>");
        return response;
    }

    protected HttpResponse handleGet(HttpRequest request) throws Exception {
        return handleNotFound("Path not found");
    }


    protected HttpResponse handlePost(HttpRequest request) throws Exception {
        return handleNotFound("Path not found");
    }

}
