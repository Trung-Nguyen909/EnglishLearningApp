IF EXISTS (SELECT * FROM sys.databases WHERE name = 'QuanLyHocTap')
BEGIN
    USE master;
    ALTER DATABASE QuanLyHocTap SET SINGLE_USER WITH ROLLBACK IMMEDIATE;
    DROP DATABASE QuanLyHocTap;
END
GO

CREATE DATABASE QuanLyHocTap;
GO

USE QuanLyHocTap;
GO

-- 1. BẢNG NGƯỜI DÙNG
CREATE TABLE NguoiDung (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    tenDangNhap NVARCHAR(50) NOT NULL UNIQUE,
    matKhau NVARCHAR(100) NOT NULL,
    email NVARCHAR(100) NOT NULL UNIQUE,
    anhDaiDien NVARCHAR(255),
    LastLogin DATETIME,
    Streak INT DEFAULT 0,
    role VARCHAR(30) DEFAULT 'USER'
);

-- 2. BẢNG KHÓA HỌC
CREATE TABLE KhoaHoc (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    tenKhoaHoc NVARCHAR(100) NOT NULL,
    moTa NVARCHAR(500),
    trinhDo NVARCHAR(50),
    ngayTao DATE NOT NULL
);

-- 3. BẢNG BÀI HỌC
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

-- 4. BẢNG CẤP ĐỘ
CREATE TABLE CapDo (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    DoKho NVARCHAR(500) NOT NULL
);

-- 5. BẢNG BÀI TẬP
CREATE TABLE BaiTap (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDBaiHoc INT NOT NULL,
    loaiBaiTap NVARCHAR(50), -- Listening, Speaking, Reading...
    trangThai NVARCHAR(50),
    FOREIGN KEY (IDBaiHoc) REFERENCES BaiHoc(ID)
);

-- 6. BẢNG CÂU HỎI (QUAN TRỌNG: Đã update theo JSON)
CREATE TABLE CauHoi (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDCapDo INT NOT NULL,
    IDBaiTap INT NULL, -- Có thể Null nếu câu hỏi này chưa gán vào bài tập nào (nếu dùng cho Test)
    NoiDungCauHoi NVARCHAR(MAX) NOT NULL,
    LoaiCauHoi NVARCHAR(50) NOT NULL, -- 'ABCD', 'Listening', 'FillBlank'...
    DuLieuDapAn NVARCHAR(MAX) NOT NULL, -- Lưu JSON: {"A":"...", "B":"...", "Correct":"A"}
    GiaiThich NVARCHAR(MAX),
    Audio_URL NVARCHAR(255),
    FOREIGN KEY (IDCapDo) REFERENCES CapDo(ID),
    FOREIGN KEY (IDBaiTap) REFERENCES BaiTap(ID)
);

-- 7. BẢNG TEST
CREATE TABLE Test (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDKhoaHoc INT NOT NULL,
    TenBaiTest NVARCHAR(100) NOT NULL,
    SoCauHoi INT NOT NULL,
    TgianLam TIME,
    FOREIGN KEY (IDKhoaHoc) REFERENCES KhoaHoc(ID)
);

-- 8. BẢNG TEST_CAUHOI (Mới: Liên kết N-N giữa Test và Câu Hỏi)
CREATE TABLE Test_CauHoi (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDTest INT NOT NULL,
    IDCauHoi INT NOT NULL,
    FOREIGN KEY (IDTest) REFERENCES Test(ID),
    FOREIGN KEY (IDCauHoi) REFERENCES CauHoi(ID)
);

-- 9. BẢNG LỊCH SỬ BÀI LÀM (Thay thế cho KetQua cũ)
CREATE TABLE LichSuBaiLam (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDNguoiDung INT NOT NULL,
    IDTest INT NULL,   -- Null nếu làm bài tập
    IDBaiTap INT NULL, -- Null nếu làm bài test
    LoaiBai NVARCHAR(20), -- 'TEST' hoặc 'BAITAP'
    DiemSo DECIMAL(10,2),
    TrangThai NVARCHAR(50), -- 'Hoàn thành', 'Đang làm'
    TgianNopBai DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (IDNguoiDung) REFERENCES NguoiDung(ID),
    FOREIGN KEY (IDTest) REFERENCES Test(ID),
    FOREIGN KEY (IDBaiTap) REFERENCES BaiTap(ID)
);

