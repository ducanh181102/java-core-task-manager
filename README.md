# Java Core Task Manager

Ứng dụng console quản lý công việc, viết bằng Java 21. Project dùng Maven để quản lý thư viện SQLite JDBC và lưu công việc vào SQLite.

## Chức năng

- Xem, thêm và xóa công việc.
- Đổi trạng thái hoàn thành.
- Sửa tên và tìm công việc theo ID.
- Đếm số công việc đã hoàn thành.
- Thêm nhiều công việc trong một giao dịch.

Dữ liệu chính được lưu tại `data/tasks.db` tính từ thư mục chạy ứng dụng. Vì vậy, hãy chạy ứng dụng với working directory là thư mục gốc của project để luôn dùng đúng file database.

## Yêu cầu

- JDK 21 trở lên.
- Maven.

## Chạy từ Terminal trên macOS/Linux

Mở Terminal tại thư mục gốc của project rồi chạy:

```sh
mvn compile
mvn dependency:build-classpath -Dmdep.outputFile=target/classpath.txt
java -cp "target/classes:$(cat target/classpath.txt)" com.example.coretasks.Main
```

Trong IntelliJ IDEA, mở project bằng `pom.xml`, chờ Maven tải SQLite JDBC, rồi chạy `com.example.coretasks.Main`. Đặt working directory là thư mục gốc của project.

## Cấu trúc chính

- `Task.java` — dữ liệu của một công việc.
- `TaskRepository.java` — tạo bảng và đọc/ghi SQLite bằng JDBC.
- `TaskService.java` — kiểm tra dữ liệu và điều phối thao tác.
- `Main.java` — menu console, nhập dữ liệu và hiển thị kết quả.
- `FileStorageDemo.java` — ví dụ riêng về lưu dữ liệu dạng văn bản.
- `JdbcConnectionDemo.java` — ví dụ riêng về thao tác SQLite/JDBC; chạy class này sẽ thêm dữ liệu mẫu vào `data/tasks.db`.
- `pom.xml` — cấu hình Java 21 và thư viện SQLite JDBC.

## Đọc mã theo thứ tự

1. `Task.java` — class, object, field, constructor và method.
2. `TaskRepository.java` — SQL, JDBC, `Connection`, `PreparedStatement` và `ResultSet`.
3. `TaskService.java` — kiểm tra dữ liệu và gọi repository.
4. `Main.java` — `Scanner`, điều kiện, `switch`, vòng lặp và gọi service.

Project `servlet-jsp-lib` trong thư mục `Dev` là bước tiếp theo để chuyển từ ứng dụng console sang ứng dụng web với Servlet/JSP và Tomcat.
