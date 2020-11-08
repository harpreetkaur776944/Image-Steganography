 import java.awt.image.BufferedImage;
import java.io.File;
 import javax.imageio.*;
 
 public class EmbedMessage 
 {

 private BufferedImage bf;
 private File f;
 public EmbedMessage() {
   
    }
 

 public String embedMessage(BufferedImage img, String mess,File file, int storageBit) {
      f=file;
    bf=img;
    int messageLength = mess.length();
    
    int imageWidth = bf.getWidth(), imageHeight = bf.getHeight(),
       imageSize = imageWidth * imageHeight;
    System.out.println("messageLength"+ messageLength+"\timage size"+imageSize*2 );
    if(messageLength * 8 + 32 > imageSize) {
                 return null;      
       }
         embedInteger(bf, messageLength, 0, storageBit);
         System.out.println("inside embed"+storageBit);
         byte b[] = mess.getBytes();
     for(int i=0; i<b.length; i++) {
         embedByte(bf, b[i], i * 8 + 32, storageBit);
       }
    try
      {
        
      String newImage=  saveImage();
        return newImage;
      }
    catch(Exception e)
      {
        return e.getMessage();
      }
    }
 
  
 
 
 private void embedInteger(BufferedImage img, int n, int start, int storageBit) {
    int maxX = img.getWidth(), 
        maxY = img.getHeight(), 
        startX = start/maxY, 
        startY = start - startX*maxY, 
        count=0,rgb,bit;
     for(int i=startX; i<maxX && count<32; i++) {
       for(int j=startY; j<maxY && count<32; j++) {  
         rgb = img.getRGB(i, j);
         //int a = (rgb>>24)&0xff; 
         int r = (rgb>>16)&0xff; 
         int g = (rgb>>8)&0xff; 
         int b =  rgb&0xff;
          bit = getBitValue(n, count);
          b=setBitValue(b,storageBit,bit);
          bit = getBitValue(n, count+1);
          g=setBitValue(g,storageBit,bit);
          bit = getBitValue(n, count+2);
          r=setBitValue(r,storageBit,bit);
          rgb=(r<<16) | (g<<8) | b;
          img.setRGB(i, j, rgb);
          count=count+3;
           if(j==maxY-1 && count<8){
               j=-1;
               i++;
           }
          }
       }
    }
 
 private void embedByte(BufferedImage img, byte b, int start, int storageBit) {
    int maxX = img.getWidth(), 
        maxY = img.getHeight(), 
        startX = start/maxY, 
        startY = start - startX*maxY, 
        count=0,rgb,bit;
    
     for(int i=startX; i<maxX && count<8; i++) {
       for(int j=startY; j<maxY && count<8; j++) {
          rgb = img.getRGB(i, j);
         
         //int a = (rgb>>24)&0xff; 
         int r = (rgb>>16)&0xff; 
         int g = (rgb>>8)&0xff; 
         int blue =  rgb&0xff;

          bit = getBitValue(b, count);
          blue=setBitValue(blue,storageBit,bit);
          bit = getBitValue(b, count+1);
          g=setBitValue(g,storageBit,bit);
          bit = getBitValue(b, count+2);
          r=setBitValue(r,storageBit,bit);
          rgb =  (r<<16) | (g<<8) | blue;
          img.setRGB(i, j, rgb);
           if(j==maxY-1 && count<8){
               j=-1;
               i++;
           }
           count=count+3;
          }
       }
    }
 
  private String saveImage( )throws Exception{
    String name = f.getName();
    String ext = name.substring(name.lastIndexOf(".")+1).toLowerCase();
    
   if(!ext.equals("png")) {
          ext = "png";
          f = new java.io.File(f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-4)+"_generated.png");
          }
    
    try {
       if(f.exists()) f.delete();
        f = new java.io.File(f.getAbsolutePath().substring(0,f.getAbsolutePath().length()-4)+"_generated.png");
       ImageIO.write(bf, ext, f);
       return f.getAbsolutePath();
       } catch(Exception ex) { 
           throw ex;
            }   
    }

 
 private int getBitValue(int n, int location) {
    return (1 & (n >> location )); 
    }
 
 private int setBitValue(int n, int location, int bit) {
    int mask = 1 << location;
	return (n & ~mask) | ((bit << location) & mask); 
    }
 
 
}