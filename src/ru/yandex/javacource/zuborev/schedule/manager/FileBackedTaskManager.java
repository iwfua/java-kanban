package ru.yandex.javacource.zuborev.schedule.manager;

import ru.yandex.javacource.zuborev.schedule.task.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {
    private final Path pathFile = Path.of("src/ru/yandex/javacource/zuborev/schedule/resourses/saved.csv");

    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
//        this.pathFile = pathFile;
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

    static void FileBackedTaskManager() {

    }

    static void loadFromFile(File file) {

    }

    public Path getPathFile() {
        return pathFile;
    }

    private void save() {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(pathFile)) {
            bufferedWriter.write("id,type,name,status,description,epic\n");
            // Сохраняем задачи
            List<Task> allTasks = super.getAllTask();
            List<Epic> allEpics = super.getEpic();
            List<Subtask> allSubtasks = super.getSubtask();
            if (!allTasks.isEmpty()) {
                for (Task task : allTasks) {
                    bufferedWriter.write(toStringTask(task) + "\n");
                }
            }
            if (!allEpics.isEmpty()) {
                for (Epic epic : allEpics) {
                    bufferedWriter.write(toStringEpic(epic) + "\n");
                }
            }
            if (!allSubtasks.isEmpty()) {
                for (Subtask subtask : allSubtasks) {
                    bufferedWriter.write(toStringSubTask(subtask) + "\n");
                }
            }
        } catch (IOException exp) {
            throw new ManagerSaveException("Ошибка: " + exp.getMessage());
        }
    }

    //задачу -> строку
    private String toStringTask(Task task) {
        return task.getId() + "," + TypeTask.TASK + "," + task.getName() + ","
                + task.getTaskStatus() + "," + task.getDescription() + ",";
    }

    private String toStringEpic(Epic epic) {
        return epic.getId() + "," + TypeTask.EPIC + "," + epic.getName() + "," + epic.getTaskStatus()
                + "," + epic.getDescription();
    }

    public String toStringSubTask(Subtask subtask) {
        return subtask.getId() + "," + TypeTask.SUBTASK + "," + subtask.getName() + ","
                + subtask.getTaskStatus() + "," + subtask.getDescription() + "," + subtask.getEpicId();
    }

    //созданиe задачи из строки
    public List<Task> taskFromString(String value) {
        List<Task> tasksFromString = new ArrayList<>();
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(value))) {
            List<String> lines = Files.readAllLines(Path.of(value));
            lines.remove(0);
            for (String line : lines) {
                String[] aboutTask = line.split(",");
                int id = Integer.parseInt(aboutTask[0]);
                String name = aboutTask[2];
                String description = aboutTask[4];
                TaskStatus taskStatus = TaskStatus.valueOf(aboutTask[3]);
                if (tasks.containsKey(id)) {
                    Task task = new Task(name, description, taskStatus, id);
                    tasksFromString.add(task);
                } else if (epics.containsKey(id)) {
                    Epic epic = new Epic(name, description);
                    tasksFromString.add(epic);
                } else if (subtasks.containsKey(id)){
                    int epicId = Integer.parseInt(aboutTask[aboutTask.length - 1]);
                    Subtask subtask = new Subtask(name, description,epicId,taskStatus);
                    tasksFromString.add(subtask);
                }
            }
        } catch (IOException exp) {
            throw new ManagerSaveException("Ошибка: " + exp.getMessage());
        }
        return tasksFromString;
    }
}
