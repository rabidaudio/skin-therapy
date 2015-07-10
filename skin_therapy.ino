#include "Chain.h"

Chain yellow(4);
Chain green(5);

void setup(){
  Serial.begin(9600);
  
  yellow.period = 1000;
  yellow.brightness = 128;
  yellow.enable();
  
  green.period = 100;
  green.waveShape = SINE_WAVE;
  green.enable();
}

void loop(){
  
// if(Serial.available()){
//   long val = Serial.parseInt(); //TODO read bytes directly
// }
  
  yellow.increment();
  green.increment();
  delayMicroseconds(10);
}
