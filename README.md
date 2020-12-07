# Capstone

# How to Build

- Download the Source Code from the pre-release section.
- Navigate to the source code directory and open it in the command line or terminal for linux/mac users
- For Linux & Mac users type the following command "chmod +x ./gradlew" without the quotation marks
- Type in the following command "./gradlew desktop:dist" (Linux/Mac) or "gradlew desktop:dist" (Windows) without the quotation marks
- An executable jar file will be built and put under desktop/build/libs folder.

# How to Run

- Java is required to run this game. If you do not have java installed please go to https://www.java.com/en/download/ to download the latest version
- For Linux & Mac users make the output file executable by running the command "chmod +x desktop-1.0.jar" which is located in desktop/build/libs folder
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
- Server crashes sometimes (still unknown reasons)
