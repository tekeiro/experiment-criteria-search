package org.keirobm.jpacriteriabuilder.jpaquerybuilder;

import org.keirobm.jpacriteriabuilder.annotations.DbEntity;
import org.keirobm.jpacriteriabuilder.annotations.DbField;
import org.keirobm.jpacriteriabuilder.annotations.DbPropertyPath;
import org.keirobm.jpacriteriabuilder.criteriaset.ICriteriaSet;
import org.keirobm.jpacriteriabuilder.jpacriteria.JPAQueryCriteriaOperations;
import org.keirobm.jpacriteriabuilder.jpaquerybuilder.onequeryhelpers.ConditionBuilder;
import org.keirobm.jpacriteriabuilder.jpaquerybuilder.onequeryhelpers.PutArgsInMapData;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;


/**
 * Proxy for a custom implementation of
 * {@link org.keirobm.jpacriteriabuilder.criteriaset.ICriteriaSet}
 * that implements methods that follows
 */
public class OneQueryJPACriteriaDynamicProxy implements InvocationHandler {
    private final Class<? extends ICriteriaSet> criteriaSetClass;

    private final List<String> orderBy = new LinkedList<>();
    private Integer maxResults = null;
    private Integer firstResult = null;
    private final List<String> joins = new LinkedList<>();
    private final List<String> grouping = new LinkedList<>();


    private final Map<String, CriteriaData> mapCriteria = new LinkedHashMap<>();
    private final Map<String, Object> mapData = new LinkedHashMap<>();

    private String dbEntity;
    private String letter;
    private String[] defaultJoins;


    private final OperationsSupported[] operations = OperationsSupported.values();
    private final PutArgsInMapData putArgsInMapData = new PutArgsInMapData(mapData, mapCriteria);


    public OneQueryJPACriteriaDynamicProxy(Class<? extends ICriteriaSet> criteriaSetClass) {
        Objects.requireNonNull(criteriaSetClass);
        this.criteriaSetClass = criteriaSetClass;

        parseCriteriaSetClass();
    }

    private OperationsSupported findOperation(Method method) {
        final String metName = method.getName();
        return Arrays.stream(operations)
                .filter(op -> op.matches(metName))
                .findFirst()
                .orElse(null)
                ;
    }


    private void parseCriteriaSetClass() {

        if (criteriaSetClass.isAnnotationPresent(DbEntity.class)) {
            final DbEntity dbEntity = criteriaSetClass.getAnnotation(DbEntity.class);
            this.dbEntity = dbEntity.value();
            this.letter = dbEntity.letter();
            if (dbEntity.defaultJoins().length > 0) {
                this.defaultJoins = dbEntity.defaultJoins();
            }
            else
                this.defaultJoins = new String[0];
        }

        for (Method method : criteriaSetClass.getMethods()) {
            final OperationsSupported op = findOperation(method);
            if (op != null) {
                final String criteriaName = Objects.requireNonNull(
                        op.extractCriteriaName(method.getName())
                );

                String dbColumna = criteriaName;
                if (method.isAnnotationPresent(DbField.class)) {
                    final DbField dbField = method.getAnnotation(DbField.class);
                    dbColumna = dbField.value();
                    dbColumna = dbColumna.replaceAll("%l", letter);
                }

                String propertyPath = "";
                if (method.isAnnotationPresent(DbPropertyPath.class)) {
                    final DbPropertyPath propPath = method.getAnnotation(DbPropertyPath.class);
                    propertyPath = propPath.value();
                }

                final CriteriaData critData = new CriteriaData(criteriaName, dbColumna, op, propertyPath);
                mapCriteria.put(method.getName(), critData);
            }
        }

    }


    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final String metName = method.getName();

        // Check if args is null
        if (args == null)
            args = new Object[0];

        // Is a Criteria Method
        final CriteriaData critData = mapCriteria.get(metName);
        if (critData != null) {
            putArgsInMapData.putArgs(method, critData, args);

            // Returning this because its a Builder method
            return proxy;
        }


        // Some methods of ICriteriaSet using a delegate
        final Optional<Method> optICriteriaSet = findMethodInClass(criteriaSetDelegate.getClass(), metName);
        if (optICriteriaSet.isPresent()) {
            optICriteriaSet.get().invoke(criteriaSetDelegate, args);
            return proxy;
        }

        // Methods from JPACriteriaOperations
        final Optional<Method> optJPAQueryCriteriaOps =
                findMethodInClass(jpaOperations.getClass(), metName);
        if (optJPAQueryCriteriaOps.isPresent()) {
            return optJPAQueryCriteriaOps.get().invoke(jpaOperations, args);
        }

