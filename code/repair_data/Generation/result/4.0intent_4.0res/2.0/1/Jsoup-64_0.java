private static void handleRawtext(Token.StartTag startTag, HtmlTreeBuilder tb) {
    // Check if the startTag is self-closing and handle accordingly
    if (startTag.isSelfClosing()) {
        // Insert both an opening and a closing tag to handle elements like <style> correctly
        tb.insert(startTag);
        tb.insert(new Token.EndTag(startTag.name()));
    } else {
        tb.insert(startTag);
    }
    tb.tokeniser.transition(TokeniserState.Rawtext);
    tb.markInsertionMode();
    tb.transition(Text);
}