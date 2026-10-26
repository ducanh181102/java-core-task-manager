package com.example.coretasks;

/** Một công việc. Đây là class do mình tự tạo, chưa dùng framework. */
public class Task {
    private final int id;
    /*#1 Thêm chức năng sửa tên công việc.*/
    /*start mod*/
    /*private final String title;*/
    private String title;
    /*end mod*/
    private boolean completed;

    public Task(int id, String title, boolean completed) {
        this.id = id;
        this.title = title;
        this.completed = completed;
    }

    public Task(int id, String title) {
        this.id = id;
        this.title = title;
        this.completed = false;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void toggleCompleted() {
        completed = !completed;
    }
}
