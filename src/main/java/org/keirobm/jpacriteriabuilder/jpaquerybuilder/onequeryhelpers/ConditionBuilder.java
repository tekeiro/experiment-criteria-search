package org.keirobm.jpacriteriabuilder.jpaquerybuilder.onequeryhelpers;

import org.keirobm.jpacriteriabuilder.jpaquerybuilder.CriteriaData;
import org.keirobm.jpacriteriabuilder.jpaquerybuilder.OperationsSupported;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * Build a condition for a Criteria
 *
 * @author Angel Biedma
 */
public class ConditionBuilder {
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy");

    private final String letter;

    public ConditionBuilder(String letter) {
        this.letter = letter;
    }

    private String parseDbColumna(String dbCol) {
        return dbCol.replaceAll("%l", letter);
    }

    private String parseArg(Object arg) {
        if (arg instanceof String)
            return "'" + arg.toString() + "'";
        else if (arg instanceof Date)
            return "'" + df.format((Date) arg) + "'";
        return arg.toString();
    }

    private String parseArgAsStringPart(Object arg) {
        if (arg instanceof Date)
            return df.format((Date) arg);
        return arg.toString();
    }

    private String parseArgList(List<Object> args) {
        return args.stream()
                .map(this::parseArg)
                .collect(Collectors.joining(","))
                ;
    }


    public String buildCondition(CriteriaData critData, List<Object> args) {
        Objects.requireNonNull(critData);
        Objects.requireNonNull(args);


        final OperationsSupported op = critData.getOperation();
        switch (op) {
            case IS_TRUE:
                return String.format("%s = TRUE", parseDbColumna(critData.getDbColumna()));
            case IS_FALSE:
                return String.format("%s = FALSE", parseDbColumna(critData.getDbColumna()));
            case IS_NOTNULL:
                return String.format("%s IS NOT NULL", parseDbColumna(critData.getDbColumna()));
            case IS_NULL:
                return String.format("%s IS NULL", parseDbColumna(critData.getDbColumna()));
            case GT:
                return String.format("%s > %s",
                        parseDbColumna(critData.getDbColumna()),
                        parseArg(args.get(0))
                );
            case LT:
                return String.format("%s < %s",
                        parseDbColumna(critData.getDbColumna()),
                        parseArg(args.get(0))
                );
            case GTEQ:
                return String.format("%s >= %s",
                        parseDbColumna(critData.getDbColumna()),
                        parseArg(args.get(0))
                );
            case LTEQ:
                return String.format("%s <= %s",
                        parseDbColumna(critData.getDbColumna()),
                        parseArg(args.get(0))
                );
            case NOTEQ:
                return String.format("%s != %s",
                        parseDbColumna(critData.getDbColumna()),
                        parseArg(args.get(0))
                );
            case EQ:
                return String.format("%s > %s",
                        parseDbColumna(critData.getDbColumna()),
                        parseArg(args.get(0))
                );
            case RANGE:
                return String.format("(%s BETWEEN %s AND %s)",
                        parseDbColumna(critData.getDbColumna()),
                        parseArg(args.get(0)),
                        parseArg(args.get(1))
                );
            case NOT_IN:
                return String.format("%s NOT IN (%s)",
                        parseDbColumna(critData.getDbColumna()),
                        parseArgList(args)
                );
            case IN:
                return String.format("%s IN (%s)",
                        parseDbColumna(critData.getDbColumna()),
                        parseArgList(args)
                );
            case STR_STARTS:
                return String.format("%s LIKE '%s%%'",
                        parseDbColumna(critData.getDbColumna()),
                        parseArgAsStringPart(args.get(0))
                );
            case STR_ENDS:
                return String.format("%s LIKE '%%%s'",
                        parseDbColumna(critData.getDbColumna()),
                        parseArgAsStringPart(args.get(0))
                );
            case STR_CONTAINS:
                return String.format("%s LIKE '%%%s%%'",
                        parseDbColumna(critData.getDbColumna()),
                        parseArgAsStringPart(args.get(0))
                );
            case DATES_BETWEEN:
                return String.format("(%s BETWEEN %s AND %s)",
                        parseDbColumna(critData.getDbColumna()),
                        parseArg(args.get(0)),
                        parseArg(args.get(1))
                );
        }
        return null;
    }

}
