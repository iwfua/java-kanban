package ru.yandex.javacource.Zuborev.schedule.manager;

import ru.yandex.javacource.Zuborev.schedule.task.Epic;
import ru.yandex.javacource.Zuborev.schedule.task.Subtask;
import ru.yandex.javacource.Zuborev.schedule.task.Task;
import ru.yandex.javacource.Zuborev.schedule.task.TaskStatus;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int id = 0;

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();


    public ArrayList<Task> getAllTask() {
        return new ArrayList<>(tasks.values());
    }
    public ArrayList<Epic> getEpic() {
        return new ArrayList<>(epics.values());
    }
    public ArrayList<Subtask> getSubtask() {
        return new ArrayList<>(subtasks.values());
    }

    // добавление новых задач
    public int addNewTask(Task newTask) {
        newTask.setId(idGenerator());
        tasks.put(newTask.getId(), newTask);
        return newTask.getId();
    }

    public int addNewEpic(Epic newEpic) {
        newEpic.setId(idGenerator());
        epics.put(newEpic.getId(), newEpic);
        return newEpic.getId();
    }

    public Integer addNewSubtask(Subtask subtask) {
        int epicId = subtask.getEpicId();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        int id = idGenerator();
        subtask.setId(id);
        subtasks.put(id, subtask);
        epic.addNewSubtask(subtask.getId());
        updateStatus(epic);
        return id;
    }

    // изменение статуса задачи

    // удаление всех задач
    public void deleteAllTasks() {
        tasks.clear();
    }

    public void deleteAllEpics() {
        subtasks.clear();
        epics.clear();
    }

    public void deleteAllSubtasks() {
        for (Epic epic : epics.values()) {
            epic.deleteAllSubtasksOfEpic();
            updateStatus(epic);
        }
        subtasks.clear();
    }


    // получение задачи по идентификатору
    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    // получение списка задач определенного эпика
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> tasks = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return null;
        }
        for (int id : epic.getSubtaskIds()) {
            tasks.add(subtasks.get(id));
        }
        return tasks;
    }


    // получение списка всех задач
    public ArrayList<Task> getTasks() {
        ArrayList<Task> allTasks = new ArrayList<>();
        for (Task task : tasks.values()) {
            allTasks.add(tasks.get(task.getId()));
        }
        return allTasks;
    }

    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> allEpics = new ArrayList<>();
        for (Epic epic : epics.values()) {
            allEpics.add(epics.get(epic.getId()));
        }
        return allEpics;
    }

    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> allSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            allSubtasks.add(subtasks.get(subtask.getId()));
        }
        return allSubtasks;
    }

    // удаление по идентификатору

    public void deleteTaskId(int id) {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
        }
    }

    public void deleteEpic(int id) {
        final Epic epic = epics.remove(id);
        for (Integer subtaskId : epic.getSubtaskIds()) {
            subtasks.remove(subtaskId);
        }
    }

    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.deleteSubtask(id);
        updateStatus(epic);
    }


    // обновление задачи
    public void updateTask(Task updateTask) {
        if (tasks.containsKey(updateTask.getId())) {
            tasks.put(updateTask.getId(), updateTask);
        }
    }

    public void updateSubtask(Subtask subtask) {
        int id = subtask.getId();
        int epicId = subtask.getEpicId();
        Subtask savedSubtask = subtasks.get(id);
        if (savedSubtask == null) {
            return;
        }
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return;
        }
        subtasks.put(id, subtask);
        updateStatus(epic);
    }

    public void updateEpic(Epic updateEpic) {
        if (epics.containsKey(updateEpic.getId())) {
            epics.get(updateEpic.getId()).setName(updateEpic.getName());
            epics.get(updateEpic.getId()).setDescription(updateEpic.getDescription());
        }
    }

    private void updateStatus(Epic epic) {

        if (epic.getSubtaskIds().isEmpty()) {
            for (Integer subtask: epic.getSubtaskIds()) {
                subtasks.get(subtask).setTaskStatus(TaskStatus.NEW);
            }
            return;
        }

        int done = 0;
        int New = 0;
        int inProgress = 0;


        for (Integer id : epic.getSubtaskIds()) {
            switch (subtasks.get(id).getTaskStatus()) {
                case NEW:
                    New += 1;
                    break;
                case DONE:
                    done += 1;
                    break;
                case IN_PROGRESS:
                    inProgress += 1;
                    break;
            }
        }

        if ((done > 0 && New > 0) || inProgress > 0) {
            epic.setTaskStatus(TaskStatus.IN_PROGRESS);
            return;
        } else if (New > 0 && done < 0) {
            epic.setTaskStatus(TaskStatus.NEW);
        } else {
            epic.setTaskStatus(TaskStatus.DONE);
        }
    }

    // генерация id
    private int idGenerator() {
        id++;
        return id;
    }
}