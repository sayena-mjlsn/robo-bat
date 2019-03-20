# robo-bat

In this activity, you will learn how sound waves interact with objects, and with different types of materials. You and a friend can experiment with different items and materials to see how
a bat’s echolocation interacts with them.

## Getting Started
To get started, make sure that the ultrasonic sensor is appropriately connected to the Ardunio Uno. To run the Arduino code, connect the Arduino’s **Pin 12** to the sensor’s
**Trig Pin** and connect the Arduino’s **Pin ~11** to the sensor’s **Echo Pin**.

### Prerequisites

Clone or download the files. To download the files, click on the **Clone or Download** button. Then, download the files as a Zip and extract the Zip file.

### Executing Arduino code


1. Download the Arduino IDE available at https://www.arduino.cc/en/Main/Software
2. Install the IDE
3. Open **robo-bat-arduino.ino** available in the **robo-bat-arduino** folder
4. Upload the file

## Testing Arduino

To make sure that the Arduino code is running, follow the following steps.
1. Go to **Tools**
2. Click on **Serial Monitor**
If you see the arduino output, then you have successfully executed the code.
**Now make sure that you close the Serial Monitor before running the processing code.**

If you cannot see the output, go to the **Prerequisites** section and make sure that you have followed the instructions.

## Executing Processing code
1. Download the Processing IDE available at https://processing.org/download/
2. Install the IDE
3. Open **robo-bat-processing.pde** available in the **robo-bat-processing** folder
4. Go to **line 4** and make sure that **ARDUINO_SERIAL_PORT_INDEX** is has the correct value of the Arduino port based on your computer/laptop.
```final int ARDUINO_SERIAL_PORT_INDEX = 7; // may need to change '[7]' with '[Arduino_port_on_Processing]'```
5. Run the code
