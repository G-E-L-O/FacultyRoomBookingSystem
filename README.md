

# 🎓 **FacultyRoomBookingSystem**

Sistema avanzado de reservas de aulas universitarias, diseñado para docentes. Desarrollado con **JavaFX** y **MySQL**, ofrece una experiencia moderna, intuitiva y adaptable para gestionar horarios y espacios académicos.

---

## 🧠 **Resumen**

Un sistema profesional para:

- 📅 Visualizar el uso de aulas por semana  
- 🧑‍🏫 Reservar espacios de forma personalizada  
- 📥 Exportar horarios en formato Excel estilizado  
- 🔍 Buscar y filtrar reservas con precisión  
- 🛡️ Prevenir conflictos automáticamente  

---

## 🛠️ **Tecnologías**

| Lenguaje | Framework | Base de Datos | Librerías | IDE |
|---------|-----------|---------------|-----------|-----|
| Java 17 | JavaFX 19 + FXML | MySQL | JDBC, Apache POI | IntelliJ IDEA |

---

## 🗂️ **Arquitectura del Proyecto**

Organizado en capas siguiendo **MVC + DAO + DTO**, para escalabilidad y mantenimiento.

```
FacultyRoomBookingSystem/
├── src/
│   ├── controller/   → Lógica de interfaz
│   ├── dao/          → Acceso a datos
│   ├── model/        → Entidades del sistema
│   ├── utils/        → Funciones auxiliares
│   ├── view/         → Vistas JavaFX
│   └── Main.java     → Punto de entrada
├── resources/
│   ├── images/
│   └── styles.css
├── pom.xml
└── README.md
```

---

## 🖼️ **Capturas de Pantalla**

| 🏠 Inicio                            | 📅 Horario Semanal                     |
|--------------------------------------|----------------------------------------|
| ![main](docs/assets/img/dash(1).png) | ![weekly](docs/assets/img/dash(2).png) |

| 📄 Exportación Excel                 |
|-------------------------------------|
| ![excel](docs/assets/img/dash(3).png) |

---

## 📤 **Exportación Inteligente**

Desde la vista semanal puedes exportar el horario en un archivo Excel que incluye:

- Estilo moderno
- Datos del docente
- Semana seleccionada

---

## 🔮 **Próximas Mejoras**

- 🔐 Autenticación de usuarios  
- 🏢 Gestión de facultades  
- 📄 Exportación en PDF  
- 🧑‍💼 Panel administrativo  

---

## 👤 **Autor**

**Angelo Aarom Alama Quesada**  
📎 [GitHub - FacultyRoomBookingSystem](https://github.com/G-E-L-O/FacultyRoomBookingSystem)

---

## 📄 **Licencia**

Este proyecto está licenciado bajo los términos de la licencia **Creative Commons Attribution-NonCommercial 4.0 International (CC BY-NC 4.0)**.  
No se permite el uso comercial del mismo.  
Consulta los términos completos en el archivo [LICENSE](LICENSE) o en [creativecommons.org/licenses/by-nc/4.0](https://creativecommons.org/licenses/by-nc/4.0/).

---
