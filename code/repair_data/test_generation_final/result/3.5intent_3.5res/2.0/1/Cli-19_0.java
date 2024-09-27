private void processOptionToken(String token, boolean stopAtNonOption) throws UnrecognizedOptionException {
    if (options.hasOption(token)) {
        currentOption = options.getOption(token);
        tokens.add(token);
    } else if (stopAtNonOption) {
        eatTheRest = true;
        tokens.add(token);
    } else {
        throw new UnrecognizedOptionException("Unrecognized option: " + token);
    }
}