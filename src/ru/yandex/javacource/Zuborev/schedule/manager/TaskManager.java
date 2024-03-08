package ru.yandex.javacource.Zuborev.schedule.manager;

import ru.yandex.javacource.Zuborev.schedule.task.Epic;
import ru.yandex.javacource.Zuborev.schedule.task.Subtask;
import ru.yandex.javacource.Zuborev.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    ArrayList<Task> getAllTask();

    ArrayList<Epic> getEpic();

    ArrayList<Subtask> getSubtask();

    // добавление новых задач
    int addNewTask(Task newTask);

    int addNewEpic(Epic newEpic);

    Integer addNewSubtask(Subtask subtask);

    // удаление всех задач
    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    // получение задачи по идентификатору
    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    // получение списка задач определенного эпика
    ArrayList<Subtask> getEpicSubtasks(int epicId);

    // получение списка всех задач
    ArrayList<Task> getTasks();

    ArrayList<Epic> getEpics();

    ArrayList<Subtask> getSubtasks();

    void deleteTaskId(int id);

    void deleteEpic(int id);

    void deleteSubtask(int id);

    // обновление задачи
    void updateTask(Task updateTask);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic updateEpic);

    List<Task> getHistory();
}

