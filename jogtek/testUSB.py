#!/usr/bin/python
import jogtek
import sys
from select import select
ret = jogtek.rfid_open("/dev/ttyUSB0")
if ret < 0:
    print "failed to open RFID reader"
    sys.exit()

print
print "press ENTER to exit."
print
done = False
while (done == False):
   timeout = 0.1
   rlist, _, _ = select([sys.stdin], [], [], timeout)
   if rlist:
      s = sys.stdin.readline()
      done = True

   id = jogtek.rfid_15693inventory()
   if (id != ""):
      print id

jogtek.rfid_close()
