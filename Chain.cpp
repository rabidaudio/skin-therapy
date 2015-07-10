#include "Arduino.h"
#include "Chain.h"

Chain::Chain(int pin)
{
  pinMode(pin, OUTPUT);
  _pin = pin;
  brightness = MAX_BRIGHTNESS;
  period = 1*10000; //1 second
  waveShape = CONSTANT;
  disable();
}

void Chain::increment()
{
  _timePosition = ++_timePosition % period;
  if(_enabled){
    short lastVal = currentVal;
    
    switch(waveShape){
      case CONSTANT:
        currentVal = brightness;
        break;
      case TRIANGLE_WAVE:
        currentVal = round(brightness*(abs((period/2)-_timePosition)/(float)(period/2)));
        break;
      case SINE_WAVE:
        currentVal = round(brightness*(0.5+sin(2.0*3.1416*_timePosition/(float)period)/2));
        break;
      default:
        currentVal = 0;
    }
    if(currentVal != lastVal){
      analogWrite(_pin, min(currentVal, MAX_BRIGHTNESS));
    }
  }
}

void Chain::enable(){
  _enabled = true;
}

void Chain::disable(){
  _enabled = false;
  analogWrite(_pin, 0);
}
