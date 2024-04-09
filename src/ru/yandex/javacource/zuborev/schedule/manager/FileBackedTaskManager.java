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

    public static String historyToString(HistoryManager manager) {
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

    public static List<Integer> historyFromString(String value) {
        String[] historys = value.split(", ");
        List<Integer> historyIds = new ArrayList<>();
        for (String history : historys) {
            historyIds.add(Integer.valueOf(history));
        }
        return historyIds;
    }

    //метод, который будет восстанавливать данные менеджера из файла при запуске программы
    public static FileBackedTaskManager loadFromFile(File file) {
//        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(Managers.getDeaultHistoryManager());
//        String path = file.getPath();
//        fileBackedTaskManager.taskFromString(path);
//        return fileBackedTaskManager;
        FileBackedTaskManager manager = new FileBackedTaskManager(Managers.getDeaultHistoryManager());

        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
            bufferedReader.readLine();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1) {
                    int id = Integer.parseInt(parts[0]);
                    TypeTask type = TypeTask.valueOf(parts[1]);
                    String name = parts[2];
                    TaskStatus status = TaskStatus.valueOf(parts[3]);
                    String description = parts[4];
                    if (type == TypeTask.TASK) {
                        Task task = new Task(name, description, status, id);
                        manager.addNewTask(task);
                    } else if (type == TypeTask.EPIC) {
                        Epic epic = new Epic(name, description);
                        manager.addNewEpic(epic);
                    } else if (type == TypeTask.SUBTASK) {
                        int epicId = Integer.parseInt(parts[5]);
                        Subtask subtask = new Subtask(name, description, epicId, status);
                        manager.addNewSubtask(subtask);
                    }
                }
            }
        } catch (IOException exp) {
            throw new ManagerSaveException("Ошибка: " + exp.getMessage());
        }
        return manager;
    }

    //сохранение в файл
    private void save() {
        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(pathFile)) {
            bufferedWriter.write("id,type,name,status,description,epic\n");
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
    public String toStringTask(Task task) {
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
    //тут нужно строки прочитать из файла и обработать или метод будет принимать 1 строку и ее обрабатывать?
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
                TypeTask typeTask = TypeTask.valueOf(aboutTask[1]);
                TaskStatus taskStatus = TaskStatus.valueOf(aboutTask[3]);
                if (typeTask == TypeTask.TASK) {
                    Task task = new Task(name, description, taskStatus, id);
                    tasksFromString.add(task);
                } else if (typeTask == TypeTask.EPIC) {
                    Epic epic = new Epic(name, description);
                    tasksFromString.add(epic);
                } else if (typeTask == TypeTask.SUBTASK) {
                    int epicId = Integer.parseInt(aboutTask[aboutTask.length - 1]);
                    Subtask subtask = new Subtask(name, description, epicId, taskStatus);
                    tasksFromString.add(subtask);
                }
            }
        } catch (IOException exp) {
            throw new ManagerSaveException("Ошибка: " + exp.getMessage());
        }
        return tasksFromString;
    }
}
