# Asset Management System

A Spring Boot REST API for managing organizational assets, employees, assignments, and maintenance logs. Includes JWT authentication, Swagger UI, Docker support, and a Jenkins CI/CD pipeline with Kubernetes deployment.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Backend | Java 17, Spring Boot 3.2.5 |
| Security | Spring Security, JWT (jjwt 0.11.5) |
| Database | MySQL 8.0 |
| ORM | Spring Data JPA / Hibernate |
| API Docs | SpringDoc OpenAPI (Swagger UI) |
| Build | Maven 3.x |
| Containerization | Docker, Docker Compose |
| Orchestration | Kubernetes |
| CI/CD | Jenkins |

---

## Project Structure

```
assetmanagementsystem/
├── src/main/java/com/wip/assetmanagementsystem/
│   ├── controller/       # REST controllers
│   ├── service/          # Business logic (interface + impl)
│   ├── repository/       # Spring Data JPA repositories
│   ├── entity/           # JPA entities
│   ├── dto/              # Data Transfer Objects
│   ├── security/         # JWT filter, util, security config
│   ├── exception/        # Global exception handler
│   ├── config/           # App configuration
│   └── util/             # Helper utilities
├── src/main/resources/
│   ├── application.properties
│   └── static/           # Frontend HTML/JS files
├── k8s/                  # Kubernetes manifests
├── Dockerfile
├── docker-compose.yml
└── JenkinsFile
```

---

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/auth/login` | Login and get JWT token |
| POST | `/api/auth/register` | Register new user account |

### Assets
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/assets` | Get all assets |
| GET | `/api/assets/{id}` | Get asset by ID |
| POST | `/api/assets` | Create new asset |
| PUT | `/api/assets/{id}` | Update asset |
| DELETE | `/api/assets/{id}` | Delete asset |

### Employees
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/employees` | Get all employees |
| GET | `/api/employees/{id}` | Get employee by ID |
| POST | `/api/employees` | Add employee |
| PUT | `/api/employees/{id}` | Update employee |
| DELETE | `/api/employees/{id}` | Delete employee |

### Asset Assignments
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/assignments` | Get all assignments |
| POST | `/api/assignments` | Assign asset to employee |
| PUT | `/api/assignments/{id}` | Update assignment |
| DELETE | `/api/assignments/{id}` | Remove assignment |

### Categories
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/categories` | Get all categories |
| POST | `/api/categories` | Create category |
| PUT | `/api/categories/{id}` | Update category |
| DELETE | `/api/categories/{id}` | Delete category |

### Maintenance Logs
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/maintenance` | Get all logs |
| POST | `/api/maintenance` | Create maintenance log |
| PUT | `/api/maintenance/{id}` | Update log |
| DELETE | `/api/maintenance/{id}` | Delete log |

### Actuator (Health & Metrics)
| Endpoint | Description |
|----------|-------------|
| `/actuator/health` | Application health status |
| `/actuator/info` | Application info |
| `/actuator/metrics` | Metrics data |

---

## Running Locally

### Prerequisites
- Java 17
- Maven 3.x
- MySQL 8.0

### Setup

**1. Create MySQL database**
```sql
CREATE DATABASE assetdb;
CREATE USER 'assetuser'@'localhost' IDENTIFIED BY 'strongpassword';
GRANT ALL PRIVILEGES ON assetdb.* TO 'assetuser'@'localhost';
```

**2. Configure `application.properties`**
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/assetdb?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true
spring.datasource.username=assetuser
spring.datasource.password=strongpassword
server.port=8085
```

**3. Build and run**
```bash
mvn clean install
mvn spring-boot:run
```

App will be available at: `http://localhost:8085`  
Swagger UI: `http://localhost:8085/swagger-ui/index.html`

---

## Running with Docker

### Using Docker Compose
```bash
docker-compose up --build
```
This starts the app on `http://localhost:8085` and connects to MySQL on the host machine.

### Using Docker manually
```bash
# Build image
docker build -t assetmanagementsystem:latest .

# Run container
docker run -p 8085:8085 \
  -e SPRING_DATASOURCE_URL=jdbc:mysql://host.docker.internal:3306/assetdb \
  -e SPRING_DATASOURCE_USERNAME=assetuser \
  -e SPRING_DATASOURCE_PASSWORD=strongpassword \
  assetmanagementsystem:latest
```

---

## Kubernetes Deployment

Apply manifests in this order:

```bash
# 1. Create secrets (DB credentials)
kubectl apply -f k8s/mysql-secret.yaml

# 2. Create config map (DB URL, server port)
kubectl apply -f k8s/app-configmap.yaml

# 3. Deploy MySQL
kubectl apply -f k8s/mysql-deployment.yaml

# 4. Deploy application (waits for MySQL automatically)
kubectl apply -f k8s/app-deployment.yaml
```

App will be accessible at: `http://localhost:30084`

> **Note:** `imagePullPolicy: Never` is set in the deployment, so build the Docker image locally before deploying to Kubernetes.

### Check deployment status
```bash
kubectl get pods
kubectl get services
kubectl logs deployment/assetmanagementsystem-deployment
```

---

## CI/CD Pipeline (Jenkins)

### Jenkins Setup

**1. Install required plugins**
- Git Plugin
- Pipeline Plugin
- Docker Pipeline Plugin

**2. Configure Global Tools** (`Manage Jenkins → Tools`)

| Tool | Name (exact) |
|------|-------------|
| JDK | `Java 17` |
| Maven | `Maven 3.x` |

**3. Create Pipeline Job**
- New Item → Pipeline
- Definition: `Pipeline script from SCM`
- SCM: Git
- Repository URL: `https://github.com/shreya8003/assetmanagement.git`
- Branch: `*/main`
- Script Path: `JenkinsFile`

### Pipeline Stages

```
Checkout → Build → Test → Package → Docker Build
```

| Stage | Command | Description |
|-------|---------|-------------|
| Checkout | `checkout scm` | Pull latest code from Git |
| Build | `mvn clean compile` | Compile source code |
| Test | `mvn test` | Run unit tests |
| Package | `mvn package -DskipTests` | Create .jar file |
| Docker Build | `docker build` | Build Docker image |

---

## Authentication Flow

1. Register: `POST /api/auth/register` with username, password, and email
2. Login: `POST /api/auth/login` → returns JWT token
3. Use token in all subsequent requests:
   ```
   Authorization: Bearer <your-jwt-token>
   ```

**Roles:**
- `ADMIN` — full access (default admin user)
- `USER` — standard employee access

---

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `SPRING_DATASOURCE_URL` | MySQL connection URL | `jdbc:mysql://localhost:3306/assetdb` |
| `SPRING_DATASOURCE_USERNAME` | DB username | `assetuser` |
| `SPRING_DATASOURCE_PASSWORD` | DB password | `strongpassword` |
| `SERVER_PORT` | Application port | `8085` |
