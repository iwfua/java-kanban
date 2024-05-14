package ru.yandex.javacource.zuborev.schedule.server;

import com.google.gson.*;
import com.sun.net.httpserver.HttpExchange;
import ru.yandex.javacource.zuborev.schedule.manager.ManagerSaveException;
import ru.yandex.javacource.zuborev.schedule.manager.TaskManager;
import ru.yandex.javacource.zuborev.schedule.server.adapters.DurationAdapter;
import ru.yandex.javacource.zuborev.schedule.server.adapters.LocalDateTimeAdapter;
import ru.yandex.javacource.zuborev.schedule.task.Task;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class TasksHandler extends BaseHttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;


    public TasksHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
        this.gson = new GsonBuilder().setPrettyPrinting()
                .registerTypeAdapter(Duration.class, new DurationAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    @Override
    public void handle(HttpExchange exchange) {
        Endpoint endpoint = getEndpoint(exchange.getRequestURI().getPath(), exchange.getRequestMethod());

        switch (endpoint) {
            case POST_TASKS -> handleAddTask(exchange);
            case GET_TASKS -> handleGetTasks(exchange);
            case DELETE_TASK -> handleDeleteTask(exchange);
            case UNKNOWN -> sendResponse(exchange, "Некорректный метод", 404);
        }
    }

    private void handleGetTasks(HttpExchange exchange) {
        String[] path = exchange.getRequestURI().getPath().split("/");
        if (path.length == 2) {
            List<Task> tasks = taskManager.getAllTask();
            if (!tasks.isEmpty()) {
                String tasksResponse = gson.toJson(taskManager.getAllTask());
                sendResponse(exchange, tasksResponse, 200);
                return;
            }
        } else if (path.length == 3) {

            Optional<Integer> taskIdOptional = getTaskId(exchange);

            if (taskIdOptional.isPresent()) {

                if (taskManager.getTaskById(taskIdOptional.get()) != null) {
                    sendResponse(exchange, gson.toJson(taskManager.getTaskById(taskIdOptional.get())), 200);
                    return;
                }
            }
        }
        sendResponse(exchange, "Task Not Found", 404);
    }

    //как понял нужно удалять только по id или нужно удалять и все таски по эндпоинту DELETE /tasks?
    private void handleDeleteTask(HttpExchange exchange) {
        Optional<Integer> taskIdOptional = getTaskId(exchange);

        if (taskIdOptional.isPresent() && taskManager.getTaskById(taskIdOptional.get()) != null) {
            taskManager.deleteTaskById(taskIdOptional.get());
            sendResponse(exchange, "Task deleted successfully", 200);
        }
        sendResponse(exchange, "Task Not Found", 404);
    }


    private void handleAddTask(HttpExchange exchange) {
        String[] path = exchange.getRequestURI().getPath().split("/");

        try {
            String body = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8))
                    .lines()
                    .collect(Collectors.joining("\n"));

            Task taskFromJson = gson.fromJson(body, Task.class);

            if (taskFromJson != null) {
                if (path.length == 2) {
                    taskManager.addNewTask(taskFromJson);
                    sendResponse(exchange, gson.toJson(taskFromJson), 201);
                } else if (path.length == 3) {
                    Optional<Integer> taskId = getTaskId(exchange);

                    if (taskId.isPresent() && taskManager.getTaskById(taskId.get()) != null) {
                        taskManager.updateTask(taskFromJson);
                        sendResponse(exchange, gson.toJson("Задача обновлена") + gson.toJson(taskFromJson), 201);
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
