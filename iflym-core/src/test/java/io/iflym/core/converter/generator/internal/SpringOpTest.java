package io.iflym.core.converter.generator.internal;

/**
 * created at 2019-04-16
 *
 * @author flym
 */
public class SpringOpTest {
    public static void springClassNotExist() {
        SpringOptions.springClassExist4Test = false;
    }

    public static void revertSpringClassExistValue() {
        SpringOptions.springClassExist4Test = true;
    }
}
