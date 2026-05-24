# Tech Product Manager

## Yêu cầu hệ thống
- Java 17+ (JRE hoặc JDK)

## Clone dự án
```bash
git clone https://github.com/HUU7911/TechProduct_Manager.git
```
## Cách chạy
```bash
java -jar TechProductManager.jar
```
- Hoặc double-click file `TechProductManager.jar` nếu Java được cài đặt đúng cách.
- Hoặc chạy file `Main.java` trong package.

## Tài khoản mặc định
- **Username:** admin
- **Password:** admin123

## Cấu trúc dữ liệu
Sau khi chạy lần đầu, thư mục `data/` sẽ được tạo:
- `data/products.txt` — Danh sách 1000 sản phẩm (định dạng text)
- `data/admins.bin`   — Danh sách tài khoản admin (mã hóa binary)

## Tính năng
### Sản phẩm
- Thêm / Sửa / Xóa sản phẩm
- Tìm kiếm theo tên, thương hiệu, danh mục
- Sắp xếp theo giá (tăng / giảm dần)
- 1000 sản phẩm mẫu được tạo ngẫu nhiên khi khởi động lần đầu

### Tài khoản
- Đăng ký / Đăng nhập / Đăng xuất
- Thêm / Sửa / Xóa tài khoản admin
- Đổi mật khẩu
- Mật khẩu được mã hóa an toàn (SHA-256 + salt)

### Dashboard
- Banner giới thiệu hệ thống
- Thống kê tổng sản phẩm, tổng giá trị, số tài khoản
- Biểu đồ thanh phân loại sản phẩm theo danh mục

## Cấu trúc package
```
com.techstore
├── entity/       — Product, Admin (POJO)
├── mapper/       — ProductMapper, AdminMapper (interface)
├── service/      — ProductService, AdminService, PasswordUtil (impl)
├── ui/           — Toàn bộ giao diện Swing
Main.java     — Entry point
```
## Lưu ý:
- Dự án được build bằng `JDK21` + `Maven` và được thực thi trên hệ điều hành `Linux` phiên bản `Ubuntu 26.04`
