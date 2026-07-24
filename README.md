# 🏫 School Result Management System

A full-stack web application built with **Spring Boot** and **MySQL** that allows schools to manage student results, grades and marksheets digitally with role-based access control and a professional dark-themed UI.

---

## ✨ Features

- **Animated role-selection login page** — Admin, Teacher and Student each with their own card
- **Role-based login** — separate dark-themed dashboards for each role
- **Admin** can manage students, teachers, classes and subjects with a dark navy dashboard and Chart.js bar chart
- **Teacher** can enter marks — grades are calculated automatically, subject cards with dynamic icons
- **Student** can view results with percentage stats and download PDF marksheet
- **PDF Marksheet** generation using iText 7 — color coded grades and pass/fail
- **Spring Security** with BCrypt password hashing and CSRF protection

---

## 🎨 UI Design

Each role has its own professional dark theme with a distinct accent color:

| Role | Theme | Accent Color |
|---|---|---|
| Admin | Dark Navy | Blue |
| Teacher | Dark Green | Emerald |
| Student | Dark Purple | Violet |

All portals share the same design language — fixed sidebar, top navbar with profile, stat cards, dark tables and welcome banners.

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3 |
| Security | Spring Security, BCrypt |
| Database | MySQL 8, Spring Data JPA |
| Frontend | Thymeleaf, HTML, CSS, Bootstrap 5 |
| Charts | Chart.js |
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

Create a `.env` file in the project root:

```env
DB_URL=jdbc:mysql://localhost:3306/school_db?createDatabaseIfNotExist=true
DB_USERNAME=root
DB_PASSWORD=your_mysql_password
```

`application.properties` reads these via `${DB_URL}`, `${DB_USERNAME}` and `${DB_PASSWORD}` (loaded using `dotenv-java`), so no credentials are hardcoded in the source.

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

Admin account is created automatically on first startup by `DataInitializer.java`.

| Role | Email | Password |
|---|---|---|
| Admin | vanshagarwal953@gmail.com | HackerGeu |
| Teacher | Created by Admin | Set by Admin |
| Student | Created by Admin | Set by Admin |

---

## 📊 Grade Calculation Logic

Calculated automatically in `ResultService.java` when marks are saved:

| Percentage | Grade |
|---|---|
| 90% and above | A+ |
| 80% — 89% | A |
| 70% — 79% | B |
| 60% — 69% | C |
| 50% — 59% | D |
| Below 50% | F |

---

## 🗃️ Database Schema

```
users        — id, name, email, password, role
students     — id, user_id (FK), classroom_id (FK), rollNumber, phone
teachers     — id, user_id (FK), employeeCode, department
classrooms   — id, name, section, academicYear
subjects     — id, name, maxMarks, passingMarks, teacher_id (FK), classroom_id (FK)
results      — id, student_id (FK), subject_id (FK), marksObtained, grade, isPassed
```

Tables are auto-created by Hibernate on first run — no SQL scripts needed.

---

## 🔒 Security

- Passwords hashed with **BCrypt** — never stored as plain text
- Role-based URL protection — `/admin/**`, `/teacher/**`, `/student/**`
- CSRF protection via `CookieCsrfTokenRepository` with `XorCsrfTokenRequestAttributeHandler` — a cookie-based CSRF token setup (rather than the session-based default), so it's ready to also serve a future REST/mobile client
- Unauthorized access redirects to access-denied page
- Each role sees only its own data

---

## 🚀 Future Improvements

- [ ] Timetable management for teachers
- [ ] Docker support
- [ ] Attendance tracking
- [ ] Email notifications for results
- [ ] REST API layer for mobile app integration

---

## 👨‍💻 Author

**Vansh Agarwal**
- GitHub: [Hackergeu](https://github.com/Hackergeu)
- Email: vanshagarwal953@gmail.com
