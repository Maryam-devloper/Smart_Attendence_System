# Smart Attendance Management System
Smart Attendance Management System is a desktop-based academic management application developed using Java Swing GUI, NetBeans IDE, and MySQL Database. The system provides separate portals for teachers and students, enabling efficient management of attendance, assignments, announcements, course materials, and student queries.
The application aims to digitize classroom administration and improve communication between teachers and students.
## Features
### User Authentication
- Student Registration and Login
- Teacher Registration and Login
- Secure authentication using MySQL database

<img width="986" height="718" alt="sas2" src="https://github.com/user-attachments/assets/f69b3875-a915-4641-a899-1fcc83ce889b" />


<img width="982" height="711" alt="sas" src="https://github.com/user-attachments/assets/da41a511-6d8d-409b-8fac-1d42336e8e4c" />


<img width="1478" height="733" alt="sas5" src="https://github.com/user-attachments/assets/80c12673-f565-4956-94ac-896c271b5b1b" />

### Teacher Dashboard
- Manage student attendance
- Create and manage assignments
- Approve or reject assignment submissions
- Answer student queries
- Upload lecture notes
- Manage announcements
- View student requests
- Manage courses and academic resources

<img width="1361" height="880" alt="sas4" src="https://github.com/user-attachments/assets/e9122d48-2b33-4da9-a0f6-c6acf1fa5349" />

### Student Dashboard
- View attendance records
- Submit assignments
- Track assignment status
- View announcements
- Access lecture notes
- Access library resources
- Submit academic queries
- View responses from teachers
- Send requests to teachers

<img width="1485" height="861" alt="sas3" src="https://github.com/user-attachments/assets/bbd8f56c-12d4-4067-b882-64631e3e01fa" />

## Technologies Used
- Java
- Java Swing (GUI)
- NetBeans IDE
- MySQL Database
- JDBC (MySQL Connector)
### Additional Features
- Role-Based Authentication
- MySQL Database Integration
- GUI-Based Interface
- Attendance Summary
- Online Learning Resources
- Data Persistence
- User-Friendly Design
## Database Tables
The system utilizes multiple MySQL tables including:
- users
- attendance
- assignments
- announcements
- course_outline
- leave_requests
- lecture_notes
- library_resources
- student_queries
- miscellaneous
- guidelines
## Project Structure
Smart_Attendence_System
├── DataBase/
│   └── smart_attendance.sql
├── release/
│   └── Smart_Attendence_System.jar
├── src/
├── nbproject/
├── README.md
├── build.xml
└── manifest.mf

## Installation
### Requirements
Before running the project, install:
- Java JDK 8 or later
- NetBeans IDE
- MySQL Server
- MySQL Workbench
- MySQL Connector/J
## Database Setup
### Step 1: Create Database
Open MySQL Workbench and execute:  sql
CREATE DATABASE smart_attendance;
### Step 2: Import Database
1. Open MySQL Workbench.
2. Go to Server → Data Import.
3. Select "Import from Self-Contained File".
4. Browse and select:
DataBase/smart_attendance.sql
5. Click Start Import.
## Configure Database Connection
Update your database credentials in the Java source code:
```java
String url = "jdbc:mysql://localhost:3306/smart_attendance";
String user = "root";
String password = "your_password";
```
Replace:
- your_password with your MySQL password.
## Running the Project
### Option 1: Using NetBeans
1. Open NetBeans IDE.
2. Open the project folder.
3. Clean and Build Project.
4. Run Project.
### Option 2: Using JAR File
Navigate to:
release/
Run:
```bash
java -jar Smart_Attendence_System.jar
```
or simply double-click the JAR file.
## All Setup 
### 1. Clone the Repository
```bash
git clone https://github.com/Maryam-devloper/Smart_Attendence_System.git
```
### 2. Create a MySQL Database
### 3. Import the Provided SQL File
### 4. Configure Database Credentials in the Connection Class.
      String url = "jdbc:mysql://localhost:3306/smart_attendance";
      String username = "root";
      String password = "your_password";
### 5. Add MySQL Connector/J library to NetBeans.
### 6. Run the Project

### Future Enhancements
- Email Notifications
- Password Recovery
- Online Attendance Marking
- Cloud Database Integration
- Mobile Application
- Analytics Dashboard
- QR Code Attendance
## Author
Developed by Maryam
GitHub Repository:
https://github.com/Maryam-devloper/Smart_Attendence_System
