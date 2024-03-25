package ru.yandex.javacource.zuborev.schedule.manager;

import ru.yandex.javacource.zuborev.schedule.task.Task;
import java.util.HashSet;
import java.util.Set;

public class InMemoryHistoryManager implements HistoryManager {
    private final Set<Task> history = new HashSet<>();

    @Override
    public void add(Task task) {
        history.add(task);
    }

    @Override
    public void remove(int id) {
        history.remove(id);
    }

    @Override
    public Set<Task> getHistory() {
        return history;
    }
}
