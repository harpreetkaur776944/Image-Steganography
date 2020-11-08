
 import java.awt.image.*;
 
 
 public class DecodeMessage 
 {
 
 public DecodeMessage() {
    
    }
 
 public String decodeMessage(BufferedImage image, int storageBit)  {
    int len = extractInteger(image, 0, storageBit);
    byte b[] = new byte[len];
    for(int i=0; i<len; i++)
       b[i] = extractByte(image, i*8+32, storageBit);
   return (new String(b));
    }
 
 private int extractInteger(BufferedImage img, int start, int storageBit) {
   int maxX = img.getWidth(), maxY = img.getHeight(), 
       startX = start/maxY, startY = start - startX*maxY, count=0;
    int length = 0;
    int rgb,bit;
    for(int i=startX; i<maxX && count<32; i++) {
       for(int j=startY; j<maxY && count<32; j++) {
          rgb = img.getRGB(i, j);
          int b=rgb&0xff;
          int r = (rgb>>16)&0xff; 
          int g = (rgb>>8)&0xff; 
          bit = getBitValue(b, storageBit);
          length = setBitValue(length, count, bit);
          bit = getBitValue(g, storageBit);
          length = setBitValue(length, count+1, bit);
          bit = getBitValue(r, storageBit);
          length = setBitValue(length, count+2, bit);
          count=count+3;
           if(j==maxY-1 && count<8){
               j=-1;
               i++;
           }
          }
       }
    return length;
    }
 
 private byte extractByte(BufferedImage img, int start, int storageBit) {
    int maxX = img.getWidth(), maxY = img.getHeight(), 
       startX = start/maxY, startY = start - startX*maxY, count=0;
    byte b = 0;
    int rgb,bit;
    for(int i=startX; i<maxX && count<8; i++) {
       for(int j=startY; j<maxY && count<8; j++) {
          rgb = img.getRGB(i, j);
           int blue=rgb&0xff;
           int r = (rgb>>16)&0xff; 
           int g = (rgb>>8)&0xff; 
           
          bit = getBitValue(blue, storageBit);
          b = (byte)setBitValue(b, count, bit);
          bit = getBitValue(g, storageBit);
          b = (byte)setBitValue(b, count+1, bit);
          bit = getBitValue(r, storageBit);
          b = (byte)setBitValue(b, count+2, bit);
          count=count+3;
           if(j==maxY-1 && count<8){
               j=-1;
               i++;
           }
          
          }
       }
    return b;
    }
 private int getBitValue(int n, int location) {
   return (1 & (n >> location )); 
    }
 
 private int setBitValue(int n, int location, int bit) {
    int mask = 1 << location;
	return (n & ~mask) | ((bit << location) & mask); 
    }
 }