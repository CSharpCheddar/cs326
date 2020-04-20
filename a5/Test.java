// Test SHA512

class Test extends SHA512 {
  public static void main(String[] args) {
    String[] files = {"test1.txt", "test2.txt", "test3.txt"};
    for (String file : files) {
      long[] message = prepareMessage(file);
      long[] hash = hash(message);
      System.out.println();
    }
  }
}
