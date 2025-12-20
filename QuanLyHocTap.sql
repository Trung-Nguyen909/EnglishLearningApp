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
    role VARCHAR(30) DEFAULT 'USER',
    TongThoiGianHoatDong INT DEFAULT 0
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
	TenBaiTap nvarchar(max),
    loaiBaiTap NVARCHAR(50), -- Listening, Speaking, Reading...
    trangThai NVARCHAR(50),
	capdo nvarchar(100),
	thoigian Time,
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

CREATE TABLE TienTrinhKhoaHoc (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IdKhoaHoc INT NOT NULL,
    IdUser INT NOT NULL,       -- Trong ảnh là IdUser, tham chiếu tới bảng NguoiDung
    trangthai NVARCHAR(50),    -- Ví dụ: 'DangHoc', 'HoanThanh'
    PhanTram DECIMAL(5, 2) DEFAULT 0, -- Lưu phần trăm (ví dụ: 50.50)
    LanLamGanNhat DATETIME DEFAULT GETDATE(),
    Tgianhoc INT,              -- Tổng thời gian học (tính bằng phút hoặc giây)
    NgayBatDau DATETIME DEFAULT GETDATE(),
    NgayHoanThanh DATETIME,

    -- Khóa ngoại
    CONSTRAINT FK_TTKH_KhoaHoc FOREIGN KEY (IdKhoaHoc) REFERENCES KhoaHoc(ID),
    CONSTRAINT FK_TTKH_NguoiDung FOREIGN KEY (IdUser) REFERENCES NguoiDung(ID)
);

