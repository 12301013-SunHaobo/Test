package others.genericenum;

/** 
 * stub interface to apply to features which may be toggled.  a feature
 * just needs a name for lookup.  typical implementation would be an 
 * enum.
 */
public interface Feature {
    public String getName();
}