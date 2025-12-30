---
name: "✨ 기능 개발"
about: "새로운 기능 개발 이슈를 등록할 때 사용하세요"
title: "[FEAT] "
labels: ["feature"]
assignees: []
---

## 📌 작업 개요
- 

---

## 📝 작업 내용
- **슈퍼관리자 자동 생성**
  - `DefaultDataInitializer.insertSuperAdmin`
  - `admins` 테이블에 `is_super_admin = true` 계정이 없을 경우 기본 계정(`admin/admin123`) 생성
  - 비밀번호는 `PasswordEncoder`로 암호화 후 저장

- **기타**
  - `ApplicationRunner`를 구현한 `DefaultDataInitializer`로 애플리케이션 시작 시 자동 실행

---

## 📎 관련 브랜치
- feat/data-initializer

---

## 📎 관련 이슈
- clabi-lab/kea-admin-backend#8

---

## ✅ 예상 결과
- 