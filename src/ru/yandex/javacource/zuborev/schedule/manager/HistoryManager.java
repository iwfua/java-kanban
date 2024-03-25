package ru.yandex.javacource.zuborev.schedule.manager;

import ru.yandex.javacource.zuborev.schedule.task.Task;

import java.util.List;
import java.util.Set;

public interface HistoryManager {
    void add(Task task);
    void remove(int id);
    Set<Task> getHistory();
}
