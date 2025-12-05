# JBG Reclamos

Proyecto desarrollado para la asignatura Desarrollo de Aplicaciones Móviles.  
Consiste en una aplicación Android hecha en Kotlin con Jetpack Compose, conectada a un backend propio en Node.js y usando también una API externa.

## Integrante
- Genesis Manque, Benjamin Arriaza, Jose Castillo  

## Descripción General
La aplicación permite gestionar reclamos desde un dispositivo móvil.  
Se puede crear, ver, editar y eliminar reclamos.  
Toda la información se guarda en una base de datos en la nube.

El backend se desarrolló en Node.js con Express y PostgreSQL.  
La app se conecta a este backend usando Retrofit.

Para obtener la dirección a partir de la ubicación (latitud y longitud), se usa una API externa llamada Nominatim, que entrega la dirección correspondiente.

## Funcionalidades de la App
- Inicio de sesión con token.
- Crear reclamos.
- Editar reclamos.
- Eliminar reclamos.
- Listado de reclamos.
- Captura de foto.
- Lectura de coordenadas GPS.
- Obtener dirección con API externa.
- Navegación entre pantallas con Compose.

## Tecnologías Utilizadas
### En Android:
- Kotlin
- Jetpack Compose
- Retrofit
- ViewModel
- State
- Navigation
- Coroutines

### En Backend:
- Node.js
- Express
- PostgreSQL
- JWT para autenticación

## API Externa
Se usa la API Nominatim para convertir coordenadas GPS en dirección real.

## Pruebas Unitarias
El proyecto contiene pruebas unitarias para ViewModels y Repository, usando JUnit y Mockk.

## APK Firmado
Se generó un APK firmado en modo release para la entrega final, junto con el archivo de firma digital (.jks).

## Backend
El backend contiene rutas para manejar reclamos, hacer login y consultar la API externa.  
Incluye base de datos en PostgreSQL, autenticación con JWT y controladores REST.

## Ejecución del Proyecto
Para la app Android:
- Abrir en Android Studio
- Sincronizar Gradle
- Ejecutar en dispositivo o emulador

Para el backend:
- Instalar dependencias con npm
- Configurar variables de entorno
- Iniciar el servidor

## Gestión del Proyecto
Se utilizó un repositorio en GitHub para controlar versiones y un tablero de trabajo con las tareas divididas en pendiente, en proceso y hecho.

## Estado del Proyecto
El proyecto cuenta con:
- CRUD completo
- API externa funcionando
- App Android operativa
- Backend desplegado
- Pruebas unitarias
- APK firmado
- Arquitectura MVVM

