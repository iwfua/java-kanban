package server;
import com.sun.net.httpserver.HttpServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.zuborev.schedule.manager.Managers;
import ru.yandex.javacource.zuborev.schedule.manager.TaskManager;
import ru.yandex.javacource.zuborev.schedule.server.HttpTaskServer;
import ru.yandex.javacource.zuborev.schedule.task.Epic;
import ru.yandex.javacource.zuborev.schedule.task.Task;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class TaskServerTests {
    private static final int PORT = 8080;
    private TaskManager taskManager;
    private HttpTaskServer httpTaskServer;
    private HttpServer server;

    @BeforeEach
    public void BeforeEach() throws IOException {
        taskManager = Managers.getDefaultTaskManager();
        httpTaskServer = new HttpTaskServer(taskManager);
        server = HttpServer.create(new InetSocketAddress(PORT), 0); // Создаем новый HttpServer
        try {
            httpTaskServer.startServer(server); // Передаем созданный HttpServer в метод startServer
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testServerAddTasks() {
        try {
            // Отправляем POST-запрос для добавления задачи на сервер
            URL url = new URL("http://localhost:8080/tasks");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type", "application/json");

            String taskJson = "{\"id\":1,\"name\":\"Task 1\",\"description\":\"Description for Task 1\"}";

            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = taskJson.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            assertEquals(201, responseCode); // Проверяем, что код ответа равен 201 (Created)

            // Проверяем, что задача успешно добавлена в менеджер задач
            List<Task> tasks = taskManager.getAllTask();
            assertEquals(1, tasks.size());
            assertEquals(1, tasks.get(0).getId());
        } catch (IOException e) {
            fail("Exception occurred while adding task: " + e.getMessage());
        }
    }

    @AfterEach
    public void AfterEach() {
        // Убеждаемся, что сервер остановлен после каждого теста
        httpTaskServer.stopServer();
    }

}
