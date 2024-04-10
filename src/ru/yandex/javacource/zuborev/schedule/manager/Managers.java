package ru.yandex.javacource.zuborev.schedule.manager;

public class Managers {
    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDeaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
