#ifndef FastSine_h
#define FastSine_h

#include "Arduino.h"

// first quadrant data
// round(127*sin(2*PI*[0..63]/256)) + 127
byte table[] = {
  127, 130, 133, 136, 139, 143, 146, 149, 152,
  155, 158, 161, 164, 167, 170, 173, 176, 178,
  181, 184, 187, 190, 192, 195, 198, 200, 203,
  205, 208, 210, 212, 215, 217, 219, 221, 223,
  225, 227, 229, 231, 233, 234, 236, 238, 239,
  240, 242, 243, 244, 245, 247, 248, 249, 249,
  250, 251, 252, 252, 253, 253, 253, 254, 254,
  254
};

byte fsin(byte t)
{
  if(t<64)
  {
    return table[t];
  }
  else if(t<128)
  {
    return table[127-t];
  }
  else if(t<192)
  {
    return 256-table[t-128];
  }
  else //192<t<256
  {
    return 256-table[255-t];
  }
}

#endif
