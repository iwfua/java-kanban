package ru.yandex.javacource.zuborev.schedule.manager;

import ru.yandex.javacource.zuborev.schedule.task.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CSVTaskFormat {
    public static String getHeader() {
        return "id,type,name,status,description,startTime,endTime,duration,epic\n";
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
                + "," + task.getTaskStatus() + "," + task.getDescription()
                + "," + task.getStartTime() + "," + task.getEndTime() + "," + task.getDuration()
                + "," + (task.getType().equals(TypeTask.SUBTASK) ? ((Subtask) task).getEpicId() : "");
    }

//    Из строки в задачу
    public static Task taskFromString(String value) {
        LocalDateTime startTime = null;
        Duration duration = Duration.ZERO;
        final String[] values = value.split(",");
        final int id = Integer.parseInt(values[0]);
        final TypeTask type = TypeTask.valueOf(values[1]);
        final String name = values[2];
        final TaskStatus taskStatus = TaskStatus.valueOf(values[3]);
        final String description = values[4];
        if (!values[5].equals("null")){
            startTime = LocalDateTime.parse(values[5]);
        }
        if (!values[7].equals("null")) {
            duration = Duration.parse(values[7]);
        }
        if (type == TypeTask.TASK) {
            return new Task(name, description, taskStatus,startTime,duration,id);
        }
        if (type == TypeTask.SUBTASK) {
            final int epicId = Integer.parseInt(values[8]);
            return new Subtask(name,description,taskStatus,startTime,duration,id,epicId);
        } else {
            return new Epic(name, description, id);
        }
    }


}