-- 2. Tạo bảng Tiến Trình Bài Học (Chi tiết từng bài học trong khóa đó)
CREATE TABLE TienTrinhBaiHoc (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IdTienTrinhKhoaHoc INT NOT NULL, -- Liên kết với bảng cha ở trên
    IDBaiHoc INT NOT NULL,           -- Tham chiếu tới bảng BaiHoc
    status NVARCHAR(50),             -- Ví dụ: 'Completed', 'Failed'
    Ngaybatdau DATETIME DEFAULT GETDATE(),
    Ngayketthuc DATETIME,
    solanlam INT DEFAULT 0,
    tgianlam INT,                    -- Thời gian làm bài (giây hoặc phút)

    -- Khóa ngoại
    CONSTRAINT FK_TTBH_TienTrinhKhoaHoc FOREIGN KEY (IdTienTrinhKhoaHoc) REFERENCES TienTrinhKhoaHoc(ID) ON DELETE CASCADE,
    CONSTRAINT FK_TTBH_BaiHoc FOREIGN KEY (IDBaiHoc) REFERENCES BaiHoc(ID)
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

CREATE TABLE NhatKyHoatDong (
    ID INT IDENTITY(1,1) PRIMARY KEY,
    IDNguoiDung INT NOT NULL,
    NgayHoatDong DATE NOT NULL, -- Chỉ lưu ngày (yyyy-MM-dd), không lưu giờ
    SoPhutHoc INT DEFAULT 0,
    TongSoBaiDaLam INT DEFAULT 0,
    
    FOREIGN KEY (IDNguoiDung) REFERENCES NguoiDung(ID),
    CONSTRAINT UQ_NhatKy_User_Date UNIQUE (IDNguoiDung, NgayHoatDong) 
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
    IDNguoiDung INT NOT NULL,
    IDTuVung INT NOT NULL,
	primary key (IDNguoiDung, IDTuVung),
    FOREIGN KEY (IDNguoiDung) REFERENCES NguoiDung(ID),
    FOREIGN KEY (IDTuVung) REFERENCES TuVung(ID)
);

-- CONSTRAINTS
ALTER TABLE BaiHoc ADD CONSTRAINT CK_TrangThai_BaiHoc CHECK (TrangThai IN (N'Đã hoàn thành', N'Đang Hoàn thành', N'Chưa làm'));
ALTER TABLE BaiTap ADD CONSTRAINT CK_TrangThai_BaiTap CHECK (TrangThai IN (N'Đã hoàn thành', N'Đang Hoàn thành', N'Chưa làm'));
ALTER TABLE DanhGiaKyNang ADD CONSTRAINT CK_Diem_DanhGiaKyNang CHECK (Diem >= 0 AND Diem <= 100);

-- =============================================
-- INSERT DỮ LIỆU MẪU (ĐÃ CẬP NHẬT JSON)
-- =============================================

SET DATEFORMAT YMD;

-- 1. Insert NguoiDung
INSERT INTO NguoiDung 
(tenDangNhap, matKhau, email, anhDaiDien, LastLogin, Streak, role, TongThoiGianHoatDong)
VALUES
('user1', '$2a$10$PLLK1jr5F0hDTVVoKFSGj.T7nhAKhH/Qo563lNtuaaEMndxZwy.Ky', 'ttuankhanh4@gmail.com', 'U0001_ava.jpg', '2024-01-15 08:30:00', 5, 'ADMIN', 3600),
('user2', '$2a$10$OgyHCP.YQ4xoGGXFpD6z3O01TwJNIhUG8t60Tu82HnwFkThK7BrUy', 'chientranminh355@gmail.com', 'U0002_ava.jpg', '2024-01-14 10:15:00', 3, 'USER', 1800),
('user3', '$2a$10$l7GqvKZDYVsReEiGCc0LpeGoGgy0beEJ2yrAmSvzOM2BcOWUfStDC', 'huynhletienhien@gmail.com', 'U0003_ava.jpg', '2024-01-13 14:20:00', 7, 'USER', 7200);

-- 2. Insert KhoaHoc
INSERT INTO KhoaHoc (tenKhoaHoc, moTa, trinhDo, ngayTao) VALUES
(N'Tiếng Anh Cơ Bản', N'Khóa học tiếng Anh dành cho người mới bắt đầu', N'Beginner', '2024-01-01'),
(N'Tiếng Anh Trung Cấp', N'Khóa học tiếng Anh trình độ trung cấp', N'Intermediate', '2024-01-05');

-- 3. Insert BaiHoc
INSERT INTO BaiHoc (IDKhoaHoc, tenBaiHoc, moTa, noiDung, thuTuBaiHoc, trangThai) VALUES
(1, N'Gia đình', N'Family Members', N'Vocabulary about family...', 1, N'Đã hoàn thành'),
(1, N'Cuộc sống học đường', N'School Life', N'School supplies and subjects...', 2, N'Chưa làm'),
(1, N'Thức ăn & Đồ uống', N'Food & Drinks', N'Meals and ordering food...', 3, N'Chưa làm'),
(1, N'Nhà của tôi', N'My House', N'Rooms and furniture...', 4, N'Chưa làm'),
(1, N'Mua sắm', N'Shopping', N'Clothes and prices...', 5, N'Chưa làm'),
(1, N'Động vật', N'Animals', N'Wild and domestic animals...', 6, N'Chưa làm');

-- 4. Insert CapDo
INSERT INTO CapDo (DoKho) VALUES (N'Dễ'), (N'Trung bình'), (N'Khó');

-- 5. Insert BaiTap
INSERT INTO BaiTap (IDBaiHoc, TenBaiTap, loaiBaiTap, trangThai, capdo, thoigian) 
VALUES
-- Bài 1: Gia đình (ID 1-3)
(1, N'Từ vựng thành viên gia đình', N'Đọc', N'Đã hoàn thành', N'Dễ', '00:10:00'),
(1, N'Nghe giới thiệu về gia đình', N'Nghe', N'Đã hoàn thành', N'Trung bình', '00:15:00'),
(1, N'Nói về người thân', N'Nói', N'Chưa làm', N'Khó', '00:15:00'),

-- Bài 2: Học đường (ID 4-6)
(2, N'Dụng cụ học tập', N'Đọc', N'Chưa làm', N'Dễ', '00:10:00'),
(2, N'Nghe thời khóa biểu', N'Nghe', N'Chưa làm', N'Trung bình', '00:15:00'),
(2, N'Viết về môn học yêu thích', N'Viết', N'Chưa làm', N'Khó', '00:20:00'),

-- Bài 3: Thức ăn (ID 7-9)
(3, N'Tên các món ăn', N'Đọc', N'Chưa làm', N'Dễ', '00:10:00'),
(3, N'Nghe gọi món nhà hàng', N'Nghe', N'Chưa làm', N'Trung bình', '00:15:00'),
(3, N'Nói về món ăn yêu thích', N'Nói', N'Chưa làm', N'Khó', '00:15:00'),

-- Bài 4: Nhà cửa (ID 10-12)
(4, N'Các phòng trong nhà', N'Đọc', N'Chưa làm', N'Dễ', '00:10:00'),
(4, N'Nghe vị trí đồ vật', N'Nghe', N'Chưa làm', N'Trung bình', '00:15:00'),
(4, N'Viết mô tả phòng ngủ', N'Viết', N'Chưa làm', N'Khó', '00:20:00'),

-- Bài 5: Mua sắm (ID 13-15)
(5, N'Đọc biển báo giảm giá', N'Đọc', N'Chưa làm', N'Dễ', '00:10:00'),
(5, N'Nghe giá tiền', N'Nghe', N'Chưa làm', N'Trung bình', '00:15:00'),
(5, N'Nói: Hỏi mua quần áo', N'Nói', N'Chưa làm', N'Khó', '00:15:00'),

-- Bài 6: Động vật (ID 16-18)
(6, N'Động vật hoang dã', N'Đọc', N'Chưa làm', N'Dễ', '00:10:00'),
(6, N'Nghe tiếng kêu động vật', N'Nghe', N'Chưa làm', N'Trung bình', '00:15:00'),
(6, N'Viết về thú cưng', N'Viết', N'Chưa làm', N'Khó', '00:20:00');

-- 6. Insert CauHoi (SỬ DỤNG JSON)
-- Lưu ý: IDBaiTap = 1 (Trắc nghiệm), IDBaiTap = 2 (Nghe hiểu)

INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich) VALUES
(1, 1, N'Who is your father''s wife?', 'ABCD', N'{"A": "Sister", "B": "Mother", "C": "Aunt", "D": "Grandma", "Correct": "B"}', N'Vợ của bố là Mẹ (Mother).'),
(1, 1, N'Your mother''s brother is your _____ .', 'ABCD', N'{"A": "Uncle", "B": "Aunt", "C": "Cousin", "D": "Nephew", "Correct": "A"}', N'Anh/em trai của mẹ là Cậu (Uncle).'),
(1, 1, N'Who is your sister''s brother?', 'ABCD', N'{"A": "Uncle", "B": "Father", "C": "Brother", "D": "Grandpa", "Correct": "C"}', N'Anh của chị gái chính là Anh/Em trai (Brother).'),
(1, 1, N'My father has a son. He is my _____ .', 'ABCD', N'{"A": "Sister", "B": "Brother", "C": "Mother", "D": "Cousin", "Correct": "B"}', N'Con trai của bố là Anh/em trai.'),
(1, 1, N'Your uncle''s child is your _____ .', 'ABCD', N'{"A": "Brother", "B": "Sister", "C": "Cousin", "D": "Niece", "Correct": "C"}', N'Con của chú bác là Anh em họ (Cousin).'),
(1, 1, N'Which word means "Grandmother"?', 'ABCD', N'{"A": "Mom", "B": "Grandma", "C": "Aunt", "D": "Sister", "Correct": "B"}', N'Grandma là từ gọi thân mật của Grandmother.'),
(1, 1, N'I have two sisters and one _____ .', 'ABCD', N'{"A": "brother", "B": "brothers", "C": "mother", "D": "fathers", "Correct": "A"}', N'One brother (số ít).'),
(1, 1, N'This is my family _____ .', 'ABCD', N'{"A": "photo", "B": "photos", "C": "image", "D": "music", "Correct": "A"}', N'Family photo (Ảnh gia đình).'),
(1, 1, N'Opposite of "Old" is _____ .', 'ABCD', N'{"A": "Big", "B": "Small", "C": "Young", "D": "Tall", "Correct": "C"}', N'Trái nghĩa với Già là Trẻ (Young).'),
(1, 1, N'Do you have any _____ ?', 'ABCD', N'{"A": "brother", "B": "siblings", "C": "sister", "D": "child", "Correct": "B"}', N'Siblings là anh chị em ruột.');

