public class StrBuilder {
    private StringBuilder buffer;
    private int size;

    public StrBuilder() {
        this.buffer = new StringBuilder();
        this.size = 0;
    }

    // Existing method for reference
    public StrBuilder appendFixedWidthPadRight(Object obj, int width, char padChar) {
        if (width > 0) {
            ensureCapacity(size + width);
            String str = (obj == null ? getNullText() : obj.toString());
            int strLen = str.length();
            if (strLen >= width) {
                str.getChars(0, strLen, buffer.toString().toCharArray(), size);
            } else {
                int padLen = width - strLen;
                str.getChars(0, strLen, buffer.toString().toCharArray(), size);
                for (int i = 0; i < padLen; i++) {
                    buffer.append(padChar);
                }
            }
            size += width;
        }
        return this;
    }

    // Existing method to use
    public StrBuilder appendFixedWidthPadRight(int value, int width, char padChar) {
        return appendFixedWidthPadRight(String.valueOf(value), width, padChar);
    }

    private void ensureCapacity(int minCapacity) {
        if (minCapacity - buffer.length() > 0) {
            buffer.append(new StringBuilder(minCapacity - buffer.length()));
        }
    }

    // Other methods would go here
}

// Example usage of the StrBuilder class
public class Main {
    public static void main(String[] args) {
        StrBuilder builder = new StrBuilder();
        builder.appendFixedWidthPadRight("Test", 10, '*').append('\n');
        System.out.print(builder.toString());
    }
}