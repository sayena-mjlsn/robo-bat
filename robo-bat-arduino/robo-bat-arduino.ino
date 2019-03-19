/*  code belongs to 
 *  https://randomnerdtutorials.com/complete-guide-for-ultrasonic-sensor-hc-sr04/
 */

int trigPin = 12;   
int echoPin = 11;    
float duration, cm, inches;

void setup() {
//Serial Port begin
Serial.begin (9600);
//Define inputs and outputs
pinMode(trigPin, OUTPUT);
pinMode(echoPin, INPUT);
}

void loop() {
// The sensor is triggered by a HIGH pulse of 10 or more microseconds.
// Give a short LOW pulse beforehand to ensure a clean HIGH pulse:
digitalWrite(trigPin, LOW);
delayMicroseconds(5);
digitalWrite(trigPin, HIGH);
delayMicroseconds(10);
digitalWrite(trigPin, LOW);

// Read the signal from the sensor: a HIGH pulse whose
// duration is the time (in microseconds) from the sending
// of the ping to the reception of its echo off of an object.
pinMode(echoPin, INPUT);
duration = pulseIn(echoPin, HIGH);

// Convert the time into a distance
inches = (duration/2.0) / 74.0 - 1.0;   // Divide by 74 or multiply by 0.0135

//for arduino serial monitor
Serial.println(inches);


delay(500);
}