-----------------------------------------------------------
-- BÀI 1: FAMILY - LISTENING (Trắc nghiệm - ABCD)
-----------------------------------------------------------
INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich, Audio_URL) VALUES
(1, 2, N'Listen. Who is speaking?', 'Listening', N'{"A": "Father", "B": "Mother", "C": "Baby", "D": "Grandpa", "Correct": "A"}', N'Audio: I am your father.', 'dad.mp3'),
(1, 2, N'Listen. How many people are there?', 'Listening', N'{"A": "Three", "B": "Four", "C": "Five", "D": "Six", "Correct": "B"}', N'Audio: There are four people.', 'four.mp3'),
(1, 2, N'Listen. Who is she?', 'Listening', N'{"A": "Sister", "B": "Mother", "C": "Grandmother", "D": "Aunt", "Correct": "C"}', N'Audio: She is my grandma.', 'grandma.mp3'),
(1, 2, N'Listen. What is the boy''s name?', 'Listening', N'{"A": "Tom", "B": "John", "C": "Alex", "D": "Ben", "Correct": "A"}', N'Audio: My name is Tom.', 'tom.mp3'),
(1, 2, N'Listen. How old is the sister?', 'Listening', N'{"A": "Eight", "B": "Nine", "C": "Ten", "D": "Eleven", "Correct": "C"}', N'Audio: She is ten years old.', 'ten.mp3'),
(1, 2, N'Listen. Where are they?', 'Listening', N'{"A": "At home", "B": "At school", "C": "In the park", "D": "At the zoo", "Correct": "A"}', N'Audio: We are at home.', 'home.mp3'),
(1, 2, N'Listen. Who is cooking?', 'Listening', N'{"A": "Dad", "B": "Mom", "C": "Grandma", "D": "Sister", "Correct": "B"}', N'Audio: Mom is cooking dinner.', 'mom_cook.mp3'),
(1, 2, N'Listen. Is he happy?', 'Listening', N'{"A": "Yes", "B": "No", "Correct": "A"}', N'Audio: He is very happy.', 'happy.mp3'),
(1, 2, N'Listen. What implies "Baby"?', 'Listening', N'{"A": "Crying sound", "B": "Barking", "C": "Singing", "D": "Talking", "Correct": "A"}', N'Tiếng em bé khóc.', 'baby.mp3'),
(1, 2, N'Listen. Choose the correct picture.', 'Listening', N'{"A": "Old man", "B": "Young boy", "C": "Baby", "D": "Woman", "Correct": "A"}', N'Audio describes an old man.', 'oldman.mp3');

-----------------------------------------------------------
-- BÀI 1: FAMILY - SPEAKING (Luyện nói)
-----------------------------------------------------------
INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich) VALUES
(1, 3, N'Read aloud: "This is my family."', 'Speaking', N'{"Correct": "This is my family"}', N'Đây là gia đình tôi.'),
(1, 3, N'Say: "I love my mother."', 'Speaking', N'{"Correct": "I love my mother"}', N'Tôi yêu mẹ tôi.'),
(1, 3, N'Answer: "Who is he?" (Hint: Father)', 'Speaking', N'{"Correct": "He is my father"}', N'Ông ấy là bố tôi.'),
(1, 3, N'Read aloud: "Brother and Sister"', 'Speaking', N'{"Correct": "Brother and Sister"}', N'Anh trai và chị gái.'),
(1, 3, N'Say: "My grandmother is old."', 'Speaking', N'{"Correct": "My grandmother is old"}', N'Bà tôi đã già.'),
(1, 3, N'Read aloud: "Happy Family"', 'Speaking', N'{"Correct": "Happy Family"}', N'Gia đình hạnh phúc.'),
(1, 3, N'Say: "Uncle and Aunt"', 'Speaking', N'{"Correct": "Uncle and Aunt"}', N'Chú và Dì.'),
(1, 3, N'Read aloud: "Cousin"', 'Speaking', N'{"Correct": "Cousin"}', N'Anh chị em họ.'),
(1, 3, N'Say: "Nice to meet you."', 'Speaking', N'{"Correct": "Nice to meet you"}', N'Rất vui được gặp bạn.'),
(1, 3, N'Introduce yourself: "My name is..."', 'Speaking', N'{"Correct": "My name is"}', N'Giới thiệu tên.');

-----------------------------------------------------------
-- BÀI 2: SCHOOL LIFE - READING (Trắc nghiệm)
-----------------------------------------------------------
INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich) VALUES
(1, 4, N'We write with a _____ .', 'ABCD', N'{"A": "Ruler", "B": "Pen", "C": "Rubber", "D": "Book", "Correct": "B"}', N'Pen (Bút).'),
(1, 4, N'Teachers write on the _____ .', 'ABCD', N'{"A": "Desk", "B": "Chair", "C": "Board", "D": "Bag", "Correct": "C"}', N'Board (Bảng).'),
(1, 4, N'We read books in the _____ .', 'ABCD', N'{"A": "Library", "B": "Gym", "C": "Cafeteria", "D": "Garden", "Correct": "A"}', N'Library (Thư viện).'),
(1, 4, N'What subject involves numbers?', 'ABCD', N'{"A": "English", "B": "Math", "C": "Art", "D": "Music", "Correct": "B"}', N'Math (Toán).'),
(1, 4, N'A student carries books in a _____ .', 'ABCD', N'{"A": "Box", "B": "Bag", "C": "Pocket", "D": "Hat", "Correct": "B"}', N'Bag (Cặp sách).'),
(1, 4, N'The _____ teaches students.', 'ABCD', N'{"A": "Doctor", "B": "Farmer", "C": "Teacher", "D": "Driver", "Correct": "C"}', N'Teacher (Giáo viên).'),
(1, 4, N'We paint pictures in _____ class.', 'ABCD', N'{"A": "Math", "B": "Art", "C": "History", "D": "Science", "Correct": "B"}', N'Art (Mỹ thuật).'),
(1, 4, N'A ruler is used to _____ .', 'ABCD', N'{"A": "Measure", "B": "Write", "C": "Cut", "D": "Glue", "Correct": "A"}', N'Measure (Đo).'),
(1, 4, N'School usually starts in the _____ .', 'ABCD', N'{"A": "Evening", "B": "Afternoon", "C": "Morning", "D": "Night", "Correct": "C"}', N'Morning (Buổi sáng).'),
(1, 4, N'A pencil needs a _____ .', 'ABCD', N'{"A": "Pen", "B": "Sharpener", "C": "Ruler", "D": "Table", "Correct": "B"}', N'Sharpener (Gọt bút chì).');

-----------------------------------------------------------
-- BÀI 2: SCHOOL LIFE - LISTENING (Trắc nghiệm - ABCD)
-----------------------------------------------------------
INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich, Audio_URL) VALUES
(1, 5, N'Listen. What object is this?', 'Listening', N'{"A": "Pencil", "B": "Book", "C": "Ruler", "D": "Eraser", "Correct": "B"}', N'Audio: Open your book.', 'book.mp3'),
(1, 5, N'Listen. What class is next?', 'Listening', N'{"A": "Math", "B": "English", "C": "Music", "D": "PE", "Correct": "C"}', N'Audio: Next is Music class.', 'music_class.mp3'),
(1, 5, N'Listen. What color is the bag?', 'Listening', N'{"A": "Red", "B": "Blue", "C": "Green", "D": "Yellow", "Correct": "B"}', N'Audio: It is a blue bag.', 'bluebag.mp3'),
(1, 5, N'Listen. What does the teacher say?', 'Listening', N'{"A": "Stand up", "B": "Sit down", "C": "Be quiet", "D": "Go out", "Correct": "A"}', N'Audio: Please stand up.', 'standup.mp3'),
(1, 5, N'Listen. How many students?', 'Listening', N'{"A": "10", "B": "20", "C": "30", "D": "40", "Correct": "B"}', N'Audio: Twenty students.', '20students.mp3'),
(1, 5, N'Listen. What time is it?', 'Listening', N'{"A": "7:00", "B": "8:00", "C": "9:00", "D": "10:00", "Correct": "A"}', N'Audio: It is 7 o''clock.', '7am.mp3'),
(1, 5, N'Listen. Is this a pen?', 'Listening', N'{"A": "Yes, it is", "B": "No, it isn''t", "Correct": "B"}', N'Audio: No, it is a pencil.', 'not_pen.mp3'),
(1, 5, N'Listen. Where are they?', 'Listening', N'{"A": "Classroom", "B": "Playground", "C": "Library", "D": "Bus", "Correct": "B"}', N'Audio sounds like playing.', 'playground.mp3'),
(1, 5, N'Listen. Identify the sound.', 'Listening', N'{"A": "Bell", "B": "Drum", "C": "Piano", "D": "Guitar", "Correct": "A"}', N'School bell ringing.', 'bell.mp3'),
(1, 5, N'Listen. Who is he?', 'Listening', N'{"A": "Student", "B": "Teacher", "C": "Principal", "D": "Janitor", "Correct": "A"}', N'Audio: I am a new student.', 'new_student.mp3');

