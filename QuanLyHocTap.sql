IF EXISTS (SELECT * FROM sys.databases WHERE name = 'QuanLyHocTap')
BEGIN
    USE master; -- Chuyển sang cơ sở dữ liệu master để có thể xóa được cơ sở dữ liệu khác
    ALTER DATABASE QuanLyHocTap SET SINGLE_USER WITH ROLLBACK IMMEDIATE; -- Ngắt mọi kết nối
    DROP DATABASE QuanLyHocTap; -- Xóa cơ sở dữ liệu
END
GO

-- Tạo cơ sở dữ liệu
CREATE DATABASE QuanLyHocTap;
GO

USE QuanLyHocTap;
GO

-- Tạo bảng Người Dùng
CREATE TABLE NguoiDung (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    tenDangNhap NVARCHAR(50) NOT NULL UNIQUE,
    matKhau NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    anhDaiDien NVARCHAR(255),
    LastLogin DATETIME,
    Streak INT DEFAULT 0,
	role varchar(30) default 'USER'
);

-- Tạo bảng Khóa Học
CREATE TABLE KhoaHoc (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    tenKhoaHoc NVARCHAR(100) NOT NULL,
    moTa NVARCHAR(500),
    trinhDo NVARCHAR(50),
    ngayTao DATE NOT NULL
);

-- Tạo bảng Tiến Trình
CREATE TABLE TienTrinh (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDNguoiDung INT NOT NULL,
    IDKhoaHoc INT NOT NULL,
    ngayLam DATE NOT NULL,
    DaLamDuocBaoNhieu Decimal(4,2) DEFAULT 0,
    TgianLam TIME,
    TrangThai NVARCHAR(50),
    SoLanLam INT DEFAULT 0,
    FOREIGN KEY (IDNguoiDung) REFERENCES NguoiDung(ID),
    FOREIGN KEY (IDKhoaHoc) REFERENCES KhoaHoc(ID)
);

-- Tạo bảng Bài Học
CREATE TABLE BaiHoc (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDKhoaHoc INT NOT NULL,
    tenBaiHoc NVARCHAR(100) NOT NULL,
    moTa NVARCHAR(500),
    noiDung NVARCHAR(MAX),
    thuTuBaiHoc INT,
    trangThai NVARCHAR(50),
    FOREIGN KEY (IDKhoaHoc) REFERENCES KhoaHoc(ID)
);

-- Tạo bảng Câu Hỏi
CREATE TABLE CauHoi (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    CauHoi NVARCHAR(500) NOT NULL,
    A NVARCHAR(200),
    B NVARCHAR(200),
    C NVARCHAR(200),
    D NVARCHAR(200),
    CauTraLoi NVARCHAR(5) -- 'A', 'B', 'C', 'D'
);

-- Tạo bảng Bài Tập
CREATE TABLE BaiTap (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDBaiHoc INT NOT NULL,
    IDCauHoi INT NOT NULL,
    loaiBaiTap NVARCHAR(50),
    trangThai NVARCHAR(50),
    FOREIGN KEY (IDBaiHoc) REFERENCES BaiHoc(ID),
    FOREIGN KEY (IDCauHoi) REFERENCES CauHoi(ID)
);

-- Tạo bảng Test
CREATE TABLE Test (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDKhoaHoc INT NOT NULL,
    TenBaiTest NVARCHAR(100) NOT NULL,
    SoCauHoi INT NOT NULL,
    TgianLam TIME,
    FOREIGN KEY (IDKhoaHoc) REFERENCES KhoaHoc(ID)
);

-- Tạo bảng Kết Quả
CREATE TABLE KetQua (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDTest INT NOT NULL,
    IDNguoiDung INT NOT NULL,
    diem DECIMAL(10,2),
    TgianLam TIME,
    NgayLam DATE,
    FOREIGN KEY (IDTest) REFERENCES Test(ID),
    FOREIGN KEY (IDNguoiDung) REFERENCES NguoiDung(ID)
);

-- Tạo bảng Từ Vựng
CREATE TABLE TuVung (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDBaiHoc INT NOT NULL,
    tuTiengAnh NVARCHAR(100) NOT NULL,
    nghiaTiengViet NVARCHAR(100) NOT NULL,
    phienAm NVARCHAR(100),
    viDu NVARCHAR(500),
    amThanhPhienAm NVARCHAR(255),
    FOREIGN KEY (IDBaiHoc) REFERENCES BaiHoc(ID)
);

-- Tạo bảng Ngữ Pháp
CREATE TABLE NguPhap (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDBaiHoc INT NOT NULL,
    tenNguPhap NVARCHAR(100) NOT NULL,
    giaiThich NVARCHAR(1000),
    viDu NVARCHAR(500),
    FOREIGN KEY (IDBaiHoc) REFERENCES BaiHoc(ID)
);

