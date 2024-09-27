void read(Tokeniser t, CharacterReader r) {
    char c = r.consume();
    switch (c) {
        case '>':
            t.tagPending.selfClosing = true;
            t.emitTagPending();
            t.transition(Data);
            break;
        case '/':
            if (r.matches('>')) {
                t.tagPending.selfClosing = true;
                r.consume(); // Consume the '>'
                t.emitTagPending();
                t.transition(Data);
            } else {
                t.tagPending.newAttribute();
                t.tagPending.appendAttributeName(c);
                t.transition(AttributeName);
            }
            break;
        case eof:
            t.eofError(this);
            t.transition(Data);
            break;
        default:
            t.tagPending.newAttribute();
            t.tagPending.appendAttributeName(c);
            t.transition(AttributeName);
            break;
    }
}