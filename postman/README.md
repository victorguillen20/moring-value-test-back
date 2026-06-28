# Postman Collection

Esta carpeta contiene la coleccion Postman para testear la API del Torneo Galactico.

## Archivos

- `torneo-galactico.postman_collection.json` — Coleccion con los 8 endpoints del backend
- `torneo-galactico-local.postman_environment.json` — Environment local (`http://localhost:8080/api`)
- `torneo-galactico-prod.postman_environment.json` — Environment de produccion (`https://torneo-galactico-back.onrender.com/api`)

## Como usar

1. Abrir Postman
2. Importar la coleccion: File > Import > seleccionar `torneo-galactico.postman_collection.json`
3. Importar el environment local o produccion
4. Seleccionar el environment en el dropdown superior derecho
5. Ejecutar los requests

## Orden de uso recomendado

1. **Registrar usuario** (`Auth > Registrar usuario`) — crea una cuenta
2. **Iniciar sesion** (`Auth > Iniciar sesion`) — copia el token de la response
3. Setear la variable `token` en el environment con el valor del JWT
4. Ejecutar los demas endpoints

## Endpoints incluidos

### Auth
- `POST /api/auth/register` — publico
- `POST /api/auth/login` — publico

### Especies
- `GET /api/especies` — publico
- `POST /api/especies` — protegido (requiere Bearer token)

### Combates
- `POST /api/combates` — protegido
- `POST /api/combates/aleatorio` — protegido
- `GET /api/combates` — protegido

### Ranking
- `GET /api/ranking` — publico

## Variables de entorno

- `baseUrl` — URL base de la API (cambia entre local y prod)
- `token` — JWT Bearer token para endpoints protegidos
