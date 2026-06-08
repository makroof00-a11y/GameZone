# 🎮 GameZone – Sistema de Gestión de Videojuegos

> **Examen Final – Programación de Computadores II (SS300)**  
> Universidad Popular del César · Ing. Esp. Amilkar Hernandez

## 👥 Integrantes

| Nombre | Código |
|--------|-------
| María Mercedes Romero Fuentes | |
| (Compañero/a) | |

---

## 🏗️ Arquitectura en capas

```
src/main/java/gamezone/
├── interfaces/
│   ├── Sellable.java          ← interfaz vender / info
│   └── Displayable.java       ← interfaz tabla / info
├── model/
│   ├── VideoGame.java         ← clase abstracta (herencia)
│   ├── DigitalVideoGame.java  ← implementa Sellable + Displayable
│   ├── PhysicalVideoGame.java ← implementa Sellable + Displayable
│   └── Sale.java              ← modelo de venta
├── repository/
│   ├── VideoGameRepository.java  ← CRUD JSON (videogames.json)
│   └── SaleRepository.java       ← CRUD JSON (sales.json)
├── service/
│   └── VideoGameService.java     ← lógica de negocio + validaciones
└── ui/
    ├── MainApp.java              ← menú principal JavaFX
    ├── BaseView.java             ← clase base de vistas
    ├── AgregarVideojuegoView.java← CRUD completo
    ├── ListarVideojuegosView.java
    ├── BuscarTituloView.java
    ├── BuscarPlataformaView.java
    ├── VenderView.java
    └── ListarVentasView.java
```

---

## ▶️ Cómo ejecutar

### Requisitos
- Java 21+
- Maven 3.8+

### Comando
```bash
mvn javafx:run
```

Los archivos JSON se crean automáticamente en `data/` al primer uso.

---

## ✅ Criterios cubiertos

| Criterio | ✔ |
|----------|---|
| Uso de Herencia (`VideoGame` → `DigitalVideoGame`, `PhysicalVideoGame`) | ✔ |
| Estructura de clases correcta (abstracta + interfaces) | ✔ |
| Constructores completos, getters, setters, toString() | ✔ |
| Uso de JSON (archivo de texto sin librerías externas) | ✔ |
| Arquitectura en capas (model / repository / service / ui) | ✔ |
| Alertas UI (JavaFX Alert – ERROR, WARNING, INFO, CONFIRMATION) | ✔ |
| Métodos funcionando sin errores | ✔ |
| Interfaz UI con menú interactivo operativo (JavaFX) | ✔ |
| Código limpio y bien organizado | ✔ |
| Convenciones de nombres en Java (camelCase, PascalCase) | ✔ |
