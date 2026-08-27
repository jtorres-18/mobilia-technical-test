# Mobilia Software - Prueba Técnica

Aplicación web desarrollada para la prueba técnica de **Mobilia Software**, orientada a la consulta del historial de inmuebles, contratos y personas asociadas.

La solución permite consultar todos los contratos registrados y realizar búsquedas por información relacionada con el contrato, inmueble o personas participantes, mostrando de forma clara el estado del contrato y los roles asociados.

---

## Funcionalidades

- Consulta general de contratos.
- Búsqueda de contratos por:
    - Código del contrato.
    - Dirección del inmueble.
    - Nombre de una persona.
    - Apellido.
    - Nombre completo.
    - Documento de identidad.
    - Correo electrónico.
- Visualización del estado del contrato:
    - Activo.
    - Inactivo.
- Visualización de las personas asociadas al contrato:
    - Arrendatario.
    - Propietarios.
    - Deudor Solidario.
- Priorización de contratos activos en los resultados.
- Manejo controlado de búsquedas sin resultados.
- Interfaz web responsive desarrollada en React.

---

# Arquitectura

El backend fue desarrollado siguiendo una arquitectura limpia y modular, separando las responsabilidades de dominio, casos de uso, adaptadores de infraestructura y puntos de entrada.

```text
backend/
├── applications/
│   └── app-service/
│
├── domain/
│   ├── model/
│   └── usecase/
│
├── infrastructure/
│   ├── driven-adapters/
│   │   └── r2dbc-mysql/
│   │
│   └── entry-points/
│       └── reactive-web/
│
├── Dockerfile
├── pom.xml
├── mvnw
└── mvnw.cmd
```

### Responsabilidad de cada módulo

#### `domain/model`

Contiene las entidades y objetos principales del dominio:

- `Contract`
- `Property`
- `Person`
- `ContractParty`
- Enumeraciones de estados, roles y tipos de inmueble.

Este módulo no depende de Spring.

#### `domain/usecase`

Contiene la lógica de aplicación:

- Consulta de todos los contratos.
- Búsqueda de contratos.

Los casos de uso interactúan con el exterior a través de interfaces o gateways definidos por el dominio.

#### `infrastructure/driven-adapters/r2dbc-mysql`

Implementa el acceso reactivo a MySQL mediante:

- Spring Data R2DBC.
- `DatabaseClient`.
- Consultas SQL parametrizadas.

Es la implementación concreta del gateway definido por el dominio.

#### `infrastructure/entry-points/reactive-web`

Expone la API REST utilizando WebFlux y routing funcional.

Incluye:

- Routes.
- Handlers.
- DTO de respuesta.
- Manejo de errores.
- Configuración CORS.

#### `applications/app-service`

Es el módulo encargado de ensamblar y ejecutar la aplicación Spring Boot.

También contiene:

- Configuración de beans.
- Configuración de aplicación.
- Migraciones Flyway.

---

# Tecnologías utilizadas

## Backend

- Java 21
- Spring Boot
- Spring WebFlux
- Spring Data R2DBC
- MySQL 8.4
- Flyway
- Maven
- Lombok
- Project Reactor

## Frontend

- React
- Vite
- JavaScript
- Axios
- CSS

## Calidad y pruebas

- JUnit 5
- Mockito
- Reactor Test
- StepVerifier
- WebTestClient
- JaCoCo
- SonarQube

## Infraestructura

- Docker
- Docker Compose
- Nginx

---

# Modelo de datos

La aplicación utiliza cuatro tablas principales:

```text
properties
    │
    │ 1
    │
    └────────── N contracts
                     │
                     │ 1
                     │
                     └────────── N contract_parties N ────────── 1 persons
```

## `properties`

Representa un inmueble.

Campos principales:

```text
id
address
type
```

Tipos soportados:

```text
HOUSE
APARTMENT
COMMERCIAL_SPACE
```

---

## `contracts`

Representa un contrato asociado a un inmueble.

Campos principales:

```text
id
code
status
property_id
```

Estados:

```text
ACTIVE
INACTIVE
```

El código del contrato es único.

---

## `persons`

Representa una persona que participa en uno o varios contratos.

Campos:

```text
id
first_name
last_name
identity_document
email
```

Una misma persona puede estar asociada a diferentes contratos.

---

## `contract_parties`

Tabla intermedia que relaciona personas y contratos.

Además de establecer la relación, define el rol de cada persona:

```text
TENANT
OWNER
GUARANTOR
```

La combinación:

```text
contract_id + person_id
```

es única para impedir que una misma persona aparezca duplicada dentro del mismo contrato.

---

# Reglas de negocio consideradas

De acuerdo con el dominio planteado:

