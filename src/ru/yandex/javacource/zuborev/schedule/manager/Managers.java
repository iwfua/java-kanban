package ru.yandex.javacource.zuborev.schedule.manager;

public class Managers {
    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager(getDeaultHistoryManager());
    }

    public static HistoryManager getDeaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
