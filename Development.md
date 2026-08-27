# Ejecución con Docker

La forma recomendada de ejecutar el proyecto es mediante Docker Compose.

## Requisitos

Únicamente es necesario tener instalado:

- Docker Desktop
- Git

No es necesario instalar manualmente:

- Java.
- Maven.
- Node.js.
- Nginx.
- MySQL.

---

## 1. Clonar el repositorio

```bash
git clone <https://github.com/jtorres-18/mobilia-technical-test.git>
```

Entrar al proyecto:

```bash
cd mobilia-technical-test
```

---

## 2. Configurar variables de entorno

El repositorio incluye:

```text
.env.example
```

Crear una copia llamada:

```text
.env
```

En PowerShell:

```powershell
Copy-Item .env.example .env
```

En Linux/macOS:

```bash
cp .env.example .env
```

Configuración por defecto:

```env
MYSQL_DATABASE=mobilia_db
MYSQL_USER=mobilia
MYSQL_PASSWORD=mobilia_dev
MYSQL_ROOT_PASSWORD=root_dev
```

> El archivo `.env` no se versiona en Git.

---

## 3. Levantar la aplicación

Desde la raíz del proyecto:

```bash
docker compose up --build
```

Docker levantará automáticamente:

```text
MySQL
  ↓
Spring Boot
  ↓
React + Nginx
```

MySQL incluye un healthcheck y el backend espera a que la base de datos esté disponible antes de iniciar.

Flyway ejecutará automáticamente las migraciones necesarias.

---

## URLs

### Frontend

```text
http://localhost:5173
```

### Backend

```text
http://localhost:8080
```

### MySQL

```text
localhost:3306
```

---

## Detener la aplicación

```bash
docker compose down
```

Para detenerla y eliminar también el volumen de MySQL:

```bash
docker compose down -v
```

---
# Ejecución manual del backend

Si no se desea utilizar Docker para el backend, se requiere:

- Java 21.
- MySQL 8.
- Maven Wrapper incluido en el proyecto.

Primero debe existir una instancia MySQL accesible en:

```text
localhost:3306
```

Desde:

```text
backend
```

en Windows:

```powershell
.\mvnw.cmd clean verify
```

Para ejecutar Spring Boot:

```powershell
.\mvnw.cmd -pl applications/app-service -am spring-boot:run
```

Las propiedades admiten configuración mediante variables de entorno.

Ejemplo:

```properties
spring.r2dbc.url=${SPRING_R2DBC_URL:r2dbc:mysql://localhost:3306/mobilia_db}
```

---

# Ejecución manual del frontend

Requiere Node.js.

Desde:

```text
frontend
```

instalar dependencias:

```bash
npm install
```

Ejecutar Vite:

```bash
npm run dev
```

Abrir:

```text
http://localhost:5173
```

La URL del backend puede configurarse mediante:

```env
VITE_API_URL=http://localhost:8080/api/v1
```