-- Tạo bảng Từ Vựng Yêu Thích
CREATE TABLE TuVungYeuThich (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDNguoiDung INT NOT NULL,
    IDTuVung INT NOT NULL,
    FOREIGN KEY (IDNguoiDung) REFERENCES NguoiDung(ID),
    FOREIGN KEY (IDTuVung) REFERENCES TuVung(ID)
);

-- Tạo bảng Bình Luận
CREATE TABLE BinhLuan (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDKhoaHoc INT NOT NULL,
    IDNguoiDung INT NOT NULL,
    NgayLam DATE NOT NULL,
    NoiDung NVARCHAR(1000),
    LikeCount INT DEFAULT 0,
    DislikeCount INT DEFAULT 0,
    FOREIGN KEY (IDNguoiDung) REFERENCES NguoiDung(ID),
    FOREIGN KEY (IDKhoaHoc) REFERENCES KhoaHoc(ID)
);

-- Tạo bảng Người Dùng - Bình Luận (Junction table)
CREATE TABLE NguoiDung_BinhLuan (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDNguoiDung INT NOT NULL,
    IDBinhLuan INT NOT NULL,
    Type NVARCHAR(20), -- 'Like' hoặc 'Dislike'
    NgayTao DATE DEFAULT GETDATE(),
    FOREIGN KEY (IDNguoiDung) REFERENCES NguoiDung(ID),
    FOREIGN KEY (IDBinhLuan) REFERENCES BinhLuan(ID)
);

-- Tạo bảng Phản Hồi
CREATE TABLE PhanHoi (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDNguoiDung INT NOT NULL,
    Title NVARCHAR(200) NOT NULL,
    NoiDung NVARCHAR(1000),
    NgayTao DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (IDNguoiDung) REFERENCES NguoiDung(ID)
);

-- Tạo bảng Kỹ Năng
CREATE TABLE KyNang (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    TenKyNang NVARCHAR(100) NOT NULL UNIQUE
);

-- Tạo bảng Danh Giá Kỹ Năng
CREATE TABLE DanhGiaKyNang (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDKyNang INT NOT NULL,
    IDNguoiDung INT NOT NULL,
    diem DECIMAL(10,2),
    NgayCapNhat DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (IDKyNang) REFERENCES KyNang(ID),
    FOREIGN KEY (IDNguoiDung) REFERENCES NguoiDung(ID)
);

-- Tạo bảng Thành Tích
CREATE TABLE ThanhTich (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    tenThanhTich NVARCHAR(100) NOT NULL,
    moTa NVARCHAR(500),
    bieuTuong NVARCHAR(255)
);

-- Tạo bảng Người Dùng - Thành Tích (Junction table)
CREATE TABLE NguoiDung_ThanhTich (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDNguoiDung INT NOT NULL,
    IDThanhTich INT NOT NULL,
    NgayDat DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (IDNguoiDung) REFERENCES NguoiDung(ID),
    FOREIGN KEY (IDThanhTich) REFERENCES ThanhTich(ID)
);

alter table TienTrinh
	add constraint CK_DaLamDuocBN_TienTrinh
		check (DaLamDuocBaoNhieu >= 0 and DaLamDuocBaoNhieu <= 100),
		constraint CK_TrangThai_TienTrinh
		check (TrangThai in (N'Đã hoàn thành', N'Đang Hoàn thành', N'Chưa làm'))
alter table BaiHoc
	add constraint CK_TrangThai_BaiHoc
		check (TrangThai in (N'Đã hoàn thành', N'Đang Hoàn thành', N'Chưa làm'))
alter table BaiTap
	add constraint CK_trangThai_BaiTap
		check (TrangThai in (N'Đã hoàn thành', N'Đang Hoàn thành', N'Chưa làm'))
alter table KetQua
	add constraint CK_Diem_KetQua
		check (Diem >= 0 and Diem <= 100 )
alter table NguoiDung_BinhLuan
	add constraint CK_Type_NguoiDung_BinhLuan
		check (Type in ('Like', 'Dislike'))
alter table DanhGiaKyNang 
	add constraint CK_diem_DanhGiaKyNang 
		check (Diem >= 0 and Diem <= 100 )

-- Insert NguoiDung 
--Admin pass: 1, user2 pass: 2, user3 pass: 3
set dateformat ymd
INSERT INTO NguoiDung (tenDangNhap, matKhau, email, anhDaiDien, LastLogin, Streak, role) VALUES
('user1', '$2a$10$PLLK1jr5F0hDTVVoKFSGj.T7nhAKhH/Qo563lNtuaaEMndxZwy.Ky', 'ttuankhanh4@email.com', 'avatar1.jpg', '2024-01-15 08:30:00', 5, 'ADMIN'),
('user2', '$2a$10$OgyHCP.YQ4xoGGXFpD6z3O01TwJNIhUG8t60Tu82HnwFkThK7BrUy', 'user2@email.com', 'avatar2.jpg', '2024-01-14 10:15:00', 3, 'USER'),
('user3', '$2a$10$l7GqvKZDYVsReEiGCc0LpeGoGgy0beEJ2yrAmSvzOM2BcOWUfStDC', 'user3@email.com', 'avatar3.jpg', '2024-01-13 14:20:00', 7, 'USER');

