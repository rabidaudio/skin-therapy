#include "Arduino.h"
#include "Chain.h"

Chain::Chain(int pin)
{
  pinMode(pin, OUTPUT);
  _pin = pin;
  
  brightness = MAX_BRIGHTNESS;
  enabled = false;
  period = 0;
  
  analogWrite(_pin, 0);
}

void Chain::increment()
{
  _timePosition = ++_timePosition % period;
  if(enabled){

    analogWrite(_pin, min(currentValue(), MAX_BRIGHTNESS));
  }
}

short Chain::currentValue(){
    short currentVal = brightness;
    if(period>0){
      currentVal = round(brightness*(abs((period/2)-_timePosition)/(float)(period/2)));
    }
    return currentVal;
}

float Chain::test(){
  return round(brightness*(abs((period/2)-_timePosition)/(float)(period/2)));
}
