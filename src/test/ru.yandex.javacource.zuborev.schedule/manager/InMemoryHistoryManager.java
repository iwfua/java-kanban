package test.ru.yandex.javacource.zuborev.schedule.manager;

import ru.yandex.javacource.zuborev.schedule.manager.HistoryManager;
import ru.yandex.javacource.zuborev.schedule.task.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> history = new ArrayList<>();
    private static final int MAX_SIZE_HISTORY = 10;

    @Override
    public void add(Task task) {
        if (history.size() >= MAX_SIZE_HISTORY) {
            history.remove(0);
        }
        history.add(task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}
