# DynamicSaltEncryption

一个基于 saltKey 如`userId` 的动态加盐加密算法库。

## 功能特性
- 基于 saltKey如`userId` 生成唯一盐值。
- 支持多种哈希算法（如 SHA-256、bcrypt）。
- 提供简单的加密和验证接口。

## 使用实例
```java
String encryptedPassword  = DynamicSaltEncryption.encryptValue("password", "userId");
boolean isValid = DynamicSaltEncryption.verifyValue("password","encryptedPassword","userId");
```
