import java.io.*;
import java.util.ArrayList;
import java.util.List;
import task.*;

/**
 * Handles saving and loading tasks to and from a file.
 */
public class Storage {
    private static final String FILE_PATH = "./data/diana.txt";

    public static void saveTasks(TaskList tasks) throws IOException {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            file.getParentFile().mkdirs();
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));

        for (Task task : tasks.getTasks()) {
            writer.write(task.toFileFormat());
            writer.newLine();
        }

        writer.close();
    }

    public static TaskList loadTasks() throws IOException {
        TaskList tasks = new TaskList();
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            System.out.println("File does not exist");
            return tasks;
        }

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;

        while ((line = reader.readLine()) != null) {
            tasks.addTask(parseTask(line));
        }

        System.out.println("Loaded " + tasks.getTasks().size() + " tasks");

        reader.close();
        return tasks;
    }

    private static Task parseTask(String line) {
        String[] parts = line.split(" \\| ");
        String taskType = parts[0];
        Boolean isDone = parts[1].equals("1");
        String description = parts[2];

        Task task;
        switch (taskType) {
        case "T":
            task = new Todo(description);
            break;
        case "D":
            String by = parts[3];
            task = new Deadline(description, by);
            break;
        case "E":
            String from = parts[3];
            String to = parts[4];
            task = new Event(description, from, to);
            break;
        default:
            task = new Task(description);
            System.out.println("Unknown task type: " + taskType);
        }

        if (isDone) {
            task.markAsDone();
        }

        return task;
    }
}
