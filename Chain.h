/*
  Chain.h - A class allowing the triggering of a set of LEDs together.
  Created by Charles Julian Knight, July 9, 2015.
  
  Usage: Initalize with the pin number, configure using public attributes. Then
  on every time cycle, call increment();
*/
#define MAX_BRIGHTNESS 255

#ifndef Chain_h
#define Chain_h

#include "Arduino.h"

class Chain
{
  public:
    Chain(int pin);
    short brightness;
    long period;
    boolean enabled;
    void increment();
    short currentValue();
    float test();
  private:
    int _timePosition;
    int _pin;
};

#endif