-----------------------------------------------------------
-- BÀI 2: SCHOOL LIFE - WRITING (Điền từ/Viết)
-----------------------------------------------------------
INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich) VALUES
(2, 6, N'Complete: This is a _____ (quyển sách).', 'FillBlank', N'{"Correct": "book"}', N'Book.'),
(2, 6, N'Rearrange: is / name / My / Sarah.', 'FillBlank', N'{"Correct": "My name is Sarah"}', N'Tên tôi là Sarah.'),
(2, 6, N'Complete: I am a _____ (học sinh).', 'FillBlank', N'{"Correct": "student"}', N'Student.'),
(2, 6, N'Complete: He _____ (be) my teacher.', 'FillBlank', N'{"Correct": "is"}', N'To be "is".'),
(2, 6, N'Complete: I like _____ (môn Toán).', 'FillBlank', N'{"Correct": "Math"}', N'Math.'),
(2, 6, N'Correct the word: "Pencel" -> ?', 'FillBlank', N'{"Correct": "Pencil"}', N'Pencil.'),
(2, 6, N'Complete: The board is _____ (màu xanh).', 'FillBlank', N'{"Correct": "green"}', N'Green.'),
(2, 6, N'Complete: Sit _____ (ngồi xuống).', 'FillBlank', N'{"Correct": "down"}', N'Sit down.'),
(2, 6, N'Write the number: 15 -> ?', 'FillBlank', N'{"Correct": "fifteen"}', N'Fifteen.'),
(2, 6, N'Complete: Open the _____ (cửa).', 'FillBlank', N'{"Correct": "door"}', N'Door.');

-----------------------------------------------------------
-- BÀI 3: FOOD - READING (Trắc nghiệm)
-----------------------------------------------------------
INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich) VALUES
(1, 7, N'Which one is a fruit?', 'ABCD', N'{"A": "Chicken", "B": "Apple", "C": "Rice", "D": "Bread", "Correct": "B"}', N'Apple (Táo).'),
(1, 7, N'I want to drink _____ .', 'ABCD', N'{"A": "Water", "B": "Pizza", "C": "Cake", "D": "Meat", "Correct": "A"}', N'Water (Nước).'),
(1, 7, N'We eat soup with a _____ .', 'ABCD', N'{"A": "Fork", "B": "Knife", "C": "Spoon", "D": "Stick", "Correct": "C"}', N'Spoon (Thìa).'),
(1, 7, N'Which food is yellow?', 'ABCD', N'{"A": "Tomato", "B": "Banana", "C": "Grape", "D": "Kiwi", "Correct": "B"}', N'Banana (Chuối).'),
(1, 7, N'I am hungry. I want to _____ .', 'ABCD', N'{"A": "Drink", "B": "Sleep", "C": "Eat", "D": "Run", "Correct": "C"}', N'Eat (Ăn).'),
(1, 7, N'Milk comes from _____ .', 'ABCD', N'{"A": "Cows", "B": "Chicken", "C": "Pigs", "D": "Fish", "Correct": "A"}', N'Cows (Bò).'),
(1, 7, N'A hamburger is a kind of _____ .', 'ABCD', N'{"A": "Drink", "B": "Food", "C": "Vegetable", "D": "Fruit", "Correct": "B"}', N'Food (Đồ ăn).'),
(1, 7, N'We have breakfast in the _____ .', 'ABCD', N'{"A": "Morning", "B": "Afternoon", "C": "Evening", "D": "Night", "Correct": "A"}', N'Morning (Sáng).'),
(1, 7, N'Which one is spicy?', 'ABCD', N'{"A": "Candy", "B": "Chili", "C": "Ice cream", "D": "Banana", "Correct": "B"}', N'Chili (Ớt).'),
(1, 7, N'Do you like pizza? - Yes, I _____ .', 'ABCD', N'{"A": "am", "B": "do", "C": "is", "D": "does", "Correct": "B"}', N'Yes, I do.');

-----------------------------------------------------------
-- BÀI 3: FOOD - LISTENING (Trắc nghiệm - ABCD)
-----------------------------------------------------------
INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich, Audio_URL) VALUES
(1, 8, N'Listen. What does he want?', 'Listening', N'{"A": "Coffee", "B": "Tea", "C": "Milk", "D": "Juice", "Correct": "A"}', N'Audio: I want coffee.', 'coffee.mp3'),
(1, 8, N'Listen. What food is this?', 'Listening', N'{"A": "Sandwich", "B": "Burger", "C": "Pizza", "D": "Salad", "Correct": "C"}', N'Audio: I love pizza.', 'pizza.mp3'),
(1, 8, N'Listen. Is she hungry?', 'Listening', N'{"A": "Yes", "B": "No", "Correct": "A"}', N'Audio: I am so hungry.', 'hungry.mp3'),
(1, 8, N'Listen. How much is the cake?', 'Listening', N'{"A": "$2", "B": "$5", "C": "$3", "D": "$4", "Correct": "A"}', N'Audio: Two dollars.', '2dollars.mp3'),
(1, 8, N'Listen. What fruit is mentioned?', 'Listening', N'{"A": "Orange", "B": "Apple", "C": "Mango", "D": "Berry", "Correct": "A"}', N'Audio: Orange juice.', 'orange.mp3'),
(1, 8, N'Listen. What meal is it?', 'Listening', N'{"A": "Breakfast", "B": "Lunch", "C": "Dinner", "Correct": "C"}', N'Audio: Time for dinner.', 'dinner.mp3'),
(1, 8, N'Listen. Does he like fish?', 'Listening', N'{"A": "Yes", "B": "No", "Correct": "B"}', N'Audio: I don''t like fish.', 'nolike_fish.mp3'),
(1, 8, N'Listen. Choose the drink.', 'Listening', N'{"A": "Water", "B": "Soda", "C": "Milk", "D": "Tea", "Correct": "C"}', N'Audio: Milk please.', 'milk.mp3'),
(1, 8, N'Listen. Is the tea hot or cold?', 'Listening', N'{"A": "Hot", "B": "Cold", "Correct": "A"}', N'Audio: Hot tea.', 'hot_tea.mp3'),
(1, 8, N'Listen. Where are they?', 'Listening', N'{"A": "Restaurant", "B": "School", "C": "Park", "D": "Zoo", "Correct": "A"}', N'Audio: Order food sounds.', 'restaurant.mp3');

