import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class PaxHeaderParser {

    private Map<String, String> globalPaxHeaders = new HashMap<>();

    public Map<String, String> parsePaxHeaders(final InputStream i) throws IOException {
        final Map<String, String> headers = new HashMap<>(globalPaxHeaders);
        // Format is "length keyword=value\n";
        while(true) {  // get length
            int ch;
            int len = 0;
            int read = 0;
            while((ch = i.read()) != -1) {
                read++;
                if (ch == ' ') {
                    // Get keyword
                    final ByteArrayOutputStream coll = new ByteArrayOutputStream();
                    while((ch = i.read()) != -1) {
                        read++;
                        if (ch == '=') { // end of keyword
                            final String keyword = coll.toString(StandardCharsets.UTF_8);
                            // Get rest of entry
                            final int restLen = len - read;
                            if (restLen == 1) { // only NL
                                headers.remove(keyword);
                            } else {
                                final byte[] rest = new byte[restLen];
                                final int got = IOUtils.readFully(i, rest);
                                if (got != restLen) {
                                    throw new IOException("Failed to read Paxheader. Expected " + restLen + " bytes, read " + got);
                                }
                                // Drop trailing NL
                                final String value = new String(rest, 0, restLen - 1, StandardCharsets.UTF_8);
                                headers.put(keyword, value);
                            }
                            break;
                        }
                        coll.write((byte) ch);
                    }
                    break; // Processed single header
                }
                if (ch >= '0' && ch <= '9') { // Parse length if it is a number
                    len *= 10;
                    len += ch - '0';
                } else { // Reset if non-digit found and not ' '
                    len = 0;
                    read = 0;
                }
            }
            if (ch == -1 || len == 0) { // EOF or invalid length (possible blank line)
                break;
            }
        }
        return headers;
    }
}