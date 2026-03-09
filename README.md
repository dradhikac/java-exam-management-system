# Java Exam Management System

A **Java console-based examination management system** designed to automate and manage university examination processes including student records, attendance tracking, subject management, exam scheduling, and admit card generation.

---

# Features

- Student Management
- Subject Management
- Attendance Tracking
- Eligibility Checking
- Internal Assessment Management
- Faculty Assignment
- Exam Schedule Management
- Admit Card Generation (PDF)
- Authentication System
- CSV Data Import/Export

---

# Architecture

The project follows a **layered architecture**:

CLI Layer → User interaction  
Service Layer → Business logic  
DAO Layer → Database access  
Model Layer → Entity classes  

```
CLI → Service → DAO → Database
```

---

# Tech Stack

Language:
- Java

Database:
- MySQL

Libraries:
- JDBC
- OpenPDF
- Apache PDFBox

Architecture:
- Layered Architecture
- DAO Pattern

---

# Project Structure

```
src/
 ├── auth        # Authentication logic
 ├── cli         # Command line interface
 ├── dao         # Database access objects
 ├── model       # Entity classes
 ├── service     # Business logic
 ├── util        # Helper utilities
 ├── seed        # Default data loaders
 └── Main.java   # Application entry point
```

---

# How to Run

### 1 Install Java

Java 8 or above required.

Check version:

```
java -version
```

---

### 2 Setup MySQL

Create database:

```
CREATE DATABASE exam_system;
```

Update DB credentials in:

```
DBConnectionManager.java
```

---

### 3 Compile Project

```
javac -cp ".;lib/mysql-connector-j.jar" src/Main.java
```

---

### 4 Run Application

```
java Main
```

---

# Sample Functionalities

- Add new students
- Assign subjects
- Track attendance
- Check exam eligibility
- Generate admit cards
- Assign faculty to subjects
- Manage exam schedules

---



---

# Future Improvements

- Web Interface
- REST API
- Role-based authentication
- Email notifications
- Docker deployment

---

# Author

Radhika D Chougale

---

⭐ If you found this project useful, consider giving it a star.
