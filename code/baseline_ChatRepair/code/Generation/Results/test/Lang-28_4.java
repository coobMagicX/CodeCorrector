public int translate(CharSequence input, int index, Writer out) throws IOException {
    if (index + 2 >= input.length() || input.charAt(index) != '&' || input.charAt(index + 1) != '#') {
        return 0;
    }

    int start = index + 2;
    boolean isHex = false;

    char firstChar = input.charAt(start);
    if (firstChar == 'x' || firstChar == 'X') {
        start++;
        isHex = true;
    }

    if (start >= input.length()) {
        return 0; 
    }

    int end = start;
    while (end < input.length() && input.charAt(end) != ';') {
        end++;
    }

    if (end == input.length()) {
        return 0; 
    }

    String number = input.subSequence(start, end).toString();
    if (number.isEmpty()) {
        return 0; 
    }

    try {
        int entityValue = Integer.parseInt(number, isHex ? 16 : 10);

        if (Character.isValidCodePoint(entityValue)) {
            if (entityValue > 0xFFFF) {
                
                out.write(Character.toChars(entityValue));
            } else {
                out.write(entityValue);
            }
        } else {
            return 0