-----------------------------------------------------------
-- BÀI 3: FOOD - SPEAKING (Luyện nói)
-----------------------------------------------------------
INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich) VALUES
(2, 9, N'Say: "I am hungry."', 'Speaking', N'{"Correct": "I am hungry"}', N'Tôi đói.'),
(2, 9, N'Say: "I like chicken."', 'Speaking', N'{"Correct": "I like chicken"}', N'Tôi thích gà.'),
(2, 9, N'Ask: "Do you like pizza?"', 'Speaking', N'{"Correct": "Do you like pizza"}', N'Bạn thích pizza không?'),
(2, 9, N'Order: "One hamburger, please."', 'Speaking', N'{"Correct": "One hamburger please"}', N'Cho 1 hăm-bơ-gơ.'),
(2, 9, N'Say: "It is delicious."', 'Speaking', N'{"Correct": "It is delicious"}', N'Nó rất ngon.'),
(2, 9, N'Read aloud: "Breakfast"', 'Speaking', N'{"Correct": "Breakfast"}', N'Bữa sáng.'),
(2, 9, N'Say: "Water, please."', 'Speaking', N'{"Correct": "Water please"}', N'Cho tôi nước.'),
(2, 9, N'Read aloud: "Vegetables"', 'Speaking', N'{"Correct": "Vegetables"}', N'Rau củ.'),
(2, 9, N'Say: "No, thank you."', 'Speaking', N'{"Correct": "No thank you"}', N'Không, cảm ơn.'),
(2, 9, N'Read aloud: "Ice cream"', 'Speaking', N'{"Correct": "Ice cream"}', N'Kem.');

-----------------------------------------------------------
-- BÀI 4: MY HOUSE - READING (Trắc nghiệm)
-----------------------------------------------------------
INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich) VALUES
(1, 10, N'Where do you sleep?', 'ABCD', N'{"A": "Kitchen", "B": "Bedroom", "C": "Bathroom", "D": "Garden", "Correct": "B"}', N'Bedroom (Phòng ngủ).'),
(1, 10, N'Where do you cook?', 'ABCD', N'{"A": "Living room", "B": "Kitchen", "C": "Garage", "D": "Hall", "Correct": "B"}', N'Kitchen (Bếp).'),
(1, 10, N'A sofa is usually in the _____ .', 'ABCD', N'{"A": "Bathroom", "B": "Living room", "C": "Kitchen", "D": "Garden", "Correct": "B"}', N'Living room (Phòng khách).'),
(1, 10, N'We take a shower in the _____ .', 'ABCD', N'{"A": "Bathroom", "B": "Bedroom", "C": "Kitchen", "D": "Attic", "Correct": "A"}', N'Bathroom (Phòng tắm).'),
(1, 10, N'My house has a big _____ with flowers.', 'ABCD', N'{"A": "Roof", "B": "Garden", "C": "Floor", "D": "Wall", "Correct": "B"}', N'Garden (Vườn).'),
(1, 10, N'The cat is _____ the table (ở dưới).', 'ABCD', N'{"A": "On", "B": "In", "C": "Under", "D": "Above", "Correct": "C"}', N'Under (Ở dưới).'),
(1, 10, N'Open the _____ to see outside.', 'ABCD', N'{"A": "Door", "B": "Window", "C": "Box", "D": "Chair", "Correct": "B"}', N'Window (Cửa sổ).'),
(1, 10, N'We watch _____ in the living room.', 'ABCD', N'{"A": "TV", "B": "Radio", "C": "Book", "D": "Lamp", "Correct": "A"}', N'TV.'),
(1, 10, N'I put my clothes in the _____ .', 'ABCD', N'{"A": "Fridge", "B": "Wardrobe", "C": "Oven", "D": "Sink", "Correct": "B"}', N'Wardrobe (Tủ quần áo).'),
(1, 10, N'The car is in the _____ .', 'ABCD', N'{"A": "Bedroom", "B": "Garage", "C": "Kitchen", "D": "Bathroom", "Correct": "B"}', N'Garage (Gara).');

-----------------------------------------------------------
-- BÀI 4: MY HOUSE - LISTENING (Trắc nghiệm - ABCD)
-----------------------------------------------------------
INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich, Audio_URL) VALUES
(1, 11, N'Listen. What room is this?', 'Listening', N'{"A": "Kitchen", "B": "Bathroom", "C": "Bedroom", "D": "Living room", "Correct": "A"}', N'Audio: Cooking sounds.', 'kitchen.mp3'),
(1, 11, N'Listen. What object is mentioned?', 'Listening', N'{"A": "Sofa", "B": "Bed", "C": "Chair", "D": "Table", "Correct": "A"}', N'Audio: Nice sofa.', 'sofa.mp3'),
(1, 11, N'Listen. Where is the cat?', 'Listening', N'{"A": "On the bed", "B": "Under the table", "C": "In the box", "D": "Outside", "Correct": "A"}', N'Audio: Cat on the bed.', 'cat_bed.mp3'),
(1, 11, N'Listen. What is the sound?', 'Listening', N'{"A": "Door bell", "B": "Phone", "C": "Dog", "D": "TV", "Correct": "A"}', N'Audio: Ding dong.', 'doorbell.mp3'),
(1, 11, N'Listen. Identify the item.', 'Listening', N'{"A": "Lamp", "B": "Clock", "C": "Mirror", "D": "Fan", "Correct": "B"}', N'Audio: Ticking clock.', 'clock.mp3'),
(1, 11, N'Listen. How many chairs?', 'Listening', N'{"A": "One", "B": "Two", "C": "Three", "D": "Four", "Correct": "D"}', N'Audio: Four chairs.', '4chairs.mp3'),
(1, 11, N'Listen. Is the house big?', 'Listening', N'{"A": "Yes", "B": "No", "Correct": "A"}', N'Audio: Very big house.', 'big_house.mp3'),
(1, 11, N'Listen. Where is Dad?', 'Listening', N'{"A": "Garden", "B": "Garage", "C": "Kitchen", "D": "Room", "Correct": "B"}', N'Audio: Dad is in the garage.', 'garage.mp3'),
(1, 11, N'Listen. What color is the door?', 'Listening', N'{"A": "Red", "B": "Blue", "C": "White", "D": "Brown", "Correct": "A"}', N'Audio: Red door.', 'red_door.mp3'),
(1, 11, N'Listen. What is he doing?', 'Listening', N'{"A": "Sleeping", "B": "Eating", "C": "Washing", "D": "Reading", "Correct": "A"}', N'Audio: Snoring.', 'sleep.mp3');

