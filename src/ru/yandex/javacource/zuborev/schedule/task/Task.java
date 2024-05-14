package ru.yandex.javacource.zuborev.schedule.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private TaskStatus taskStatus;
    private int id;
    private Duration duration;
    private LocalDateTime startTime;

    public Task(String name, String description, TaskStatus taskStatus) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
    }

    public Task(String name, String description, TaskStatus taskStatus, int id) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.id = id;
    }

    public Task(String name, String description, int id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }


    public Task(String name, String description, TaskStatus taskStatus,LocalDateTime startTime,Duration duration, int id) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
        this.id = id;
    }

    public Task(String name, String description, TaskStatus taskStatus,LocalDateTime startTime,Duration duration) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Duration getDuration() {
        if (duration == null) {
            return null;
        } else {
            return Duration.ofSeconds(duration.getSeconds());
        }
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plus(duration);
        } else {
            return null;
        }
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Integer getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public TaskStatus getTaskStatus() {
        return taskStatus;
    }

    public void setStatus(TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
    }

    public void setId(int id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        Task task = (Task) object;
        return id == task.id && Objects.equals(name, task.name) && Objects.equals(description, task.description) && taskStatus == task.taskStatus;
    }

    public TypeTask getType() {
        return TypeTask.TASK;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, taskStatus, id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                ", id=" + id +
                ", startTime=" + startTime +
                ", duration=" + getDuration() +
                ", endTime=" + getEndTime() +
                '}';
    }
}