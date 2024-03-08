package ru.yandex.javacource.Zuborev.schedule.manager;

import ru.yandex.javacource.Zuborev.schedule.task.Task;

import java.util.List;

public interface HistoryManager {
    void add(Task task);

    List<Task> getHistory();
}
