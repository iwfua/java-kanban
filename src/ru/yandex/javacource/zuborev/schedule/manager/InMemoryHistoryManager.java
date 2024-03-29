package ru.yandex.javacource.zuborev.schedule.manager;

import ru.yandex.javacource.zuborev.schedule.task.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private TaskNode first;
    private TaskNode last;
    private final HashMap<Integer, TaskNode> history;

    public InMemoryHistoryManager() {
        first = null;
        last = null;
        history = new HashMap<>();
    }


    //Добавление в историю
    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }
        final int id = task.getId();
        removeNode(id);
        linkLast(task);
        history.put(id, last);
    }

    private void linkLast(Task task) {
        final TaskNode node = new TaskNode(task, last, null);
        if (first == null) {
            first = node;
        } else {
            last.next = node;
        }
        last = node;
    }

    //Удаление из истории по ключу
    @Override
    public void remove(int id) {
        removeNode(id);
    }

    //удаление ссылки
    private void removeNode(int id) {
        final TaskNode node = history.remove(id);
        if (node == null) {
            return;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
            if (node.next == null) {
                last = node.prev;
            } else {
                node.next.prev = node.prev;
            }
        } else {
            first = node.next;
            if (first == null) {
                last = null;
            } else {
                first.prev = null;
            }
        }
    }

    // Печать истории
    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        TaskNode current = first;
        while (current != null) {
            historyList.add(current.task);
            current = current.next;
        }
        return historyList;
    }

    static class TaskNode {
        private final Task task;
        private TaskNode prev;
        private TaskNode next;

        public TaskNode(Task task, TaskNode prev, TaskNode next) {
            this.task = task;
            this.prev = prev;
            this.next = next;
        }

        @Override
        public String toString() {
            return "TaskNode{" +
                    "task=" + task +
                    '}';
        }
    }
}
