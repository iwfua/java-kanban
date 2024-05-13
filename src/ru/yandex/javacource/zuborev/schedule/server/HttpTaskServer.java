package ru.yandex.javacource.zuborev.schedule.server;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;
import ru.yandex.javacource.zuborev.schedule.manager.Managers;
import ru.yandex.javacource.zuborev.schedule.manager.TaskManager;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private final TaskManager taskManager;
    protected HttpServer server;


    public HttpTaskServer(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    public void startServer(HttpServer server) throws IOException {
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
    }


    public void stopServer() {
        if (server != null) {
            server.stop(0);
            System.out.println("Server stopped");
        }
    }

    public static void main(String[] args) throws Exception {

        // Создание экземпляра TaskManager
        TaskManager taskManager = Managers.getDefaultTaskManager(); // Предполагается, что у вас есть реализация TaskManager

        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.server = null;
        httpTaskServer.startServer(httpTaskServer.server);

    }
}