package ru.yandex.javacource.zuborev.schedule.manager;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.zuborev.schedule.task.*;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public abstract class TaskManagerTest<T extends TaskManager> {

    protected abstract T createTaskManager();

    private static Task task1;
    private static Task task2;
    private static Epic epic1;
    T taskManager;
    LocalDateTime localDateTime;
    LocalDateTime localDateTime1;
    Duration duration;
    Duration duration1;

    @BeforeEach
    public void beforeEach() {
        localDateTime = LocalDateTime.of(2020,10,10,10,0);
        localDateTime1 = LocalDateTime.of(2020,10,10,11,0);
        duration = Duration.ofMinutes(10);
        duration1 = Duration.ofMinutes(10);
        task1 = new Task("сходить в магазин", " ", TaskStatus.NEW,localDateTime,duration,1);
        task2 = new Task("сделать уборку", " ", TaskStatus.NEW,localDateTime1,duration1,2);
        epic1 = new Epic("успеть сдать Проект", " ", 0);
        taskManager = createTaskManager();

    }

    @Test
    public void testEpicStatus() {
        //все NEW
        Epic epic = new Epic("Epic 1", "Description", 0);
        taskManager.addNewEpic(epic);
        taskManager.addNewSubtask(new Subtask("Subtask 1", "Description", epic.getId(), TaskStatus.NEW));
        taskManager.addNewSubtask(new Subtask("Subtask 2", "Description", epic.getId(), TaskStatus.NEW));
        taskManager.addNewSubtask(new Subtask("Subtask 3", "Description", epic.getId(), TaskStatus.NEW));

        assertEquals(TaskStatus.NEW, epic.getTaskStatus());

        //все DONE
        Epic epic3 = new Epic("Epic 2", "Description", 1);
        taskManager.addNewEpic(epic3);
        taskManager.addNewSubtask(new Subtask("Subtask 1", "Description", epic3.getId(), TaskStatus.DONE));
        taskManager.addNewSubtask(new Subtask("Subtask 2", "Description", epic3.getId(), TaskStatus.DONE));
        taskManager.addNewSubtask(new Subtask("Subtask 3", "Description", epic3.getId(), TaskStatus.DONE));

        assertEquals(TaskStatus.DONE, epic3.getTaskStatus());

        //все IN_PROGRESS
        Epic epic4 = new Epic("Epic 2", "Description", 2);
        taskManager.addNewEpic(epic4);
        taskManager.addNewSubtask(new Subtask("Subtask 1", "Description", epic4.getId(), TaskStatus.IN_PROGRESS));
        taskManager.addNewSubtask(new Subtask("Subtask 2", "Description", epic4.getId(), TaskStatus.IN_PROGRESS));
        taskManager.addNewSubtask(new Subtask("Subtask 3", "Description", epic4.getId(), TaskStatus.IN_PROGRESS));

        assertEquals(TaskStatus.IN_PROGRESS, epic4.getTaskStatus());

        //DONE и NEW
        Epic epic5 = new Epic("Epic 2", "Description", 3);
        taskManager.addNewEpic(epic5);
        taskManager.addNewSubtask(new Subtask("Subtask 1", "Description", TaskStatus.DONE, 15, epic5.getId()));
        taskManager.addNewSubtask(new Subtask("Subtask 2", "Description", TaskStatus.NEW,16, epic5.getId()));

        assertEquals(TaskStatus.IN_PROGRESS, epic5.getTaskStatus());
    }


    //проверка, что InMemoryTaskManager действительно добавляет задачи разного типа и может найти их по id
    @Test
    public void getCheckAddTaskSubTaskEpicAndGetById() {
        taskManager.addNewTask(task1);
        taskManager.addNewEpic(epic1);
        Epic expectedEpic = taskManager.getEpicById(epic1.getId());
        Task expectedTAsk = taskManager.getTaskById(task1.getId());

        Assertions.assertEquals(epic1, expectedEpic);
        Assertions.assertEquals(task1, expectedTAsk);
    }

    @Test
    public void testAddNewTask() {
        taskManager.addNewTask(task1);
        assertEquals(taskManager.getTaskById(task1.getId()), task1);
    }

    @Test
    public void testAddNewEpic() {
        Epic epic = new Epic("Test Epic", "Description");
        taskManager.addNewEpic(epic);
        assertEquals(taskManager.getEpicById(epic.getId()), epic);
    }

    @Test
    public void testAddNewSubtask() {
        int epicId = taskManager.addNewEpic(epic1);
        Subtask subtask = new Subtask("Test Subtask", "Description", epicId, TaskStatus.NEW);
        taskManager.addNewSubtask(subtask);
        assertEquals(taskManager.getSubtaskById(subtask.getId()), subtask);
    }

    @Test
    public void getDeleteTaskByID() {
        taskManager.addNewTask(task1);
        taskManager.deleteTaskId(task1.getId());
        assertEquals("[]", taskManager.getTasks().toString());
    }

    @Test
    public void checkSavePrioritizeTasks() {
        Duration duration = Duration.ofMinutes(100);
        LocalDateTime localDateTime = LocalDateTime.of(2020, 10, 11, 12, 13);
        LocalDateTime localDateTime2 = LocalDateTime.of(2020, 10, 11, 15, 13);
        LocalDateTime localDateTime3 = LocalDateTime.of(2020, 10, 11, 15, 15);


        Task task = new Task("name", "descrp", TaskStatus.NEW,localDateTime,duration,1);
        Task task1 = new Task("name", "descrp", TaskStatus.NEW,localDateTime2,duration,2);
        taskManager.addNewTask(task);
        taskManager.addNewTask(task1);

        int expectedSize = 2;
        int currentSize = taskManager.getPrioritizedTasks().size();
        Assertions.assertEquals(expectedSize,currentSize);

        Task task2 = new Task("name", "descrp", TaskStatus.NEW,localDateTime3,duration,3);

        Assertions.assertThrows(ManagerSaveException.class, () -> taskManager.addNewTask(task2)
                ,"Задача id=3 пересекаются с id=2 c 2020-10-11T15:13 по 2020-10-11T16:53");
    }

    @Test
    public void getShouldBeNotChangesAfterAddInManager() {
        Task currentTask1 = new Task("Таск", "таск", TaskStatus.NEW,localDateTime,duration,0);
        Task updatedTask1 = new Task("Обновленный таск", "обновленный",
                TaskStatus.IN_PROGRESS,localDateTime1,duration,1);

        taskManager.addNewTask(currentTask1);
        taskManager.addNewTask(updatedTask1);

        updatedTask1.setId(currentTask1.getId());
        taskManager.updateTask(updatedTask1);

        assertNotEquals(currentTask1.getTaskStatus(), taskManager.getTasks().get(0).getTaskStatus());
        assertNotEquals(currentTask1.getName(), taskManager.getTasks().get(0).getName());
        assertNotEquals(currentTask1.getDescription(), taskManager.getTasks().get(0).getDescription());
    }


    //Проверка получения Таска по ID
    @Test
    public void getTaskById() {
        taskManager.addNewTask(task1);
        assertEquals(task1, taskManager.getTaskById(task1.getId()));
    }

    //проверьте, что экземпляры класса Task равны друг другу, если равен их id
    @Test
    public void getEqualTasksIfIdEqualTo() {
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        task1.setId(task2.getId());
        taskManager.updateTask(task1);

        assertEquals(taskManager.getTasks().get(0), taskManager.getTasks().get(1));
    }

    //проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
    @Test
    public void getEqualToIdsDontConflicted() {
        Task firstTask = new Task("task1", "desckr1", TaskStatus.NEW ,localDateTime,duration,0);
        Task secondTask = new Task("task1", "desckr1", TaskStatus.NEW,localDateTime1,duration,1);
        taskManager.addNewTask(firstTask);
        taskManager.addNewTask(secondTask);
        assertNotEquals(taskManager.getTaskById(firstTask.getId()), taskManager.getTaskById(secondTask.getId()));
    }

    @Test
    public void getDefaultTest() {
        Assertions.assertNotNull(taskManager);
    }


    //Проверка генерации ID
    @Test
    public void getTaskCheckGenerationId() {
        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        Assertions.assertEquals(task2.getId(), task1.getId() + 1);
    }

    // Добавьте тесты для остальных методов интерфейса TaskManager
}