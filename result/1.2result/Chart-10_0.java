public String generateToolTipFragment(String toolTipText) {
    return " title=\"" + escapeHtml(toolTipText) + "\" alt=\"\"";
}

private String escapeHtml(String input) {
    return input.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#39;");
}