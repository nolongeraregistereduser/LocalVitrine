# 🏪 LocalVitrine

> **An AI-powered Landing Page Builder SaaS for Local Businesses.**

## 📖 Overview

**LocalVitrine** is a full-stack SaaS platform designed to solve a critical pain point for small and medium-sized enterprises (SMEs): establishing a professional online presence quickly and without coding expertise. 

The platform enables local businesses to create, customize, and publish conversion-focused landing pages using a combination of pre-built templates, a drag-and-drop visual editor, and AI-assisted content generation.

## ✨ Key Features & Business Value

* **Visual No-Code Editor:** Integrated GrapesJS to provide an intuitive drag-and-drop page building experience.
* **AI Content Generation:** Helps businesses overcome writer's block by automatically generating tailored, conversion-focused copy.
* **Full Publishing Workflow:** Seamless journey from business profile setup to template selection, editing, and live publication.
* **Public Rendering via SEO Slugs:** Published projects are instantly accessible via unique, SEO-friendly URLs (`/p/:slug`).
* **Role-Based Governance:** Distinct environments and dashboards for Users (SMEs) and Admins (Platform Management).
* **Production-Ready Foundations:** Robust error handling, data validation, integration testing, and full Docker containerization.

## 💻 Technical Stack

| Category | Technology |
| :--- | :--- |
| **Frontend** | Angular 17 (Standalone Components, Lazy Loading), TypeScript, SCSS |
| **State & Logic** | RxJS, GrapesJS (Visual Editor Integration) |
| **Backend** | Java 17, Spring Boot 3 (Web, Security, Data JPA, Validation) |
| **Security** | JWT Stateless Authentication, Role-Based Access Control (RBAC) |
| **Database** | MySQL (Production), H2 (Local/Test Profile) |
| **Infrastructure / DevOps** | Docker, Docker Compose, Nginx Reverse Proxy |
| **Testing** | Spring Boot Integration Tests, MockMvc, JUnit 5 |

## 🏗️ Architecture Highlights

### Backend Layering & Security
The backend enforces a strict, clean separation of concerns: `Controller → Service → Repository → Entity`. Security is handled statelessly via **JWT**, with clear role-based authorization ensuring protected admin routes and isolated user data.

### Platform Lifecycle & Seeding
The application initializes with automated seeders/bootstrap processes to instantly provision roles, a default admin account, and a library of starter templates upon first deployment. 

### Admin Module
A dedicated admin domain manages the lifecycle of landing page templates, oversees user management, and provides dashboard statistics on platform usage.

## 🚀 Getting Started

The entire application stack (Frontend, Backend, Database, and Nginx proxy) is containerized for easy, reproducible local environments.

### Prerequisites
* Docker & Docker Compose

### Running the Application

1. Clone the repository:
```bash
   git clone [https://github.com/yourusername/LocalVitrine.git](https://github.com/yourusername/LocalVitrine.git)
   cd LocalVitrine
