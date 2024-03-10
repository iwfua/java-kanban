package test.ru.yandex.javacource.zuborev.schedule.task;

import ru.yandex.javacource.zuborev.schedule.task.Task;
import ru.yandex.javacource.zuborev.schedule.task.TaskStatus;

public class Subtask extends Task {

    private int epicId;


    public Subtask(String name, String description, int epicId, TaskStatus taskStatus){
        super(name, description, taskStatus);
        this.epicId = epicId;
    }


    public int getEpicId() {
        return epicId;
    }


    @Override
    public String toString() {
        return "ru.yandex.javacource.Zuborev.ru.yandex.javacource.Zuborev.schedule.task.Subtask{name='" + getName() + "', " +
                "epicId=" + epicId +
                "ru.yandex.javacource.Zuborev.ru.yandex.javacource.Zuborev.schedule.task.TaskStatus=" + getTaskStatus() +
                '}';
    }
}