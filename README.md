# Capstone

# How to Build

- Download the Source Code from the pre-release section.
- Navigate to the source code directory and open it in the command line or terminal for linux/mac users
- Type in the following command "./gradlew desktop:dist" without the quotation marks.
- An executable jar file will be built and put under desktop/build/libs folder.

# How to Run

- Java is required to run this game. If you do not have java installed please go to https://www.java.com/en/download/ to download the latest version
- Double click the executable jar that was made from the "How to Build" section.

# Feature List

- Main menu with options to play single player or multiplayer modes
- Single player
  - Random map generation
  - Random items & enemies placements
  - Moving between levels through the use of stairs
  - Camera follows the player when moving
  - Player movement with input from the keyboard using w,a,s,d,q,e,z,c & arrow keys
  - Enemies move towards & attack player on a timer
  - Inventory system that can be accessed with pressing i on the keyboard and clicking with a mouse on the specific item to use
  - Stats windows that shows up in attack mode if the player attacked an enemy or vice-versa
  - Attack functionality by moving towards an enemy
  - Equipping weapons to increase hero damage
  - Escape menu that allows the user to leave the game back to main menu or closing the game that can be accessed by pressing the escape key
- Multiplayer
  - Same base features as single player
  - A centralized server that handles the communications between players
  - Game lobby that shows other connected players names/colors before the game starts
  - Multiplayer specific items that allows interactions between the players
  - Pop up alert windows that shows the interactions between players
 
# Known Bugs

- Enemies moving through each other
- Map generation stuck on placing stairs on map which causes the game to freeze
- Empty stack exception causing the game to crash
- Moving to previous level in multiplayer causes an exception that crashes the game
