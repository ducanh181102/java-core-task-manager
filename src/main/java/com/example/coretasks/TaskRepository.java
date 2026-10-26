package com.example.coretasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** Đọc và ghi công việc trong SQLite bằng JDBC. */
public class TaskRepository {
    private static final Path DATABASE_PATH = Path.of("data", "tasks.db");
    private static final String DATABASE_URL = "jdbc:sqlite:" + DATABASE_PATH;

    public TaskRepository() throws IOException, SQLException {
        Files.createDirectories(DATABASE_PATH.getParent());
        createTableIfNeeded();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL);
    }

    private void createTableIfNeeded() throws SQLException {
        String sql = """
                CREATE TABLE IF NOT EXISTS tasks (
                    id INTEGER PRIMARY KEY,
                    title TEXT NOT NULL,
                    completed INTEGER NOT NULL DEFAULT 0
                )
                """;

        try (Connection connection = connect();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(sql);
        }
    }

    public Task insert (String title) throws SQLException {

        String sql = """
                INSERT INTO tasks (title) VALUES (?)
                """;

        try (Connection connection = connect();
            PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, title);
            statement.executeUpdate();

            String findSql = """
                    SELECT * FROM tasks
                    WHERE id = last_insert_rowid()
                    """;
            try (Statement idStatement = connection.createStatement()) {
                ResultSet resultSet = idStatement.executeQuery(findSql);
                if (resultSet.next()) {
                    return getTask(resultSet);
                }
            }
        }
        throw new SQLException("Không lấy được id của công việc vừa thêm");
    }

    public List<Task> findAll() throws SQLException {
        String sql = """
                SELECT * FROM tasks
                """;
        List<Task> tasks = new ArrayList<>();

        try (Connection connection = connect();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
                while (resultSet.next()) {
                    tasks.add(getTask(resultSet));
                }
        }
        return List.copyOf(tasks);
    }


    private Task getTask (ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt("id");
        String title = resultSet.getString("title");
        boolean completed = resultSet.getInt("completed") == 1;
        return new Task(id, title, completed);
    }

    public boolean toggleCompleted(int id) throws SQLException{
        String sql = """
                UPDATE tasks
                SET completed = CASE WHEN completed = 1 THEN 0 ELSE 1 END
                WHERE id = ?
                """;
        try (Connection connection = connect();
             PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
        }
    }

    public boolean deleteById(int id) throws SQLException {
        String sql = """
                DELETE FROM tasks
                WHERE id = ?
                """;
        try (Connection connection = connect();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);
            return statement.executeUpdate() == 1;
        }
    }

    public boolean updateTitle(int id, String title) throws SQLException {
        String sql = """
                UPDATE tasks SET title = ?
                WHERE id = ?
                """;
        try (Connection connection = connect();
            PreparedStatement statement = connection.prepareStatement(sql);
        ) {
            statement.setString(1, title);
            statement.setInt(2, id);
            return statement.executeUpdate() == 1;
        }
    }

    public int countCompleted() throws SQLException {
        String sql = """
                SELECT count(*)
                FROM tasks
                WHERE completed = 1
                """;
        try(Connection connection = connect();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql)) {

            if (resultSet.next()) {
                return resultSet.getInt(1);
            }
        }
        throw new SQLException("Không đếm được công việc hoàn thành");
    }

    public Task findById(int id) throws SQLException {
        String sql = """
                SELECT * FROM tasks
                WHERE id = ?
                """;
        try(Connection connection = connect();
            PreparedStatement statement = connection.prepareStatement(sql)
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return getTask(resultSet);
                }
            }
        }
        return null;
    }

    public int manyInsert(List<String> titles) throws SQLException {
        if (titles.isEmpty()) {
            return 0;
        }

        String sql = """
                INSERT INTO tasks (title) VALUES (?)
                """;

        try (Connection connection = connect()) {
            connection.setAutoCommit(false);
            try {
                int insertedCount = 0;
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    for (String title : titles) {
                        statement.setString(1, title);
                        insertedCount += statement.executeUpdate();
                    }
                }
                connection.commit();
                return insertedCount;
            } catch (SQLException e) {
                try {
                    connection.rollback();
                } catch (SQLException rollbackException) {
                    e.addSuppressed(rollbackException);
                }
                throw new SQLException("Có lỗi xảy ra trong tiến trình thêm công việc.", e);
            }
        }
    }
}
