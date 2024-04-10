package ru.yandex.javacource.zuborev.schedule.manager;

import ru.yandex.javacource.zuborev.schedule.task.*;

import java.io.*;
import java.util.Map;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private File file;


    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    @Override
    public int addNewTask(Task task) {
        super.addNewTask(task);
        save();
        return task.getId();

    }

    @Override
    public int addNewSubtask(Subtask subtask) {
        super.addNewSubtask(subtask);
        save();
        return subtask.getId();
    }

    @Override
    public int addNewEpic(Epic epic) {
        super.addNewEpic(epic);
        save();
        return epic.getId();
    }

    protected void addAnyTask(Task task) {
        final int id = task.getId();
        switch (task.getType()) {
            case TASK:
                tasks.put(id, task);
                break;
            case SUBTASK:
                subtasks.put(id, (Subtask) task);
                break;
            case EPIC:
                epics.put(id, (Epic) task);
                break;
        }
    }

    protected Task findTask(Integer id) {
        final Task task = tasks.get(id);
        if (task != null) {
            return task;
        }

        final Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            return subtask;
        }

        return epics.get(id);
    }

    //сохранение в файл
    protected void save() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
            bufferedWriter.write(CSVTaskFormat.getHeader());
            bufferedWriter.newLine();

            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                final Task task = entry.getValue();
                bufferedWriter.write(CSVTaskFormat.taskToString(task));
                bufferedWriter.newLine();
            }

            for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
                final Subtask task = entry.getValue();
                bufferedWriter.write(CSVTaskFormat.taskToString(task));
                bufferedWriter.newLine();
            }

            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                final Epic task = entry.getValue();
                bufferedWriter.write(CSVTaskFormat.taskToString(task));
                bufferedWriter.newLine();
            }

            bufferedWriter.newLine();
            bufferedWriter.write(CSVTaskFormat.historyToString(historyManager));
            bufferedWriter.newLine();
        } catch (IOException e) {
            throw new ManagerSaveException("Can't save to file: " + file.getName());
        }
    }
}
