package uk.ac.cam.km687.demo;

class ExceptionTest {
   public static void main(String[] args) {
      System.out.print("J");
      try {
         a();
      } catch (Exception e) {
          System.out.print(e.getMessage());
      }
      System.out.println("!");
   }

   public static void a() throws Exception {
      System.out.print("A");
      b();
      System.out.print("J");
   }

   public static void b() throws Exception {
      System.out.print("V");
      if (1+2+3==6)
         throw new Exception("A");
      System.out.print("V");
   }
}