        throw new IllegalArgumentException("Method not supported : " + metName);
    }

    private Optional<Method> findMethodInClass(Class<?> klass, String methodName) {
        return Arrays.stream(klass.getMethods())
                .filter(m -> methodName.equals(m.getName()))
                .findFirst();
    }


    private final ICriteriaSet criteriaSetDelegate = new ICriteriaSet() {
        @Override
        public ICriteriaSet setOrderBy(String... orderBy) {
            final String letter = OneQueryJPACriteriaDynamicProxy.this.letter;

            if (orderBy != null) {
                OneQueryJPACriteriaDynamicProxy.this.orderBy
                        .addAll(
                                Arrays.stream(orderBy)
                                    .map(o -> o.replaceAll("%l", letter))
                                    .collect(Collectors.toList())
                        );
            }
            return this;
        }

        @Override
        public ICriteriaSet setLimit(int maxResults) {
            OneQueryJPACriteriaDynamicProxy.this.maxResults = maxResults;
            return this;
        }

        @Override
        public ICriteriaSet setFirstResult(int firstResult) {
            OneQueryJPACriteriaDynamicProxy.this.firstResult = firstResult;
            return this;
        }

        @Override
        public ICriteriaSet addJoins(String... joins) {
            final String letter = OneQueryJPACriteriaDynamicProxy.this.letter;

            if (joins != null) {
                OneQueryJPACriteriaDynamicProxy.this.joins
                        .addAll(
                                Arrays.stream(joins)
                                    .map(j -> j.replaceAll("%l", letter))
                                    .collect(Collectors.toList())
                        );
            }
            return this;
        }

        @Override
        public ICriteriaSet setGrouping(String... groupings) {
            final String letter = OneQueryJPACriteriaDynamicProxy.this.letter;

            if (groupings != null) {
                OneQueryJPACriteriaDynamicProxy.this.grouping
                        .addAll(
                                Arrays.stream(groupings)
                                    .map(g -> g.replaceAll("%l", letter))
                                    .collect(Collectors.toList())
                        );
            }
            return this;
        }
    };

    private String buildAllJoins() {
        final List<String> finalJoins = new LinkedList<>();
        finalJoins.addAll(Arrays.asList(defaultJoins));
        finalJoins.addAll(joins);

        return finalJoins.stream().collect(Collectors.joining(" "));
    }

    private final JPAQueryCriteriaOperations jpaOperations = new JPAQueryCriteriaOperations() {

        private String buildWherePart() {
            final String letter = OneQueryJPACriteriaDynamicProxy.this.letter;
            final List<String> conditions = new LinkedList<>();
            final List<String> ordersBy = OneQueryJPACriteriaDynamicProxy.this.orderBy;
            final List<String> grouping = OneQueryJPACriteriaDynamicProxy.this.grouping;
            final ConditionBuilder condBuilder = new ConditionBuilder(letter);

            for (String metName : mapCriteria.keySet()) {
                final CriteriaData critData = mapCriteria.get(metName);
                // Si mapData contiene valor para el nombre de metodo significa que
                //   se ha añadido un filtro para ese criterio.
                //   En otro caso, no se ha añadido filtro y no sera entonces añadida la
                //   condicion a la consulta.
                if (mapData.containsKey(metName)) {
                    final List<Object> args = (List<Object>) mapData.get(metName);

                    final String cond = condBuilder.buildCondition(critData, args);
                    if (cond != null)
                        conditions.add(cond);
                }
            }

            final StringBuilder sbGroup = new StringBuilder();
            if (!grouping.isEmpty()) {
                sbGroup.append("GROUP BY ");
                sbGroup.append(
                        grouping.stream().collect(Collectors.joining(", "))
                );
            }

            final StringBuilder sbOrder = new StringBuilder();
            if (!ordersBy.isEmpty()) {
                sbOrder.append("ORDER BY ");
                sbOrder.append(
                        ordersBy.stream()
                                .collect(Collectors.joining(", "))
                );
            }


            return String.format(
                    " %s %s %s",
                    conditions.stream().collect(Collectors.joining(" AND ")),
                    sbGroup.toString(),
                    sbOrder.toString()
            );
        }


        @Override
        public String buildQuery() {
            return String.format(
                    "SELECT %s FROM %s %s %s WHERE %s",
                    OneQueryJPACriteriaDynamicProxy.this.letter,
                    OneQueryJPACriteriaDynamicProxy.this.dbEntity,
                    OneQueryJPACriteriaDynamicProxy.this.letter,
                    OneQueryJPACriteriaDynamicProxy.this.buildAllJoins(),
                    buildWherePart()
            );
        }

        @Override
        public String buildQueryCount() {
            return String.format(
                    "SELECT COUNT(%s) FROM %s %s %s WHERE %s",
                    OneQueryJPACriteriaDynamicProxy.this.letter,
                    OneQueryJPACriteriaDynamicProxy.this.dbEntity,
                    OneQueryJPACriteriaDynamicProxy.this.letter,
                    OneQueryJPACriteriaDynamicProxy.this.buildAllJoins(),
                    buildWherePart()
            );
        }

        @Override
        public <T> String buildQueryForCustomObject(Class<T> objectResultantClass, String... selectColumns) {
            String selectColumnsJoined = Arrays.stream(selectColumns).collect(Collectors.joining(", "));

            return String.format(
                    "SELECT NEW %s (%s) FROM %s %s %s WHERE %s",
                    objectResultantClass.getCanonicalName(),
                    selectColumnsJoined,
                    OneQueryJPACriteriaDynamicProxy.this.dbEntity,
                    OneQueryJPACriteriaDynamicProxy.this.letter,
                    OneQueryJPACriteriaDynamicProxy.this.buildAllJoins(),
                    buildWherePart()
            );
        }

        @Override
        public String buildQueryAsMap(String... selectColumns) {
            String selectColumnsJoined = Arrays.stream(selectColumns).collect(Collectors.joining(", "));

            return String.format(
                    "SELECT NEW Map (%s) FROM %s %s %s WHERE %s",
                    selectColumnsJoined,
                    OneQueryJPACriteriaDynamicProxy.this.dbEntity,
                    OneQueryJPACriteriaDynamicProxy.this.letter,
                    OneQueryJPACriteriaDynamicProxy.this.buildAllJoins(),
                    buildWherePart()
            );
        }
    };

}
