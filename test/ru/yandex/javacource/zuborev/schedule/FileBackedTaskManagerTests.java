package ru.yandex.javacource.zuborev.schedule;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.zuborev.schedule.manager.FileBackedTaskManager;
import ru.yandex.javacource.zuborev.schedule.manager.Managers;
import ru.yandex.javacource.zuborev.schedule.task.Task;
import ru.yandex.javacource.zuborev.schedule.task.TaskStatus;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.*;

public class FileBackedTaskManagerTests {
    FileBackedTaskManager fileBackedTaskManager;
    private static final String FILE_NAME = "temp_tasks.csv";

    //временный файл с данными
    @BeforeEach
    void createdTempFile() throws IOException {
        String fileContent = "id,type,name,status,description\n" +
                "1,TASK,Task 1,IN_PROGRESS,Description 1\n" +
                "3,EPIC,Epic 1,IN_PROGRESS,Description 3\n" +
                "4,SUBTASK,Subtask 1,DONE,Description 4,3\n";

        Files.write(Paths.get(FILE_NAME), fileContent.getBytes());
    }

    @AfterEach
    void deleteTempFile() {
        File tempFile = new File(FILE_NAME);
        if (tempFile.exists()) {
            tempFile.delete();
        }
    }

    //проверка создания задачи из строки
    @Test
    void getTaskFromString() {
        // вызовем метод taskFromString
        fileBackedTaskManager = new FileBackedTaskManager(Managers.getDeaultHistoryManager());
        List<Task> tasks = fileBackedTaskManager.taskFromString(FILE_NAME);

        Task expected =  new Task("Task 1", "Description 1", TaskStatus.IN_PROGRESS, 1);
        Task actual = tasks.get(0);

        assertEquals(3, tasks.size());
        assertEquals(expected,actual);

    }

    //проверка создания строки из задачи
    @Test
    void getTaskToString() {
        fileBackedTaskManager = new FileBackedTaskManager(Managers.getDeaultHistoryManager());
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.DONE,1);
        fileBackedTaskManager.addNewTask(task1);
        String expexted = "1,TASK,Task 1,DONE,Description 1,";
        String  actual = fileBackedTaskManager.toStringTask(task1);

        assertEquals(expexted, actual);
    }

    @Test
    void getLoadFromFile() {
        File tempFile = new File(FILE_NAME);

        // вызовем метод loadFromFile
        FileBackedTaskManager testBacked = FileBackedTaskManager.loadFromFile(tempFile);
        List<Task> loadedTasks = testBacked.getAllTask();

        // Проверка на успешное добавление
        assertEquals(1, testBacked.getAllTask().size());
        assertEquals(1, testBacked.getEpic().size());
        assertEquals("Task 1", loadedTasks.get(0).getName());
        assertEquals("Description 1", loadedTasks.get(0).getDescription());
    }
}
