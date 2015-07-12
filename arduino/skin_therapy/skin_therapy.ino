#include "Chain.h"
#include "Comm.h"

Chain yellow(4, 0);
Chain green(5, 1);

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
  
  if(++cycles == 100) //every 100 cycles, check for data
  {
    cycles = 0;
    if(Serial.available() >= PACKET_LEN)
    {
      handlePacket();
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


void handlePacket()
{
  Packet p;
  Serial.readBytes(p.bytes, PACKET_LEN);
  switch(p.CID)
  {
    case SET_PACKET: //write the parameters to the chains
      SetPacket i;
      i.raw = p;
      
      for(int x=0; x<NUM_CHAINS; x++)
      {
        if(bitRead(i.whichChains, x))
        {
          Chain c = (*chains[x]);
          c.brightness = i.brightness;
          c.period = i.period;
          c.waveShape = i.waveShape;
          (i.enable ? c.enable() : c.disable());
        }
      }
      break;
    case READ_PACKET: //respond back with the chain's state
      ReadPacket r;
      r.raw = p;
      if(r.chainIndex < 0 || r.chainIndex > NUM_CHAINS){
        sendError();
      }else{
        StatePacket state = (*chains[r.chainIndex]).getState();
        Serial.write(state.raw.bytes);
      }
      break;
    default:
      sendError(); //unknown packet
  }
  delay(100);
}
void sendError()
{
  Packet error;
  error.CID = ERROR_PACKET;
  Serial.write(error.bytes);
}
