import java.io.IOException;
import java.io.InputStream;

public class CustomInputStream extends InputStream {
    private InputStream input;

    public CustomInputStream(InputStream input) {
        this.input = input;
    }

    @Override
    public int read() throws IOException {
        return input.read();
    }

    @Override
    public long skip(long numToSkip) throws IOException {
        long available = numToSkip;
        while (numToSkip > 0) {
            long skipped = input.skip(numToSkip);
            if (skipped == 0) {
                break;
            }
            numToSkip -= skipped;
        }
        return available - numToSkip;
    }
}

public static long skip(InputStream input, long numToSkip) throws IOException {
    long available = numToSkip;
    while (numToSkip > 0) {
        long skipped = input.skip(numToSkip);
        if (skipped == 0) {
            break;
        }
        numToSkip -= skipped;
    }
    return available - numToSkip;
}