# GatWay service

## Основные методы системы

### 1. Image: `image-service/api/images`
- `/{productId}` **POST**
    - Доступ разрешен: **ADMIN**
- `/{imageId}` **DELETE**
    - Доступ разрешен: **ADMIN**

### 2. Inventory: `inventory-service/api/inventory`
- `/{productId}` **PUT**
    - Доступ разрешен: **ADMIN**

### 3. Order: `order-service/api/orders`
- `/customer/{customerId}` **GET**
    - Доступ разрешен: **USER**
- `/order/{orderId}` **GET** 
    - Доступ разрешен: **USER**
- `/` **POST**
    - Доступ разрешен: **ADMIN**
- `/{orderId}` **DELETE**
    - Доступ разрешен: **ADMIN**

### 4. Product: `product-service/api/products`
- `/` **GET**
    - Доступ: **ALL**
- `/` **POST**
    - Доступ: **ADMIN**
- `/{productId}` **GET**
    - Доступ: **ALL**
- `/{productId}/price` **GET**
    - Доступ: **ALL**
- `/{productId}` **PUT**
    - Доступ: **ADMIN**
- `/{productId}` **DELETE**
    - Доступ: **ADMIN**  
