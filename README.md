# JBG Reclamos

Proyecto desarrollado para la asignatura Desarrollo de Aplicaciones Móviles.  
Consiste en una aplicación Android hecha en Kotlin con Jetpack Compose, conectada a un backend propio y usando también una API externa.

## Integrantes
- Genesis Manque
- Benjamin Arriaza
- Jose Castillo

## Descripción General
La aplicación permite gestionar reclamos desde un dispositivo móvil.  
El usuario puede crear, ver, editar y eliminar reclamos, los cuales se almacenan en una base de datos remota.

El backend fue desarrollado en Node.js con Express y usa PostgreSQL como base de datos.  
La comunicación entre la app y el backend se realiza mediante Retrofit.

Para obtener la dirección a partir de coordenadas (latitud y longitud), se utiliza la API externa Nominatim (OpenStreetMap).

## Funcionalidades de la App
- Inicio de sesión con token (JWT)
- Crear reclamos
- Editar reclamos
- Eliminar reclamos
- Listar reclamos
- Ver detalle de reclamo
- Obtener dirección con API externa
- Uso de cámara
- Uso de GPS

## Backend Propio (CRUD)
El servidor implementa:
- Login con JWT
- Crear reclamo
- Obtener reclamos
- Obtener reclamo por ID
- Editar reclamo
- Eliminar reclamo
- Conexión con PostgreSQL

## API Externa Utilizada
Se utilizó Nominatim (OpenStreetMap) para convertir lat/lon en dirección.

## Pruebas Unitarias
Se desarrollaron pruebas para:
- ViewModels
- Repository
- Lógica interna

Usando:
- JUnit
- MockK

## Compilación y Ejecución de la App
1. Abrir el proyecto en Android Studio
2. Sincronizar Gradle
3. Ejecutar en dispositivo o instalar la APK release

## Ejecutar Backend
1. Instalar dependencias con:
   npm install
2. Crear archivo .env con las variables de entorno
3. Iniciar servidor con:
   npm start

## Entregables Incluidos
- APK firmado (app-release.apk)
- Archivo de firma (reclamos_keystore.jks)
- Repositorio Android
- Repositorio backend
- Tablero Trello del proyecto
