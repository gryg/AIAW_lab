TODO: FIX shutting automatically shutting down after all of the dirt is removed && Improve diagonal detection
The rest of the code is commented in the files.

Used some of the concepts described in this README file, https://github.com/eishub/vacuumworld/blob/master/README.md ,
which, although were for a more complex design of a similar problem, proved to be useful in delevoping the solution for this laboratory.

# VacAgent basic strategy

## Initialization
- Starts at (0,0), facing north.
- Marks start position as open and visited.

## Perception
- Receives environment data (obstacles, dirt).
- Updates status of current position.

## Decision Making
- Cleans if current position is dirty.
- Moves forward if facing open, unvisited space.
- Chooses turn direction based on unexplored areas.
- Seeks nearest unvisited position when other options unavailable.

## State Updating
- Post-action: marks position as clean/visited or changes orientation.

## Completion
- Stops when all positions visited and cleaned.
