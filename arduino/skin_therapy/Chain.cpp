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
  _timePosition = ++_timePosition % (2*period);
  if(_enabled)
  {
     int lastVal = currentVal;
    
    switch(waveShape)
    {
      case TRIANGLE_WAVE:
        currentVal = (_timePosition < period
            ? map(_timePosition, 0, period, 0, brightness)//brightness*(_timePosition/(float)period)
            : map(2*period - _timePosition, 0, period, 0, brightness)//brightness*((2*period - _timePosition)/(float)period)
            );

        break;
      case SINE_WAVE:
        currentVal = round(brightness*(0.5+sin(2.0*3.1416*_timePosition/(float)period)/2));
        break;
      case CONSTANT:
      default:
        currentVal = brightness;
    }
    if(currentVal != lastVal)
    {
      analogWrite(_pin, currentVal);
    }
  }
}

void Chain::enable()
{
  _enabled = true;
}

void Chain::disable()
{
  _enabled = false;
  analogWrite(_pin, 0);
}
