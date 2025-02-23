    try {
      if (options.dependencyOptions.needsManagement() && options.closurePass) {
        processDependencies();
      }
    } catch (DependencyManagementException e) {
      reportError(e);
      return null;
    }

    // Rest of the code...
