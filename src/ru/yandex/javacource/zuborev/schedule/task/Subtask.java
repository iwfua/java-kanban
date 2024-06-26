package ru.yandex.javacource.zuborev.schedule.task;

import java.time.Duration;
import java.time.LocalDateTime;

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

    public Subtask(String name, String description, TaskStatus taskStatus, LocalDateTime startTime, Duration duration, int id, int epicId) {
        super(name, description, taskStatus, startTime, duration, id);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, TaskStatus taskStatus, LocalDateTime startTime, Duration duration, int epicId) {
        super(name, description, taskStatus, startTime, duration);
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
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", TaskStatus=" + getTaskStatus() +
                ", id=" + getId() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                ", endTime=" + getEndTime() +
                ", epicId=" + epicId +
                '}';
    }
}