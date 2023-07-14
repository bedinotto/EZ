package EZrt;

import java.io.*;

public class Runtime {
    static BufferedReader the_buffer;

    static public int initialize() {
        System.out.println("EZ language runtime system. Version 1");
        the_buffer = new BufferedReader(new InputStreamReader(System.in));

        if (the_buffer == null) {
            System.err.println("Error: can not open the input");
            return -1;
        }
        return 0;
    }

    static public void finilizy() {
        try {
            the_buffer.close();
        } catch (IOException e) {
        }
    }

    static public int readInt(){
        String line = null;
        int value;
        try {
            line = the_buffer.readLine();
            value = Integer.parseInt(line);
        } catch (IOException e) {
            System.err.println("Error: can not read the input");
            System.err.println("Reason: " + e.getMessage());
            return 0;
        } catch (NumberFormatException e) {
            System.err.println("Error: wrong number format");
            return 0;
        }
        return value;
    }

    static public String readString() {
        String line = null;
        try {
            line = the_buffer.readLine();
        } catch (IOException e) {
            System.err.println("Error: can not read the input");
            System.err.println("Reason: " + e.getMessage());
        }
        return line;
    }
}
