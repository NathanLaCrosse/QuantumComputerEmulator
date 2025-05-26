# Notice
I'm currently coming back to this project to spruce it up and make this readme more presentable (as well as the code). The tutorial has been added, with more to come. Stay tuned.

# Quantum Computer Emulator

This project runs a quantum computing emulator that allows you to visualize and create quantum circuits. Along with this, there are a few other projects I made along the way in here, such as a double slit experiment and a matrix calculator.

## Notes on Setting up JavaFX (If running the program)
First, you'll need a javafx sdk downloaded on your device. If you don't have this, download the sdk here: https://gluonhq.com/products/javafx/. Once downloaded, unzip the file and keep track of where it 
is on your computer, as we'll need to reference it a few times.

Next, clone this repository and open it in your IDE. I used VS Code to develop this, so all of the instructions will be in that IDE. In vscode, do ctrl+shift+P and look for the command labelled 
Java:Configure Classpath. Finally, click on the tab labelled library. Your result should look similar to this:

![tutorial1](https://github.com/user-attachments/assets/530131e5-5056-4edb-95b3-2229125b478a)

To get JavaFX working, we need to add its code to the library. Click on the add library button and enter the lib folder in the sdk downloaded in the first step. Select all of the jar files in the folder
and confirm selection.

![tutorial2](https://github.com/user-attachments/assets/dcb09cb5-29bb-48b7-aafd-4e07b034f028)

Your library tab should look like below:

![tutorial3](https://github.com/user-attachments/assets/db41dd90-4525-43bf-9ee3-223a9a9f44b9)

There is one final step: telling the Java runtime where the sdk is located. Open the launch.json file. Edit the two "vmArgs" so that the file path routes to your sdk's lib folder.

![tutorial4](https://github.com/user-attachments/assets/f683f7d5-938f-4174-a896-211b6b0e847b)

That's it! Try running the program - an application after a moment. 
