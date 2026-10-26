package com.example.coretasks;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/** Kiểm tra dữ liệu đầu vào và điều phối các thao tác công việc. */
public class TaskService {
    private final TaskRepository repository;

    public TaskService() throws IOException, SQLException {
        repository = new TaskRepository();
    }

    public Task addTask(String title) throws SQLException {
        if (title == null || title.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên công việc không được để trống.");
        }
        return repository.insert(title.trim());
    }

    public List<Task> getAllTasks() throws SQLException {
        return repository.findAll();
    }

    public boolean toggleTask(int id) throws SQLException {
        return repository.toggleCompleted(id);
    }

    public boolean deleteTask(int id) throws SQLException {
        return repository.deleteById(id);
    }

    public boolean updateNameTask(int id, String name) throws SQLException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Tên công việc không được để trống.");
        }
        return repository.updateTitle(id, name.trim());
    }

    public int countCompletedTasks() throws SQLException {
        return repository.countCompleted();
    }

    public Task getTaskById(int id) throws SQLException {
        return repository.findById(id);
    }

    public int addManyTask(List<String> titles) throws SQLException {
        if (titles == null) {
            throw new IllegalArgumentException("Danh sách công việc không được null.");
        }

        List<String> normalizedTitles = new ArrayList<>(titles.size());
        for (String title : titles) {
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Tên công việc không được để trống.");
            }
            normalizedTitles.add(title.trim());
        }

        return repository.manyInsert(normalizedTitles);
    }
}
