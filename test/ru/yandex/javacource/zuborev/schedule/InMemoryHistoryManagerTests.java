package ru.yandex.javacource.zuborev.schedule;

import org.junit.jupiter.api.*;

import ru.yandex.javacource.zuborev.schedule.manager.HistoryManager;
import ru.yandex.javacource.zuborev.schedule.manager.InMemoryHistoryManager;
import ru.yandex.javacource.zuborev.schedule.manager.Managers;
import ru.yandex.javacource.zuborev.schedule.manager.TaskManager;
import ru.yandex.javacource.zuborev.schedule.task.Task;
import ru.yandex.javacource.zuborev.schedule.task.TaskStatus;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class InMemoryHistoryManagerTests {

    private static HistoryManager historyManager;
    private static TaskManager taskManager;
    private static Task task1;

    @BeforeEach
    public void BeforeEach() {
        historyManager = Managers.getDeaultHistoryManager();
        taskManager = Managers.getDefaultTaskManager();
    }

    @Test
    public void getDefaultHistoryTest() {
        Assertions.assertNotNull(historyManager);
    }

    //Проверка добавления элемента в историю
    @Test
    public void testAddHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        Task task1 = new Task("Task 1", "1", TaskStatus.DONE, 1);
        Task task2 = new Task("Task 2", "2", TaskStatus.DONE, 2);
        Task task3 = new Task("Task 3", "3", TaskStatus.DONE, 3);

        // Добавляем задачи
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        List<Task> expectedHistory = Arrays.asList(task1, task2, task3);
        assertEquals(expectedHistory, historyManager.getHistory());
    }

    //Проверка удаления элемента из истории
    @Test
    public void testRemoveElementFromHistory() {
        InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

        List<Task> expectedHistory;
        Task task1 = new Task("Task 1", "1", TaskStatus.DONE, 1);
        Task task2 = new Task("Task 2", "2", TaskStatus.DONE, 2);
        Task task3 = new Task("Task 3", "3", TaskStatus.DONE, 3);

        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);

        // Удаляем задачу №2 и проверяем порядок
        historyManager.remove(2);
        expectedHistory = Arrays.asList(task1, task3);
        assertEquals(expectedHistory, historyManager.getHistory());
    }
}