-- 10. BẢNG CHI TIẾT BÀI LÀM (Mới: Lưu từng câu trả lời)
CREATE TABLE ChiTietBaiLam (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDLichSuBaiLam INT NOT NULL,
    IDCauHoi INT NOT NULL,
    UserAns NVARCHAR(MAX), -- Câu trả lời của user (A, B, hoặc text)
    IsCorrect BIT, -- 1: Đúng, 0: Sai
    FOREIGN KEY (IDLichSuBaiLam) REFERENCES LichSuBaiLam(ID),
    FOREIGN KEY (IDCauHoi) REFERENCES CauHoi(ID)
);

-- CÁC BẢNG PHỤ KHÁC (Giữ nguyên logic cũ)
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

CREATE TABLE NguPhap (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDBaiHoc INT NOT NULL,
    tenNguPhap NVARCHAR(100) NOT NULL,
    giaiThich NVARCHAR(MAX),
    viDu NVARCHAR(500),
    FOREIGN KEY (IDBaiHoc) REFERENCES BaiHoc(ID)
);

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

CREATE TABLE NguoiDung_BinhLuan (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDNguoiDung INT NOT NULL,
    IDBinhLuan INT NOT NULL,
    Type NVARCHAR(20),
    NgayTao DATE DEFAULT GETDATE(),
    FOREIGN KEY (IDNguoiDung) REFERENCES NguoiDung(ID),
    FOREIGN KEY (IDBinhLuan) REFERENCES BinhLuan(ID)
);

CREATE TABLE PhanHoi (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDNguoiDung INT NOT NULL,
    Title NVARCHAR(200) NOT NULL,
    NoiDung NVARCHAR(1000),
    NgayTao DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (IDNguoiDung) REFERENCES NguoiDung(ID)
);

