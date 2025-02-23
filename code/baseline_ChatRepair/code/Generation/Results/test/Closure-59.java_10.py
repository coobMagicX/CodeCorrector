public void initOptions(CompilerOptions options) {
  this.options = options;
  if (errorManager == null) {
    if (outStream == null) {
      setErrorManager(new LoggerErrorManager(createMessageFormatter(), logger));
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
  }

  // Handle usage of global this based on the set level
  options.setWarningLevel(DiagnosticGroups.GLOBAL_THIS, options.checkGlobalThisLevel);

  if (options.getLanguageIn() == LanguageMode.ECMASCRIPT5_STRICT) {
    options.setWarningLevel(DiagnosticGroups.ES5_STRICT, CheckLevel.ERROR);
  }

  // Initialize the warnings guard.
  List<WarningsGuard> guards = Lists.newArrayList();
  if (options.checkGlobalThisLevel == CheckLevel.OFF) {
    guards.add(new DiagnosticGroupWarningsGuard(DiagnosticGroups.GLOBAL_THIS, CheckLevel.OFF));
  }
  guards.add(new SuppressDocWarningsGuard(getDiagnosticGroups().getRegisteredGroups()));
  guards.add(options.getWarningsGuard());

  // Finalize the warning guard instance that combines all specified rules.
  ComposeWarningsGuard composedGuards = new ComposeWarningsGuard(guards);
  
  if (!options.checkSymbols &&
      !composedGuards.enables(DiagnosticGroups.CHECK_VARIABLES)) {
    composedGuards.addGuard(new DiagnosticGroupWarningsGuard(DiagnosticGroups.CHECK_VARIABLES, CheckLevel.OFF));
  }

  this.warningsGuard = composedGuards;
}
