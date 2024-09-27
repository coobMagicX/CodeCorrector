    public void testMath272() throws OptimizationException {
        LinearObjectiveFunction f = new LinearObjectiveFunction(new double[] { 2, 2, 1 }, 0);
        Collection<LinearConstraint> constraints = new ArrayList<LinearConstraint>();
        constraints.add(new LinearConstraint(new double[] { 1, 1, 0 }, Relationship.GEQ,  1));
        constraints.add(new LinearConstraint(new double[] { 1, 0, 1 }, Relationship.GEQ,  1));
        constraints.add(new LinearConstraint(new double[] { 0, 1, 0 }, Relationship.GEQ,  1));

        SimplexSolver solver = new SimplexSolver();
        RealPointValuePair solution = solver.optimize(f, constraints, GoalType.MINIMIZE, true);
        
        assertEquals(0.0, solution.getPoint()[0], .0000001);
        assertEquals(1.0, solution.getPoint()[1], .0000001);
        assertEquals(1.0, solution.getPoint()[2], .0000001);
        assertEquals(3.0, solution.getValue(), .0000001);
      }