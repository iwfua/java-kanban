package ru.yandex.javacource.zuborev.schedule;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.zuborev.schedule.manager.FileBackedTaskManager;
import ru.yandex.javacource.zuborev.schedule.manager.Managers;
import ru.yandex.javacource.zuborev.schedule.task.Task;
import ru.yandex.javacource.zuborev.schedule.task.TaskStatus;
import java.io.File;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTests {
    FileBackedTaskManager fileBackedTaskManager;
    File file = new File("/Users/nikolay/IdeaProjects/java-kanban/src/ru/yandex" +
            "/javacource/zuborev/schedule/resourses/saved.csv");
    Task task1;

    @BeforeEach
    public void beforeEach() {
        fileBackedTaskManager = new FileBackedTaskManager(Managers.getDeaultHistoryManager());
        task1 = new Task("Task 1", "Description 1", TaskStatus.DONE, 1);

    }

    @Test
    public void getTaskToString() {
        fileBackedTaskManager.addNewTask(task1);
        String expexted = "1,TASK,Task 1,DONE,Description 1,";
        String  actual = fileBackedTaskManager.toStringTask(task1);

        assertEquals(expexted, actual);
    }

    //проверка создания задачи из строки
    @Test
    public void getTaskFromString() {
        fileBackedTaskManager = new FileBackedTaskManager(Managers.getDeaultHistoryManager());
        String expected = "[Task{id=1, name='Task 1', description='Description 1', taskStatus=DONE}]";

        fileBackedTaskManager.addNewTask(task1);
        String actual = fileBackedTaskManager.taskFromString(file.toString()).toString();

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void getLoadFromFile() {

        FileBackedTaskManager manager = new FileBackedTaskManager(Managers.getDeaultHistoryManager());
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.DONE, 1);

        manager.addNewTask(task1);

        // проверка loadedManager
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        assertNotNull(loadedManager);

        // Проверяем, что задачи были успешно загружены
        List<Task> loadedTasks = loadedManager.getAllTask();
        assertEquals(1, loadedTasks.size());
        assertEquals("Task 1", loadedTasks.get(0).getName());
        assertEquals("Description 1", loadedTasks.get(0).getDescription());
        assertEquals(TaskStatus.DONE, loadedTasks.get(0).getTaskStatus());
        assertEquals(1, loadedTasks.get(0).getId());
    }
}
