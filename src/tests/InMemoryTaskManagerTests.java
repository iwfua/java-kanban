package tests;

import org.junit.jupiter.api.*;
import ru.yandex.javacource.zuborev.schedule.manager.HistoryManager;
import ru.yandex.javacource.zuborev.schedule.manager.Managers;
import ru.yandex.javacource.zuborev.schedule.manager.TaskManager;
import ru.yandex.javacource.zuborev.schedule.task.Epic;
import ru.yandex.javacource.zuborev.schedule.task.Subtask;
import ru.yandex.javacource.zuborev.schedule.task.Task;
import ru.yandex.javacource.zuborev.schedule.task.TaskStatus;


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


    @BeforeEach
    public void beforeEach() {
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

    //убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных.
    @Test
    public void getCheckChangesAfterUpdatedInfoInTask() {
        Task currentTask = new Task("сделать дз", "kemf", TaskStatus.NEW);
        Task updatedTask = new Task("Обновленный таск", "kemf", TaskStatus.NEW);

        Task expected = currentTask;

        taskManager.addNewTask(currentTask);
        taskManager.addNewTask(updatedTask);

        taskManager.getTaskById(currentTask.getId());
        taskManager.getHistory();

        updatedTask.setId(currentTask.getId());
        taskManager.updateTask(updatedTask);

        Assertions.assertEquals(expected, taskManager.getHistory().get(task1.getId()));
    }

    //проверьте, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id
    @Test
    public void getCheckAddTaskSubTaskEpicAndGetById() {
        taskManager.addNewTask(task1);
        taskManager.addNewEpic(epic1);
        Epic expectedEpic = taskManager.getEpicById(epic1.getId());
        Task expectedTAsk = taskManager.getTaskById(task1.getId());

        Assertions.assertEquals(epic1, expectedEpic);
        Assertions.assertEquals(task1, expectedTAsk);
    }

    //проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
    @Test
    public void getEqualToIdsDontConflicted() {
        Task firstTask = new Task("task1", "desckr1", TaskStatus.NEW);
        Task secondTask = new Task("task1", "desckr1", TaskStatus.NEW,0);
        taskManager.addNewTask(firstTask);
        taskManager.addNewTask(secondTask);
        Assertions.assertNotEquals(taskManager.getTaskById(firstTask.getId()), taskManager.getTaskById(secondTask.getId()));
    }

    @Test
    public void getDefaultTest() {
        Assertions.assertNotNull(taskManager);
    }


    @Test
    public void getDeleteTaskByID() {
        taskManager.addNewTask(task1);
        taskManager.deleteTaskId(task1.getId());
        Assertions.assertEquals("[]", taskManager.getTasks().toString());
    }



    //создайте тест, в котором проверяется неизменность задачи (по всем полям) при добавлении задачи в менеджер
    @Test
    public void getShouldBeNotChangesAfterAddInManager() {
        Task currentTask1 = new Task("Таск", "таск", TaskStatus.NEW);
        Task updatedTask1 = new Task("Обновленный таск", "обновленный", TaskStatus.IN_PROGRESS);
        taskManager.addNewTask(currentTask1);
        taskManager.addNewTask(updatedTask1);

        updatedTask1.setId(currentTask1.getId());
        taskManager.updateTask(updatedTask1);

        Assertions.assertNotEquals(currentTask1.getTaskStatus(), taskManager.getTasks().get(0).getTaskStatus());
        Assertions.assertNotEquals(currentTask1.getName(), taskManager.getTasks().get(0).getName());
        Assertions.assertNotEquals(currentTask1.getDescription(), taskManager.getTasks().get(0).getDescription());

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


    //проверьте, что экземпляры класса Task равны друг другу, если равен их id
    @Test
    public void getEqualTasksIfIdEqualTo() {
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        task1.setId(task2.getId());
        taskManager.updateTask(task1);

        Assertions.assertEquals(taskManager.getTasks().get(0), taskManager.getTasks().get(1));
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