CREATE TABLE KyNang (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    TenKyNang NVARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE DanhGiaKyNang (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDKyNang INT NOT NULL,
    IDNguoiDung INT NOT NULL,
    diem DECIMAL(10,2),
    NgayCapNhat DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (IDKyNang) REFERENCES KyNang(ID),
    FOREIGN KEY (IDNguoiDung) REFERENCES NguoiDung(ID)
);

CREATE TABLE ThanhTich (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    tenThanhTich NVARCHAR(100) NOT NULL,
    moTa NVARCHAR(500),
    bieuTuong NVARCHAR(255)
);

CREATE TABLE NguoiDung_ThanhTich (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDNguoiDung INT NOT NULL,
    IDThanhTich INT NOT NULL,
    NgayDat DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (IDNguoiDung) REFERENCES NguoiDung(ID),
    FOREIGN KEY (IDThanhTich) REFERENCES ThanhTich(ID)
);

CREATE TABLE TuVungYeuThich (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDNguoiDung INT NOT NULL,
    IDTuVung INT NOT NULL,
    FOREIGN KEY (IDNguoiDung) REFERENCES NguoiDung(ID),
    FOREIGN KEY (IDTuVung) REFERENCES TuVung(ID)
);

-- CONSTRAINTS
ALTER TABLE TienTrinh ADD CONSTRAINT CK_DaLamDuocBN_TienTrinh CHECK (DaLamDuocBaoNhieu >= 0 AND DaLamDuocBaoNhieu <= 100);
ALTER TABLE TienTrinh ADD CONSTRAINT CK_TrangThai_TienTrinh CHECK (TrangThai IN (N'Đã hoàn thành', N'Đang Hoàn thành', N'Chưa làm'));
ALTER TABLE BaiHoc ADD CONSTRAINT CK_TrangThai_BaiHoc CHECK (TrangThai IN (N'Đã hoàn thành', N'Đang Hoàn thành', N'Chưa làm'));
ALTER TABLE BaiTap ADD CONSTRAINT CK_TrangThai_BaiTap CHECK (TrangThai IN (N'Đã hoàn thành', N'Đang Hoàn thành', N'Chưa làm'));
ALTER TABLE NguoiDung_BinhLuan ADD CONSTRAINT CK_Type_NguoiDung_BinhLuan CHECK (Type IN ('Like', 'Dislike'));
ALTER TABLE DanhGiaKyNang ADD CONSTRAINT CK_Diem_DanhGiaKyNang CHECK (Diem >= 0 AND Diem <= 100);

-- =============================================
-- INSERT DỮ LIỆU MẪU (ĐÃ CẬP NHẬT JSON)
-- =============================================

SET DATEFORMAT YMD;

-- 1. Insert NguoiDung
INSERT INTO NguoiDung (tenDangNhap, matKhau, email, anhDaiDien, LastLogin, Streak, role) VALUES
('user1', '$2a$10$PLLK1jr5F0hDTVVoKFSGj.T7nhAKhH/Qo563lNtuaaEMndxZwy.Ky', 'ttuankhanh4@gmail.com', 'avatar1.jpg', '2024-01-15 08:30:00', 5, 'ADMIN'),
('user2', '$2a$10$OgyHCP.YQ4xoGGXFpD6z3O01TwJNIhUG8t60Tu82HnwFkThK7BrUy', 'chientranminh355@gmail.com', 'avatar2.jpg', '2024-01-14 10:15:00', 3, 'USER'),
('user3', '$2a$10$l7GqvKZDYVsReEiGCc0LpeGoGgy0beEJ2yrAmSvzOM2BcOWUfStDC', 'user3@gmail.com', 'avatar3.jpg', '2024-01-13 14:20:00', 7, 'USER');

-- 2. Insert KhoaHoc
INSERT INTO KhoaHoc (tenKhoaHoc, moTa, trinhDo, ngayTao) VALUES
(N'Tiếng Anh Cơ Bản', N'Khóa học tiếng Anh dành cho người mới bắt đầu', N'Beginner', '2024-01-01'),
(N'Tiếng Anh Trung Cấp', N'Khóa học tiếng Anh trình độ trung cấp', N'Intermediate', '2024-01-05');

-- 3. Insert BaiHoc
INSERT INTO BaiHoc (IDKhoaHoc, tenBaiHoc, moTa, noiDung, thuTuBaiHoc, trangThai) VALUES
(1, N'Chào hỏi cơ bản', N'Học cách chào hỏi', N'Hello, Hi...', 1, N'Đã hoàn thành'),
(1, N'Giới thiệu bản thân', N'Học cách giới thiệu', N'My name is...', 2, N'Đã hoàn thành');

-- 4. Insert CapDo
INSERT INTO CapDo (DoKho) VALUES (N'Dễ'), (N'Trung bình'), (N'Khó');

-- 5. Insert BaiTap
INSERT INTO BaiTap (IDBaiHoc, loaiBaiTap, trangThai) VALUES
(1, N'Trắc nghiệm', N'Đang Hoàn thành'),
(1, N'Nghe hiểu', N'Đang Hoàn thành');

-- 6. Insert CauHoi (SỬ DỤNG JSON)
-- Lưu ý: IDBaiTap = 1 (Trắc nghiệm), IDBaiTap = 2 (Nghe hiểu)
INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich) VALUES
-- Câu 1 (Thuộc bài tập 1)
(1, 1, N'What color is a banana?', 'ABCD', 
N'{"A": "Red", "B": "Blue", "C": "Yellow", "D": "Black", "Correct": "C"}', 
N'Chuối thường có màu vàng.'),

-- Câu 2 (Thuộc bài tập 1)
(1, 1, N'She _____ a teacher.', 'ABCD', 
N'{"A": "is", "B": "are", "C": "am", "D": "be", "Correct": "A"}', 
N'She đi với is.'),

-- Câu 3 (Thuộc bài tập 2 - Listening)
(2, 2, N'Nghe và điền từ còn thiếu: I _____ to music.', 'FillBlank', 
N'{"Correct": "listen", "Audio": "listen_music.mp3"}', 
N'Từ cần điền là listen.'),

-- Câu 4 (Dùng cho bài Test, chưa gán vào bài tập nào cụ thể hoặc tái sử dụng)
(2, 1, N'They _____ lunch right now.', 'ABCD', 
N'{"A": "are having", "B": "have", "C": "had", "D": "will have", "Correct": "A"}', 
N'Right now là dấu hiệu thì hiện tại tiếp diễn.');

