# Context
`GridSpaceAgent` simuleaza un agent care se misca intr-o grila bidimensionala. Agentul are capacitatea de a detecta daca celulele adiacente sunt libere sau sunt limite/obstacole.

## Short explanation
- Agentul incepe in pozitia data, cu fata spre nord.
- Se misca continuu, urmarind o logica simpla: daca in fata e liber, merge in
- Tot timpul incearca sa tina limita grilei sau orice obstacol la stanga lui, ca un soi de "ghid".
- Deplasarea agentului e infinita, adica nu se opreste singur. Asta e marcat in cod printr-o bucla while (true).

### Potentiale imbunatatiri
- Adaugare unei logici de gestionare a cazurilor in care incepe de la interiorul unui obstacol si a situatiilor in care se schimba dinamic & interactiunea cu altor entitati din grila.