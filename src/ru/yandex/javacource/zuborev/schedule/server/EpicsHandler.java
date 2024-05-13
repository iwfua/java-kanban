package ru.yandex.javacource.zuborev.schedule.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.zuborev.schedule.manager.TaskManager;
import ru.yandex.javacource.zuborev.schedule.server.adapters.DurationAdapter;
import ru.yandex.javacource.zuborev.schedule.server.adapters.LocalDateTimeAdapter;
import ru.yandex.javacource.zuborev.schedule.task.Epic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

public class EpicsHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicsHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_EPICS -> handleGetEpics(exchange);
            case DELETE_EPICS -> handleDeleteTask(exchange);
            case POST_EPICS -> handleAddTask(exchange);
        }
    }

    protected void handleGetEpics(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");

        if (path.length == 2) {
            if (!taskManager.getEpics().isEmpty()) {
                String epicResponse = gson.toJson(taskManager.getEpics());
                sendResponse(exchange, epicResponse, 200);
                return;
            }
        } else if (path.length == 3) {
            Optional<Integer> epicId = getTaskId(exchange);
            if (epicId.isPresent() && taskManager.getEpicById(epicId.get()) != null) {
                String epicResponse = gson.toJson(taskManager.getEpicById(epicId.get()));
                sendResponse(exchange, epicResponse, 200);
                return;
            }
        }
        sendResponse(exchange, "Epic Not Found", 404);
    }

    protected void handleDeleteTask(HttpExchange exchange) {
        Optional<Integer> epicId = getTaskId(exchange);
        if (epicId.isPresent() && taskManager.getEpicById(epicId.get()) != null) {
            taskManager.deleteEpic(epicId.get());
            sendResponse(exchange, "Epic удалён", 200);
            return;
        }
        sendResponse(exchange,"Epic Not Found", 404);
    }

    protected void handleAddTask(HttpExchange exchange) {
        String[] path = exchange.getRequestURI().getPath().split("/");

        String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        Epic epicFromJson = gson.fromJson(body, Epic.class);

        if (path.length == 2) {
            taskManager.addNewEpic(epicFromJson);
            sendResponse(exchange, gson.toJson(epicFromJson), 201);
        } else if (path.length == 3) {
            Optional<Integer> taskId = getTaskId(exchange);
            if (taskId.isPresent()) {
                taskManager.updateEpic(epicFromJson);
                sendResponse(exchange, gson.toJson("Эпик обновлен \n" + epicFromJson), 201);
            } else {
                sendResponse(exchange, "Задача не найдена", 404);
            }
        }
    }
}
