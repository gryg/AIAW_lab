
# Descriere Algoritm - Problema Lumii Blocurilor

## Overview
Acest set de fisiere implementeaza un algoritm pentru rezolvarea problemei lumii blocurilor folosind un planificator partial ordonat (POP).

## Clase Principale

### `Action`
- Reprezinta o actiune cu preconditiile si efectele acesteia.
- Verifica aplicabilitatea actiunii intr-o stare data.

### `State`
- Reprezinta starea lumii blocurilor.
- Verifica satisfacerea preconditiilor de catre o stare.

### `Plan`
- Gestioneaza lista de actiuni, legaturile cauzale si constrangerile de ordine.
- Adauga actiuni la plan, actualizand ordinea si rezolvand amenintarile.

### `POP`
- Implementeaza logica planificatorului.
- Genereaza un plan pentru trecerea de la starea initiala la starea finala.

### `BlockWorldProblem`
- Clasa principala care initializeaza problema.
- Seteaza starea initiala si finala, si defineste actiunile posibile.
- Utilizeaza `POP` pentru a gasi un plan de actiuni.

## Algoritm
1. **Initializare**: Se stabilesc starea initiala si starea finala dorita, impreuna cu setul de actiuni posibile.
2. **Planificare**: Se utilizeaza `POP` pentru a construi un plan care sa transforme starea initiala in starea finala.
   - Fiecare actiune este evaluata in functie de preconditiile si efectele sale.
   - Se construiesc legaturi cauzale si constrangeri de ordine pentru a asigura un plan coerent si fara conflicte.
3. **Executie**: Daca se gaseste un plan valid, acesta este afisat. Daca nu, se semnaleaza imposibilitatea gasirii unei solutii.

## Observatii
- Algoritmul este util in situatii unde este necesara planificarea unor actiuni intr-o ordine specifica pentru a atinge un anumit obiectiv.
- Eficienta algoritmului depinde de complexitatea starii problemei si de numarul actiunilor disponibile.
