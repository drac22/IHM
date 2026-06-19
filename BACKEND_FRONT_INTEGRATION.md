# Backend Front Integration

## Base URL

`http://localhost:8080`

## CORS

El backend acepta peticiones desde:

`http://localhost:4200`

Configurado en `SecurityConfig` con:

- metodos: `GET, POST, PUT, DELETE, OPTIONS`
- headers: `Authorization, Content-Type`
- credentials: `true`

## Autenticacion

Todos los endpoints privados usan:

`Authorization: Bearer <token>`

### Login

`POST /auth/login`

Request:

```json
{
  "email": "admin@ihm.local",
  "password": "Admin123*"
}
```

Response:

```json
{
  "token": "JWT_AQUI"
}
```

## Usuarios de desarrollo

- `admin@ihm.local` / `Admin123*`
- `ti@ihm.local` / `Ti123456*`
- `user@ihm.local` / `User1234*`

## Endpoints publicos

- `POST /auth/login`
- `GET /api/v1/meta/server-time`
- `GET /api/v1/meta/ticket-options`
- `GET /v3/api-docs`
- `GET /swagger-ui/index.html`

## Endpoints privados

### Usuarios

- `GET /api/v1/usuario/me`
- `GET /api/v1/usuario`
- `GET /api/v1/usuario/{id}`
- `POST /api/v1/usuario`
- `PUT /api/v1/usuario/{id}`
- `DELETE /api/v1/usuario/{id}`

### Categorias

- `GET /api/v1/categoria`

### Tickets

- `GET /api/v1/ticket`
- `GET /api/v1/ticket/{id}`
- `GET /api/v1/ticket/usuario/{userId}`
- `GET /api/v1/ticket/my-tickets`
- `GET /api/v1/ticket/my-tickets-created`
- `POST /api/v1/ticket`
- `PUT /api/v1/ticket/{id}/asignacion`
- `PUT /api/v1/ticket/{id}/culminar`
- `DELETE /api/v1/ticket/{id}`

## Respuestas reales

### GET /api/v1/usuario/me

```json
{
  "apellido": "IHM",
  "celular": "999111222",
  "email": "admin@ihm.local",
  "id": 1,
  "nombre": "Admin",
  "roles": ["ROLE_ADMIN"]
}
```

### GET /api/v1/usuario

```json
[
  {
    "apellido": "IHM",
    "celular": "999111222",
    "email": "admin@ihm.local",
    "id": 1,
    "nombre": "Admin",
    "roles": ["ROLE_ADMIN"]
  },
  {
    "apellido": "Tecnico",
    "celular": "999111333",
    "email": "ti@ihm.local",
    "id": 2,
    "nombre": "Soporte",
    "roles": ["ROLE_TI"]
  },
  {
    "apellido": "Final",
    "celular": "999111444",
    "email": "user@ihm.local",
    "id": 3,
    "nombre": "Usuario",
    "roles": ["ROLE_USER"]
  }
]
```

### GET /api/v1/ticket

```json
[
  {
    "categoria": "Hardware",
    "creadoPor": "Usuario",
    "descripcion": "Primer ticket de prueba",
    "estado": "PENDIENTE",
    "fechaAsignacion": null,
    "fechaCulminacion": null,
    "fechaRegistro": "2026-06-18 10:07:29",
    "id": 1,
    "prioridad": null,
    "titulo": "Prueba desde Insomnia",
    "usuarioAsignado": null
  },
  {
    "categoria": "Hardware",
    "creadoPor": "Admin",
    "descripcion": "Segundo ticket de prueba",
    "estado": "PENDIENTE",
    "fechaAsignacion": null,
    "fechaCulminacion": null,
    "fechaRegistro": "2026-06-18 10:38:38",
    "id": 33,
    "prioridad": null,
    "titulo": "Prueba desde Insomnia",
    "usuarioAsignado": null
  }
]
```

### GET /api/v1/categoria

Ejemplo esperado:

```json
[
  { "id": 1, "nombre": "Hardware" },
  { "id": 2, "nombre": "Software" },
  { "id": 3, "nombre": "Redes" },
  { "id": 4, "nombre": "Accesos" }
]
```

### GET /api/v1/meta/ticket-options

```json
{
  "prioridades": ["BAJA", "MEDIA", "ALTA", "CRITICA"],
  "estados": ["PENDIENTE", "EN_PROCESO", "RESUELTO"]
}
```

## Request utiles para el front

### Crear ticket

`POST /api/v1/ticket`

```json
{
  "titulo": "Error de acceso",
  "descripcion": "No puedo entrar al sistema",
  "categoriaId": 1
}
```

### Asignar ticket

`PUT /api/v1/ticket/{id}/asignacion`

```json
{
  "idUsuarioAsignado": 2,
  "prioridad": "ALTA"
}
```
