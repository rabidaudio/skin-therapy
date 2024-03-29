/*
  Chain.h - A class allowing the triggering of a set of LEDs together.
  Created by Charles Julian Knight, July 9, 2015.
  
  Usage: Initalize with the pin number, configure using public attributes. Then
  on every time cycle, call increment();
  
  brightness: the peak brightness of the LED, in the range 0 to MAX_BRIGHTNESS
  period: the period (in 10's of microseconds) for a full puse cycle (default 1 second)
  waveShape: the shape of the waveform to use, e.g. CONSTANT (default), TRIANGLE_WAVE, SINE_WAVE
*/
#ifndef Chain_h
#define Chain_h

#define MAX_BRIGHTNESS 255

#define CONSTANT      0x1
#define TRIANGLE_WAVE 0x2
#define SINE_WAVE     0x4

#include "Arduino.h"
#include "Comm.h"

class Chain
{
  public:
    Chain(int pin, int id);
    byte brightness;
    unsigned short period;
    byte waveShape;
    int currentVal;
    
    void increment();
    void enable();
    void disable();
    StatePacket getState();
  private:
    int _pin;
    int _id;
    int _timePosition;
    boolean _enabled;
};

#endif
