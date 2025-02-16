use student_system_database;

CREATE TABLE courses (
    course_id VARCHAR(15) PRIMARY KEY,
    course_name VARCHAR(100) NOT NULL,
    credits INT NOT NULL,
    lecturer VARCHAR(100) NOT NULL,
    semester ENUM('Học kỳ 1', 'Học kỳ 2', 'Học kỳ 3') NOT NULL,
    status ENUM('Đang mở', 'Đóng') NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

INSERT INTO courses (course_id, course_name, credits, lecturer, semester, status) VALUES
('CS101', 'Lập trình Java', 3, 'ThS. Nguyễn Văn A', 'Học kỳ 1', 'Đang mở'),
('CS102', 'Cấu trúc dữ liệu', 4, 'ThS. Trần Thị B', 'Học kỳ 2', 'Đang mở'),
('CS103', 'Cơ sở dữ liệu', 3, 'TS. Lê Văn C', 'Học kỳ 3', 'Đóng'),
('CS104', 'Lập trình C++', 3, 'ThS. Phạm Văn D', 'Học kỳ 1', 'Đang mở'),
('CS105', 'Trí tuệ nhân tạo', 4, 'PGS. TS. Nguyễn Văn E', 'Học kỳ 2', 'Đóng'),
('CS106', 'Mạng máy tính', 3, 'ThS. Lê Thị F', 'Học kỳ 3', 'Đang mở'),
('CS107', 'Hệ điều hành', 3, 'TS. Trần Văn G', 'Học kỳ 1', 'Đang mở'),
('CS108', 'Phát triển ứng dụng Web', 4, 'ThS. Đỗ Thị H', 'Học kỳ 2', 'Đang mở'),
('CS109', 'Kiến trúc máy tính', 3, 'TS. Bùi Văn I', 'Học kỳ 3', 'Đóng'),
('CS110', 'Kỹ thuật lập trình', 3, 'ThS. Vũ Thị K', 'Học kỳ 1', 'Đang mở');
