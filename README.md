# 🏫 School Result Management System

A full-stack web application built with **Spring Boot** and **MySQL** that allows schools to manage student results, grades and marksheets digitally with role-based access control.

---

## ✨ Features

- **Role-based login** — separate dashboards for Admin, Teacher and Student
- **Admin** can manage students, teachers, classes and subjects
- **Teacher** can enter marks — grades are calculated automatically
- **Student** can view results and download PDF marksheet
- **PDF Marksheet** generation using iText 7
- **Spring Security** with BCrypt password hashing and CSRF protection

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3 |
| Security | Spring Security, BCrypt |
| Database | MySQL 8, Spring Data JPA |
| Frontend | Thymeleaf, HTML, CSS, Bootstrap 5 |
| PDF | iText 7 |
| Build | Maven |

---

## ⚙️ Getting Started

**Step 1 — Clone the repository**

```bash
git clone https://github.com/Hackergeu/result-management.git
cd result-management
```

**Step 2 — Configure the database**

Open `src/main/resources/application.properties` and update:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/school_db?createDatabaseIfNotExist=true
spring.datasource.username=root
spring.datasource.password=your_mysql_password
```

**Step 3 — Run the application**

```bash
mvn spring-boot:run
```

**Step 4 — Open in browser**

```
http://localhost:8080/login
```

---

## 🔑 Default Login Credentials

Admin account is created automatically on first startup.

| Role | Email                     | Password     |
|---|---------------------------|--------------|
| Admin | vanshagarwal953@gmail.com | HackerGeu    |
| Teacher | Created by Admin          | Set by Admin |
| Student | Created by Admin          | Set by Admin |

---

## 📊 Grade Calculation Logic

| Percentage | Grade |
|---|---|
| 90% and above | A+ |
| 80% — 89% | A |
| 70% — 79% | B |
| 60% — 69% | C |
| 50% — 59% | D |
| Below 50% | F |

---

## 🔒 Security

- Passwords hashed with **BCrypt** — never stored as plain text
- Role-based URL protection — `/admin/**`, `/teacher/**`, `/student/**`
- CSRF tokens on every form
- Unauthorized access redirects to access-denied page

---

## 🚀 Future Improvements

- [ ] Timetable management for teachers
- [ ] Docker support
- [ ] Attendance tracking
- [ ] Email notifications for results

---

## 👨‍💻 Author

**Vansh Agarwal**
- GitHub: [Hackergeu](https://github.com/Hackergeu)

- Email: vanshagarwal953@gmail.com
