# ⚡ Redes Trujillo | Sistema de Gestión Eléctrica
Plataforma Web Full-Stack distribuida para la monitorización, telemetría en tiempo real y administración de puntos de carga de la red eléctrica corporativa. El sistema implementa **Control de Acceso Basado en Roles (RBAC)** en la capa de presentación y se conecta de manera asíncrona mediante un pipeline REST nativo con el motor de negocio en Java.

## 🗂️ Arquitectura del Proyecto

El repositorio sigue una separación estricta de responsabilidades, manteniendo desacoplado el núcleo de negocio en Java de la capa de interfaz web nativa:

```plaintext
Redes_Trujillo/
│
├── .gitignore                   # Exclusión de binarios (.class), IDEs y temporales
├── README.md                    # Documentación técnica del proyecto
│
├── Redes_Trujillo/              # CORE BACKEND (Java)
│   ├── .classpath
│   ├── .project
│   └── src/                     # Código fuente de controladores y modelos de dominio
│       └── com/
│           └── redestrujillo/
│               ├── controller/  # Controladores REST HTTP
│               ├── model/       # Entidades (Estacion, PuntoDeCarga)
│               └── service/     # Lógica de gestión de la red eléctrica
│
└── web/                         # FRONTEND WEB NATIVO
    ├── index.html               # Gateway de selección de perfil de acceso
    ├── dashboard.html           # Panel de control interactivo de la red
    ├── css/
    │   ├── portal.css           # Estilos específicos del Gateway (CSS Grid)
    │   └── main.css             # Estilos del Dashboard (Tema oscuro corporativo)
    └── js/
        └── app.js               # Módulo JS: Gestión RBAC, consumo Fetch y mutaciones DOM
