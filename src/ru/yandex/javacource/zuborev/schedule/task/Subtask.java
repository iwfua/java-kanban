package ru.yandex.javacource.zuborev.schedule.task;

public class Subtask extends Task {

    private int epicId;


    public Subtask(String name, String description, int epicId, TaskStatus taskStatus) {
        super(name, description, taskStatus);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus taskStatus, int id, int epicId) {
        super(name, description, taskStatus, id);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public TypeTask getType() {
        return TypeTask.SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "id= " + getId() +
                ", name='" + getName() +
                ", epicId=" + epicId +
                ", TaskStatus=" + getTaskStatus() +
                '}';
    }
}