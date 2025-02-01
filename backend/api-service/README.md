# 📌 API Service - Spring Boot Application

This is a **Spring Boot-based API service** for managing meetings and participants. It integrates with **RabbitMQ** for messaging and **PostgreSQL** for database storage.

---

## 🚀 **Getting Started**

### **1. Prerequisites**
Ensure you have the following installed:
- **Java 23** ([Download](https://www.oracle.com/fr/java/technologies/downloads/))
- **Gradle 8+** ([Download](https://gradle.org/install/))
- **Docker** ([Download](https://www.docker.com/get-started))
- **Git** ([Download](https://git-scm.com/downloads))

---

## 🛠 **Building the Application**
Clone the repository:
```sh
git clone https://github.com/tyirvine/UOL-CM2020-Team-36-Project.git
cd UOL-CM2020-Team-36-Project/backend/api-service
```
Build the application:
```sh 
./gradlew clean build 
```
## 🚀 **Running the Application**
```sh 
./gradlew bootRun
```
docker-compose up --build