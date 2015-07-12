#include "Chain.h"
#include "Comm.h"

#define DEBUG 1

Chain yellow(4);
Chain green(5);

Chain* chains[] = { &yellow, &green };
const int NUM_CHAINS = 2;

void setup()
{
  Serial.begin(9600);
  
//  yellow.period = 100;
//  yellow.brightness = 128;
//  yellow.waveShape = SINE_WAVE;
//  yellow.enable();
//  
//  green.period = 100;
//  green.waveShape = SINE_WAVE;
//  green.period = 100;
//  green.enable();
}

int cycles = 0;

void loop()
{
   long t = micros(); 
  //every 100 cycles, check for data
  if(++cycles == 100) //TODO
  {
    cycles = 0;
    
    if(Serial.available()>=PACKET_LEN)
    {
      Incoming i; 
      Serial.readBytes(i.rawBytes, PACKET_LEN);
      
      if(DEBUG)
      {
        Serial.println(sizeof(i.packet));
        Serial.println(sizeof(i.rawBytes));
        Serial.print("rawBytes:");
        Serial.println(i.rawBytes);
        Serial.print("whichChains:");
        Serial.println(i.packet.whichChains);
      }
      for(int x=0; x<NUM_CHAINS; x++)
      {
        if(bitRead(i.packet.whichChains, x))
        {
          if(DEBUG)
          {
            Serial.print("==for ");
            Serial.println(x);
            Serial.println( (i.packet.enable ? "enabled" : "disabled"));
            Serial.print("brightness: ");
            Serial.println(i.packet.brightness);
            Serial.print("period: ");
            Serial.println(i.packet.period);
            Serial.print("wave shape: ");
            Serial.println(i.packet.waveShape);
          }
          
          Chain c = (*chains[x]);
          c.brightness = i.packet.brightness;
          c.period = i.packet.period;
          c.waveShape = i.packet.waveShape;
          (i.packet.enable ? c.enable() : c.disable());
        }
      }
      delay(100);
    }
  }

  for(int x=0; x<NUM_CHAINS; x++)
  {
    (*chains[x]).increment();
  }

  //execution time depends significantly on the waveShape and the number of chains
  // for more accuracy, we can add an external timer, but it shouldn't be neccessary
  delayMicroseconds(1000-(micros()-t));
}
