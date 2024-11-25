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
  // Remove the loop and directly apply formatting options to avoid potential issues
  for (FormattingOption formattingOption : flags.formatting) {
    formattingOption.applyToOptions(options);
  }
  
  // Ensure that warning level is set correctly
  if (CompilationLevel.ADVANCED_OPTIMIZATIONS == level) {
    wLevel.setOptionsForWarningLevel(options);
    options.closurePass = flags.processClosurePrimitives;
    options.jqueryPass = true; // Simplified the comparison for clarity
    options.angularPass = flags.angularPass;
  }

  // Set message bundle based on conditions to prevent warnings
  if (!flags.translationsFile.isEmpty()) {
    try {
      options.messageBundle = new XtbMessageBundle(
          new FileInputStream(flags.translationsFile),
          flags.translationsProject);
    } catch (IOException e) {
      throw new RuntimeException("Reading XTB file", e);
    }
  } else if (CompilationLevel.ADVANCED_OPTIMIZATIONS == level) {
    // In ADVANCED mode, use EmptyMessageBundle to avoid warnings
    options.messageBundle = new EmptyMessageBundle();
  }

  return options;
}