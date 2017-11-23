package mars;

import java.util.Vector;

public class memoryEater {
    public static void main(String[] args) {
        int count = 0;
        Vector v = new Vector();
        while (true) {
            byte b[] = new byte[1048576];
            v.add(b);
            Runtime rt = Runtime.getRuntime();
            count += 1;
            System.out.println( "mbs used: " + count);
        }
    }
}
