# POSTMAN TEST DATA - Submit Bài Làm

## Endpoint
```
POST http://localhost:8080/lichsu/submit
```

## Headers
```
Content-Type: application/json
Authorization: Bearer {YOUR_JWT_TOKEN}
```

## Request Body - Ví dụ 1: Test 4 câu hỏi (Multiple Choice)

```json
{
  "idTest": 1,
  "idBaiTap": null,
  "tenBai": "English Fundamentals Test",
  "loaiBai": "TEST",
  "tgianLam": 300,
  "cauTraLoi": [
    {
      "idCauHoi": 1,
      "userAns": "A",
      "correctAns": "A"
    },
    {
      "idCauHoi": 2,
      "userAns": "B",
      "correctAns": "C"
    },
    {
      "idCauHoi": 3,
      "userAns": "D",
      "correctAns": "D"
    },
    {
      "idCauHoi": 4,
      "userAns": "A",
      "correctAns": "A"
    }
  ]
}
```

**Kết quả dự kiến:** 
- Số câu đúng: 3/4 
- Điểm: 7.5/10

---

## Request Body - Ví dụ 2: Bài Tập 5 câu hỏi

```json
{
  "idTest": null,
  "idBaiTap": 5,
  "tenBai": "Vocabulary Exercise - Unit 2",
  "loaiBai": "BAITAP",
  "tgianLam": 600,
  "cauTraLoi": [
    {
      "idCauHoi": 10,
      "userAns": "beautiful",
      "correctAns": "beautiful"
    },
    {
      "idCauHoi": 11,
      "userAns": "happy",
      "correctAns": "happy"
    },
    {
      "idCauHoi": 12,
      "userAns": "run",
      "correctAns": "run"
    },
    {
      "idCauHoi": 13,
      "userAns": "eat",
      "correctAns": "eat"
    },
    {
      "idCauHoi": 14,
      "userAns": "walking",
      "correctAns": "walking"
    }
  ]
}
```

**Kết quả dự kiến:**
- Số câu đúng: 5/5
- Điểm: 10/10

---

## Request Body - Ví dụ 3: Test Listening (String dài)

```json
{
  "idTest": 2,
  "idBaiTap": null,
  "tenBai": "TOEIC Listening Test",
  "loaiBai": "TEST",
  "tgianLam": 900,
  "cauTraLoi": [
    {
      "idCauHoi": 20,
      "userAns": "The meeting is scheduled for tomorrow at 3 PM",
      "correctAns": "The meeting is scheduled for tomorrow at 3 PM"
    },
    {
      "idCauHoi": 21,
      "userAns": "She works in the marketing department",
      "correctAns": "She works in the marketing department"
    },
    {
      "idCauHoi": 22,
      "userAns": "They are planning to launch a new product",
      "correctAns": "They are planning to launch a new product"
    },
    {
      "idCauHoi": 23,
      "userAns": "I think we should postpone the decision",
      "correctAns": "I think we should cancel the decision"
    },
    {
      "idCauHoi": 24,
      "userAns": "The project deadline is next week",
      "correctAns": "The project deadline is next week"
    }
  ]
}
```

**Kết quả dự kiến:**
- Số câu đúng: 4/5
- Điểm: 8.0/10

---

## Cách Test trên Postman

### Bước 1: Tạo Request
1. Mở Postman
2. Tạo NEW Request
3. Chọn **POST** method
4. Nhập URL: `http://localhost:8080/lichsu/submit`

### Bước 2: Set Headers
- Vào tab **Headers**
- Thêm:
  - Key: `Content-Type` | Value: `application/json`
  - Key: `Authorization` | Value: `Bearer <YOUR_JWT_TOKEN>`

### Bước 3: Set Body
1. Vào tab **Body**
2. Chọn **raw**
3. Chọn **JSON** từ dropdown
4. Paste một trong các ví dụ trên
5. Click **Send**

### Bước 4: Kiểm Tra Response
Bạn sẽ nhận được response như sau:

