# MultiMedia1
This project covers resampling and filtering and how it affects visual media types like images and video.

How to run this program:

run from the command file, first compile:

>> javac ImageDisplay.java

and then run the program with desired inputs, for example:

>> java ImageDisplay images/worldmap.rgb 0.5 1 200

this is run ImageDisplay on the worldmap.rgb image that is within the images folder.
The S value is: 0.5
The A value is: true
The w value: 200

another example:

>> java ImageDisplay images/miamibeach.rgb 0.7 0 400

this is run ImageDisplay on the worldmap.rgb image that is within the images folder.
The S value is: 0.7
The A value is: false
The w value: 400


to view the overlay of pixels press the control button and move the mouse to see the pixels in a wxw window.
The overlay is displayed in the top left corner (i could not get it to appear around the mouse and properly reflect the mouses coordinates)

////////////

A sample program to read and display an image in JavaFX panels. By default, this program will read in the first frame of a given .rgb video file.


Unzip the folder to where you want.
To run the code from command line, first compile with:

>> javac ImageDisplay.java

and then, you can run it to take in two parameters "pathToRGBVideo" and "n":

>> java ImageDisplay pathToRGBVideo n

where the parameter "n" is a "dummy" parameter used to give an additional example of command line args in Ja
