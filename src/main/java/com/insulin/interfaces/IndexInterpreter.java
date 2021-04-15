package com.insulin.interfaces;

/**
 * Interface to provide an interpretation of indexes calculation result
 * To not be confused with Interpreter interface, which is used specially for interpreting
 * glucose values.
 * <p>
 * As each indices has a different range to be checked against, the interface will
 * force each indices to return the relevant interval for healthy persons.
 * <p>
 * This interface will be applied to the classes created for each indices.
 * For the indices that have no interpretation, a simple dash (-) will be returned.
 * For indices that have a interpretation value, the value will be healthy or not
 * (specific for each indices).
 */
public interface IndexInterpreter {
    String interpret(double result);
    String getInterval();
}
