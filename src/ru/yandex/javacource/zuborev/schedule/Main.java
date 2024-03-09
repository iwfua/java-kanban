package ru.yandex.javacource.zuborev.schedule;

import ru.yandex.javacource.zuborev.schedule.manager.Managers;
import ru.yandex.javacource.zuborev.schedule.manager.TaskManager;
import ru.yandex.javacource.zuborev.schedule.task.Epic;
import ru.yandex.javacource.zuborev.schedule.task.Subtask;
import ru.yandex.javacource.zuborev.schedule.task.Task;
import ru.yandex.javacource.zuborev.schedule.task.TaskStatus;

public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefaultTaskManager();

        Task task1 = new Task("task1", "проверка1", TaskStatus.NEW);
        Task task2 = new Task("task2", "проверка2", TaskStatus.NEW);
        Task task3 = new Task("task3", "проверка3", TaskStatus.NEW);


        Epic epic = new Epic("epic","ed");
        Epic epic2 = new Epic("epic","ed");

        Subtask subtask = new Subtask("subtask1", "d", 1, TaskStatus.DONE);
        Subtask subtask1 = new Subtask("subtask2", "d", 1, TaskStatus.NEW);

        Subtask subtask2 = new Subtask("subtask3", "d", 2, TaskStatus.NEW);

        taskManager.addNewEpic(epic);
        taskManager.addNewEpic(epic2);


        taskManager.addNewSubtask(subtask);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);
        taskManager.addNewTask(task3);

        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());

        taskManager.getSubtaskById(subtask1.getId());

        taskManager.getEpicById(1);



        System.out.println("History");
        System.out.println(taskManager.getHistory());


    }
}
