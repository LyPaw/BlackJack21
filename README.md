# Blackjack 21

Juego de Blackjack desarrollado en **JavaFX 21** con arquitectura **MVC + DAO** y persistencia en **SQLite**.

<p align="center">
  <img src="src/main/resources/logo/logo.png" alt="Blackjack 21" width="600">
</p>

## Requisitos

- **JDK 17 o superior** (recomendado JDK 21+)
- **Maven 3.9+**
- Sistema operativo: Windows, Linux o macOS

## Compilar y ejecutar

```bash
# Con Maven (descarga JavaFX automaticamente)
mvn clean package
mvn javafx:run

# O con scripts de Windows (sin restricciones de PowerShell)
.\run.bat              # CMD - doble clic, funciona en cualquier equipo

# Con PowerShell (si la politica de ejecucion lo permite)
.\run.ps1
```

## Estructura del proyecto

```
src/
├── main/java/
│   ├── com/blackjack/app/
│   │   └── Juego21App.java          # Punto de entrada (JavaFX Application)
│   ├── controller/
│   │   ├── JuegoController.java     # Logica del juego
│   │   └── MusicManager.java        # Gestion de musica y efectos
│   ├── model/
│   │   ├── Carta.java               # Modelo de carta
│   │   └── JugadorRanking.java      # Modelo de registro de ranking
│   ├── database/
│   │   └── ConexionDB.java          # Conexion a SQLite (ranking.db)
│   ├── dao/
│   │   ├── RankingDAO.java          # Interfaz DAO
│   │   └── RankingDAOImpl.java      # Implementacion SQLite
│   ├── service/
│   │   └── RankingService.java      # Capa de servicio
│   └── view/
│       └── RankingView.java         # UI de ranking y registro
├── main/resources/
│   ├── image/                       # 52 cartas PNG (2C.png ... AS.png)
│   ├── logo/                        # logo.png
│   └── sound/                       # barajar.wav, musica_fondo.wav
└── test/java/
    └── BackendTest.java             # 10 tests unitarios (JUnit 5)
```

## Caracteristicas

- Baraja completa de 52 cartas con valores de Blackjack
- As flexible (vale 11 o 1 segun convenga)
- Musica de fondo y efecto de sonido al barajar
- Control de volumen integrado
- Puntuacion de la banca visible con cartas graficas
- Ranking persistente en SQLite (TOP 5)
- Dialogos superpuestos (no minimizan la pantalla completa)
- Pantalla completa

## Testing

```bash
mvn test
```

Los 10 tests unitarios cubren modelo, DAO y servicio.
