# 🏆 Torneo Galáctico

Sistema de gestión del Gran Torneo Galáctico, donde las especies más poderosas del universo compiten por el título intergaláctico.

Este es el backend de la aplicación, desarrollado con **Spring Boot 3** siguiendo **arquitectura hexagonal** y desplegado en Railway.

🌐 **Demo en vivo:** https://moring-value-test-front-production.up.railway.app

<img width="1918" height="1123" alt="imagen" src="https://github.com/user-attachments/assets/3ccd73ca-d7df-4c7a-a878-d49e352f827d" />
<img width="1918" height="1126" alt="imagen" src="https://github.com/user-attachments/assets/c70fc89e-1da0-4b6c-9e2f-e81276e74f74" />
<img width="1915" height="1129" alt="imagen" src="https://github.com/user-attachments/assets/c6f94529-b9b7-4c99-95ea-fab8d2f5ffcd" />


---

## 🚀 Stack técnico

- **Java 17** + **Spring Boot 3.5.5**
- **Spring Security Crypto** (BCrypt para passwords)
- **JWT** (jjwt 0.12.6) para autenticación stateless
- **Spring Data JPA** + **H2** (en memoria, se reinicia con cada deploy)
- **Bean Validation** (jakarta.validation) para validación de inputs
- **OpenAPI/Swagger** para documentación automática
- **Docker** (multi-stage build) para deploy
- **JUnit 5** + **Mockito** + **AssertJ** (61 tests)

## 🏛️ Arquitectura

El backend sigue una **arquitectura hexagonal** (Ports & Adapters) estricta:

┌────────────────────────────────────────────────────┐
│              domain/ (repositorio puro)             │
│   model/  port/in/  port/out/  service/  exception  │
└────────────────────────────────────────────────────┘
                          ▲
                          │ implementa
┌────────────────────────────────────────────────────┐
│   application/ (casos de uso + DTOs + mappers)     │
└────────────────────────────────────────────────────┘
                          ▲
                          │ depende
┌────────────────────────────────────────────────────┐
│  infrastructure/ (adaptadores: REST, JPA, filtros)  │
│   persistence/  rest/  config/  util/              │
└────────────────────────────────────────────────────┘

**Capas:**

- `domain/` — Entidades, value objects, puertos, lógica de negocio pura (sin Spring ni Hibernate)
- `application/` — DTOs, mappers, orquestación
- `infrastructure/` — REST controllers, JPA adapters, filtros, configs

## ✨ Funcionalidad

### 🌱 Gestión de especies
- Registrar especie (nombre, nivel de poder 1-1000, habilidad especial)
- Listar todas las especies (público)
- Listar por id

### ⚔️ Combates
- Iniciar combate entre 2 especies (público, con auth)
- Cada combate asigna un **modificador aleatorio 1-50** a cada especie
- Gana quien tenga mayor **nivel efectivo** (nivel_poder + modificador)
- Si hay empate, gana la que tenga nombre **alfabéticamente menor**
- **Bonus:** combate aleatorio entre 2 especies elegidas al azar
- **Bonus:** soporte para "combate de desempate" (especie vs sí misma si está en primer puesto empatado)

### 🏆 Ranking
- Ordenado por cantidad de victorias (descendente)
- Empates se resuelven por nombre alfabético
- Muestra la posición correcta (1, 1, 3, etc.) para especies empatadas

### 🔐 Autenticación
- Registro de usuarios con validación (username 3-30, email, password 8+)
- Login con JWT
- Endpoints protegidos requieren `Authorization: Bearer <token>`
- Hash de passwords con BCrypt strength 10

## 📡 API Endpoints

| Método | Path | Auth | Descripción |
|--------|------|------|-------------|
| `POST` | `/api/auth/register` | ❌ | Registrar nuevo usuario |
| `POST` | `/api/auth/login` | ❌ | Login, devuelve JWT |
| `GET` | `/api/especies` | ❌ | Listar todas las especies |
| `POST` | `/api/especies` | ✅ | Registrar nueva especie |
| `GET` | `/api/especies/{id}` | ❌ | Obtener especie por id |
| `POST` | `/api/combates` | ✅ | Iniciar combate entre 2 especies |
| `POST` | `/api/combates/aleatorio` | ✅ | Iniciar combate aleatorio |
| `GET` | `/api/combates` | ✅ | Listar historial de combates |
| `GET` | `/api/ranking` | ❌ | Ver ranking de especies |
| `GET` | `/api/habilidades` | ❌ | Listar habilidades disponibles |
| `GET` | `/actuator/health` | ❌ | Health check |
| `GET` | `/swagger-ui.html` | ❌ | Documentación interactiva |

