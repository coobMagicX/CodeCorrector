protected CompilerOptions createOptions() {
  CompilerOptions options = new CompilerOptions();
  if (flags.processJqueryPrimitives) {
    options.setCodingConvention(new JqueryCodingConvention());
  } else {
    options.setCodingConvention(new ClosureCodingConvention());
  }

  options.setExtraAnnotationNames(flags.extraAnnotationName);

  CompilationLevel level = flags.compilationLevel;
  level.setOptionsForCompilationLevel(options);

  if (flags.debug) {
    level.setDebugOptionsForCompilationLevel(options);
  }

  if (flags.useTypesForOptimization) {
    level.setTypeBasedOptimizationOptions(options);
  }

  if (flags.generateExports) {
    options.setGenerateExports(flags.generateExports);
  }

  WarningLevel wLevel = flags.warningLevel;
  wLevel.setOptionsForWarningLevel(options);
  for (FormattingOption formattingOption : flags.formatting) {
    formattingOption.applyToOptions(options);
  }

  options.closurePass = flags.processClosurePrimitives;

  options.jqueryPass = CompilationLevel.ADVANCED_OPTIMIZATIONS == level &&
      flags.processJqueryPrimitives;

  options.angularPass = flags.angularPass;

  if (!flags.translationsFile.isEmpty()) {
    try {
      options.messageBundle = new XtbMessageBundle(
          new FileInputStream(flags.translationsFile),
          flags.translationsProject);
    } catch (IOException e) {
      throw new RuntimeException("Reading XTB file", e);
    }
  }

  // Adding a condition to avoid i18n warnings when in ADVANCED mode and no translations file is specified
  if (CompilationLevel.ADVANCED_OPTIMIZATIONS == level && flags.translationsFile.isEmpty()) {
    options.setWarningLevel(WarningLevel.OFF); // Turn off warnings related to i18n for ADVANCED mode without translations
  }

  return options;
}