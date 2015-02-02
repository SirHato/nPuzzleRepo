## GENERAL ## 

This app implements a n-puzzle game. The n-puzzle is a game known by a variety of names: Game of Fifteen, 8-puzzle, 15-puzzle, Mystic Square, and others. All the names refer to the same game, however: a two-dimensional puzzle with one empty space into which some numbered tiles can slide horizontally or vertically to occupy. The goal is to arrange the board from smallest to largest. A variation of this game is to replace the numbers on the tiles with cropped images from a larger one. This app uses the variation. 

In the home activity the user can choose between taking a picture or selecting one from the phone's gallery. Then, the picture gets cropped and the user is given the option to apply a filter to the image, cancel or continue to the game. When pressing continue the (edited) image is saved and the user is taken to a screen where the difficulty has to be selected. After choosing the difficulty, the game is started. The player wins when the puzzle is solved. 

## IMPLEMENTATION ##  

- HomeActivity - 
The buttons contain the function function that needs to be called in their xml file. 
The "Take Photo" button calls the takePhoto method. In this method a new Intent to take a picture with android's camera framework. Once the photo is saved, the uri is put into the intent using putExtra and then onActivityResult is called. This also happens for the "Choose Photo" button, except that it calls the choosePhoto method. 

In the onActivityResult method we check which action we are dealing with using the requestcode. Then, the Edit activity is started with the uri in the intent. 


- EditActivity - 
When the uri is saved the program tries to open the image using that uri. If it succeeds, a copy will be made in order to obtain a mutable bitmap, which we can edit. 
There might me other ways to get a mutable bitmap or transform a regular bitmap to a mutable one, but out of all found options, this was best solution. However, the program can crash due to an out of memory error if the opened out of the gallery is too large. The mutable bitmap gets cropped and then saved. 

All the buttons refer to onClick. In onClick the code checks which button called the function using a switch-case with v.getId()  

Storing the bitmap is done with functions found on the web, which i modified:  
- https://www.pceworld.com/tags/android-image/63
- http://developer.android.com/guide/topics/media/camera.html#saving-media

In the filter function, the pixels of the bitmap are put into an array in order to change the RGB values of each pixel. The change of the values depend on which filter was pressed. Everytime the filter function gets called, mutableBitmap is reset to prevent double filtering. 


- GameActivity - 
First the uri of the image and the difficulty settings are retrieved from Edit and then the image is retrieved using the uri. The image is sliced into equal pieces (squares) by calling the slicer function and then put in an array. Then a new adapter is created and the grid is set. The player has to wait 3 seconds before he/she has the ability to play. 

The onBackPressed function is there to disable the use of the back button. In my eyes this was the easiest to do this. The back button was causing some issues. 

By pressing the menu button the player can start a new game with a different, shuffle the board or quit. 

To improve in next versions:
- Check if game can be solved
- Better memory management
- Tighten up the navigation. For example, if you press the back button when te game is finished, you get back in the game screen. That shouldn't be able to happen. It doesn't cause the program to crash though. 

If all the images are in ascending order, the puzzle is solved. This is checked by the function checkWin, which gets called every time a move is made.


- MyAdapter x Cell -
When the adapter is made, an ArrayList is filled with my custom objects: Cell. The last cell in the list is the empty tile.  A Cell contains a bitmap (one of the slices), id, position and a boolean to check if it's the empty tile. I'm aware of the fact that I could've used the Id of the last cell in the list, but I chose to use a boolean because it seemed more appropriate to me and less code. 

In getView the code checks if this view has already been searched for. If not, the view gets placed in the ViewHolder to avoid the constant use of findById calls. 
I found this technique on youtube and decided to implement it as well to keep the gameplay as fast as possible. 