-- 7. Insert Test
INSERT INTO Test (IDKhoaHoc, TenBaiTest, SoCauHoi, TgianLam) VALUES
(1, N'Kiểm tra bài 1', 10, '00:15:00');

-- 8. Insert Test_CauHoi (Liên kết câu hỏi vào bài Test)
INSERT INTO Test_CauHoi (IDTest, IDCauHoi) VALUES
(1, 1), -- Bài Test 1 có câu hỏi ID 1
(1, 2), -- Bài Test 1 có câu hỏi ID 2
(1, 4); -- Bài Test 1 có câu hỏi ID 4

-- 9. Insert LichSuBaiLam (Thay cho KetQua cũ)
-- User 1 làm bài Test 1 được 8.5 điểm
INSERT INTO LichSuBaiLam (IDNguoiDung, IDTest, IDBaiTap, LoaiBai, DiemSo, TrangThai, TgianNopBai) VALUES
(1, 1, NULL, 'TEST', 8.5, N'Hoàn thành', '2024-01-15 08:45:00'),
-- User 1 làm Bài tập 1 được 10 điểm
(1, NULL, 1, 'BAITAP', 10.0, N'Hoàn thành', '2024-01-15 09:00:00');

-- 10. Insert ChiTietBaiLam (Chi tiết đúng sai)
-- Chi tiết cho Lịch sử bài làm ID = 1 (Bài Test của User 1)
INSERT INTO ChiTietBaiLam (IDLichSuBaiLam, IDCauHoi, UserAns, IsCorrect) VALUES
(1, 1, 'C', 1), -- Câu 1 chọn C (Đúng)
(1, 2, 'B', 0), -- Câu 2 chọn B (Sai - Đáp án đúng là A)
(1, 4, 'A', 1); -- Câu 4 chọn A (Đúng)

-- CÁC DỮ LIỆU KHÁC (Phản hồi, Bình luận...)
INSERT INTO TuVung (IDBaiHoc, tuTiengAnh, nghiaTiengViet, phienAm, viDu, amThanhPhienAm) VALUES
(1, 'Hello', N'Xin chào', '/həˈloʊ/', 'Hello, how are you?', 'hello.mp3');

INSERT INTO NguPhap (IDBaiHoc, tenNguPhap, giaiThich, viDu) VALUES
(1, N'Động từ TO BE', N'Am, is, are...', 'I am a student.');

INSERT INTO BinhLuan (IDKhoaHoc, IDNguoiDung, NgayLam, NoiDung) VALUES
(1, 1, '2024-01-15', N'Khóa học rất hay!');

INSERT INTO KyNang (TenKyNang) VALUES (N'Nghe'), (N'Nói'), (N'Đọc'), (N'Viết');

INSERT INTO DanhGiaKyNang (IDKyNang, IDNguoiDung, diem) VALUES (1, 1, 7.5);

INSERT INTO ThanhTich (tenThanhTich, moTa, bieuTuong) VALUES (N'Người mới bắt đầu', N'Hoàn thành bài đầu', 'badge.png');

INSERT INTO NguoiDung_ThanhTich (IDNguoiDung, IDThanhTich) VALUES (1, 1);

INSERT INTO PhanHoi (IDNguoiDung, Title, NoiDung) VALUES (1, N'Góp ý', N'Thêm bài tập nhé.');

PRINT N'Cập nhật Database thành công theo ERD mới!';

-- TEST QUERY: Lấy lịch sử làm bài và chi tiết câu hỏi
SELECT 
    ls.ID as LichSuID,
    u.tenDangNhap,
    CASE WHEN ls.LoaiBai = 'TEST' THEN t.TenBaiTest ELSE bt.loaiBaiTap END as TenBai,
    ct.UserAns as NguoiDungChon,
    ch.NoiDungCauHoi,
    ch.DuLieuDapAn
FROM LichSuBaiLam ls
JOIN NguoiDung u ON ls.IDNguoiDung = u.ID
LEFT JOIN Test t ON ls.IDTest = t.ID
LEFT JOIN BaiTap bt ON ls.IDBaiTap = bt.ID
JOIN ChiTietBaiLam ct ON ct.IDLichSuBaiLam = ls.ID
JOIN CauHoi ch ON ct.IDCauHoi = ch.ID;
select * from CauHoi