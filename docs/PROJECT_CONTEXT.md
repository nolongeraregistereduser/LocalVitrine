# 🚀 LocalVitrine – Full Project Context (Cursor Ready)

## 🧠 Product Overview

LocalVitrine is a SaaS platform that allows small local businesses (restaurants, salons, real estate agents, coaches, etc.) to create and publish professional landing pages easily.

The platform is designed for non-technical users and focuses on:
- Simplicity
- Speed
- Conversion (marketing-focused pages)

---

## 🎯 Main Goal

Enable a user to:
1. Create an account
2. Create a landing page project
3. Fill business information
4. Choose a template
5. Customize visually (editor)
6. Generate content using AI
7. Publish a public landing page

---

## 🏗 Architecture

Monorepo structure:

/backend → Spring Boot (REST API)
/frontend → Angular SPA

---

## ⚙️ Tech Stack

Backend:
- Spring Boot
- Spring Security + JWT
- JPA / Hibernate
- PostgreSQL

Frontend:
- Angular (standalone)
- RxJS
- HTTP Client

Other:
- GrapesJS (visual editor)
- OpenAI API (AI content)
- Docker

---

## 📊 Core Data Model

- User
- Role
- Project
- BusinessProfile
- Template
- GeneratedContent

---

## 🔐 Security

- JWT Authentication
- BCrypt password hashing
- Role-based access (USER / ADMIN)

---

## 📦 Features Scope

- Authentication
- Project CRUD
- Business profile
- Templates
- Visual editor
- AI generation
- Public landing page

---

# 🧩 DEVELOPMENT STRATEGY

We build the product EPIC by EPIC.

Each EPIC must include:
- Backend implementation
- Frontend implementation
- API connection