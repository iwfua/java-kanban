package test.ru.yandex.javacource.zuborev.schedule.manager;

import ru.yandex.javacource.zuborev.schedule.manager.HistoryManager;
import ru.yandex.javacource.zuborev.schedule.manager.InMemoryHistoryManager;
import ru.yandex.javacource.zuborev.schedule.manager.InMemoryTaskManager;
import ru.yandex.javacource.zuborev.schedule.manager.TaskManager;

public class Managers {
    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager(getDeaultHistoryManager());
    }

    public static HistoryManager getDeaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
