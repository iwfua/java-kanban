package ru.yandex.javacource.zuborev.schedule.manager;

import ru.yandex.javacource.zuborev.schedule.task.Task;
import ru.yandex.javacource.zuborev.schedule.task.Epic;
import ru.yandex.javacource.zuborev.schedule.task.Subtask;

import java.util.List;

public interface TaskManager {
    List<Task> getAllTask();

    List<Epic> getAllEpic();

    List<Subtask> getSubtask();

    // добавление новых задач
    void addNewTask(Task newTask);

    int addNewEpic(Epic newEpic);

    void addNewSubtask(Subtask subtask);

    // удаление всех задач
    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();

    // получение задачи по идентификатору
    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    // получение списка задач определенного эпика
    List<Subtask> getEpicSubtasks(int epicId);

    // получение списка всех задач
    List<Task> getTasks();

    List<Epic> getEpics();

    List<Subtask> getSubtasks();

    void deleteTaskById(int id);

    void deleteEpic(int id);

    void deleteSubtaskById(int id);

    // обновление задачи
    Integer updateTask(Task updateTask);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic updateEpic);

    List<Task> getPrioritizedTasks();

    List<Task> getHistory();
}

