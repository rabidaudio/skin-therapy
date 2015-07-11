

/*
  Comm.h - Definition of Serial protocol.
  Created by Charles Julian Knight, July 9, 2015.
*/
#ifndef Comm_h
#define Comm_h

#define PACKET_LEN 8

union Incoming
{
  struct
  {
    byte whichChains;
    byte enable;
    byte brightness;
    unsigned short period;
    byte waveShape;
  } packet;
  char rawBytes[PACKET_LEN-1];
};

#endif
