package com.example.coretasks;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class FileStorageDemo {

    public static void writeTasks(List<Task> tasks) throws IOException {
        Path path = Path.of("data", "tasks.txt");
        Files.createDirectories(path.getParent());
        List<String> lines = new ArrayList<>();
        for (Task task : tasks) {
            String line = task.getId() + "|" + task.getTitle() + "|" + task.isCompleted();
            lines.add(line);
        }
        Files.write(path, lines, StandardCharsets.UTF_8);
    }

    public static List<Task> readTasks() throws IOException {
        Path path = Path.of("data", "tasks.txt");

        if (Files.notExists(path)) {
            return new ArrayList<>();
        }

        List<Task> tasks = new ArrayList<>();
        for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
            if (line.isBlank()) {
                continue;
            }
            String[] fields = line.split("\\|", 3);

            int id = Integer.parseInt(fields[0]);
            String title = fields[1];
            boolean completed = Boolean.parseBoolean(fields[2]);

            Task task = new Task(id, title, completed);

            tasks.add(task);
        }

        return tasks;
    }
}