package others.genericenum;



/**
 * Common features toggles in Beacon applications. 
 *
 */
public enum OutcomesFeature implements Feature {
    REPORTS("Reports"),
    ASSESSMENT_MANAGEMENT("AssessmentManagement"),
    
    MATRIX("Matrix"),
    LEARNING_MAP("LearningMap"),
    BY_STANDARD_MATRIX("ByStandardMatrix"),
    INST_AVERAGE_COLUMNS("InstAverageColumns"),
    COURSE_AND_PERIOD_FOR_RPT("CourseAndPeriodForRPT"),
    CUSTOM_PERFLEVELS_FOR_RPT("CustomRPTPerformanceLevels");
    
    private String _name;

    private OutcomesFeature(String name) {
        _name = name;
    }
    
    @Override
    public String getName() {
        return _name;
    }

    public static OutcomesFeature forName(String featureName) {
        for (OutcomesFeature feature: values()) {
            if (feature.getName().equals(featureName))
                return feature;
        }
        return null;
    }
}