### Ejemplo: Registrar especie

```bash
curl -X POST https://torneo-galactico-back-production.up.railway.app/api/especies \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer <token>" \
  -d '{"nombre":"Alfa Centauri","nivelPoder":500,"habilidadEspecial":"Vuelo Rapido"}'
Ejemplo: Login
curl -X POST https://torneo-galactico-back-production.up.railway.app/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"victor","password":"password123"}'
Respuesta:
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tipo": "Bearer",
  "username": "victor",
  "expiresIn": 86400
}

```
🛠️ Requisitos para ejecutarlo localmente
- Java 17 o superior
- Maven 3.9+ (o usar el wrapper mvnw.cmd / ./mvnw incluido)
- 256 MB de RAM mínimo (para H2 + Spring Boot)
- Conexión a internet (solo la primera vez, para descargar dependencias)
🚀 Instrucciones de instalación y ejecución
Opción 1: Con Maven (modo desarrollo)
# Clonar el repositorio
git clone https://github.com/victorguillen20/moring-value-test-back.git
cd moring-value-test-back

# Compilar
./mvnw clean package -DskipTests

# Ejecutar
./mvnw spring-boot:run
# O también:
java -jar target/torneo-galactico-0.1.0.jar
La aplicación queda corriendo en http://localhost:8080.
Opción 2: Con Docker
# Build de la imagen
docker build -t torneo-galactico .

# Ejecutar el container
docker run -d -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JWT_SECRET=$(openssl rand -base64 48) \
  -e CORS_ALLOWED_ORIGINS=http://localhost:4200 \
  --name torneo-galactico \
  torneo-galactico
Verificar que está corriendo
curl http://localhost:8080/actuator/health
Debe devolver:
{"status":"UP"}
Acceder a la documentación
- Swagger UI: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console (jdbc:h2:mem:torneogalactico, user: sa)
- API base: http://localhost:8080/api
🌍 Variables de entorno
Variable	Descripción
SPRING_PROFILES_ACTIVE	Profile activo
JWT_SECRET	Secret para firmar JWT (mínimo 32 chars)
CORS_ALLOWED_ORIGINS	Origins separados por coma
PORT	Puerto del server (default 8080)
🧪 Tests
./mvnw test
Los tests cubren:
- Servicios del dominio (lógica de negocio)
- Filtros (JWT, CORS, observabilidad)
- Integration test end-to-end (CombateIntegrationTest con @SpringBootTest)
🐛 Datos de prueba (DataInitializer)
Al arrancar, se cargan datos de prueba si la BD está vacía:
- Usuario: victor / password123
- Especies: Alfa Centauri, Betelgeuse, Cygnus X-1, Draco
- Combates: 2 combates pre-cargados (Cygnus X-1 es el líder)
📁 Estructura del proyecto
src/main/java/com/morning/torneo/
├── domain/                        # Capa de dominio (puro)
│   ├── model/                     #   - Especie, Combate, Usuario
│   ├── port/                      #   - Interfaces de puertos
│   │   ├── in/                   #   - Use cases
│   │   └── out/                  #   - Repositorios
│   ├── service/                   #   - Implementaciones
│   └── exception/                 #   - Excepciones de dominio
│
├── application/                   # Capa de aplicación
│   ├── dto/                       #   - DTOs (request/response)
│   └── mapper/                    #   - Mappers
│
└── infrastructure/               # Capa de infraestructura
    ├── persistence/               #   - JPA entities, repositories, adapters
    │   ├── entity/
    │   ├── repository/
    │   └── adapter/
    ├── rest/                      #   - Controllers, filters, exception handlers
    │   ├── controller/
    │   ├── filter/
    │   ├── auth/
    │   └── exception/
    ├── config/                    #   - ApplicationConfig, CorsConfig, etc
    └── util/                      #   - JwtTokenProvider

src/main/resources/
├── application.yml
└── postman/                       # Colección Postman para tests de API
🌐 Demo en vivo
- Frontend: https://moring-value-test-front-production.up.railway.app
- Backend API: https://torneo-galactico-back-production.up.railway.app
- Swagger: https://torneo-galactico-back-production.up.railway.app/swagger-ui.html
🔗 Repos relacionados
- Frontend (Angular 17): moring-value-test-front (https://github.com/victorguillen20/moring-value-test-front)
- Postman collection: Ver postman/torneo-galactico.postman_collection.json
---
<img width="1915" height="1126" alt="imagen" src="https://github.com/user-attachments/assets/28529519-abad-41cc-ab12-8df4ccf63254" />
