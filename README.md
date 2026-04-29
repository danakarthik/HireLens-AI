
# 🚀 HireLens-AI – FAANG-Level ATS Simulation & Resume Optimization Engine

> A production-ready GenAI system that simulates real ATS (Applicant Tracking Systems) used by FAANG companies to evaluate resumes, identify skill gaps, and optimize profiles for 90%+ match scores.

---

## 🔥 Overview

ATSensei is a **backend-focused AI system** built using **Spring Boot + Spring AI + RAG + Vector Database (MariaDB)** that:

* Simulates **real ATS filtering logic**
* Evaluates resumes against job descriptions
* Detects **missing skills & keyword gaps**
* Provides **actionable optimization suggestions**
* Generates a roadmap to achieve **90%+ ATS score**

---

## 🧠 Key Features

### ✅ FAANG-Level ATS Simulation

* Strict keyword-based filtering
* Multi-skill requirement evaluation
* Core vs Advanced skill differentiation
* Auto-rejection rules (real-world hiring behavior)

### 🔍 Skill Gap Detection Engine

* Extracts job description keywords
* Identifies:

  * Missing skills
  * Weak skills
  * Critical gaps

### 📊 Deterministic ATS Scoring

* MATCH / PARTIAL / NO MATCH logic
* Weighted scoring system
* Realistic hiring decision output

### 🛠 Resume Optimization Engine

* Suggests:

  * Keyword placement
  * Bullet rewrites
  * Project improvements

### 🎯 90%+ ATS Roadmap

* Step-by-step improvement strategy
* Honest, experience-based recommendations

---

## 🏗️ System Architecture

```text
Client (Job Description)
        ↓
Spring Boot REST API
        ↓
Spring AI ChatClient
        ↓
Custom ATS System Prompt Engine
        ↓
RAG Pipeline
  - Query Transformation
  - Multi Query Expansion
  - Context Augmentation
        ↓
Vector Store (MariaDB)
        ↓
LLM (OpenAI)
        ↓
ATS Evaluation Output
```

---

## ⚙️ Tech Stack

### Backend

* Java
* Spring Boot
* Spring AI

### AI / GenAI

* OpenAI (GPT-4.x)
* RAG (Retrieval-Augmented Generation)
* Prompt Engineering

### Database

* **MariaDB (Vector Store)**
* Vector similarity search (Cosine distance)

### Data Processing

* Apache Tika (Resume parsing)
* Token-based text splitting

### DevOps

* **Docker (Containerized deployment)**

### API Layer

* REST APIs

---

## 🐳 Docker Setup

### Dockerfile (Example)

```dockerfile
FROM openjdk:17-jdk-slim
COPY target/atsensei.jar atsensei.jar
ENTRYPOINT ["java","-jar","/atsensei.jar"]
```

---

### Run with Docker

```bash
docker build -t atsensei .
docker run -p 8081:8081 atsensei
```

---

### Optional: Docker Compose (MariaDB + App)

```yaml
version: "3.8"

services:
  mariadb:
    image: mariadb:latest
    container_name: atsensei-db
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: springai
    ports:
      - "3308:3306"

  app:
    build: .
    container_name: atsensei-app
    ports:
      - "8081:8081"
    depends_on:
      - mariadb
```

---

## 🧠 How It Works

### 1. Resume Ingestion

* Parses PDF/DOCX using Apache Tika
* Converts resume into structured text

### 2. Data Transformation

* Splits text into chunks for vector storage

### 3. Vector Storage (MariaDB)

* Stores embeddings for semantic retrieval
* Uses cosine similarity for matching

### 4. RAG Pipeline

* Enhances user query using:

  * Query rewriting
  * Multi-query expansion
  * Context augmentation

### 5. ATS Evaluation Engine

* Applies strict FAANG-level rules:

  * Keyword matching
  * Skill validation
  * Gap detection
  * Score calculation

---

## 🚀 API Usage

### Endpoint:

```http
POST /chat?q=job_description
```

### Example:

```bash
curl -X POST "http://localhost:8081/chat?q=Java Spring Boot Developer with AWS and Kafka"
```

---

## 📈 Sample Output

```text
ATS Score: 65%
Decision: Borderline

Critical Missing Skills:
- Kafka (HIGH)
- Distributed Systems (HIGH)
- AWS (MEDIUM)

Improvement Plan:
- Add AWS deployment project
- Include system design concepts
- Improve backend scalability experience
```

---

## 💡 Why This Project Stands Out

✔ Combines **GenAI + Backend Engineering**
✔ Uses **RAG + Vector DB (MariaDB)**
✔ Implements **real ATS decision logic**
✔ Containerized with **Docker**
✔ Production-style architecture

👉 This is NOT just a chatbot
👉 This is a **real-world hiring system simulation**

---

## ⚠️ Security Note

* Do NOT commit API keys to GitHub
* Use environment variables instead

---

## 📌 Future Enhancements

* Resume auto-rewriter (LLM)
* LinkedIn job scraper
* React dashboard for visualization
* Auto-apply job agent

---

## 👨‍💻 Author

**Karthik Bharatapu**

* Java + Spring Boot Developer
* GenAI Backend Engineer
* Focused on FAANG-level systems

---

## ⭐ Support

If you like this project, give it a ⭐ on GitHub!
