private static void handleRawtext(Token.StartTag startTag, HtmlTreeBuilder tb) {
    tb.insert(startTag);
    tb.tokeniser.transition(TokeniserState.Rawtext);
    tb.markInsertionMode();
    tb.transition(Text);

    // Insert the "style" tag
    Token.Tag styleTag = new Token.Tag("style");
    tb.insert(styleTag);
    tb.transition(Text);
}