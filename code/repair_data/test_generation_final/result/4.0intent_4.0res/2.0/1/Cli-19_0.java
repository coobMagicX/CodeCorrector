private void processOptionToken(String token, boolean stopAtNonOption) {
    if (options.hasOption(token)) {
        currentOption = options.getOption(token);
        tokens.add(token);
    } else {
        // Throw an UnrecognizedOptionException for unrecognized options
        throw new UnrecognizedOptionException("Unrecognized option: " + token);
    }
}