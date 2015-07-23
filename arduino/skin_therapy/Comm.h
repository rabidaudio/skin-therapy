

/*
  Comm.h - Definition of Serial protocol.
  Created by Charles Julian Knight, July 9, 2015.
*/
#ifndef Comm_h
#define Comm_h

#define PACKET_LEN 8

#define SET_PACKET 0x57  //W for write
#define READ_PACKET 0x52 //R for read

#define STATE_PACKET 0x53 //S for state
#define ERROR_PACKET 0x45 //E for error

typedef struct
{
    union
    {
        byte CID;
        char bytes[PACKET_LEN];
    };
} Packet;

typedef struct
{
  union
  {
    Packet raw;
    struct __attribute__((packed)) {
      byte CID;
      byte whichChains;
      byte enable;
      byte brightness;
      unsigned short period;
      byte waveShape;
      //byte _reserved;
    };
  };
} SetPacket;

typedef struct
{
  union
  {
    Packet raw;
    struct __attribute__((packed)) {
      byte CID;
      byte chainIndex;
      //byte _reserved[6];
    };
  };
} ReadPacket;

typedef struct
{
  union
  {
    Packet raw;
    struct __attribute__((packed)) {
      byte CID;
      byte chainIndex;
      byte enabled;
      byte brightness;
      unsigned short period;
      byte waveShape;
      //byte _reserved;
    };
  };
} StatePacket;

#endif
