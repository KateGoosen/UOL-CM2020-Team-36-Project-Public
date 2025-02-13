
# 🚀 Project Setup

## **1. Install Docker Desktop**
If you haven't installed Docker yet, download and install it from the official website:

🔗 [Download Docker Desktop](https://www.docker.com/get-started/)

## **2. Start Docker**
- Open **Docker Desktop** and ensure it is running before proceeding.

## **3. Run the Application**
Once Docker is running, navigate to the project directory (/backend) and start the application using:

```sh
docker-compose up -d
```

- The `-d` flag runs the containers in **detached mode** (background).
- To check running containers:
  ```sh
  docker ps
  ```

## **4. Open API Documentation (Swagger UI)**
After starting the application, you can access the **Swagger API documentation** in your browser:

🔗 **[Swagger UI](http://localhost:8080/swagger-ui/index.html)**  
or manually open:
```
http://localhost:8080/swagger-ui/index.html
```

- To check the raw OpenAPI JSON:
  ```
  http://localhost:8080/v3/api-docs
  ```

## **5. Stopping the Application**
To stop and remove the running containers:

```sh
docker-compose down
```

## **6. Additional Commands**
- Rebuild the containers if necessary:
  ```sh
  docker-compose up --build -d
  ```
- View logs:
  ```sh
  docker-compose logs -f
  ```

---