```json
{
    "id": 1,
    "idNguoiDung": 101,
    "idTest": 1,
    "idBaiTap": null,
    "tenBai": "English Fundamentals Test",
    "loaiBai": "TEST",
    "diemSo": 7.50,
    "trangThai": "COMPLETED",
    "tgianNopBai": "2025-12-25T10:30:45.123456",
    "tgianLam": 300
}
```

---

## Request Body - Ví dụ 4: Test với Case Sensitive

```json
{
  "idTest": 3,
  "idBaiTap": null,
  "tenBai": "Grammar Test",
  "loaiBai": "TEST",
  "tgianLam": 450,
  "cauTraLoi": [
    {
      "idCauHoi": 30,
      "userAns": "is",
      "correctAns": "is"
    },
    {
      "idCauHoi": 31,
      "userAns": "Are",
      "correctAns": "are"
    },
    {
      "idCauHoi": 32,
      "userAns": "was",
      "correctAns": "was"
    },
    {
      "idCauHoi": 33,
      "userAns": "WERE",
      "correctAns": "were"
    }
  ]
}
```

**Lưu ý:** So sánh sử dụng `equalsIgnoreCase()` nên case không quan trọng

**Kết quả dự kiến:**
- Số câu đúng: 4/4 (vì dùng equalsIgnoreCase)
- Điểm: 10/10

---

## GET Endpoint - Xem Lịch Sử Bài Làm

```
GET http://localhost:8080/lichsu/user
```

**Headers:**
```
Authorization: Bearer {YOUR_JWT_TOKEN}
```

**Response mẫu:**
```json
[
    {
        "id": 1,
        "idNguoiDung": 101,
        "idTest": 1,
        "idBaiTap": null,
        "tenBai": "English Fundamentals Test",
        "loaiBai": "TEST",
        "diemSo": 7.50,
        "trangThai": "COMPLETED",
        "tgianNopBai": "2025-12-25T10:30:45.123456",
        "tgianLam": 300
    },
    {
        "id": 2,
        "idNguoiDung": 101,
        "idTest": null,
        "idBaiTap": 5,
        "tenBai": "Vocabulary Exercise - Unit 2",
        "loaiBai": "BAITAP",
        "diemSo": 10.00,
        "trangThai": "COMPLETED",
        "tgianNopBai": "2025-12-25T11:15:30.654321",
        "tgianLam": 600
    }
]
```

---

## GET Endpoint - Xem Chi Tiết Bài Làm

```
GET http://localhost:8080/bai-lam/lich-su/{lichSuId}
```

**Ví dụ:**
```
GET http://localhost:8080/bai-lam/lich-su/1
```

**Response mẫu:**
```json
[
    {
        "cauHoiId": 1,
        "noiDung": "What is the capital of France?",
        "dapAnNguoiDung": "A",
        "dapAnDung": "{\"A\":\"Paris\", \"B\":\"London\", \"C\":\"Berlin\", \"D\":\"Madrid\", \"Correct\":\"A\"}",
        "dungSai": true,
        "giaiThich": "Paris là thủ đô của nước Pháp"
    },
    {
        "cauHoiId": 2,
        "noiDung": "Which country is known for its sushi?",
        "dapAnNguoiDung": "B",
        "dapAnDung": "{\"A\":\"Thailand\", \"B\":\"China\", \"C\":\"Japan\", \"D\":\"Korea\", \"Correct\":\"C\"}",
        "dungSai": false,
        "giaiThich": "Japan nổi tiếng với sushi"
    }
]
```

---

## Lưu Ý Quan Trọng

1. **Authentication:** Bạn cần có JWT Token hợp lệ từ endpoint login
2. **User ID:** Lấy từ `authentication.getName()` - phải là Integer hợp lệ
3. **Điểm số:** Tính theo công thức: `(số câu đúng × 10) / tổng số câu`
4. **Case Insensitive:** So sánh sử dụng `equalsIgnoreCase()`
5. **Thời gian:** `tgianLam` tính bằng giây (seconds)

---

## Cách Lấy JWT Token

Nếu chưa có JWT Token, thực hiện login trước:

```
POST http://localhost:8080/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "password123"
}
```

Response sẽ trả về JWT Token, copy token đó và sử dụng trong headers.

