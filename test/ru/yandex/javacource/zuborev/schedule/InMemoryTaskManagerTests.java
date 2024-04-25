package ru.yandex.javacource.zuborev.schedule;

import org.junit.jupiter.api.*;
import ru.yandex.javacource.zuborev.schedule.manager.*;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class InMemoryTaskManagerTests extends TaskManagerTest<InMemoryTaskManager> {

    public InMemoryTaskManager createTaskManager() {
        return (InMemoryTaskManager) Managers.getDefaultTaskManager();
    }

    @Test
    public void testEpicStatus() {
        super.testEpicStatus();
    }

    @Test
    public void addNewTask() {
        super.testAddNewTask();
    }

    @Test
    public void testAddNewEpic() {
        super.testAddNewEpic();
    }

    @Test
    public void testAddNewSubTask() {
        super.testAddNewSubtask();
    }

    @Test
    public void getDeleteTaskByID() {
        super.getDeleteTaskByID();
    }

    @Test
    public void getTaskById() {
        super.getTaskById();
    }

    @Test
    public void getShouldBeNotChangesAfterAddInManager() {
        super.getShouldBeNotChangesAfterAddInManager();
    }

    @Test
    public void getCheckAddTaskSubTaskEpicAndGetById() {
        super.getCheckAddTaskSubTaskEpicAndGetById();
    }

    @Test
    public void getTaskCheckGenerationId() {
        super.getTaskCheckGenerationId();
    }

    //экземпляры класса Task равны друг другу, если равен их id
    @Test
    public void getEqualTasksIfIdEqualTo() {
        super.getEqualTasksIfIdEqualTo();
    }

    @Test
    public void getDefaultTest() {
        super.getDefaultTest();
    }

    //проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
    @Test
    public void getEqualToIdsDontConflicted() {
        super.getEqualToIdsDontConflicted();
    }


}

