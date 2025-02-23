public void initOptions(CompilerOptions options) {
    this.options = options;
    if (errorManager == null) {
        if (outStream == null) {
            setErrorManager(new LoggerErrorManager(createMessageFormatter(), logger));
        } else {
            PrintStreamErrorManager printer = new PrintStreamErrorManager(createMessageFormatter(), outStream);
            printer.setSummaryDetailLevel(options.summaryDetailLevel);
            setErrorManager(printer);
        }
    }

    // DiagnosticGroups override the plain checkTypes option.
    if (options.enables(DiagnosticGroups.CHECK_TYPES)) {
        options.checkTypes = true;
    } else if (options.disables(DiagnosticGroups.CHECK_TYPES)) {
        options.checkTypes = false;
    }

    // Correctly respond to the checkGlobalThisLevel settings
    options.setWarningLevel(DiagnosticGroups.GLOBAL_THIS, options.checkGlobalThisLevel);

    if (options.getLanguageIn() == LanguageMode.ECMASCRIPT5_STRICT) {
        options.setWarningLevel(DiagnosticGroups.ES5_STRICT, CheckLevel.ERROR);
    }

    // Initialize the warnings guard.
    List<WarningsGuard> guards = Lists.newArrayList();

    // Ensure specific warning levels are added based on configuration
    guards.add(options.getWarningsGuard());

    // Disable check for global `this` if the level is OFF
    if(options.checkGlobalThisLevel == CheckLevel.OFF) {
        guards.add(new DiagnosticGroupWarningsGuard(DiagnosticGroups.GLOBAL_THIS, CheckLevel.OFF));
    }

    ComposeWarningsGuard composedGuards = new ComposeWarningsGuard(guards);

    // Ensure other checks are set appropriately
    if (!options.checkSymbols) {
        composedGuards.addGuard(new DiagnosticGroupWarningsGuard(DiagnosticGroups.CHECK_VARIABLES, CheckLevel.OFF));
    }

    this.warningsGuard = composedGuards;
}
