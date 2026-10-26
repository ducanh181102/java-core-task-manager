package com.example.coretasks;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

/** Thử kết nối SQLite và tạo bảng tasks nếu bảng chưa tồn tại. */
public class JdbcConnectionDemo {

    public static void main(String[] args) throws IOException, SQLException {
        Path databasePath = Path.of("data", "tasks.db");
        Files.createDirectories(databasePath.getParent());

        String url = "jdbc:sqlite:" + databasePath;
        String createTableSql = """
                CREATE TABLE IF NOT EXISTS tasks (
                    id INTEGER PRIMARY KEY,
                    title TEXT NOT NULL,
                    completed INTEGER NOT NULL DEFAULT 0
                )
                """;

        try (Connection connection = DriverManager.getConnection(url)) {
            System.out.println("Kết nối SQLite thành công!");
            System.out.println("File database: " + databasePath.toAbsolutePath());

            try (Statement statement = connection.createStatement()) {
                statement.executeUpdate(createTableSql);
                System.out.println("Đã tạo hoặc xác nhận bảng tasks.");
            }

            String insertSql = "INSERT INTO tasks (title, completed) VALUES (?, ?)";

            try (PreparedStatement statement = connection.prepareStatement(insertSql)) {
                statement.setString(1, "Học JDBC");
                statement.setInt(2, 1); // 0 nghĩa là chưa hoàn thành
                statement.executeUpdate();
            }

//            String selectSql = "SELECT id, title, completed FROM tasks WHERE completed = ?";
//
//            try (PreparedStatement statement = connection.prepareStatement(selectSql)) {
//                statement.setBoolean(1, true);
//                ResultSet resultSet = statement.executeQuery();
//                while (resultSet.next()) {
//                    int id = resultSet.getInt("id");
//                    String title = resultSet.getString("title");
//                    boolean completed = resultSet.getInt("completed") == 1;
//
//                    System.out.println(id + " | " + title + " | hoàn thành: " + completed);
//                }
//            }
//
//
            String updateSql = "UPDATE tasks SET title = ?, completed = ? WHERE id = ?";

            try (PreparedStatement statement = connection.prepareStatement(updateSql)) {
                statement.setString(1, "Đi học");
                statement.setBoolean(2, false);
                statement.setInt(3, 7);
                statement.executeUpdate();
            }
//
//            // delete
//            String deleteSql = "DELETE FROM tasks WHERE id = ?";
//
//            try (PreparedStatement statement = connection.prepareStatement(deleteSql)) {
//                statement.setInt(1, 5);
//                statement.executeUpdate();
//            }
        }


    }
}
