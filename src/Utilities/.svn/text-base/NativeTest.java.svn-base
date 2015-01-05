package Utilities;

public class NativeTest
{
   static
   {
      // This is done to ensure that the "nativetest" library can find the tridef
      //  library already loaded into the process
      System.loadLibrary("tridef_converter_test" );
      System.loadLibrary("nativetest");
   }

   public static native int processRGB(int[] pixels, int w, int h, int [] result);
   public static native void setSceneDepth(int depth);
}

