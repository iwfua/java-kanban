package ru.yandex.javacource.zuborev.schedule.manager;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.javacource.zuborev.schedule.task.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class FileBackedTaskManagerTests extends TaskManagerTest<FileBackedTaskManager> {
    private File tempFile;


    public FileBackedTaskManager createTaskManager() {
        return (FileBackedTaskManager) Managers.getDefaultFile();
    }

    private FileBackedTaskManager fileBackedTaskManager;

    @BeforeEach
    public void addFile() throws IOException {
        tempFile = File.createTempFile("test", ".csv");
    }

    @Test
    public void testLoadFromFile() throws IOException {
        // Записываем в файл тестовые данные
        String fileContent = CSVTaskFormat.getHeader() + "1,TASK,name,NEW,decrp,2020-10-11T12:13,2020-10-11T12:23,PT10M,\n" +
                "2,TASK,name,NEW,decrp,2020-10-11T12:12,2020-10-11T12:22,PT10M,\n" +
                "4,SUBTASK,name,NEW,descrp,2020-10-11T12:13,2020-10-11T12:23,PT10M,3\n" +
                "5,SUBTASK,name,NEW,descrp,2020-10-11T12:13,2020-10-11T12:23,PT10M,3\n" +
                "7,SUBTASK,name,NEW,descrp,null,null,null,3\n" +
                "3,EPIC,name,NEW,desrp,null,null,null,";
        Files.write(tempFile.toPath(), fileContent.getBytes());

        // Загружаем данные из файла
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);

        // Проверяем, что данные были успешно загружены
        assertNotNull(fileBackedTaskManager);


        // Проверяем, что задачи были добавлены
        assertEquals(2, fileBackedTaskManager.getAllTask().size());
        assertEquals(1, fileBackedTaskManager.getEpics().size());
        assertEquals(3, fileBackedTaskManager.getSubtasks().size());

        // Проверяем первую задачи
        Task task1 = fileBackedTaskManager.getTaskById(1);
        assertEquals("name", task1.getName());
        assertEquals("decrp", task1.getDescription());
        assertEquals(LocalDateTime.of(2020, 10, 11, 12, 13), task1.getStartTime());
        assertEquals(LocalDateTime.of(2020, 10, 11, 12, 23), task1.getEndTime());
        assertEquals(Duration.ofMinutes(10), task1.getDuration());

        // Проверяем вторую задачи
        Task task2 = fileBackedTaskManager.getTaskById(2);
        assertEquals("name", task2.getName());
        assertEquals("decrp", task2.getDescription());
        assertEquals(LocalDateTime.of(2020, 10, 11, 12, 12), task2.getStartTime());
        assertEquals(LocalDateTime.of(2020, 10, 11, 12, 22), task2.getEndTime());
        assertEquals(Duration.ofMinutes(10), task2.getDuration());

        // Проверяем первого сабтаска
        Task subtask1 = fileBackedTaskManager.getSubtaskById(4);
        assertEquals("name", subtask1.getName());
        assertEquals("descrp", subtask1.getDescription());
        assertEquals(LocalDateTime.of(2020, 10, 11, 12, 13), subtask1.getStartTime());
        assertEquals(LocalDateTime.of(2020, 10, 11, 12, 23), subtask1.getEndTime());
        assertEquals(Duration.ofMinutes(10), subtask1.getDuration());

        // Проверяем второго сабтаска
        Task subtask2 = fileBackedTaskManager.getSubtaskById(5);
        assertEquals("name", subtask2.getName());
        assertEquals("descrp", subtask2.getDescription());
        assertEquals(LocalDateTime.of(2020, 10, 11, 12, 13), subtask2.getStartTime());
        assertEquals(LocalDateTime.of(2020, 10, 11, 12, 23), subtask2.getEndTime());
        assertEquals(Duration.ofMinutes(10), subtask2.getDuration());

        // Проверяем третьего сабтаска
        Task subtask3 = fileBackedTaskManager.getSubtaskById(7);
        assertEquals("name", subtask3.getName());
        assertEquals("descrp", subtask3.getDescription());
        assertNull(subtask3.getStartTime());
        assertNull(subtask3.getEndTime());

        // Проверяем корректность данных эпика
        Epic epic = fileBackedTaskManager.getEpicById(3);
        assertEquals("name", epic.getName());
        assertEquals("desrp", epic.getDescription());
    }


    //проверка создания строки из задачи
    @Test
    void getTaskToString() {
        fileBackedTaskManager = new FileBackedTaskManager(tempFile);
        Task task1 = new Task("Task 1", "Description 1", TaskStatus.DONE, 1);
        fileBackedTaskManager.addNewTask(task1);
        String expected = "1,TASK,Task 1,DONE,Description 1,null,null,null,";
        String actual = CSVTaskFormat.taskToString(task1);
        System.out.println();
        assertEquals(expected, actual);
    }

    //проверка создания задачи из строки
    @Test
    public void testTaskFromString() {
        String taskString = "5,SUBTASK,name,NEW,descrp,2020-10-11T12:13,2020-10-11T13:53,PT10M,3";

        Task task = CSVTaskFormat.taskFromString(taskString);

        assertNotNull(task);
        assertEquals(5, task.getId());
        assertEquals(TypeTask.SUBTASK, task.getType());
        assertEquals("name", task.getName());
        assertEquals("NEW", task.getTaskStatus().name());
        assertEquals("descrp", task.getDescription());
        assertEquals(LocalDateTime.parse("2020-10-11T12:13"), task.getStartTime());
        assertEquals(Duration.parse("PT10M"), task.getDuration());
        assertEquals(3, ((Subtask) task).getEpicId());
    }

    @AfterEach
    public void deleteTempFile() {
        if (tempFile != null) {
            tempFile.delete();
        }
    }

    @Test
    public void testSaveThrowsManagerSaveException() {
        File tempFile = null;
        try {
            tempFile = File.createTempFile("temp", ".csv");
            FileBackedTaskManager taskManager = new FileBackedTaskManager(tempFile);

            // Предположим, что временный файл недоступен для записи
            tempFile.setReadOnly();

            // проверка выбрасывания исключения
            ManagerSaveException exception = assertThrows(ManagerSaveException.class, taskManager::save);
            assertEquals("Can't save to file: " + tempFile.getName(), exception.getMessage());
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        } finally {
            if (tempFile != null) {
                tempFile.deleteOnExit();
            }
        }
    }

    @Test
    public void addNewTask() {
        super.testAddNewTask();
    }

    @Test
    public void testAddNewEpic() {
        super.testAddNewEpic();
    }

    @Test
    public void testAddNewSubTask() {
        super.testAddNewSubtask();
    }

    @Test
    public void checkSavePrioritizeTasks() {
        super.checkSavePrioritizeTasks();
    }

    @Test
    public void getDeleteTaskByID() {
        super.getDeleteTaskByID();
    }

    @Test
    public void getTaskById() {
        super.getTaskById();
    }

    @Test
    public void getShouldBeNotChangesAfterAddInManager() {
        super.getShouldBeNotChangesAfterAddInManager();
    }

    @Test
    public void getCheckAddTaskSubTaskEpicAndGetById() {
        super.getCheckAddTaskSubTaskEpicAndGetById();
    }

    @Test
    public void getTaskCheckGenerationId() {
        super.getTaskCheckGenerationId();
    }

    //экземпляры класса Task равны друг другу, если равен их id
    @Test
    public void getEqualTasksIfIdEqualTo() {
        super.getEqualTasksIfIdEqualTo();
    }

    @Test
    public void getDefaultTest() {
        super.getDefaultTest();
    }

    //проверьте, что задачи с заданным id и сгенерированным id не конфликтуют внутри менеджера
    @Test
    public void getEqualToIdsDontConflicted() {
        super.getEqualToIdsDontConflicted();
    }
}


