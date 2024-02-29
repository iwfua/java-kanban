package ru.yandex.javacource.Zuborev.schedule;

import ru.yandex.javacource.Zuborev.schedule.manager.TaskManager;
import ru.yandex.javacource.Zuborev.schedule.task.Epic;
import ru.yandex.javacource.Zuborev.schedule.task.Subtask;
import ru.yandex.javacource.Zuborev.schedule.task.Task;
import ru.yandex.javacource.Zuborev.schedule.task.TaskStatus;
public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();


        Task task1 = new Task("task1", "проверка", TaskStatus.NEW);
        Task task2 = new Task("task2", "проверка", TaskStatus.NEW);


        Epic epic = new Epic("epic","ed");
        Epic epic2 = new Epic("epic","ed");

        Subtask subtask = new Subtask("subtask1", "d", 1, TaskStatus.DONE);
        Subtask subtask1 = new Subtask("subtask2", "d", 1, TaskStatus.NEW);

        Subtask subtask2 = new Subtask("subtask3", "d", 1, TaskStatus.NEW);

        taskManager.addNewEpic(epic);
        taskManager.addNewEpic(epic2);


        taskManager.addNewSubtask(subtask);
        taskManager.addNewSubtask(subtask1);
        taskManager.addNewSubtask(subtask2);

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);


        System.out.println(epic);
        System.out.println(epic.getTaskStatus());

        System.out.println(taskManager.getEpics());


//        taskManager.deleteAllEpics();
//        taskManager.deleteAllTasks();


    }
}
