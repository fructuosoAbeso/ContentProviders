App 2: Cliente de Login (Remote Login)
¿Qué hace esta App?
Es el "Cliente" que no tiene base de datos propia. Utiliza el Content Provider de la App 1 para validar si un usuario y contraseña existen y permitirle el acceso.

Tecnologías utilizadas
ContentResolver: Para realizar consultas a la URI de la App 1.

Queries (Visibilidad): Declaración de permisos para ver otras apps.

Errores solucionados
Provider no encontrado (Android 11+): La App 2 no "veía" a la App 1 por restricciones de seguridad. Solución: Añadimos el bloque <queries> en el AndroidManifest.xml.

Bienvenido "null": Al entrar en la pantalla de inicio, no se mostraba el nombre. Solución: Implementamos intent.putExtra en el Login para pasar el nombre del usuario a la siguiente actividad.

Inconsistencia de Columnas: El Cursor fallaba al buscar columnas inexistentes. Solución: Creamos un UsuariosProviderHelper para unificar los nombres de las columnas (id, nombre_usuario, password) con los de la App 1.
