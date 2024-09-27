    public void testEscapedCharacter() throws Exception {
        final Lexer lexer = getLexer("character\\aEscaped", formatWithEscaping);
        assertThat(lexer.nextToken(new Token()), hasContent("character\\aEscaped"));
    }