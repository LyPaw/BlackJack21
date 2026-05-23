# Blackjack 21

Juego de Blackjack (Siete y Media) desarrollado en **JavaFX 21** con arquitectura **MVC + DAO** y persistencia en **SQLite**.

<p align="center">
  <img src="screenshot.png" alt="Blackjack 21" width="600">
</p>

## Requisitos

- **JDK 17 o superior** (recomendado JDK 21+)
- **Maven 3.9+**
- Sistema operativo: Windows, Linux o macOS

## Compilar y ejecutar

```bash
# Con Maven (descarga JavaFX automaticamente)
mvn clean package
mvn exec:exec@run

# O con el script de Windows (hace lo mismo)
.\run.ps1
```

## Estructura del proyecto

```
src/
├── main/java/
│   ├── Juego21App.java              # Punto de entrada (JavaFX Application)
│   ├── controller/
│   │   └── JuegoController.java     # Logica del juego
│   ├── model/
│   │   ├── Carta.java               # Modelo de carta
│   │   └── JugadorRanking.java      # Modelo de registro de ranking
│   ├── database/
│   │   └── ConexionDB.java          # Conexion a SQLite (ranking.db)
│   ├── dao/
│   │   ├── RankingDAO.java          # Interfaz DAO
│   │   └── RankingDAOImpl.java      # Implementacion SQLite
│   └── service/
│       └── RankingService.java      # Capa de servicio
├── main/resources/
│   ├── image/                       # 52 cartas PNG (2C.png ... AS.png)
│   └── sound/                       # barajar.wav
└── test/java/
    └── BackendTest.java             # 10 tests unitarios (JUnit 5)
```

## Caracteristicas

- Baraja completa de 52 cartas con valores de Blackjack
- Animacion de sonido al barajar
- Puntuacion de la banca visible con cartas graficas
- Ranking persistente en SQLite (TOP 5)
- Dialogos superpuestos (no minimizan la pantalla completa)
- Pantalla completa

## Testing

```bash
mvn test
```

Los 10 tests unitarios cubren modelo, DAO y servicio.
