package ru.yandex.javacource.zuborev.schedule;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.zuborev.schedule.manager.CSVTaskFormat;
import ru.yandex.javacource.zuborev.schedule.manager.FileBackedTaskManager;
import ru.yandex.javacource.zuborev.schedule.task.Task;
import ru.yandex.javacource.zuborev.schedule.task.TaskStatus;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileBackedTaskManagerTests {

    private File tempFile;
    private FileBackedTaskManager fileBackedTaskManager;

    @BeforeEach
    public void addFile() throws IOException {
        tempFile = File.createTempFile("test", ".csv");
    }

    @Test
    public void testLoadFromFile() throws IOException {
        // Записываем в файл тестовые данные
        String fileContent = CSVTaskFormat.getHeader() +
                "1,TASK,Task 1,DONE,Description 1\n" +
                "2,EPIC,Epic 2,NEW,desc epic\n" +
                "3,EPIC,Epic 3,NEW,desc epic\n" +
                "4,SUBTASK,Subtask 1,IN_PROGRESS,desc subtask,2\n" +
                "5,SUBTASK,Subtask 2,DONE,desc subtask2,3\n" +
                "6,SUBTASK,Subtask 3,DONE,desc subtask3,2";
        Files.write(tempFile.toPath(), fileContent.getBytes());

        // Загружаем данные из файла
        FileBackedTaskManager fileBackedTaskManager = CSVTaskFormat.loadFromFile(tempFile);

        // Проверяем загруженные данные
        assertEquals(1, fileBackedTaskManager.getAllTask().size());
        assertEquals(2, fileBackedTaskManager.getEpic().size());
        assertEquals(3, fileBackedTaskManager.getSubtask().size());
        assertEquals(1, fileBackedTaskManager.getTaskById(1).getId());
        assertEquals("Epic 2", fileBackedTaskManager.getEpicById(2).getName());
        assertEquals(2, fileBackedTaskManager.getHistory().size());
    }

    //проверка создания строки из задачи
    @Test
    void getTaskToString() {
        fileBackedTaskManager = new FileBackedTaskManager(tempFile);
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.DONE,1);
        fileBackedTaskManager.addNewTask(task1);
        String expexted = "1,TASK,Task 1,DONE,Description 1,";
        String  actual = CSVTaskFormat.taskToString(task1);
        System.out.println();
        assertEquals(expexted, actual);
    }

    //проверка создания задачи из строки
    @Test
    void getTaskFromString() {
        // вызовем метод taskFromString
        fileBackedTaskManager = new FileBackedTaskManager(tempFile);
        Task task = CSVTaskFormat.taskFromString("1,TASK,Task 1,DONE,Description 1,");

        Task expected =  new Task("Task 1", "Description 1", TaskStatus.DONE, 1);
        Task actual = task;

        assertEquals(expected,actual);

    }

    @AfterEach
    public void deleteTempFile() {
        if (tempFile != null) {
            tempFile.delete();
        }
    }
}


