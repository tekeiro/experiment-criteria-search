package org.keirobm.jpacriteriabuilder.jpaquerybuilder;


/**
 * Describes operations supported by
 * JPA Query Criteria Builder.
 *
 * @author Angel Biedma
 */
public enum OperationsSupported {
    /* Boolean operations */
    IS_TRUE("is", "True", 0, false),
    IS_FALSE("is", "False", 0, false),
    IS_NOTNULL("is", "NotNull", 0, false),
    IS_NULL("is", "Null", 0, false),

    /* Logic */
    GT("with", "Gt", 1, false),
    LT("with", "Lt", 1, false),
    GTEQ("with", "GtEq", 1, false),
    LTEQ("with", "LtEq", 1, false),
    NOTEQ("with", "NotEq", 1, false),
    EQ("with", "Eq", 1, false),

    /* Number */
    RANGE("with", "InRange", 2, false),

    /* Containment */
    NOT_IN("with", "NotIn", 1, true),
    IN("with", "In", 1, true),

    /* String */
    STR_STARTS("with", "StartsWith", 1, false),
    STR_ENDS("with", "EndsWith", 1, false),
    STR_CONTAINS("with", "Containing", 1, false),

    /* Dates */
    DATES_BETWEEN("with", "DatesBetween", 2, false),


    ;
    private final String startsWith;
    private final String endsWith;
    private final int argNumber;
    private final boolean variableArguments;

    OperationsSupported(String startsWith, String endsWith, int argNumber, boolean variableArguments) {
        this.startsWith = startsWith;
        this.endsWith = endsWith;
        this.argNumber = argNumber;
        this.variableArguments = variableArguments;
    }

    public int getArgNumber() {
        return argNumber;
    }

    public boolean isVariableArguments() {
        return variableArguments;
    }

    public boolean matches(String methodName) {
        if (methodName == null)
            return false;

        return methodName.startsWith(this.startsWith) && methodName.endsWith(this.endsWith);
    }

    public String extractCriteriaName(String methodName) {
        if (matches(methodName)) {
            final String tmp2 = methodName.replaceFirst(this.startsWith, "");
            int finalChars = this.endsWith.length();
            return tmp2.substring(0, tmp2.length()-finalChars);
        }
        else
            return null;
    }

}
