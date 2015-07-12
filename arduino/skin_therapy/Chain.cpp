#include "Arduino.h"
#include "Chain.h"
#include "FastSine.h"

Chain::Chain(int pin)
{
  pinMode(pin, OUTPUT);
  _pin = pin;
  brightness = MAX_BRIGHTNESS;
  period = 1000; //1 second
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
            ? map(_timePosition, 0, period, 0, brightness)
            : map(2*period - _timePosition, 0, period, 0, brightness));
        break;
      case SINE_WAVE:
//        currentVal = brightness*(0.5+sin(TWO_PI*_timePosition/(float)period)/2);
//        currentVal = map(255*sin(TWO_PI*_timePosition/(float)period),-255,255,0,brightness);//actually worse
        currentVal = (brightness==255
               ? fsin(map(_timePosition, 0, 2*period, 0, 255))
               : map(fsin(map(_timePosition, 0, 2*period, 0, 255)), 0, 255, 0, brightness));
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
