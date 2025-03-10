# (Team 36) Agile Software Projects Final Project

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

# ⚛️ React + TypeScript + Vite Setup

This project includes a minimal setup to get **React working with Vite**, along with Hot Module Replacement (HMR) and some **ESLint rules**.

## **1. Available Plugins**
Currently, two official plugins are available:
- `@vitejs/plugin-react` → Uses **Babel** for Fast Refresh.
- `@vitejs/plugin-react-swc` → Uses **SWC** for Fast Refresh.

## **2. Expanding the ESLint Configuration**
If you are developing a production application, we recommend updating the ESLint configuration to enable **type-aware lint rules**.

### **Configure the Top-Level `parserOptions` Property**
Update your ESLint configuration as follows:

```ts
export default tseslint.config({
  languageOptions: {
    parserOptions: {
      project: ['./tsconfig.node.json', './tsconfig.app.json'],
      tsconfigRootDir: import.meta.dirname,
    },
  },
})
```

### **Enable Type-Checked Rules**
Replace:
```ts
tseslint.configs.recommended
```
with:
```ts
tseslint.configs.recommendedTypeChecked
```
Or for stricter rules:
```ts
tseslint.configs.strictTypeChecked
```
Additionally, you may add:
```ts
...tseslint.configs.stylisticTypeChecked
```

### **Install and Configure `eslint-plugin-react`**
Ensure the React plugin is installed and update your ESLint configuration:

```ts
// eslint.config.js
import react from 'eslint-plugin-react'

export default tseslint.config({
  settings: { react: { version: '18.3' } },
  plugins: {
    react,
  },
  rules: {
    ...react.configs.recommended.rules,
    ...react.configs['jsx-runtime'].rules,
  },
})
```

This configuration helps enforce best practices and maintainability for React projects using TypeScript.

---


