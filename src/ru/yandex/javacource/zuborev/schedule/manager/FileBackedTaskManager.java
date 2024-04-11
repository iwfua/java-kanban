package ru.yandex.javacource.zuborev.schedule.manager;

import ru.yandex.javacource.zuborev.schedule.task.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
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

    //метод, который будет восстанавливать данные менеджера из файла при запуске программы
    public static FileBackedTaskManager loadFromFile(File file) {
        List<String> ewf = new ArrayList<>();
        final FileBackedTaskManager taskManager = new FileBackedTaskManager(file);
        int generatorId = 0;
        try {
            final String csv = Files.readString(file.toPath());
            final String[] lines = csv.split(System.lineSeparator());

            List<Integer> history = Collections.emptyList();
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i];
                if (line.isEmpty()) {
                    history = CSVTaskFormat.historyFromString(lines[i + 1]);
                    break;
                }
                final Task task = CSVTaskFormat.taskFromString(line);
                final int id = task.getId();
                if (id > generatorId) {
                    generatorId = id;
                }
                taskManager.addAnyTask(task);
            }
            for (Map.Entry<Integer, Subtask> e : taskManager.subtasks.entrySet()) {
                final Subtask subtask = e.getValue();
                final Epic epic = taskManager.epics.get(subtask.getEpicId());
                epic.addSubtaskId(subtask.getId());
            }
            for (Integer taskId : history) {
                taskManager.historyManager.add(taskManager.findTask(taskId));
            }
            taskManager.setId(generatorId);
        } catch (IOException e) {
            throw new ManagerSaveException("Can't read form file: " + file.getName());
        }
        return taskManager;
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
