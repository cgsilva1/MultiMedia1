# MultiMedia1 - Resampling & Filtering
This project covers resampling and filtering and how it affects visual media types like images and video.

Images are getting larger and larger in size and sometimes cannot be properly viewed on a computing screen. This application will help view the
image in a resized format and optionally browse through all the details. Input to your
program will take four parameters:
- The first parameter is the name of the image, which will be provided in an 8 bit
per channel RGB format (Total 24 bits per pixel). You may assume that all
images will be of the same size for this assignment (16xHD format 7680x4320).
- The second parameter is a floating-point value S (between 0 and 1) suggesting by
how much the image has to be down scaled so as to fit in a window for display.
This will result in resampling your image.
- The third parameter will be a Boolean value A (0 or 1) suggesting whether you
want to deal with aliasing. A 0 signifies do nothing (aliasing will remain in your
output) and a 1 signifies that anti-aliasing should be performed by using an
averaging low pass filter.
- The fourth parameter will give the square window width/height w for showing the
original image overlaid if the control key is pressed. As you move your mouse
around the image with your control key pressed, you should see the original
detailed image overlaid about the mouse area in a w x w window

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
