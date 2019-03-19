import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import processing.serial.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class robo_bat_processing extends PApplet {

  // import processing.serial.*;
Serial _serialPort;  // communicate with the Arduino via the serial port

final int ARDUINO_SERIAL_PORT_INDEX = 7; // may need to change '[1]' with '[Arduino_port_on_Processing]'

int MaxRuler = 9; // the max number shown on the ruler

boolean Reflective = true;  // if the item is reflective show the reflected signals
float Scale = 0.00f;

float _currentDist = 0.0f; // distance detected by arduino in 'inches'
float BtnX = 1114;  // 'x' value of pause/play button
float BtnY = 64;     // 'y' value of pause/play button
float x1;  // the live value of 'x' on the screen
float ToggleX = 921; // 'x' value of signal button
float ToggleY = 48;  // 'y' value of signal button
float MainBatX = 36; // 'x' value of main bat character
float [] BatX = new float[6]; // 'x' value of bat images in the background
float [] BatY = new float[6]; // 'y' value of bat images in the background
float Height;  // height of the screen
float Angle;  // angle of the flying bats shown in the background
float Value = 0.0f;  // the received value mapped to the size of the ruler on the screen

String inString = null;  //  the input received from arduino
 
boolean Reading = true;  // to receive data from arduino 
boolean Paused = false;  // to freeze the screen and pause the reading process
boolean HiddenValue = false;  // to work with the distance within the visual constraints of the interface
boolean Toggle = false;  // Toggle button to turn signals on/off

PImage[] bat = new PImage[8];
PImage bg; 
PImage Play;
PImage Pause;
PImage Pointer;
PImage Bat;
PImage MainBat;
PImage Mouse;
PImage SadBat;
PImage Signal;
PImage SentSignal;
PImage ReflectedSignal;
PImage playtext;
PImage pausetext;
PImage ToggleOff;
PImage ToggleOn;
PImage PausedBackground;

PFont f;


public void setup(){
    // the size of the interface
  textSize(23);
  //Fullscreen();
  /*
  *loading assets
  */
  bg = loadImage("bg.png");  // the backgroudn image
  Play = loadImage ("play.png");  // the play button
  Pause = loadImage ("pause.png");  // the pause button
  Pointer = loadImage ("pointer.png");  // the pointer that points to a number on the ruler
  Bat = loadImage("bat.png");  // the bats that appear in the background
  MainBat = loadImage("bat-1.png");  // the main bat character-first version
  Mouse = loadImage("mouse-1.png");  // the mouse character
  SadBat = loadImage("sad-bat.png");  // the main bat character-second version
  SentSignal = loadImage("signa-1.png");  // sent signals in yellow
  ReflectedSignal = loadImage("signa-rec.png");  // reflected signals in blue
  playtext = loadImage("play-text.png");   // label of the play button
  pausetext = loadImage("pause-text.png");  // label of the pause button
  ToggleOff = loadImage("toggle-off.png");  // toogle switch off
  ToggleOn = loadImage("toggle-on.png");    // toggle switch on
  PausedBackground = loadImage("rec-paused.png");  // paused background of the data that is being read

  _serialPort = new Serial(this, Serial.list()[ARDUINO_SERIAL_PORT_INDEX], 9600); //open the serial port
  
   
  frameRate( 60 ); 
  
   for (int i = 1; i <= 5; i = i+1) {  // randomizing the appearance of bats in the background
        BatX[i]=random(1000);
        BatY[i]=random(600);
    }
}

public void draw(){
  background(bg);  // loading the background image
  Angle = 70;  
    if (Height < 0) { // to limit the appearance of bats to the size of screen
        Height=0;
    }
    if (Height > 800) {  // to limit the appearance of bats to the size of screen
        Height=800;
    }
    
    for  (int i = 1; i <= 5; i = i+1) {    // generate bats in the background randomly
        BatX[i] = BatX[i] - cos(radians(Angle))*(10+2*i);
        image(Bat, BatX[i]-50, BatY[i]-100, 150, 52);  
        
        if (BatX[i] < -300) {
            BatX[i]=1000;
            BatY[i] = random(600);
           BatY[i] = BatY[i] - cos(radians(Angle-_currentDist))*(10+2*i);
        }
        
    Value = map(_currentDist, 0, 10, 250 , 950);  // mapping the received value to the size of the ruler
    x1 = lerp(x1, Value, 0.05f);  // the easing function
    
    /*pausing the process and freezing the screen*/
    if (!Paused){ 
    // change the pause/play button
    image(Pause,BtnX,BtnY, 21 ,21);
    image(pausetext,BtnX-9,117, 40 ,21);
    if(mousePressed){
      if(mouseX>BtnX && mouseX <BtnX+54 && mouseY>BtnY && mouseY <BtnY+54){
         println("----Pause button has been clicked----");
         Paused = true;
         Reading = false;
         delay(30);
    }
   } 
 }
 
 /*resuming the process*/
 else {
    // change the pause/play button
    image(Play,BtnX+2,BtnY, 21 ,21); 
    image(playtext,BtnX-8,117, 40 ,21); 
    image(PausedBackground,964,670, 162.39f ,66); 
    if(mousePressed){
      if(mouseX>BtnX && mouseX <BtnX+54 && mouseY>BtnY && mouseY <BtnY+54){
         println("----Play button has been clicked----");
         Paused = false;
         Reading = true;
         delay(30);
    }
   } 
 }
 
 
 /*Turning the signal button on/off*/
 if (!Toggle){  // turning off the signal button
 image(ToggleOff,ToggleX,ToggleY, 106 ,54); 
 if(mousePressed){
  if(mouseX>ToggleX && mouseX <ToggleX+106 && mouseY>ToggleY && mouseY <ToggleY+54){
   println("----Signal button has been turned on---");
   Toggle = true;
   delay(20);
   
  }
 } 
 }
 
 else {  // turning on the signal button
   image(ToggleOn,ToggleX,48, 108 ,57); 
   if(mousePressed){
     if(mouseX>ToggleX && mouseX <ToggleX+106 && mouseY>ToggleY && mouseY <ToggleY+54){
       println("----Signal button has been turned off---");
       Toggle = false;
       delay(20);
    }
   } 
 }
 
  if (!HiddenValue){  // if the received value is within the size of the ruler show the characters
    image(Pointer,x1,600, 16,14); 
    image(Mouse,x1-55,467, 123,117);
    image(MainBat,MainBatX,391, 174,191); 
  }
  else {  // if the received value is outside the size of the ruler show the sad bat character and hide other characters
    image(SadBat,MainBatX,238, 339,344);
  }
  
  if (Toggle){
    Signal_1();  // calls the signal function to send signal
    Signal_2();  // calls the signal function to reflect signal
  }
  GenerateText(255,255,255);  // calls the function to demonstrate the live distance on the screen                                                                                   
  println("Current Distance = "+_currentDist);
     }
}



/*
* called automatically when new data is received over the serial port
* code written by John F.
*/
public void serialEvent (Serial _serialPort) {
  if(Reading){  // read data if the process has not been paused by the user
  
  try {
    /* grab the data off the serial port. See: 
    *  https://processing.org/reference/libraries/serial/index.html
    */
    inString = trim(_serialPort.readStringUntil('\n'));
    if(inString != null){
      _currentDist = PApplet.parseFloat(inString);// convert the received value to float
      /* normalizing the received value */
      if (_currentDist >= MaxRuler && _currentDist <12){  // to accurately demonstrate the data, the value between 9 and 12 is considered as 9 which is the max size of the ruler
        _currentDist = MaxRuler;
      }
      
      if (_currentDist <= 0){  // do not show the value if it is less that 0
        _currentDist = 0.0f;
        
      }
      
      if (_currentDist >= 12){  // if the distance is more than 12 inches, the mouse gets hidden and the item cannot be detected by the sensor
        HiddenValue = true;
      }
      
      if (_currentDist <= 12 && _currentDist >= 0){ // if the distance is within the size of the ruler, the characters are shown on the screen
        HiddenValue = false;
      }
    }
    
  }
  catch(Exception e) {
    println(e);
    }
  }
}

/* show the value on the screen */
public void GenerateText(int r, int g, int b){
  fill(r,g,b);  // defines the text color
  
  if (_currentDist >= 12){  // if the item is not within the defined size of the ruler, show 'reading...' in the textbox
    fill(21,45,62);
    text("Reading...",990,710);
  }
            
  else{   // if the item is within the defined size of the ruler, show the value in the textbox
    fill(21,45,62);
    text(_currentDist +" inches",980,710);
  }
  
}


/* show signals */
public void Signal_1(){ 
  Scale = 0.1f;  // define the size of the signals as they are generated on the screen
  int fixedheight = 405;  // height of the first signal
  Reflective = true;  
  
  for ( float z = 210 ; z<= Value-150 ;){  
     if (Value >= 900){ 
        z+= 45;
        Reflective = false;  // do not show reflected signals when the item is not reflective
      }
      
      else{
        z = lerp(z,Value-150, 0.05f);  // the easing function
        z+=35;
      }
      
      Scale = Scale + 0.1f;  // redefine the scaling size for the reflected signals
      image(SentSignal, z, fixedheight+=2, 30*Scale, 80*Scale);
      if ( z>=1100){ // if the reflected signal goes beyond the size of the screen, stop generating them
         z = 205;
         break;
       }
    }
}
    
  public void Signal_2(){   
    if (Reflective){
    Scale = 0.2f;
    int fixedheight1 = 580;
    if ( _currentDist <= 9){
      print("jeghgre");
    for (float z = Value-100 ; z>= 230 ; z-=35){ 
       z = lerp(z, 210, 0.05f);  // the easing function
      Scale = Scale + 0.1f;
      image(ReflectedSignal, z, fixedheight1-=10, 25*Scale, 60*Scale); 
      if ( z<200 ){  // if the reflected signal is too close to the bat character, stop generating them
        break;
     }
     
    }
  }
}
    
}
  
  public void settings() {  size(1200, 800);  smooth(); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "robo_bat_processing" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
