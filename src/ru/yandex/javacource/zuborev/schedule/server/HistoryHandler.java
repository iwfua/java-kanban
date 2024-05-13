package ru.yandex.javacource.zuborev.schedule.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.zuborev.schedule.manager.TaskManager;
import ru.yandex.javacource.zuborev.schedule.server.adapters.DurationAdapter;
import ru.yandex.javacource.zuborev.schedule.server.adapters.LocalDateTimeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;

public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @Override
    public void handle(HttpExchange exchange) {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        if (endpoint == Endpoint.GET_HISTORY) {
            handleGetHistory(exchange);
        } else {
            sendResponse(exchange, "Некорректный метод", 404);
        }
    }

    protected void handleGetHistory(HttpExchange exchange) {
        if (!taskManager.getHistory().isEmpty()) {
            sendResponse(exchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
        } else {
            sendResponse(exchange, "История отсутствует", 404);
        }
    }
}
