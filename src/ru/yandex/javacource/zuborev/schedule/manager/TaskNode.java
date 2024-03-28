package ru.yandex.javacource.zuborev.schedule.manager;

import ru.yandex.javacource.zuborev.schedule.task.Task;

class TaskNode {
    Task task;
    TaskNode prev;
    TaskNode next;

    public TaskNode(Task task) {
        this.task = task;
        this.prev = null;
        this.next = null;
    }

    @Override
    public String toString() {
        return "TaskNode{" +
                "task=" + task +
                '}';
    }
}
