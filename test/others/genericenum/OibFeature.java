package others.genericenum;


/**
 * Common features toggles in Beacon applications. 
 *
 */
public enum OibFeature implements Feature {
    REPORTS("Reports"),
    ASSESSMENT_MANAGEMENT("AssessmentManagement"),
    
    ASSIGNMENT("Assignment"),
    SEARCH("Search"),
    ONLINE_ADMIN("OnlineAdmin");
    
    private String _name;

    private OibFeature(String name) {
        _name = name;
    }
    
    @Override
    public String getName() {
        return _name;
    }

    public static OibFeature forName(String featureName) {
        for (OibFeature feature: values()) {
            if (feature.getName().equals(featureName))
                return feature;
        }
        return null;
    }
}
