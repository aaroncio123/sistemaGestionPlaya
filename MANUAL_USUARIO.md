# Manual de Usuario - Proyecto Terminado (Playa Gestión)

Este documento detalla el funcionamiento, especificaciones técnicas y pasos de instalación del sistema de gestión y calificación de playas.

## 1. Especificaciones Técnicas

*   **Nombre del Proyecto:** ProyectoTerminado
*   **Plataforma Móvil:** Android
*   **Versiones de Android:**
    *   **SDK Mínimo (Min SDK):** Android 8.0 (API Level 26) - *Oreo*
    *   **SDK Objetivo (Target SDK):** Android 15 (API Level 35)
    *   **SDK de Compilación (Compile SDK):** API Level 36
*   **Librerías Principales:**
    *   Firebase Auth (Autenticación)
    *   Google Play Services Auth
    *   Volley (Conexión con base de datos MySQL vía PHP)
    *   Glide (Carga de imágenes)
    *   Material Design Components (Interfaz de usuario)
*   **Backend:** XAMPP (PHP 8.x / MySQL)
*   **Versión de Pandas (Python):** 2.2.x (Utilizada para el procesamiento de datos).

---

## 2. Funcionamiento del Sistema (Paso a Paso)

### Paso 1: Inicio del Sistema (Splash Screen)
Al abrir la aplicación, se mostrará una pantalla de inicio con el logo girando y música ambiental (`otroatardecer.mp3`). La animación dura 3 segundos antes de pasar al Login.

### Paso 2: Autenticación de Usuario
El usuario puede iniciar sesión con su cuenta de **Google** o entrar como **Invitado** para probar la aplicación.

### Paso 3: Selección de Playa
El usuario selecciona un **Distrito** y luego la **Playa** específica que desea gestionar.

### Paso 4: Selección de Acción
Se presentan dos opciones: **Calificar Playa** (para enviar reportes) o **Ver Playa** (para consultar estados previos).

### Paso 5: Reporte y Calificación
El usuario completa un formulario con:
*   Calificación por estrellas.
*   Selectores de limpieza, afluencia, clima y seguridad.
*   Subida de foto y comentarios.
*   Envío de datos al servidor MySQL.

---

## 3. Guía de Instalación para Desarrolladores

1.  **Clonar el repositorio:** `git clone https://github.com/aaroncio123/sistemaGestionPlaya.git`
2.  **Configurar la Base de Datos:**
    *   Abrir PHPMyAdmin en XAMPP.
    *   Importar el archivo SQL ubicado en la carpeta `/scripts/` del proyecto.
3.  **Configurar el Backend (PHP):**
    *   Copiar los archivos PHP del proyecto a la carpeta `C:/xampp/htdocs/playaGestion/`.
4.  **Configurar Android Studio:**
    *   Abrir el proyecto y sincronizar con Gradle.
    *   Asegurarse de tener el audio `otroatardecer.mp3` en `res/raw/`.

---

## 4. Notas Importantes
*   **Galería de Imágenes**: Para probar la subida de fotos en el emulador, asegúrate de descargar imágenes dentro del dispositivo virtual para que la galería tenga contenido.
*   **Versión Actual**: 1.0.0 (Estable).
*   **Interfaz**: Diseño unificado en tonos azules y fondo de atardecer.
