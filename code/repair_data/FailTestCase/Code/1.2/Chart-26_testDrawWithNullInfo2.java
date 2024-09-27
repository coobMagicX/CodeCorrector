    public void testDrawWithNullInfo2() {
        boolean success = false;
        try {
            JFreeChart chart = createGanttChart();
            CategoryPlot plot = (CategoryPlot) chart.getPlot();
            plot.setDataset(createDataset());
            /* BufferedImage img =*/ chart.createBufferedImage(300, 200, null);
            success = true;
        }
        catch (NullPointerException e) {
            success = false;
        }
        assertTrue(success);
    }