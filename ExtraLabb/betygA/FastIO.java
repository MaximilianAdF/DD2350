import java.io.*;

public class FastIO {
    private final byte[] buffer = new byte[1 << 18]; // 256 KB buffer
    private int bufferPointer = 0, bytesRead = 0;
    private final InputStream inputStream;

    public FastIO(InputStream input) {
        this.inputStream = input;
    }

    private int read() {
        if (bufferPointer == bytesRead) {
            bufferPointer = 0;
            try {
                bytesRead = inputStream.read(buffer);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (bytesRead == -1) return -1; // EOF
        }
        return buffer[bufferPointer++];
    }

    public int getInt() {
        int num = 0, c;
        do {
            c = read();
        } while (c <= ' ' && c != -1); // Skip whitespace
        boolean neg = c == '-';
        if (neg) c = read();
        do {
            num = num * 10 + (c - '0');
        } while ((c = read()) >= '0' && c <= '9');
        return neg ? -num : num;
    }
}