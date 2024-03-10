package ru.yandex.javacource.zuborev.schedule;

import org.junit.jupiter.api.*;

import ru.yandex.javacource.zuborev.schedule.manager.HistoryManager;
import ru.yandex.javacource.zuborev.schedule.manager.Managers;
import ru.yandex.javacource.zuborev.schedule.manager.TaskManager;
import ru.yandex.javacource.zuborev.schedule.task.Task;
import ru.yandex.javacource.zuborev.schedule.task.TaskStatus;

public class InMemoryHistoryManagerTests {

    private static HistoryManager historyManager;
    private static TaskManager taskManager;
    private static Task task1;

    @BeforeEach
    public void BeforeEach() {
        historyManager = Managers.getDeaultHistoryManager();
        taskManager = Managers.getDefaultTaskManager();
        task1 = new Task("сходить в магазин", " ", TaskStatus.NEW);
    }

    @Test
    public void getDefaultHistoryTest() {
        Assertions.assertNotNull(historyManager);
    }

    //проверка максимального кол-ва элементов в Листе Истории
    @Test
    public void getMaxElementsHistoryShouldBe10() {
        taskManager.addNewTask(task1);
        //добавим 11 значений в Лист Истории
        for(int i = 0; i < 11; i++) {
            taskManager.getTaskById(task1.getId());
        }
        Assertions.assertEquals(10, taskManager.getHistory().size());
    }

}
