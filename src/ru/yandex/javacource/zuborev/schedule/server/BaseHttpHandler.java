package ru.yandex.javacource.zuborev.schedule.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

class BaseHttpHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;

    @Override
    public void handle(HttpExchange exchange) throws IOException {
    }

    protected Endpoint getEndpoint(String path, String method) {
        String[] pathParts = path.split("/");
        String request = pathParts[1];
        switch (request) {
            case "tasks":
                return switch (method) {
                    case "GET" -> Endpoint.GET_TASKS;
                    case "POST" -> Endpoint.POST_TASKS;
                    case "DELETE" -> Endpoint.DELETE_TASK;
                    default -> Endpoint.UNKNOWN;
                };
            case "subtasks":
                return switch (method) {
                    case "GET" -> Endpoint.GET_SUBTASKS;
                    case "POST" -> Endpoint.POST_SUBTASKS;
                    case "DELETE" -> Endpoint.DELETE_SUBTASKS;
                    default -> Endpoint.UNKNOWN;
                };
            case "epics":
                return switch (method) {
                    case "GET" -> Endpoint.GET_EPICS;
                    case "POST" -> Endpoint.POST_EPICS;
                    case "DELETE" -> Endpoint.DELETE_EPICS;
                    default -> Endpoint.UNKNOWN;
                };
            case "history":
                return Endpoint.GET_HISTORY;
            case "prioritized":
                return Endpoint.GET_PRIORITIZED;
            default:
                return Endpoint.UNKNOWN;
        }
    }

    public Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] path = exchange.getRequestURI().getPath().split("/");

        try {
            return Optional.of(Integer.parseInt(path[2]));
        } catch (NumberFormatException ex) {
            return Optional.empty();
        }
    }

    protected void sendResponse(HttpExchange exchange, String response, int code)  {

        try (OutputStream os = exchange.getResponseBody()) {
            exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
            exchange.sendResponseHeaders(code, 0);
            os.write(response.getBytes(DEFAULT_CHARSET));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        exchange.close();
    }
}