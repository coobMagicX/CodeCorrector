  public void testMath283()
      throws FunctionEvaluationException, OptimizationException {
      // fails because MultiDirectional.iterateSimplex is looping forever
      // the while(true) should be replaced with a convergence check
      MultiDirectional multiDirectional = new MultiDirectional();
      multiDirectional.setMaxIterations(100);
      multiDirectional.setMaxEvaluations(1000);

      final Gaussian2D function = new Gaussian2D(0.0, 0.0, 1.0);

      RealPointValuePair estimate = multiDirectional.optimize(function,
                                    GoalType.MAXIMIZE, function.getMaximumPosition());

      final double EPSILON = 1e-5;

      final double expectedMaximum = function.getMaximum();
      final double actualMaximum = estimate.getValue();
      Assert.assertEquals(expectedMaximum, actualMaximum, EPSILON);

      final double[] expectedPosition = function.getMaximumPosition();
      final double[] actualPosition = estimate.getPoint();
      Assert.assertEquals(expectedPosition[0], actualPosition[0], EPSILON );
      Assert.assertEquals(expectedPosition[1], actualPosition[1], EPSILON );
      
  }