package ru.yandex.javacource.zuborev.schedule.manager;

import ru.yandex.javacource.zuborev.schedule.task.Task;

import java.util.*;


public class InMemoryHistoryManager implements HistoryManager {
    TaskNode head;
    TaskNode tail;
    HashMap<Integer, TaskNode> map;

    public InMemoryHistoryManager() {
        head = null;
        tail = null;
        map = new HashMap<>();
    }

    //Добавление в историю
    @Override
    public void add(Task task) {
        int taskId = task.getId();
        if (map.containsKey(taskId)) {
            TaskNode existingNode = map.get(taskId);
            removeNode(existingNode);
        }

        TaskNode newNode = new TaskNode(task);
        if (head == null) {
            head = newNode;
            tail = newNode;
        } else {
            newNode.next = head;
            head.prev = newNode;
            head = newNode;
        }
        map.put(taskId, newNode);
    }

    //Удаление из истории по ключу
    @Override
    public void remove(int id) {
        if (!map.containsKey(id)) {
            return;
        }

        TaskNode nodeToRemove = map.get(id);
        removeNode(nodeToRemove);
        map.remove(id);
    }

    //удаление ссылки
    private void removeNode(TaskNode node) {
        if (node == head) {
            head = head.next;
        }
        if (node == tail) {
            tail = tail.prev;
        }
        if (node.prev != null) {
            node.prev.next = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }
    }

    // Печать истории
    @Override
    public List<Task> getHistory() {
        List<Task> historyList = new ArrayList<>();
        TaskNode current = head;
        while (current != null) {
            historyList.add(current.task);
            current = current.next;
        }
        return historyList;
    }

    @Override
    public String toString() {
        return "TaskHistory{" +
                ", map=" + map +
                '}';
    }
}
