# 🎮 Cómo Ejecutar Javatro

## Desde Antigravity / VS Code

### ✅ Método 1: Usar el botón Run (Recomendado)
1. Abre el archivo `Main.java`
2. Presiona **F5** o haz clic en el botón **▶️ Run** en la esquina superior derecha
3. ¡El juego debería iniciar!

### ✅ Método 2: Desde el menú Run and Debug
1. Presiona `Ctrl + Shift + D` para abrir el panel de Debug
2. Selecciona "Launch Javatro" en el dropdown
3. Haz clic en el botón verde ▶️

### ✅ Método 3: Click derecho
1. Abre `Main.java`
2. Click derecho en el editor
3. Selecciona **"Run Java"**

---

## Desde Terminal / PowerShell

### Opción A: Ejecución rápida (si ya está compilado)
```powershell
cd C:\Users\julio\Downloads\ProyectJavatro\ProyectJavatro
& "C:\Program Files\Eclipse Adoptium\jre-18.0.2.101-hotspot\bin\java.exe" -cp bin javatro.app.Main
```

### Opción B: Compilar y ejecutar
```powershell
cd C:\Users\julio\Downloads\ProyectJavatro\ProyectJavatro

# Limpiar y recompilar
Remove-Item -Recurse -Force "bin" -ErrorAction SilentlyContinue
New-Item -ItemType Directory -Force -Path "bin" | Out-Null
javac --release 18 -d bin -sourcepath src -encoding UTF-8 src/javatro/app/Main.java

# Copiar recursos (imágenes y sonidos)
Copy-Item -Recurse -Force "src/javatro/recursos" "bin/javatro/"

# Ejecutar
& "C:\Program Files\Eclipse Adoptium\jre-18.0.2.101-hotspot\bin\java.exe" -cp bin javatro.app.Main
```

---

## ⚠️ Solución de Problemas

### Error: "MenuPrincipal cannot be resolved"
- **Causa:** El proyecto no ha sido compilado correctamente
- **Solución:** 
  1. Presiona `Ctrl + Shift + P`
  2. Escribe y selecciona: **"Java: Clean Java Language Server Workspace"**
  3. Haz clic en **"Restart and delete"**
  4. Espera a que VS Code recargue e indexe el proyecto

### Error: "UnsupportedClassVersionError"
- **Causa:** Error de versión de Java
- **Solución:** Asegúrate de usar Java 18 o superior para ejecutar. En VS Code, ve a la configuración de Java y verifica la versión configurada.

### La ventana del juego no aparece
- Verifica que los archivos en `recursos/` (imágenes y audio) estén copiados en `bin/javatro/recursos/`
- Revisa la consola para ver mensajes de error

---

## 📂 Estructura del Proyecto

```
ProyectJavatro/
├── src/javatro/          # Código fuente
│   ├── app/              # Clase Main
│   ├── controlador/      # Lógica del controlador
│   ├── modelo/           # Modelos de datos
│   ├── vista/            # Interfaz gráfica
│   ├── util/             # Utilidades (audio, etc.)
│   └── recursos/         # Imágenes y sonidos
├── bin/                  # Archivos compilados (.class)
└── .vscode/              # Configuración de VS Code
    ├── settings.json     # Configuración del proyecto Java
    └── launch.json       # Configuración de ejecución
```

---

## 🔧 Archivos de Configuración Creados

### `.vscode/settings.json`
Configura las rutas del proyecto Java en VS Code

### `.vscode/launch.json`
Configura cómo ejecutar la aplicación con F5

---

**¡Disfruta del juego!** 🃏✨
