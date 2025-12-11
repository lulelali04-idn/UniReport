# UniReport: Campus Issue Tracking System

UniReport is a native Android application designed to streamline the reporting and management of university facility issues. Whether it is a broken projector in a lab or a leaking tap in the hallway, UniReport bridges the gap between students and campus administration through a centralized, mobile-first platform.

This project was developed as a comprehensive assignment to demonstrate mobile application development using Java, XML, and Local Database Management.

---

## Team Members
* Kevin Heryanto [001202505027]
* Alfarid Ridwan [001202405018]
* Muhammad Naufal Gani [001202405016]
* Ahmad Anshori [001202405004]

## Project Overview

* Goal: Replace outdated paper complaints with a digital, real-time reporting system.
* Target Audience: Students (Reporters) and Admin Staff (Maintenance Team).
* Core Tech: Android SDK, Java, SQLite.

---

## Key Features

### 1. Role-Based Access Control (RBAC)
The app distinguishes between two types of users with different permissions:
* Student: Can register, log in, submit new reports, and view the status of ongoing issues.
* Admin: Has exclusive rights to delete reports and mark issues as "Fixed".
    * Note: Admin accounts are pre-configured for security (cannot be created via the sign-up page).

### 2. Image Evidence
* Users can attach photographic evidence to their reports.
* The app integrates with the device's Gallery using Android Activity Results API.
* Images are compressed and stored directly in the database (Base64 encoding) to ensure data portability.

### 3. Persistent Local Database
* Built on SQLite, the app functions completely offline.
* Data Persistence: Reports and User accounts remain saved even if the app is closed or the device is restarted.
* CRUD Operations: Fully implemented Create (Submit), Read (View List/Details), Update (Mark Fixed), and Delete (Admin only) functionality.

### 4. Modern Dashboard UI
* Clean, minimalist interface.
* Separation of Concerns: Distinct screens for the Dashboard Menu, Submission Form, and Report List.
* Visual Status Indicators: Color-coded indicators (Red for Active / Green for Fixed) in the list view.

---

## Technical Architecture

The app follows a standard MVC (Model-View-Controller) pattern:

| Component | Responsibility |
| :--- | :--- |
| Activities (Controllers) | Manage UI logic and user interaction (MainActivity, SubmitActivity, etc.). |
| XML Layouts (Views) | Define the user interface structure. |
| DBHelper (Model) | Manages SQLiteOpenHelper, table creation, and raw SQL queries. |
| SharedPreferences | Handles session management (keeping the user logged in). |

### Database Schema
Table: reports
| Column | Type | Description |
| :--- | :--- | :--- |
| id | INTEGER (PK) | Auto-incrementing ID |
| title | TEXT | Short title of the issue |
| description | TEXT | Full details |
| location | TEXT | Specific room or block |
| category | TEXT | e.g., Maintenance, IT, Cleaning |
| status | TEXT | "Active" or "Fixed" |
| image | TEXT | Base64 encoded image string |

---

## How to Run

1. Clone the Repository:
   git clone https://github.com/lulelali04-idn/UniReport.git

2. Open in Android Studio:
   * Select File > Open and choose the cloned folder.
   * Wait for Gradle to sync.

3. Run on Emulator:
   * Select a device (e.g., Pixel 6 API 33).
   * Click the green Run button (Shift+F10).

> Pro Tip for Testing Images: To test the image upload on the Emulator, drag and drop a .jpg or .png file from your computer onto the Emulator screen. It will appear in the "Downloads" folder, which you can select inside the app.

---

## User Guide

### 1. Authentication
* Sign Up: Create a new student account.
* Login: Access the dashboard.
    * Default Admin Credentials: Username: admin, Password: admin123.

### 2. The Dashboard
Upon login, you are greeted with a menu:
* New Report: Opens the submission form.
* Ongoing Problems: Opens the live feed of reports.
* Log Out: Securely ends the session.

### 3. Submitting a Report
1. Enter a Title and Location.
2. Select a Category (Maintenance, IT, etc.).
3. Write a brief Description.
4. Tap "Attach Photo" to upload proof.
5. Click Submit.

### 4. Admin Tools
* Mark as Fixed: Admins can open a report detail view and check the "Mark as Fixed" box. This updates the status for everyone.
* Delete Report: In the "Ongoing Problems" list, Admins can long-press any item to permanently delete it.
