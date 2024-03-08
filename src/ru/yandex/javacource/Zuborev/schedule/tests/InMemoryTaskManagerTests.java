package ru.yandex.javacource.Zuborev.schedule.tests;

import org.junit.jupiter.api.*;
import ru.yandex.javacource.Zuborev.schedule.manager.HistoryManager;
import ru.yandex.javacource.Zuborev.schedule.manager.Managers;
import ru.yandex.javacource.Zuborev.schedule.manager.TaskManager;
import ru.yandex.javacource.Zuborev.schedule.task.Epic;
import ru.yandex.javacource.Zuborev.schedule.task.Subtask;
import ru.yandex.javacource.Zuborev.schedule.task.Task;
import ru.yandex.javacource.Zuborev.schedule.task.TaskStatus;


public class InMemoryTaskManagerTests {

        private static TaskManager taskManager;
        private static HistoryManager historyManager;
        private static Managers manager;
        private static Task task1;
        private static Task task2;
        private static Epic epic1;
        private static Epic epic2;
        private static Subtask subtask1;
        private static Subtask subtask2;


        @BeforeAll
        public static void beforeAll() {
            manager = new Managers();
            task1 = new Task("сходить в магазин", " ", TaskStatus.NEW);
            task2 = new Task("сделать уборку", " ", TaskStatus.NEW);
            epic1 = new Epic("успеть сдать Проект", " ");
            subtask1 = new Subtask("начать делать проект", " ", epic1.getId(), TaskStatus.NEW);
            epic2 = new Epic("", " ");
            subtask2 = new Subtask("", " ", epic2.getId(), TaskStatus.NEW);
            subtask2 = new Subtask("", " ", epic2.getId(), TaskStatus.NEW);
            taskManager = Managers.getDefaultTaskManager();
            historyManager = Managers.getDeaultHistoryManager();
        }


        @Test
        public void getDefaultTest() {
            Assertions.assertNotNull(taskManager);
        }

        @Test
        public void getDefaultHistoryTest() {
            Assertions.assertNotNull(historyManager);
        }

        //Проверка генерации ID
        @Test
        public void getTaskCheckGenerationId(){
            taskManager.addNewTask(task1);
            taskManager.addNewTask(task2);
            Assertions.assertEquals(task2.getId(), task1.getId() + 1);
        }

        //Проверка удаления Таска по ID
        @Test
        public void getDeleteTaskByID() {
            taskManager.addNewTask(task1);
            taskManager.deleteTaskId(task1.getId());
            Assertions.assertEquals("[]", taskManager.getTasks().toString());
        }


        //Проверка получения Таска по ID
        @Test
        public void getTaskById() {
            taskManager.addNewTask(task1);
            Assertions.assertEquals(task1,taskManager.getTaskById(task1.getId()));
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