-----------------------------------------------------------
-- BÀI 4: MY HOUSE - WRITING (Điền từ/Viết)
-----------------------------------------------------------
INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich) VALUES
(2, 12, N'Complete: I sleep in the _____ (phòng ngủ).', 'FillBlank', N'{"Correct": "bedroom"}', N'Bedroom.'),
(2, 12, N'Rearrange: house / My / big / is.', 'FillBlank', N'{"Correct": "My house is big"}', N'Nhà tôi to.'),
(2, 12, N'Complete: This is a _____ (cái ghế).', 'FillBlank', N'{"Correct": "chair"}', N'Chair.'),
(2, 12, N'Opposite of "Big":', 'FillBlank', N'{"Correct": "Small"}', N'Small (Nhỏ).'),
(2, 12, N'Complete: The lamp is _____ the table (trên).', 'FillBlank', N'{"Correct": "on"}', N'On.'),
(2, 12, N'Complete: Open the _____ (cửa).', 'FillBlank', N'{"Correct": "door"}', N'Door.'),
(2, 12, N'Write the room name: Place to shower.', 'FillBlank', N'{"Correct": "Bathroom"}', N'Bathroom.'),
(2, 12, N'Complete: We watch _____ (ti vi).', 'FillBlank', N'{"Correct": "TV"}', N'TV.'),
(2, 12, N'Complete: Living _____ (phòng khách).', 'FillBlank', N'{"Correct": "room"}', N'Living room.'),
(2, 12, N'Complete: My house has 2 _____ (cửa sổ).', 'FillBlank', N'{"Correct": "windows"}', N'Windows (số nhiều).');

-----------------------------------------------------------
-- BÀI 5: SHOPPING - READING (Trắc nghiệm)
-----------------------------------------------------------
INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich) VALUES
(1, 13, N'How much does it _____ ?', 'ABCD', N'{"A": "cost", "B": "price", "C": "pay", "D": "buy", "Correct": "A"}', N'Cost (Giá bao nhiêu).'),
(1, 13, N'I want to buy a new _____ (áo sơ mi).', 'ABCD', N'{"A": "Shoe", "B": "Shirt", "C": "Hat", "D": "Sock", "Correct": "B"}', N'Shirt.'),
(1, 13, N'Where do we buy food?', 'ABCD', N'{"A": "Library", "B": "Supermarket", "C": "Bank", "D": "Park", "Correct": "B"}', N'Supermarket (Siêu thị).'),
(1, 13, N'Can I _____ it on?', 'ABCD', N'{"A": "try", "B": "do", "C": "go", "D": "make", "Correct": "A"}', N'Try on (Mặc thử).'),
(1, 13, N'It is too _____ (đắt).', 'ABCD', N'{"A": "Cheap", "B": "Expensive", "C": "Good", "D": "Nice", "Correct": "B"}', N'Expensive.'),
(1, 13, N'Do you accept credit _____ ?', 'ABCD', N'{"A": "money", "B": "card", "C": "cash", "D": "coin", "Correct": "B"}', N'Credit card (Thẻ tín dụng).'),
(1, 13, N'This hat is very _____ (rẻ).', 'ABCD', N'{"A": "Cheap", "B": "Expensive", "C": "High", "D": "Tall", "Correct": "A"}', N'Cheap.'),
(1, 13, N'I pay with _____ .', 'ABCD', N'{"A": "Cash", "B": "Paper", "C": "Leaf", "D": "Stone", "Correct": "A"}', N'Cash (Tiền mặt).'),
(1, 13, N'The shop is _____ (đóng cửa).', 'ABCD', N'{"A": "Open", "B": "Closed", "C": "Big", "D": "Small", "Correct": "B"}', N'Closed.'),
(1, 13, N'A pair of _____ (giày).', 'ABCD', N'{"A": "Shoes", "B": "Shirt", "C": "Hat", "D": "Dress", "Correct": "A"}', N'Shoes.');

-----------------------------------------------------------
-- BÀI 5: SHOPPING - LISTENING (Trắc nghiệm - ABCD)
-----------------------------------------------------------
INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich, Audio_URL) VALUES
(1, 14, N'Listen. How much is it?', 'Listening', N'{"A": "$10", "B": "$20", "C": "$30", "D": "$40", "Correct": "B"}', N'Audio: Twenty dollars.', '20dollars.mp3'),
(1, 14, N'Listen. What does she buy?', 'Listening', N'{"A": "Dress", "B": "Hat", "C": "Shoes", "D": "Bag", "Correct": "A"}', N'Audio: Nice dress.', 'dress.mp3'),
(1, 14, N'Listen. What color?', 'Listening', N'{"A": "Red", "B": "Black", "C": "Pink", "D": "White", "Correct": "C"}', N'Audio: Pink one.', 'pink.mp3'),
(1, 14, N'Listen. What size?', 'Listening', N'{"A": "Small", "B": "Medium", "C": "Large", "D": "XL", "Correct": "B"}', N'Audio: Medium size.', 'medium.mp3'),
(1, 14, N'Listen. Is it expensive?', 'Listening', N'{"A": "Yes", "B": "No", "Correct": "A"}', N'Audio: Too expensive.', 'expensive.mp3'),
(1, 14, N'Listen. Cash or Card?', 'Listening', N'{"A": "Cash", "B": "Card", "Correct": "B"}', N'Audio: Credit card.', 'card.mp3'),
(1, 14, N'Listen. Where are they?', 'Listening', N'{"A": "Market", "B": "School", "C": "Home", "D": "Hospital", "Correct": "A"}', N'Audio: Market sounds.', 'market.mp3'),
(1, 14, N'Listen. How many apples?', 'Listening', N'{"A": "3", "B": "4", "C": "5", "D": "6", "Correct": "C"}', N'Audio: Five apples.', '5apples.mp3'),
(1, 14, N'Listen. What is the discount?', 'Listening', N'{"A": "10%", "B": "50%", "C": "20%", "D": "30%", "Correct": "B"}', N'Audio: 50% off.', '50percent.mp3'),
(1, 14, N'Listen. Closing time?', 'Listening', N'{"A": "8 PM", "B": "9 PM", "C": "10 PM", "D": "7 PM", "Correct": "B"}', N'Audio: Close at 9.', '9pm.mp3');

