public class Main {
    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();


        Task task1 = new Task("task1", "проверка", TaskStatus.NEW);
        Task task2 = new Task("task2", "проверка", TaskStatus.NEW);


        Epic epic = new Epic("epic","ed");
        Epic epic2 = new Epic("epic","ed");

        Subtask subtask = new Subtask("subtask1", "d", 1, TaskStatus.DONE);
        Subtask subtask1 = new Subtask("subtask2", "d", 1, TaskStatus.NEW);

        Subtask subtask2 = new Subtask("subtask3", "d", 2, TaskStatus.NEW);

        taskManager.addNewEpic(epic);
        taskManager.addNewEpic(epic2);


        taskManager.addNewSubTask(subtask);
        taskManager.addNewSubTask(subtask1);
        taskManager.addNewSubTask(subtask2);

        taskManager.addNewTask(task1);
        taskManager.addNewTask(task2);


        System.out.println(epic);
        for (Integer epi: epic.getEpicSubtasks()){
            System.out.println(taskManager.getSubtasks().get(epi));
        }

//        taskManager.deleteAllEpics();
//        taskManager.deleteAllTasks();

        System.out.println(epic);

    }
}
