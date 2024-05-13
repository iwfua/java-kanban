package ru.yandex.javacource.zuborev.schedule.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.javacource.zuborev.schedule.manager.Managers;
import ru.yandex.javacource.zuborev.schedule.manager.TaskManager;
import ru.yandex.javacource.zuborev.schedule.task.Epic;
import ru.yandex.javacource.zuborev.schedule.task.Subtask;
import ru.yandex.javacource.zuborev.schedule.task.TaskStatus;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private final TaskManager taskManager;
    protected HttpServer server;


    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public HttpServer startServer(HttpServer server) throws IOException {
        server = HttpServer.create(new InetSocketAddress(PORT), 0); // Создаем новый HttpServer

        // Привязка обработчиков к соответствующим путям
        server.createContext("/tasks", new TasksHandler(taskManager));
        server.createContext("/subtasks", new SubtaskHandler(taskManager));
        server.createContext("/epics", new EpicsHandler(taskManager));
        server.createContext("/prioritized", new PrioritizedHandler(taskManager));
        server.createContext("/history", new HistoryHandler(taskManager));

        // Запуск сервера
        server.start();

        System.out.println("Server started on port " + PORT);
        return server;
    }


    public void stopServer() {
        if (server != null) {
            server.stop(0);
            System.out.println("Server stopped");
        }
    }

    public static void main(String[] args) throws Exception {
        Duration duration1 = Duration.ofMinutes(100);
        Duration duration2 = Duration.ofMinutes(100);

        LocalDateTime localDateTime = LocalDateTime.of(2020, 10, 11, 12, 13);
        LocalDateTime localDateTime2 = LocalDateTime.of(2020, 10, 11, 15, 13);

        Epic epic = new Epic("name", "desrp", 3);

        Subtask subtask = new Subtask("name", "descrp", TaskStatus.NEW, localDateTime, duration1, 1, epic.getId());
        Subtask subtask2 = new Subtask("name", "descrp", TaskStatus.NEW, localDateTime2, duration2, 2, epic.getId());


        // Создание экземпляра TaskManager
        TaskManager taskManager = Managers.getDefaultTaskManager();
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(subtask);
        taskManager.addNewSubtask(subtask2);

        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.server = null;
        httpTaskServer.startServer(httpTaskServer.server);

    }
}