-----------------------------------------------------------
-- BÀI 5: SHOPPING - SPEAKING (Luyện nói)
-----------------------------------------------------------
INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich) VALUES
(2, 15, N'Ask: "How much is this?"', 'Speaking', N'{"Correct": "How much is this"}', N'Hỏi giá.'),
(2, 15, N'Say: "I want to buy a hat."', 'Speaking', N'{"Correct": "I want to buy a hat"}', N'Tôi muốn mua mũ.'),
(2, 15, N'Say: "It is cheap."', 'Speaking', N'{"Correct": "It is cheap"}', N'Nó rẻ.'),
(2, 15, N'Ask: "Do you have red?"', 'Speaking', N'{"Correct": "Do you have red"}', N'Có màu đỏ không?'),
(2, 15, N'Say: "Too expensive."', 'Speaking', N'{"Correct": "Too expensive"}', N'Đắt quá.'),
(2, 15, N'Say: "I will take it."', 'Speaking', N'{"Correct": "I will take it"}', N'Tôi sẽ lấy nó.'),
(2, 15, N'Read: "Supermarket"', 'Speaking', N'{"Correct": "Supermarket"}', N'Siêu thị.'),
(2, 15, N'Read: "Shopping mall"', 'Speaking', N'{"Correct": "Shopping mall"}', N'Trung tâm mua sắm.'),
(2, 15, N'Ask: "Can I pay by card?"', 'Speaking', N'{"Correct": "Can I pay by card"}', N'Thanh toán thẻ.'),
(2, 15, N'Say: "Here you are."', 'Speaking', N'{"Correct": "Here you are"}', N'Của bạn đây.');

-----------------------------------------------------------
-- BÀI 6: ANIMALS - READING (Trắc nghiệm)
-----------------------------------------------------------
INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich) VALUES
(1, 16, N'A _____ barks "Woof Woof".', 'ABCD', N'{"A": "Cat", "B": "Dog", "C": "Pig", "D": "Duck", "Correct": "B"}', N'Dog (Chó).'),
(1, 16, N'The King of the Jungle is the _____ .', 'ABCD', N'{"A": "Tiger", "B": "Lion", "C": "Bear", "D": "Monkey", "Correct": "B"}', N'Lion (Sư tử).'),
(1, 16, N'A monkey likes to eat _____ .', 'ABCD', N'{"A": "Fish", "B": "Bananas", "C": "Meat", "D": "Grass", "Correct": "B"}', N'Bananas (Chuối).'),
(1, 16, N'An elephant has a long _____ .', 'ABCD', N'{"A": "Nose", "B": "Tail", "C": "Ear", "D": "Leg", "Correct": "A"}', N'Nose/Trunk (Vòi).'),
(1, 16, N'A bird can _____ .', 'ABCD', N'{"A": "Swim", "B": "Fly", "C": "Run", "D": "Climb", "Correct": "B"}', N'Fly (Bay).'),
(1, 16, N'Fish live in the _____ .', 'ABCD', N'{"A": "Sky", "B": "Water", "C": "Tree", "D": "Ground", "Correct": "B"}', N'Water (Nước).'),
(1, 16, N'A rabbit likes _____ .', 'ABCD', N'{"A": "Candy", "B": "Carrots", "C": "Meat", "D": "Fish", "Correct": "B"}', N'Carrots (Cà rốt).'),
(1, 16, N'We see animals at the _____ .', 'ABCD', N'{"A": "School", "B": "Zoo", "C": "Bank", "D": "Hospital", "Correct": "B"}', N'Zoo (Sở thú).'),
(1, 16, N'A cat says _____ .', 'ABCD', N'{"A": "Moo", "B": "Meow", "C": "Quack", "D": "Roar", "Correct": "B"}', N'Meow.'),
(1, 16, N'Which animal is black and white?', 'ABCD', N'{"A": "Tiger", "B": "Zebra", "C": "Lion", "D": "Fox", "Correct": "B"}', N'Zebra (Ngựa vằn).');

-----------------------------------------------------------
-- BÀI 6: ANIMALS - LISTENING (Trắc nghiệm - ABCD)
-----------------------------------------------------------
INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich, Audio_URL) VALUES
(1, 17, N'Listen. What animal is this?', 'Listening', N'{"A": "Dog", "B": "Cat", "C": "Bird", "D": "Cow", "Correct": "A"}', N'Audio: Dog barking.', 'dog.mp3'),
(1, 17, N'Listen. Identify the animal.', 'Listening', N'{"A": "Lion", "B": "Tiger", "C": "Bear", "D": "Wolf", "Correct": "A"}', N'Audio: Lion roaring.', 'lion.mp3'),
(1, 17, N'Listen. How many legs?', 'Listening', N'{"A": "2", "B": "4", "C": "6", "D": "8", "Correct": "B"}', N'Audio: Four legs.', '4legs.mp3'),
(1, 17, N'Listen. Where is the bird?', 'Listening', N'{"A": "In cage", "B": "On tree", "C": "In house", "D": "In water", "Correct": "B"}', N'Audio: On the tree.', 'bird_tree.mp3'),
(1, 17, N'Listen. Is it big?', 'Listening', N'{"A": "Yes", "B": "No", "Correct": "A"}', N'Audio: Elephant is big.', 'big_elephant.mp3'),
(1, 17, N'Listen. What does it eat?', 'Listening', N'{"A": "Meat", "B": "Grass", "C": "Fish", "D": "Fruit", "Correct": "B"}', N'Audio: Cows eat grass.', 'grass.mp3'),
(1, 17, N'Listen. Name the animal.', 'Listening', N'{"A": "Duck", "B": "Chicken", "C": "Goose", "D": "Bird", "Correct": "A"}', N'Audio: Quack quack.', 'duck.mp3'),
(1, 17, N'Listen. Color of the cat?', 'Listening', N'{"A": "Black", "B": "White", "C": "Orange", "D": "Grey", "Correct": "A"}', N'Audio: Black cat.', 'blackcat.mp3'),
(1, 17, N'Listen. Do you like snakes?', 'Listening', N'{"A": "Yes", "B": "No", "Correct": "B"}', N'Audio: I hate snakes.', 'hate_snake.mp3'),
(1, 17, N'Listen. What is this?', 'Listening', N'{"A": "Pig", "B": "Horse", "C": "Sheep", "D": "Goat", "Correct": "A"}', N'Audio: Pig sound.', 'pig.mp3');

