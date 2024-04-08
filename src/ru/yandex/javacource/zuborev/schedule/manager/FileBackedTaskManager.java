package ru.yandex.javacource.zuborev.schedule.manager;

import ru.yandex.javacource.zuborev.schedule.task.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager implements TaskManager {

    public void setPathFile(Path pathFile) {
        this.pathFile = pathFile;
    }

    private Path pathFile = Path.of("/Users/nikolay/IdeaProjects/java-kanban/src/ru/yandex/javacource/zuborev/schedule/resourses/saved.csv");

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

    public Task fromStringTask(String value) {
        String[] aboutTask = value.split(",");
        int id = Integer.parseInt(aboutTask[0]);
        String name = aboutTask[2];
        TaskStatus taskStatus = TaskStatus.valueOf(aboutTask[3]);
        String description = aboutTask[4];
        return new Task(name, description, taskStatus, id);
    }

//    public Epic fromStringEpic(String value) {
//        String[] aboutTask = value.split(",");
//
//        return new Epic(name, description, taskStatus, id);
//    }
//
//    public Subtask fromStringSubtask(String value) {
//        String[] aboutTask = value.split(",");
//        int
//        int epicId = Integer.parseInt(aboutTask[aboutTask.length - 1]);
//        return new Subtask(name, description,epicId,taskStatus);
//    }
}
