package ru.yandex.javacource.zuborev.schedule.task;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private TaskStatus taskStatus;
    private int id;

    public Task(String name, String description, TaskStatus taskStatus, int id) {
        this.name = name;
        this.description = description;
        this.taskStatus = taskStatus;
        this.id = id;
    }

    public Task(String name, String description, TaskStatus taskStatus) {
        this.taskStatus = taskStatus;
        this.name = name;
        this.description = description;
    }

    public int getId() {
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

    @Override
    public int hashCode() {
        return Objects.hash(name, description, taskStatus, id);
    }

    @Override
    public String toString() {
        return "ru.yandex.javacource.Zuborev.schedule.task.Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", taskStatus=" + taskStatus +
                ", id=" + id +
                '}';
    }
}