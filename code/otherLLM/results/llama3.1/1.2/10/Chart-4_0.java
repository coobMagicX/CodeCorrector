public Range getDataRange(ValueAxis axis) {

    Range result = null;
    List mappedDatasets = new ArrayList();
    List includedAnnotations = new ArrayList();
    boolean isDomainAxis = true;

    // Verify that the axis has been created with the correct type (domain or range)
    if (!axis.isDomain()) {
        isDomainAxis = false;
    }

    // Iterate through all datasets to get the union of ranges
    for (int i = 0; i < this.datasets.size(); i++) {
        List mappedAxes = (List) this.datasetToDomainAxesMap.get(new Integer(i));
        if (mappedAxes != null && !mappedAxes.isEmpty()) {
            int axisIndex = -1;
            for (Object o : mappedAxes) {
                if (((Integer) o).equals(axis.getIndex())) {
                    axisIndex = i;
                    break;
                }
            }

            if (axisIndex == 0) {
                // Grab the plot's annotations
                Iterator iterator = this.annotations.iterator();
                while (iterator.hasNext()) {
                    XYAnnotation annotation = (XYAnnotation) iterator.next();
                    if (annotation instanceof XYAnnotationBoundsInfo) {
                        includedAnnotations.add(annotation);
                    }
                }
            }

            List datasets;
            if (isDomainAxis && axisIndex >= 0) {
                datasets = getDatasetsMappedToDomainAxis(new Integer(axisIndex));
            } else if (!isDomainAxis && axisIndex >= 0) {
                datasets = getDatasetsMappedToRangeAxis(new Integer(axisIndex));
            } else {
                continue;
            }

            for (Object dataset : datasets) {
                XYDataset d = (XYDataset) dataset;
                if (d != null) {
                    XYItemRenderer r = getRendererForDataset(d);
                    if (isDomainAxis) {
                        if (r != null) {
                            result = Range.combine(result, r.findDomainBounds(d));
                        } else {
                            result = Range.combine(result,
                                    DatasetUtilities.findDomainBounds(d));
                        }
                    } else {
                        if (r != null) {
                            result = Range.combine(result, r.findRangeBounds(d));
                        } else {
                            result = Range.combine(result,
                                    DatasetUtilities.findRangeBounds(d));
                        }
                    }

                    Collection c = r.getAnnotations();
                    Iterator i = c.iterator();
                    while (i.hasNext()) {
                        XYAnnotation a = (XYAnnotation) i.next();
                        if (a instanceof XYAnnotationBoundsInfo) {
                            includedAnnotations.add(a);
                        }
                    }
                }
            }
        }
    }

    Iterator it = includedAnnotations.iterator();
    while (it.hasNext()) {
        XYAnnotationBoundsInfo xyabi = (XYAnnotationBoundsInfo) it.next();
        if (xyabi.getIncludeInDataBounds()) {
            if (isDomainAxis) {
                result = Range.combine(result, xyabi.getXRange());
            } else {
                result = Range.combine(result, xyabi.getYRange());
            }
        }
    }

    return result;
}