-----------------------------------------------------------
-- BÀI 6: ANIMALS - WRITING (Điền từ/Viết)
-----------------------------------------------------------
INSERT INTO CauHoi (IDCapDo, IDBaiTap, NoiDungCauHoi, LoaiCauHoi, DuLieuDapAn, GiaiThich) VALUES
(2, 18, N'Complete: The _____ (con chó) barks.', 'FillBlank', N'{"Correct": "dog"}', N'Dog.'),
(2, 18, N'Rearrange: like / I / cats.', 'FillBlank', N'{"Correct": "I like cats"}', N'Tôi thích mèo.'),
(2, 18, N'Complete: A _____ (con cá) swims.', 'FillBlank', N'{"Correct": "fish"}', N'Fish.'),
(2, 18, N'Name the animal: Tall neck (Giraffe).', 'FillBlank', N'{"Correct": "Giraffe"}', N'Hươu cao cổ.'),
(2, 18, N'Complete: Birds can _____ (bay).', 'FillBlank', N'{"Correct": "fly"}', N'Fly.'),
(2, 18, N'Plural of Mouse: _____ .', 'FillBlank', N'{"Correct": "Mice"}', N'Chuột (số nhiều).'),
(2, 18, N'Complete: Elephant has a big _____ (mũi/vòi).', 'FillBlank', N'{"Correct": "nose"}', N'Nose.'),
(2, 18, N'Complete: Ti_er (Con hổ).', 'FillBlank', N'{"Correct": "g"}', N'Tiger.'),
(2, 18, N'Complete: Monkeys eat _____ (chuối).', 'FillBlank', N'{"Correct": "bananas"}', N'Bananas.'),
(2, 18, N'Write: It is a pig.', 'FillBlank', N'{"Correct": "It is a pig"}', N'Nó là con heo.');

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

INSERT INTO KyNang (TenKyNang) VALUES (N'Nghe'), (N'Nói'), (N'Đọc'), (N'Viết');

INSERT INTO DanhGiaKyNang (IDKyNang, IDNguoiDung, diem) VALUES
  (1, 2, 2.5),
  (2, 2, 9.5),
  (3, 2, 3.5),
  (4, 2, 6.5),
  (1, 1, 7.5),
  (2, 1, 7.5),
  (1, 3, 7.5);

INSERT INTO ThanhTich (tenThanhTich, moTa, bieuTuong) VALUES 
(N'Người mới bắt đầu', N'Hoàn thành bài đầu', 'badge.png'),
(N'Chăm chỉ mỗi ngày', N'Hoàn thành bài học 3 ngày liên tiếp', 'streak_3.png'),
(N'Bậc thầy từ vựng', N'Học thuộc 100 từ mới', 'vocab_master.png'),
(N'Thợ săn điểm số', N'Đạt điểm tối đa trong một bài kiểm tra', 'score_hunter.png'),
(N'Người kiên trì', N'Hoàn thành 10 bài học', 'persistent.png'),
(N'Thành viên tích cực', N'Đã tham gia ứng dụng hơn 1 tuần', 'active_member.png'),
(N'Tay đua tốc độ', N'Hoàn thành bài thi dưới 1 phút', 'speed_racer.png'),
(N'Thần đồng ngôn ngữ', N'Đạt tổng 1000 điểm tích lũy', 'genius.png'),
(N'Người kết nối', N'Đã cập nhật ảnh đại diện thành công', 'avatar_connected.png');

INSERT INTO NguoiDung_ThanhTich (IDNguoiDung, IDThanhTich) VALUES 
(2, 1),
(2, 2), -- Chăm chỉ mỗi ngày
(2, 3), -- Bậc thầy từ vựng
(2, 4), -- Thợ săn điểm số
(2, 5), -- Người kiên trì
(2, 6), -- Thành viên tích cực
(2, 7), -- Tay đua tốc độ
(1, 1), -- Người mới bắt đầu
(1, 2), -- Chăm chỉ mỗi ngày
(1, 3); -- Bậc thầy từ vựng

insert into TuVungYeuThich (IDNguoiDung,IDTuVung) values (1,1);
INSERT INTO NhatKyHoatDong (IDNguoiDung, NgayHoatDong, SoPhutHoc, TongSoBaiDaLam)
VALUES 
(1, DATEADD(day, -1, GETDATE()), 45, 2),
(1, GETDATE(), 30, 1),
(2, GETDATE(), 15, 0);
INSERT INTO TienTrinhKhoaHoc (IdKhoaHoc, IdUser, trangthai, PhanTram, LanLamGanNhat, Tgianhoc, NgayBatDau)
VALUES 
(1, 1, N'Đang học', 50.00, GETDATE(), 3600, '2024-01-10 08:00:00'),
(1, 2, N'Đang học', 0.00, GETDATE(), 0, GETDATE());

INSERT INTO TienTrinhBaiHoc (IdTienTrinhKhoaHoc, IDBaiHoc, status, Ngaybatdau, Ngayketthuc, solanlam, tgianlam)
VALUES 
(1, 1, N'Completed', '2024-01-10 08:05:00', '2024-01-10 08:30:00', 1, 1500),
(1, 2, N'In Progress', '2024-01-11 09:00:00', NULL, 2, 600);


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
select * from KhoaHoc k left join  BaiHoc b on k.ID = b.IDKhoaHoc
select * from BaiTap
select * from NguoiDung_ThanhTich
select * from ThanhTich
select * from TuVungYeuThich
select * from ChiTietBaiLam
select * from LichSuBaiLam
select * from DanhGiaKyNang
--lấy lịch sử làm bài của user
select *
from LichSuBaiLam l 
where l.IDNguoiDung = 1
--lấy chi tiết bài cụ thể của user
select IDTest, l.IDBaiTap, LoaiBai, DiemSo, ch.ID as IdBaitap, NoiDungCauHoi, LoaiBai, DuLieuDapAn, GiaiThich, Audio_URL, UserAns, IsCorrect
from ChiTietBaiLam c 
	join LichSuBaiLam l on c.IDLichSuBaiLam = l.ID
	join CauHoi ch on c.IDCauHoi = ch.ID
where l.ID =1
select * from TienTrinhBaiHoc
select * from TienTrinhKhoaHoc
select * from NhatKyHoatDong
select * from NguoiDung
select * from NhatKyHoatDong
--lấy số thành tích, điểm các kỹ năng, số bài học
select count(*) soThanhTuu
from nguoiDung n join NguoiDung_ThanhTich nd on n.ID = nd.IDNguoiDung
where n.ID = 1
select k.TenKyNang, dg.diem
from nguoiDung n 
	join DanhGiaKyNang dg on n.ID = dg.IDNguoiDung
	join KyNang k on k.ID = dg.IDKyNang
where n.ID =1
select count(*) SoBaiHoc
from nguoiDung n join TienTrinhKhoaHoc t on t.IdUser = n.ID
where t.IdUser = 1 and (t.trangthai = N'Đang học' or t.trangthai = N'Đã học')

USE QuanLyHocTap;
GO

select * from KyNang

select * from BaiTap
select * from BaiHoc
select * from CauHoi



SELECT bt.ID as ID_BaiTap, bt.TenBaiTap, COUNT(ch.ID) as SoLuongCauHoi
FROM BaiTap bt
LEFT JOIN CauHoi ch ON bt.ID = ch.IDBaiTap
GROUP BY bt.ID, bt.TenBaiTap
ORDER BY bt.ID;