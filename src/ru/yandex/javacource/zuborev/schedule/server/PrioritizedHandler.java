package ru.yandex.javacource.zuborev.schedule.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.zuborev.schedule.manager.TaskManager;
import ru.yandex.javacource.zuborev.schedule.server.adapters.DurationAdapter;
import ru.yandex.javacource.zuborev.schedule.server.adapters.LocalDateTimeAdapter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static ru.yandex.javacource.zuborev.schedule.server.Endpoint.GET_PRIORITIZED;

public class PrioritizedHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private Gson gson;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        gson = new GsonBuilder()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @Override
    public void handle(HttpExchange exchange) {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        if (Objects.requireNonNull(endpoint) == GET_PRIORITIZED) {
            handleGetPrioritize(exchange);
        } else {
            sendResponse(exchange, "Некорректный метод", 404);
        }
    }

    protected void handleGetPrioritize(HttpExchange exchange) {
        if (!taskManager.getPrioritizedTasks().isEmpty()) {
            sendResponse(exchange, gson.toJson(taskManager.getPrioritizedTasks()), 200);
        } else {
            sendResponse(exchange, "Приоритетные задачи отсутствуют", 404);
        }
    }
}
