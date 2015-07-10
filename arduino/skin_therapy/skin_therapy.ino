#include "Chain.h"
#include "Comm.h"

Chain yellow(4);
Chain green(5);

Chain* chains[] = { &yellow, &green };
const int NUM_CHAINS = 2;

void setup()
{
  Serial.begin(9600);
  
  yellow.period = 1000;
  yellow.brightness = 128;
  yellow.enable();
  
  green.period = 100;
  green.waveShape = SINE_WAVE;
  green.enable();
  

}

void loop()
{
  
  if(Serial.available())
  {
    Incoming i; 
    i.raw = Serial.parseInt(); //TODO read directly
    for(int x=0; x<NUM_CHAINS; x++)
    {
      if(bitRead(i.packet.whichChains, x))
      {
        (*chains[x]).brightness = i.packet.brightness;
        (*chains[x]).period = i.packet.period;
        (*chains[x]).waveShape = i.packet.waveShape;
        (i.packet.enable ? (*chains[x]).enable() : (*chains[x]).disable());
      }
    }
  }

  for(int x=0; x<NUM_CHAINS; x++)
  {
    (*chains[x]).increment();
  }  
  delayMicroseconds(10);
}