- Un contrato tiene exactamente un arrendatario.
- Un contrato tiene uno o más propietarios.
- Un contrato puede tener cero o más fiadores.
- Un contrato debe involucrar al menos dos personas.
- Un inmueble puede tener como máximo un contrato activo.
- Un inmueble puede conservar múltiples contratos inactivos como historial.
- Una persona puede participar en diferentes contratos y tener diferentes roles en cada uno.

La aplicación desarrollada para esta prueba está orientada a **consulta**, por lo que no se implementaron endpoints de creación o modificación de contratos.

Las restricciones relacionadas con creación o actualización quedan representadas en el modelo y serían validadas en los casos de uso correspondientes si la aplicación evolucionara hacia operaciones de escritura.

---

# Estrategia de búsqueda

La búsqueda está implementada en base de datos y permite consultar contratos utilizando un único término.

Endpoint:

```http
GET /api/v1/contracts/search?q={searchTerm}
```

## Coincidencia exacta

Se utiliza coincidencia exacta, ignorando mayúsculas, minúsculas y espacios externos, para:

- Código de contrato.
- Nombre.
- Apellido.
- Nombre completo.
- Documento de identidad.
- Correo electrónico.

Por ejemplo:

```text
Ana
```

encuentra una persona cuyo nombre sea exactamente `Ana`, pero no nombres como:

```text
Anabel
Juliana
```

Esta decisión evita resultados inesperados en campos de identificación personal.

## Dirección

Para las direcciones se permite búsqueda por inicio de texto.

Ejemplo:

```text
Calle 10
```

puede encontrar:

```text
Calle 10 # 35-20, Medellin
```

---

# Preservación de participantes del contrato

La búsqueda por personas utiliza una condición `EXISTS`.

Esto permite que una persona sea utilizada como criterio para encontrar un contrato sin limitar las personas que posteriormente son retornadas.

Por ejemplo, si se busca:

```text
Ana
```

y Ana es el arrendatario de un contrato, la respuesta continúa incluyendo:

- Ana como arrendataria.
- Todos los propietarios.
- Todos los fiadores.

De esta forma se devuelve el contrato completo y no únicamente la persona que produjo la coincidencia.

---

# API REST

Base URL:

```text
http://localhost:8080/api/v1
```

## Consultar todos los contratos

```http
GET /api/v1/contracts
```

Ejemplo:

```text
http://localhost:8080/api/v1/contracts
```

Respuesta:

```json
[
  {
    "code": "MBL-A100",
    "status": "ACTIVE",
    "address": "Calle 10 # 35-20, Medellin",
    "type": "APARTMENT",
    "tenant": "Laura Gomez",
    "owners": [
      "Carlos Restrepo",
      "Ana Torres"
    ],
    "guarantors": [
      "Sofia Herrera"
    ]
  }
]
```

Si no existen contratos:

```http
200 OK
```

con:

```json
[]
```

---

## Buscar contratos

```http
GET /api/v1/contracts/search?q={searchTerm}
```

Ejemplos:

```text
/api/v1/contracts/search?q=Ana
/api/v1/contracts/search?q=Calle 10
/api/v1/contracts/search?q=MBL-A100
/api/v1/contracts/search?q=1035001003
/api/v1/contracts/search?q=ana.torres@example.com
```

---

## Búsqueda sin resultados

Si el término es válido pero no existen contratos asociados:

```http
404 Not Found
```

Ejemplo:

```json
{
  "code": "CONTRACTS_NOT_FOUND",
  "message": "No contracts found for search term: Pepito"
}
```

---

## Término de búsqueda inválido

Si `q` no se envía o está vacío:

```http
400 Bad Request
```

Ejemplo:

```json
{
  "code": "INVALID_SEARCH_TERM",
  "message": "Search term cannot be empty"
}
```

---

# Orden de resultados

Los contratos activos se muestran antes que los contratos inactivos.

```text
ACTIVE
↓
INACTIVE
```

Dentro del mismo estado se utiliza un orden determinístico por identificador.

No se implementó orden cronológico por fecha debido a que el dominio solicitado no define una fecha de inicio, finalización o creación del contrato.


# Migraciones de base de datos

Las migraciones se encuentran en:

```text
backend/applications/app-service/src/main/resources/db/migration
```

Actualmente:

```text
V1__create_initial_schema.sql
V2__insert_sample_data.sql
```

`V1` crea la estructura inicial de la base de datos.

`V2` inserta información de prueba para poder utilizar la aplicación inmediatamente después de levantarla.

Las migraciones ya aplicadas no deben modificarse.

Los cambios futuros de base de datos deberían realizarse mediante nuevas versiones:

