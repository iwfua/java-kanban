package ru.yandex.javacource.zuborev.schedule;

import ru.yandex.javacource.zuborev.schedule.manager.FileBackedTaskManager;
import ru.yandex.javacource.zuborev.schedule.manager.InMemoryHistoryManager;

public class Main {
    public static void main(String[] args) {
        // Создаем экземпляр FileBackedTaskManager
        FileBackedTaskManager manager = new FileBackedTaskManager(new InMemoryHistoryManager());

        // Получаем путь к файлу saved.csv в пакете resources
        String filePath = "src/ru/yandex/javacource/zuborev/schedule/resourses/saved.csv";
        System.out.println(manager.taskFromString(filePath));
        System.out.println();
    }
}

