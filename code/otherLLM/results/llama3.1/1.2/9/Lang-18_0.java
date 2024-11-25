DateFormatSymbols symbols = new DateFormatSymbols(mLocale);
List<Rule> rules = parsePattern();
for (Rule rule : rules) {
    if (rule instanceof TextField) {
        TextField textField = (TextField) rule;
        // Use the textField object to validate or parse dates...
    } else if (rule instanceof TwelveHourField) {
        TwelveHourField twelvehourField = (TwelveHourField) rule;
        // Use the twelvehourField object to validate or parse dates...
    }
}