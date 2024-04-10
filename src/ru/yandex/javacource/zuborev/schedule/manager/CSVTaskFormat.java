package ru.yandex.javacource.zuborev.schedule.manager;

import ru.yandex.javacource.zuborev.schedule.task.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CSVTaskFormat {
    public static String getHeader() {
        return "id,type,name,status,description,epic\n";
    }

    protected static String historyToString(HistoryManager manager) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Task> historys = manager.getHistory();
        int historySize = historys.size();
        for (int i = 0; i < historySize; i++) {
            stringBuilder.append(historys.get(i).getId());
            if (i < historySize - 1) {
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }

    protected static List<Integer> historyFromString(String value) {
        String[] historys = value.split(",");
        List<Integer> historyIds = new ArrayList<>();
        for (String history : historys) {
            historyIds.add(Integer.valueOf(history));
        }
        return historyIds;
    }

    //задачу в строку
    public static String taskToString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getName()
                + "," + task.getTaskStatus() + "," + task.getDescription() + ","
                + (task.getType().equals(TypeTask.SUBTASK) ? ((Subtask) task).getEpicId() : "");
    }

    //Из строки в задачу
    public static Task taskFromString(String value) {
        final String[] values = value.split(",");
        final int id = Integer.parseInt(values[0]);
        final TypeTask type = TypeTask.valueOf(values[1]);
        final String name = values[2];
        final TaskStatus taskStatus = TaskStatus.valueOf(values[3]);
        final String description = values[4];

        if (type == TypeTask.TASK) {
            return new Task(name, description, taskStatus, id);
        }
        if (type == TypeTask.SUBTASK) {
            final int epicId = Integer.parseInt(values[5]);
            return new Subtask(name,description,taskStatus,id,epicId);
        } else {
            return new Epic(name, description, id);
        }
    }

    //метод, который будет восстанавливать данные менеджера из файла при запуске программы
    public static FileBackedTaskManager loadFromFile(File file) {
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
}
