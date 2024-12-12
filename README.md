# **Real-Time Ticketing System**

This project simulates a concurrent **ticketing system** designed to efficiently manage tickets in real-time. The system features an interactive configuration page, dynamic graph-based reporting, and log file management. It uses **React** for the front end and **Java Spring Boot** for the back end, with **SQL** as the database.

---

## **Table of Contents**
1. [Features](#features)  
2. [Technologies Used](#technologies-used)  
3. [Architecture Overview](#architecture-overview)  
---

## **Features**
1. **Dynamic Configuration**
   - Adjust total tickets, release rates, retrieval rates, and maximum capacity through a sleek UI.
2. **Real-Time Simulation**
   - Visualize ticket distribution (released, purchased, and remaining) using bar charts.
3. **Log Management**
   - Upload and view logs in the application interface for better debugging and insights.
4. **Seamless Integration**
   - Front-end and back-end communication for storing and fetching configurations. 
5. **Responsive Design**
   - Works flawlessly across devices, from desktops to mobile phones.

---

## **Technologies Used**
| Technology        | Purpose                         |
|-------------------|---------------------------------|
| **React**         | Frontend framework             |
| **Material-UI**   | UI components and styling      |
| **Java Spring Boot** | Backend server for APIs       |
| **MySQL**         | Database for configurations    |
| **Axios**         | API communication              |
| **Yup**           | Form validation                |
| **React Hook Form** | Form management library       |

---

## **Architecture Overview**
```plaintext
User Interface (React + Material-UI)
        ↓
Frontend APIs (Axios)
        ↓
Backend (Spring Boot)
        ↓
Database (SQL)
