# Product API 사용 가이드

간단한 상품 API입니다. **업서트(등록/수정)**, **조회**, **재고 차감**을 제공합니다.  
기본 포트: `8082`

---

## 1) 상품 등록/수정 (Upsert)

**POST** `/api/products`

### 요청 본문 (JSON)
```json
{
  "productId": "PROD001",
  "name": "맥북 프로 14",
  "price": 2690000,
  "stock": 10
}
```

### 응답
- `204 No Content` (본문 없음)

### 실행 예시
```bash
curl -X POST http://localhost:8082/api/products   -H "Content-Type: application/json"   -d '{
        "productId": "PROD001",
        "name": "맥북 프로 14",
        "price": 2690000,
        "stock": 10
      }' -i
```

---

## 2) 상품 조회

**GET** `/api/products/{id}`

### 응답
- `200 OK` + 상품 JSON
- `404 Not Found` (존재하지 않음)

### 실행 예시
```bash
curl http://localhost:8082/api/products/PROD001 -i
```

### 예시 응답 (200)
```json
{
  "productId": "PROD001",
  "name": "맥북 프로 14",
  "price": 2690000,
  "stock": 10
}
```

---

## 3) 재고 차감

**POST** `/api/products/{id}/decrement?qty={정수}`

### 응답
- 성공: `200 OK`
  ```json
  { "status": "OK" }
  ```
- 재고 부족: `409 Conflict`
  ```json
  { "code": "OUT_OF_STOCK", "message": "재고 부족" }
  ```
- 잘못된 요청(음수/0 수량 등): `400 Bad Request`

### 실행 예시
```bash
# 성공
curl -X POST "http://localhost:8082/api/products/PROD001/decrement?qty=2" -i

# 재고 부족
curl -X POST "http://localhost:8082/api/products/PROD001/decrement?qty=999" -i
```
