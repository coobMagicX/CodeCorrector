import java.util.ArrayList;
import java.util.List;

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
  } else if (!options.checkTypes) {
    options.setWarningLevel(DiagnosticGroup.forType(RhinoErrorReporter.TYPE_PARSE_ERROR), CheckLevel.OFF);
  }

  if (options.checkGlobalThisLevel.isOn()) {
    options.setWarningLevel(DiagnosticGroups.GLOBAL_THIS, options.checkGlobalThisLevel);
  } else if (options.checkGlobalThisLevel.isOff()) {
    options.setWarningLevel(DiagnosticGroups.GLOBAL_THIS, CheckLevel.OFF);
  }
  
  if (options.getLanguageIn() == LanguageMode.ECMASCRIPT5_STRICT) {
    options.setWarningLevel(DiagnosticGroups.ES5_STRICT, CheckLevel.ERROR);
  }

  List<WarningsGuard> guards = new ArrayList<WarningsGuard>();
  guards.add(new SuppressDocWarningsGuard(getDiagnosticGroups().getRegisteredGroups()));
  guards.add(options.getWarningsGuard());
  
  ComposeWarningsGuard composedGuards = new ComposeWarningsGuard(guards);
  
  if (!options.checkSymbols &&
      !composedGuards.enables(DiagnosticGroups.CHECK_VARIABLES)) {
    composedGuards.addGuard(new DiagnosticGroupWarningsGuard(DiagnosticGroups.CHECK_VARIABLES, CheckLevel.OFF));
  }

  this.warningsGuard = composedGuards;
}
