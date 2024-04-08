package ru.yandex.javacource.zuborev.schedule.task;

public class Subtask extends Task {

    private int epicId;


    public Subtask(String name, String description, int epicId, TaskStatus taskStatus) {
        super(name, description, taskStatus);
        this.epicId = epicId;
    }


    public int getEpicId() {
        return epicId;
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