-- Insert KhoaHoc
set dateformat ymd
INSERT INTO KhoaHoc (tenKhoaHoc, moTa, trinhDo, ngayTao) VALUES
(N'Tiếng Anh Cơ Bản', N'Khóa học tiếng Anh dành cho người mới bắt đầu', N'Beginner', '2024-01-01'),
(N'Tiếng Anh Trung Cấp', N'Khóa học tiếng Anh trình độ trung cấp', N'Intermediate', '2024-01-05'),
(N'Tiếng Anh Nâng Cao', N'Khóa học tiếng Anh trình độ cao', N'Advanced', '2024-01-10');

-- Insert TienTrinh
set dateformat ymd
INSERT INTO TienTrinh (IDNguoiDung, IDKhoaHoc, ngayLam, DaLamDuocBaoNhieu, TgianLam, TrangThai, SoLanLam) VALUES
(1, 1, '2024-01-15', 75, '01:30:00', N'Đang Hoàn thành', 3),
(2, 2, '2024-01-14', 50, '02:15:00', N'Đang Hoàn thành', 5),
(3, 1, '2024-01-13', 90, '01:45:00', N'Đã hoàn thành', 8);

-- Insert BaiHoc
INSERT INTO BaiHoc (IDKhoaHoc, tenBaiHoc, moTa, noiDung, thuTuBaiHoc, trangThai) VALUES
(1, N'Chào hỏi cơ bản', N'Học cách chào hỏi trong tiếng Anh', N'Hello, Hi, Good morning...', 1, N'Đã hoàn thành'),
(1, N'Giới thiệu bản thân', N'Học cách giới thiệu về bản thân', N'My name is..., I am from...', 2, N'Đã hoàn thành'),
(2, N'Thì hiện tại đơn', N'Học về thì hiện tại đơn', N'Present simple tense usage', 1, N'Đã hoàn thành');

-- Insert CauHoi
INSERT INTO CauHoi (CauHoi, A, B, C, D, CauTraLoi) VALUES
(N'What is your name?', N'My name is John', N'I am fine', N'Thank you', N'Goodbye', 'A'),
(N'How are you?', N'My name is...', N'I am fine, thank you', N'See you later', N'Nice to meet you', 'B'),
(N'Where are you from?', N'I am 20 years old', N'I am a student', N'I am from Vietnam', N'I like English', 'C');

-- Insert BaiTap
INSERT INTO BaiTap (IDBaiHoc, IDCauHoi, loaiBaiTap, trangThai) VALUES
(1, 1, N'Trắc nghiệm', N'Đang Hoàn thành'),
(1, 2, N'Trắc nghiệm', N'Đang Hoàn thành'),
(2, 3, N'Trắc nghiệm', N'Đã Hoàn thành');

-- Insert Test
INSERT INTO Test (IDKhoaHoc, TenBaiTest, SoCauHoi, TgianLam) VALUES
(1, N'Kiểm tra bài 1', 10, '00:15:00'),
(1, N'Kiểm tra tổng hợp', 20, '00:30:00'),
(2, N'Kiểm tra giữa khóa', 15, '00:25:00');

-- Insert KetQua
set dateformat ymd
INSERT INTO KetQua (IDTest, IDNguoiDung, Diem, TgianLam, NgayLam) VALUES
(1, 1, 8.5, '00:12:30', '2024-01-15'),
(2, 1, 7.0, '00:25:45', '2024-01-16'),
(1, 2, 9.0, '00:10:15', '2024-01-14');

-- Insert TuVung
INSERT INTO TuVung (IDBaiHoc, tuTiengAnh, nghiaTiengViet, phienAm, viDu, amThanhPhienAm) VALUES
(1, 'Hello', N'Xin chào', '/həˈloʊ/', 'Hello, how are you?', 'hello.mp3'),
(1, 'Goodbye', N'Tạm biệt', '/ɡʊdˈbaɪ/', 'Goodbye, see you tomorrow!', 'goodbye.mp3'),
(2, 'Name', N'Tên', '/neɪm/', 'What is your name?', 'name.mp3');

-- Insert NguPhap
INSERT INTO NguPhap (IDBaiHoc, tenNguPhap, giaiThich, viDu) VALUES
(3, N'Thì hiện tại đơn', N'Diễn tả hành động thường xuyên xảy ra', 'I go to school every day.'),
(3, N'Động từ TO BE', N'Am, is, are được sử dụng với chủ ngữ tương ứng', 'I am a student. He is a teacher.');

