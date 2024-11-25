public Token tokenize() {
    // ... (existing code)

    int nextChar = input.read();

    switch (nextChar) {
        case 'f':
            if (input.peekNextChar() == 'o' && input.peekNextChar(2) == 'r') {
                // This is a FOR loop, not just the FOR keyword
                return Token.FOR_LOOP;
            }
            break;

        // ... (existing code)
    }

    // ... (existing code)
}