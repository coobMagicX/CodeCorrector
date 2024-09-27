    public void test1654215() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        JFreeChart chart = ChartFactory.createLineChart("Title", "X", "Y",
                dataset, PlotOrientation.VERTICAL, true, false, false);
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setRenderer(1, new LineAndShapeRenderer());
        boolean success = false;
        try {
            BufferedImage image = new BufferedImage(200 , 100, 
                    BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = image.createGraphics();
            chart.draw(g2, new Rectangle2D.Double(0, 0, 200, 100), null, null);
            g2.dispose();
            success = true;
        }
        catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        assertTrue(success);
    }