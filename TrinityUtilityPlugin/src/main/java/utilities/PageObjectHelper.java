package utilities;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

/**
 * Utility class for initializing page objects in the scenario context.
 */
@UtilityClass
public class PageObjectHelper {
    /**
     * Initializes all pages of the given basePage type in the scenarioContext object.
     *
     * @param scenarioContext The scenario context object.
     * @param basePage        The base page class.
     * @param paramTypes      The parameter types array.
     * @param params          The parameters array.
     * @param <T>             The type of the base page.
     * @throws IllegalArgumentException if the length of paramTypes is not equal to the length of params.
     * @throws RuntimeException         if there is an error in instantiating or setting the page object.
     */
    public <T> void initAllPageObjects(Object scenarioContext, Class<T> basePage, Class<?>[] paramTypes, Object[] params) {
        if (paramTypes.length != params.length) {
            throw new IllegalArgumentException("Parameter types array and parameters array must have the same length");
        }
        for (Field field : scenarioContext.getClass().getDeclaredFields()) {
            if (basePage.isAssignableFrom(field.getType())) {
                try {
                    Constructor<?> constructor = field.getType().getDeclaredConstructor(paramTypes);
                    Object o = constructor.newInstance(params);
                    field.setAccessible(true);
                    field.set(scenarioContext, o);
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                         NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
