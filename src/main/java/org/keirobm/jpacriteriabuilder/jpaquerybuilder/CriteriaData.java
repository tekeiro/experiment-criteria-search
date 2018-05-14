package org.keirobm.jpacriteriabuilder.jpaquerybuilder;

import java.util.Objects;


public final class CriteriaData {

    private final String criterio;
    private final String dbColumna;
    private final OperationsSupported operation;
    private final String propertyPath;

    public CriteriaData(String criterio, String dbColumna, OperationsSupported operation, String propertyPath) {
        this.criterio = criterio;
        this.dbColumna = dbColumna;
        this.operation = operation;
        this.propertyPath = propertyPath;
    }

    public String getCriterio() {
        return criterio;
    }

    public String getDbColumna() {
        return dbColumna;
    }

    public OperationsSupported getOperation() {
        return operation;
    }

    public String getPropertyPath() {
        return propertyPath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CriteriaData that = (CriteriaData) o;
        return Objects.equals(getCriterio(), that.getCriterio()) &&
                Objects.equals(getDbColumna(), that.getDbColumna()) &&
                getOperation() == that.getOperation() &&
                Objects.equals(getPropertyPath(), that.getPropertyPath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCriterio(), getDbColumna(), getOperation(), getPropertyPath());
    }
}
