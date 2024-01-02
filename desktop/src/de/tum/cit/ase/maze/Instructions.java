package de.tum.cit.ase.maze;

public class Instructions {

    /*
    Project: Maze Runner
We want you to practice composing multiple classes and files into one larger, functional piece of software. For that, we ask you to implement a small game in Java.

Game Idea
The core idea of the game is navigating a character through a maze, overcoming various challenges to reach the exit. The maze contains multiple different elements like traps, enemies, and keys that the player must interact with or evade to escape. The game unfolds in a maze enclosed by walls on all sides, featuring an entry point where the player starts and an exit to reach for completing the game. Within the maze, the player will encounter an intricate network of interior walls that create not only a challenging path but also various dead-ends. Randomly scattered throughout the maze are traps and enemies that the player must avoid or overcome, as well as keys that must be collected to unlock the exit.




Game Logic
Your game must implement the following mechanics:

Walls and Paths
The game world is designed as a maze, consisting of traversable paths and walls. Each maze has exactly one entrance and at least one, potentially multiple exits. The exit points are exclusively situated on the outer border of the maze, which is otherwise completely enclosed by walls without any gaps. The entrance can be anywhere in or on the border of the maze. When playing the game, it's crucial that the entrance and exit are clearly distinguishable from walls and each other. The entrance should be uniquely identifiable, as should the exit, but in a distinct manner.

In this context, 'paths' refer to the free space within the maze. The density of the maze can vary significantly, ranging from sparse layouts with abundant open space and fewer walls, to dense configurations characterized by numerous walls and limited walkable paths.

Character
The main character must be able to move through the maze in four directions (up, down, left, right). Generally, the character can only walk in free spaces; they cannot walk through walls. The character must have a number of lives. If the character loses all their lives before reaching and opening the exit (see below), it's game over.

Obstacles
The maze contains at least two kinds of obstacles. Essentially, an obstacle is an additional object on an otherwise free path within the maze that causes the character to lose a life when contact occurs. Like the character, obstacles can only exist within free paths and cannot pass through walls.

The two required kinds of obstacles are:

Traps: Obstacles with a fixed position within the maze.
Enemies: Obstacles that are dynamic. This means their position within the maze changes regularly in short intervals. For minimal requirements, they must move randomly throughout the maze but never leave it; however, for bonus points, they can exhibit more intelligent behavior (see below).
Keys
The ultimate goal for the main character is to exit the maze. However, to open the exit, they must have collected a key. There will be at least one, but potentially multiple keys in each maze. The character must collect at least one key to use the exit of the maze. Attempting to exit without the key should result in the path being blocked (think: the exit behaves like a wall until you have a key).

HUD
The game must have at least a small, basic HUD that is visible to the player at all times while navigating the maze. It must at least display the following information:

Amount of lives remaining
Whether a key has been collected
Game Menu
There must be a game menu displayed at the launch of the game and any time a player presses the Esc key during gameplay. If a player presses Esc to access the menu, the game itself must be paused (i.e., the main characters and all enemies stop moving). In this menu, players must at least be able to:

continue the game (if coming from Esc)
load a new map file and start a new game
exit the game
ALL UI and MENUS must be libGDX based. No JavaFX or something.

Victory and Game Over
If the player can leave the maze without losing all lives, they achieve a victory. In this case, display that the user has won, and stop the gameplay.

If the player loses all lives, the game is over. In this case, display that the game is over, and stop the gameplay.

In both cases, allow to go back to the main menu.

Technical Requirements
Maze files
You should not generate or define a maze yourself within your program code. Instead, your program must be able to run any arbitrary maze stored in a Java properties file. If the player chooses the "load map" option in your game menu, your program must open a File Chooser and allow them to select the file. Then, your program should read the file and start the game based on the maze defined in this file.

Properties files are a very simple way to store data in files in Java. Essentially, they just store key-value pairs of Strings; think of it as a Map<String, String>. In our maze files, the key specifies the x and y coordinates, separated by a comma, for example: 5,6.

The value specifies the type of the object at the given coordinates. The following types exist:

Value | Type
-------------------
0     | Wall
1     | Entry point
2     | Exit
3     | Trap (static obstacle)
4     | Enemy (dynamic obstacle)
5     | Key
The coordinate system starts at the bottom left with 0,0. x coordinates extend to the right, and y coordinates go upwards.

Check out one of the files in the maps directory to get an idea.

Graphics
Your game must be a 2D game based on libGDX with a top-down view.

Each object must be rendered as a 2D sprite at their respective coordinates as specified by the map file. Ensure to use proper, open-source 2D assets (images) to render the different object types. We recommend using simple 16x16 pixel graphics. Recommended sources include Kenney or OpenGameArt.

We provide you with a skeleton for such a game, so you don't have to start from scratch.

Viewport
Different computers have different screen sizes. Some players may want to play your game on a very large screen or maybe in a very small window.

Therefore, please make sure to fulfill the following important requirements:

The maze may be larger than the screen. Implement a camera movement mechanism that ensures that the player character is always visible within at least the middle 80% of the screen horizontally and vertically during gameplay. If the window is resized, ensure to readjust the camera position to adhere to this rule as well.
Your game must adapt to different sizes of your program window.
DO NOT scale the game items if the window size changes. If the user scaled up the window, that means they can now see a larger segment of the maze.
Music and Sounds
Include music and sound effects in your game. Use the Music and Sound classes of libGDX to achieve this. There is an example of playing music in the template.

Ensure to balance volume of music and sound effects. Choose royalty-free tracks and effects, for example from OpenGameArt.

Background Music
Gameplay: Loop a background track during gameplay, matching the game's intensity and theme.
Menu: Play a different, calmer track for the game menu.
Sound Effects
Play sound effects if something happens to make the experience more immersive. Play proper sound effects at least for the following events:

Life lost
Key collected
Victory
Game over
Code Structure
Implement your game in an object-oriented manner using concepts presented in the lecture.
The different object types like wall, entry, exit, trap, and enemy should be dedicated classes, inheriting from at least one common superclass, such as a GameObject that contains shared functionality and properties.
Avoid code duplication using inheritance, delegation, and proper usage of method extraction.
Documentation
Your code must be documented properly. Document each class and each method with proper JavaDoc!
Create a README file that documents your project structure so a reviewer can easily understand:
your code structure - how is everything organized, what does the class hierarchy look like?
how to run and use your game?
rules for and description of game mechanics that go beyond the minimal requirements
Checklist
When creating the project, remember the following points:

Your program must be able to read any maze from a properties file and play it.
The character must be movable with the arrow keys in four directions.
The character has a limited number of lives.
The character must collect a key from the maze and reach the exit before losing all lives.
Static traps and dynamically moving enemies are obstacles. On contact with any of them, the player loses one life.
Scroll Mechanism: If the character walks towards the edge of the window, the displayed segment of the maze must change so that the character is in the center of the screen again.
HUD: Display the amount of lives left and key collection status at all times.
Game Menu: Available on startup and through the Esc button; must allow to continue playing, choosing a new map, or exiting.
Victory and Game over: Your game must display that the player has won or lost, stop the gameplay and allow to return to the main menu afterwards.
Render the game using libGDX as a 2D game with a top-down view using simple 2D assets.
Support different screen sizes.
Play background music during gameplay and in the menu
Use sound effects when something happens in the game
Use object-orientation to implement your game.
Document your code and your project properly.


Extension for Bonus Points
You are free to extend the game as you like as long as the minimal requirements are and stay fulfilled.

Functionality that goes beyond the minimal requirements may result in bonus points.

Some examples follow below. Especially the first one is a cool thing to have in a game like this and will definitely earn you more than one point ;)

Enemies move intelligently towards the main character using a path-finding algorithm if they are within a certain range, instead of moving randomly.
Point systems: Can players get a score in the end, e.g., based on time?
Collectable lives.
More types of obstacles.
Further abilites for the main character other than walking, such as
combat with enemies
collectable power ups (faster running, shield, …)
Fog of War.
Movable walls.
Multiple kinds of things other than a key to exit (remember, having to collect a key is the minimal requirement; you can require more things before you open the exit).
…
Unleash Your Creativity! Treat this project as your experimental playground, a space where you can try out innovative ideas, apply your acquired knowledge, and continuously learn along the way. We're excited to see the remarkable game you'll create and be impressed by your ingenuity!

The general theme should be Harry Potter. Thus, call smart enemies "Dementors" or "Death Eaters", have the character fly over walls using brooms or implement collectible items like "love potion" or "potion of despair" to gain or lose lives would be cool.



Organizational considerations
Third party libraries
Third party code libraries that can be installed through Gradle are in general not allowed. We want you to implement things yourself and avoid relying on third party features. You are free to use all the various functionality built into libGDX. However, this covers only the base framework; not all libGDX extensions are allowed. The following libGDX extensions are approved to use: Box2D, Bullet, FreeTypeFont, Controllers, Tools, Box2DLights. Explicitly not allowed are: Ashley, AI.
     */
}