-- Insert BinhLuan
set dateformat ymd
INSERT INTO BinhLuan (IDKhoaHoc, IDNguoiDung, NgayLam, NoiDung, LikeCount, DislikeCount) VALUES
(1, 1, '2024-01-15', N'Khóa học rất hay và dễ hiểu!', 5, 0),
(1, 2, '2024-01-14', N'Nội dung phong phú, giải thích chi tiết.', 3, 0),
(2, 3, '2024-01-13', N'Khóa học chất lượng cao.', 7, 1);

-- Insert TuVungYeuThich
INSERT INTO TuVungYeuThich (IDNguoiDung, IDTuVung) VALUES
(1, 1),
(1, 2),
(2, 1),
(3, 3);

-- Insert NguoiDung_BinhLuan
set dateformat ymd
INSERT INTO NguoiDung_BinhLuan (IDNguoiDung, IDBinhLuan, Type, NgayTao) VALUES
(2, 1, 'Like', '2024-01-15'),
(3, 1, 'Like', '2024-01-15'),
(1, 2, 'Like', '2024-01-14'),
(3, 3, 'Dislike', '2024-01-13');

-- Insert KyNang
INSERT INTO KyNang (TenKyNang) VALUES
(N'Nghe'),
(N'Nói'),
(N'Đọc'),
(N'Viết'),
(N'Ngữ pháp'),
(N'Từ vựng');

-- Insert DanhGiaKyNang
set dateformat ymd
INSERT INTO DanhGiaKyNang (IDKyNang, IDNguoiDung, diem, NgayCapNhat) VALUES
(1, 1, 7.5, '2024-01-15 10:30:00'),
(2, 1, 6.0, '2024-01-15 10:30:00'),
(3, 1, 8.0, '2024-01-15 10:30:00'),
(4, 1, 5.5, '2024-01-15 10:30:00'),
(1, 2, 8.5, '2024-01-14 09:15:00'),
(2, 2, 7.0, '2024-01-14 09:15:00'),
(3, 2, 9.0, '2024-01-14 09:15:00'),
(1, 3, 9.0, '2024-01-13 14:20:00'),
(2, 3, 8.5, '2024-01-13 14:20:00'),
(3, 3, 9.5, '2024-01-13 14:20:00'),
(4, 3, 8.0, '2024-01-13 14:20:00');

-- Insert ThanhTich
INSERT INTO ThanhTich (tenThanhTich, moTa, bieuTuong) VALUES
(N'Người mới bắt đầu', N'Hoàn thành bài học đầu tiên', 'beginner_badge.png'),
(N'Học sinh chăm chỉ', N'Học liên tục 7 ngày', 'diligent_badge.png'),
(N'Thạc sĩ từ vựng', N'Học thuộc 100 từ vựng', 'vocabulary_master.png'),
(N'Chiến binh ngữ pháp', N'Hoàn thành tất cả bài tập ngữ pháp cơ bản', 'grammar_warrior.png'),
(N'Người hoàn hảo', N'Đạt điểm 10 trong bài kiểm tra', 'perfectionist.png');

-- Insert NguoiDung_ThanhTich
set dateformat ymd
INSERT INTO NguoiDung_ThanhTich (IDNguoiDung, IDThanhTich, NgayDat) VALUES
(1, 1, '2024-01-15 08:30:00'),
(1, 2, '2024-01-15 08:30:00'),
(2, 1, '2024-01-14 10:15:00'),
(2, 3, '2024-01-14 10:15:00'),
(3, 1, '2024-01-13 14:20:00'),
(3, 2, '2024-01-13 14:20:00'),
(3, 3, '2024-01-13 14:20:00'),
(3, 4, '2024-01-13 14:20:00'),
(3, 5, '2024-01-13 14:20:00');

-- Insert PhanHoi
set dateformat ymd
INSERT INTO PhanHoi (IDNguoiDung, Title, NoiDung, NgayTao) VALUES
(1, N'Góp ý về khóa học', N'Tôi thấy khóa học rất hay, nhưng nên thêm nhiều bài tập thực hành hơn.', '2024-01-15 11:30:00'),
(2, N'Báo lỗi hệ thống', N'Tôi gặp lỗi khi làm bài test, không thể submit được.', '2024-01-14 15:45:00'),
(3, N'Đề xuất tính năng mới', N'Có thể thêm chức năng chat với giáo viên được không?', '2024-01-13 09:20:00'),
(1, N'Cảm ơn', N'Cảm ơn team đã tạo ra ứng dụng học tập tuyệt vời này!', '2024-01-16 08:15:00');

PRINT N'Tạo database và insert dữ liệu mẫu thành công!';