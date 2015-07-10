#include "Chain.h"

Chain yellow(4);
Chain green(5);

void setup(){
  Serial.begin(9600);
  
  yellow.enabled = true;
  yellow.period = 1000;
  yellow.brightness = 128;
  
  green.enabled = true;
  green.period = 500;
}

void loop(){
  yellow.increment();
  green.increment();
  delay(1);
}
