public void initOptions(CompilerOptions options) {
  this.options = options;
  if (errorManager == null) {
    if (outStream == null) {
      setErrorManager(
          new LoggerErrorManager(createMessageFormatter(), logger));
    } else {
      PrintStreamErrorManager printer = 
          new PrintStreamErrorManager(createMessageFormatter(), outStream);
      printer.setSummaryDetailLevel(options.summaryDetailLevel);
      setErrorManager(printer);
    }
  }

  // DiagnosticGroups override the plain checkTypes option.
  if (options.enables(DiagnosticGroups.CHECK_TYPES)) {
    options.checkTypes = true;
  } else if (options.disables(DiagnosticGroups.CHECK_TYPES)) {
    options.checkTypes = false;
  } else if (!options.checkTypes) {
    // If DiagnosticGroups did not override the plain checkTypes
    // option, and checkTypes is enabled, then turn off the
    // parser type warnings.
    options.setWarningLevel(
        DiagnosticGroup.forType(
            RhinoErrorReporter.TYPE_PARSE_ERROR),
        CheckLevel.OFF);
  }

  // Handle checkGlobalThisLevel settings
  if (options.checkGlobalThisLevel.isOn()) {
    options.setWarningLevel(
        DiagnosticGroups.GLOBAL_THIS,
        options.checkGlobalThisLevel);
  } else {
    options.setWarningLevel(
        DiagnosticGroups.GLOBAL_THIS,
        CheckLevel.OFF);
  }

  // Handle ECMAScript strict mode warnings
  if (options.getLanguageIn() == LanguageMode.ECMASCRIPT5_STRICT) {
    options.setWarningLevel(
        DiagnosticGroups.ES5_STRICT,
        CheckLevel.ERROR);
  }

  // Initialize the warnings guard
  List<WarningsGuard> guards = new ArrayList<>();
  guards.add(new SuppressDocWarningsGuard(
      getDiagnosticGroups().getRegisteredGroups()));
  guards.add(options.getWarningsGuard());

  ComposeWarningsGuard composedGuards = new ComposeWarningsGuard(guards);

  // Check symbols and apply guards
  if (!options.checkSymbols &&
      !composedGuards.enables(DiagnosticGroups.CHECK_VARIABLES)) {
    composedGuards.addGuard(new DiagnosticGroupWarningsGuard(
        DiagnosticGroups.CHECK_VARIABLES, CheckLevel.OFF));
  }

  this.warningsGuard = composedGuards;
}
