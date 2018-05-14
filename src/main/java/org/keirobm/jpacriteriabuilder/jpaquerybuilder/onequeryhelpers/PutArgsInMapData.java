package org.keirobm.jpacriteriabuilder.jpaquerybuilder.onequeryhelpers;

import org.apache.commons.beanutils.PropertyUtilsBean;
import org.keirobm.jpacriteriabuilder.jpaquerybuilder.CriteriaData;
import org.keirobm.jpacriteriabuilder.jpaquerybuilder.OperationsSupported;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class PutArgsInMapData {
    private final Map<String, Object> mapData;
    private final Map<String, CriteriaData> mapCriteria;
    private final PropertyUtilsBean propUtils = new PropertyUtilsBean();

    public PutArgsInMapData(Map<String, Object> mapData, Map<String, CriteriaData> mapCriteria) {
        this.mapData = mapData;
        this.mapCriteria = mapCriteria;
    }


    private boolean isNumberOfArgsCorrect(CriteriaData critData, Object[] args) {
        final OperationsSupported op = critData.getOperation();

        if (op.getArgNumber() == 0 && args == null)
            return true;
        else if (op.getArgNumber() == 0 && args.length == 0)
            return true;
        else if (args != null && op.getArgNumber() > 0 && args.length == op.getArgNumber())
            return true;
        else if (op.isVariableArguments())
            return true;
        else
            return false;
    }


    private Object extractProperty(Object bean, CriteriaData critData) {
        try {
            if ("".equals(critData.getPropertyPath()))
                return bean;
            else
                return propUtils.getNestedProperty(bean, critData.getPropertyPath());
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void putArgs(Method method, CriteriaData critData, Object[] args) {
        final OperationsSupported op = critData.getOperation();
        final String metName = method.getName();

        if (!isNumberOfArgsCorrect(critData, args)) {
            throw new IllegalArgumentException(
                    "Arguments for this method [" + metName + "] must be " + op.getArgNumber()
            );
        }

        // Putting arguments into mapData
        if (op.isVariableArguments()) {
            List<Object> argList = (List<Object>) mapData.getOrDefault(metName, new LinkedList<>());
            for (Object arg : args) {
                if (arg instanceof Iterable) {
                    for (Object item : ((Iterable) arg)) {
                        argList.add(extractProperty(item, critData));
                    }
                }
                else if (arg.getClass().isArray()) {
                    for (Object item : (Object[]) arg) {
                        argList.add(extractProperty(item, critData));
                    }
                }
                else
                    argList.add(extractProperty(arg, critData));
            }
            mapData.put(metName, argList);
        }
        else {
            final List<Object> argList = new LinkedList<>();
            for (Object arg : args) {
                argList.add(extractProperty(arg, critData));
            }
            mapData.put(metName, argList);
        }
    }


}
