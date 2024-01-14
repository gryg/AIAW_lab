
# Context/basic ruleset
Agentul navigheaza intr-o grila 4x4, evitand capcane (`PIT`), gasind aur (`GOLD`) si evitand sau omorand Wumpus. 

## Structura si Logica

### Variabile Principale
- `GRID_SIZE`: Dimensiunea hartii.
- `PIT`, `GOLD`, `WUMPUS`, etc.: Constante pentru elementele din joc.
- `world`: Harta jocului, reprezentata ca o matrice 2D.
- `agentPosition`: Pozitia curenta a agentului.
- `direction`: Directia de deplasare a agentului.
- `hasArrow`, `hasGold`: Starea sagetii si aurului.
- `visited`: Set pentru memorarea pozitiilor vizitate.
- `actionStack`: Stiva pentru memorarea actiunilor agentului.

### Initializare
- Constructorul initiaza harta si plaseaza elementele (aur, Wumpus, capcane) si perceptii (miros, briza).
- Se asigura ca pozitia initiala si zonele adiacente sunt sigure.

### Miscare si Actiuni
- `moveForward()`, `turnLeft()`, `turnRight()`: Deplaseaza si roteste agentul.
- `grabGold()`: Agentul ia aurul daca se afla pe aceeasi celula.
- `shootArrow()`: Agentul trage cu sageata, daca mai are una.
- `backtrack()`: Agentul se intoarce pe pasii anteriori.

### Decizii si Strategie
- `makeDecision()`: Agentul evalueaza mediul si decide actiunea.
- `chooseAction()`: Alege intre mers inainte, rotire sau intoarcere.

### Vizualizare
- `visualizeWorld()`: Afiseaza harta si pozitia agentului.