```text
V3__example_change.sql
V4__another_change.sql
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

---

# Pruebas automatizadas

El backend cuenta con pruebas en las diferentes capas de la aplicación.

Se cubren, entre otros:

- Modelos de dominio.
- Enumeraciones.
- Casos de uso.
- Manejo de errores.
- Adaptador R2DBC.
- Conversión de filas de base de datos.
- Agrupación de participantes por contrato.
- Routing funcional.
- Handlers WebFlux.
- Respuestas HTTP.
- Configuración CORS.
- Configuración de casos de uso.
- Arranque de la aplicación.

Para ejecutar las pruebas:

```powershell
cd backend
.\mvnw.cmd clean verify
```

En Linux/macOS:

```bash
cd backend
./mvnw clean verify
```

---

# Calidad de código

El proyecto fue analizado localmente utilizando:

- JaCoCo.
- SonarQube.

Resultado final del análisis:

```text
Quality Gate        Passed
Coverage            100%
Security Issues     0
Reliability Issues  0
Maintainability     0 open issues
Duplications        0.0%
```

El objetivo del análisis fue validar tanto la cobertura de pruebas como aspectos de mantenibilidad, seguridad y calidad estática del código.

---

# Decisiones técnicas

## Spring WebFlux + R2DBC

Se utilizó un stack reactivo de extremo a extremo en el backend:

```text
HTTP Request
     ↓
WebFlux
     ↓
Use Case
     ↓
Gateway
     ↓
R2DBC
     ↓
MySQL
```

Esto evita introducir llamadas JDBC bloqueantes en el flujo principal de consultas.

Flyway utiliza JDBC exclusivamente para ejecutar las migraciones durante el arranque.

---

## Arquitectura modular

Aunque la aplicación es pequeña, se utilizó una separación clara entre:

```text
Dominio
Aplicación
Infraestructura
Entrada
```

El objetivo no es convertir la solución en microservicios, sino evitar acoplar la lógica de negocio directamente con Spring, HTTP o MySQL.

---

## SQL explícito

Para la consulta principal se utilizó `DatabaseClient` y SQL explícito.

Esto permite controlar:

- Las relaciones entre contratos, inmuebles y personas.
- Los criterios de búsqueda.
- El orden de los resultados.
- La recuperación de todos los participantes de un contrato.

---

## Sin paginación

No se agregó paginación debido al alcance de la prueba y al volumen esperado de información de demostración.

En un escenario con un volumen considerable de contratos se incorporaría paginación para evitar cargar grandes conjuntos de resultados en memoria.

---

## Sin tecnologías innecesarias

No se agregaron componentes como:

- Kafka.
- Redis.
- Kubernetes.
- CQRS.
- Microservicios.

El objetivo fue mantener una solución proporcional al problema solicitado y evitar complejidad accidental.

---

# Frontend

El frontend está desarrollado como una aplicación independiente en React.

```text
frontend/
├── src/
│   ├── components/
│   │   ├── ContractTable.jsx
│   │   ├── SearchBar.jsx
│   │   └── StatusBadge.jsx
│   │
│   ├── pages/
│   │   └── ContractsPage.jsx
│   │
│   ├── services/
│   │   └── contractService.js
│   │
│   ├── styles/
│   │   └── contracts.css
│   │
│   ├── App.jsx
│   └── main.jsx
│
├── Dockerfile
├── package.json
└── vite.config.js
```

Axios se utiliza exclusivamente desde la capa:

```text
services
```

evitando realizar llamadas HTTP directamente desde los componentes visuales.

---

# Docker

La solución utiliza imágenes multi-stage.

## Backend

```text
JDK 21
   ↓
Maven build
   ↓
JAR
   ↓
JRE 21
```

El contenedor final solo contiene lo necesario para ejecutar la aplicación.

## Frontend

```text
Node
   ↓
Vite build
   ↓
dist/
   ↓
Nginx
```

El frontend de producción es servido mediante Nginx y no mediante el servidor de desarrollo de Vite.

---

# Datos de prueba

La aplicación incluye información precargada mediante Flyway para facilitar la evaluación.

Entre los datos disponibles se encuentran varios:

- Inmuebles.
- Contratos activos.
- Contratos inactivos.
- Arrendatarios.
- Propietarios.
- Fiadores.

Esto permite probar inmediatamente búsquedas como:

```text
Ana
Calle 10
MBL-A100
1035001003
ana.torres@example.com
```

---

# Posibles mejoras futuras

Algunas mejoras que podrían incorporarse si el sistema evolucionara son:

- Paginación.
- Fechas de inicio y finalización de contratos.
- Historial cronológico real.
- Creación y actualización de contratos.
- Validación de reglas de negocio durante operaciones de escritura.
- OpenAPI / Swagger.
- Testcontainers para pruebas de integración reales con MySQL.
- CI/CD con ejecución automática de pruebas y análisis de calidad.
- Autenticación y autorización.
- Observabilidad y métricas.

Estas funcionalidades no fueron agregadas para mantener la solución alineada con el alcance solicitado.

---

# Autor

Desarrollado como solución para la prueba técnica de **Mobilia Software**.
