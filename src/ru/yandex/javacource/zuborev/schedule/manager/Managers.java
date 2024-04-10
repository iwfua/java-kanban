package ru.yandex.javacource.zuborev.schedule.manager;

import java.io.File;

public class Managers {
    public static TaskManager getDefaultTaskManager() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getDefaultFile() {
        return new FileBackedTaskManager(
                new File("src/ru/yandex/javacource/zuborev/schedule/resourses/saved.csv"));
    }

    public static HistoryManager getDeaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
