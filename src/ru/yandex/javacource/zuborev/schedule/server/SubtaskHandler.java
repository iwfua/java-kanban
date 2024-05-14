package ru.yandex.javacource.zuborev.schedule.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.zuborev.schedule.manager.ManagerSaveException;
import ru.yandex.javacource.zuborev.schedule.manager.TaskManager;
import ru.yandex.javacource.zuborev.schedule.server.adapters.DurationAdapter;
import ru.yandex.javacource.zuborev.schedule.server.adapters.LocalDateTimeAdapter;
import ru.yandex.javacource.zuborev.schedule.task.Subtask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;

public class SubtaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case GET_SUBTASKS -> handleGetTasks(exchange);
            case DELETE_SUBTASKS -> handleDeleteTask(exchange);
            case POST_SUBTASKS -> handleAddTask(exchange);
            case UNKNOWN -> sendResponse(exchange, "Некорректный метод", 404);
        }
    }

    protected void handleGetTasks(HttpExchange exchange) {
        String[] path = exchange.getRequestURI().getPath().split("/");

        if (path.length == 2) {
            if (!taskManager.getSubtasks().isEmpty()) {
                String subtaskResponse = gson.toJson(taskManager.getSubtasks());
                sendResponse(exchange, subtaskResponse, 200);
                return;
            }
        } else if (path.length == 3) {
            Optional<Integer> subtaskId = getTaskId(exchange);
            if (subtaskId.isPresent() & taskManager.getSubtaskById(subtaskId.get()) != null) {
                String subtaskResponse = gson.toJson(taskManager.getSubtaskById(subtaskId.get()));
                sendResponse(exchange, subtaskResponse, 200);
                return;
            }
        }
        sendResponse(exchange, "Subtask Not Found", 404);
    }

    protected void handleDeleteTask(HttpExchange exchange) throws IOException {
        String[] path = exchange.getRequestURI().getPath().split("/");
        Optional<Integer> subtaskId = getTaskId(exchange);

        if (path.length == 3 && taskManager.getSubtaskById(subtaskId.get()) != null) {
            taskManager.deleteSubtaskById(subtaskId.get());
            sendResponse(exchange, "Subtask удалена", 200);
            return;
        }
        sendResponse(exchange, "Subtask Not Found", 404);
    }

    private void handleAddTask(HttpExchange exchange) {
        String[] path = exchange.getRequestURI().getPath().split("/");

        try {
            String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            Subtask subTaskFromJson = gson.fromJson(body, Subtask.class);

            if (subTaskFromJson != null) {
                if (path.length == 2) {
                    taskManager.addNewSubtask(subTaskFromJson);
                    sendResponse(exchange, gson.toJson(subTaskFromJson), 201);
                } else if (path.length == 3) {
                    Optional<Integer> taskId = getTaskId(exchange);

                    if (taskId.isPresent() && taskManager.getSubtaskById(taskId.get()) != null) {
                        taskManager.updateSubtask(subTaskFromJson);
                        sendResponse(exchange, gson.toJson("Задача обновлена")
                                + gson.toJson(subTaskFromJson), 201);
                    } else {
                        sendResponse(exchange, "Задача не найдена", 404);
                    }
                }
            }
        } catch (ManagerSaveException e) {
            sendResponse(exchange, e.getMessage(), 406);
        }
    }
}
