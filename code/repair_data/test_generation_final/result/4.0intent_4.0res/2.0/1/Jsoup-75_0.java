final void html(final Appendable accum, final Document.OutputSettings out) throws IOException {
    final int sz = size;
    for (int i = 0; i < sz; i++) {
        // inlined from Attribute.html()
        final String key = keys[i];
        final String val = vals[i];
        accum.append(' ').append(key);

        // Handle boolean attributes correctly for HTML5
        if (!(out.syntax() == Document.OutputSettings.Syntax.html
            && (val == null || val.isEmpty() || val.equals(key)) && Attribute.isBooleanAttribute(key))) {
            accum.append("=\"");
            Entities.escape(accum, val == null ? "" : val, out, true, false, false);
            accum.append('"');
        }
    }
}