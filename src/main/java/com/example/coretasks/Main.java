package com.example.coretasks;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/** Chạy ứng dụng quản lý công việc bằng terminal. */
public class Main {
    private static final Scanner INPUT = new Scanner(System.in);
    private static TaskService TASKS;

    public static void main(String[] args) throws IOException, SQLException {
        TASKS = new TaskService();

        System.out.println("SỔ CÔNG VIỆC - SQLITE");

        boolean running = true;
        while (running) {
            printMenu();
            int choice = readNumber("Chọn chức năng: ");

            switch (choice) {
                case 1:
                    showTasks();
                    break;
                case 2:
                    addTask();
                    break;
                case 3:
                    toggleTask();
                    break;
                case 4:
                    deleteTask();
                    break;
                case 5:
                    updateNameTask();
                    break;
                case 6:
                    countCompletedTasks();
                    break;
                case 7:
                    getTaskById();
                    break;
                case 8:
                    addManyTask();
                    break;
                case 0:
                    running = false;
                    System.out.println("Đã thoát chương trình.");
                    break;
                default:
                    System.out.println("Chức năng không hợp lệ. Hãy chọn từ 0 đến 8.");
            }
        }

        INPUT.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1. Xem công việc");
        System.out.println("2. Thêm công việc");
        System.out.println("3. Đổi trạng thái hoàn thành");
        System.out.println("4. Xóa công việc");
        System.out.println("5. Sửa tên công việc");
        System.out.println("6. Đếm số công việc hoàn thành");
        System.out.println("7. Tìm công việc theo ID");
        System.out.println("8. Thêm nhiều công việc");

        System.out.println("0. Thoát");
    }

    private static void showTasks() throws SQLException {
        List<Task> tasks = TASKS.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("Chưa có công việc nào.");
            return;
        }

        System.out.println("\nDanh sách công việc:");
        for (Task task : tasks) {
            String mark = task.isCompleted() ? "x" : " ";
            System.out.println("[" + mark + "] " + task.getId() + ". " + task.getTitle());
        }
    }

    private static void addTask() throws SQLException {
        System.out.print("Nhập tên công việc: ");
        String title = INPUT.nextLine().trim();
        if (title.isEmpty()) {
            System.out.println("Tên công việc không được để trống.");
            return;
        }

        Task task = TASKS.addTask(title);
        System.out.println("Đã thêm công việc số " + task.getId() + ".");
    }

    private static void toggleTask() throws SQLException {
        int id = readNumber("Nhập ID công việc: ");
        if (TASKS.toggleTask(id)) {
            System.out.println("Đã đổi trạng thái công việc.");
        } else {
            System.out.println("Không tìm thấy công việc số " + id + ".");
        }
    }

    private static void deleteTask() throws SQLException {
        int id = readNumber("Nhập ID cần xóa: ");
        if (TASKS.deleteTask(id)) {
            System.out.println("Đã xóa công việc số " + id + ".");
        } else {
            System.out.println("Không tìm thấy công việc số " + id + ".");
        }
    }

    private static int readNumber(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = INPUT.nextLine().trim();
            try {
                return Integer.parseInt(value);
            } catch (NumberFormatException exception) {
                System.out.println("Hãy nhập một số nguyên.");
            }
        }
    }

    private static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = INPUT.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            } else {
                System.out.println("Không để trống.");
            }
        }
    }

    private static void updateNameTask() throws SQLException {
        int id = readNumber("Nhập ID cần update: ");
        String newName = readString("Nhập tên mới: ");
        if (TASKS.updateNameTask(id, newName)) {
            System.out.println("Đã sửa tên công việc số " + id + " thành " + newName);
        } else {
            System.out.println("Không tìm thấy công việc số " + id + ".");
        }
    }

    private static void countCompletedTasks() throws SQLException {
        System.out.println("Số công việc hoàn thành: " + TASKS.countCompletedTasks());
    }

    private static void getTaskById() throws SQLException {
        int id = readNumber("Nhập ID cần tìm: ");
        Task task = TASKS.getTaskById(id);
        if (task != null) {
            System.out.println("\nCông việc:");
            String mark = task.isCompleted() ? "x" : " ";
            System.out.println("[" + mark + "] " + task.getId() + ". " + task.getTitle());
        } else {
            System.out.println("Không tìm thấy công việc số " + id + ".");
        }
    }

    private static void addManyTask() throws SQLException {
        int numTask = readNumber("Nhập số lượng công việc cần thêm: ");
        if (numTask <= 0) {
            System.out.println("Số lượng công việc phải lớn hơn 0.");
            return;
        }

        List<String> titles = new ArrayList<>();
        for (int i = 0; i < numTask; i++) {
            titles.add(readString("Nhập công việc thứ " + (i + 1) + ": "));
        }

        int insertedCount = TASKS.addManyTask(titles);
        System.out.println("Đã thêm " + insertedCount + " công việc.");
    }
}
