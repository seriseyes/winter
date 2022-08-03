package winter.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * WinterClassLoader
 *
 * @author Баярхүү.Лув, 2022.08.02
 */
public class ClassSearcher {

    public Set<Class> findAllClassesUsingClassLoader(String packageName) {
        InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream(packageName.replaceAll("[.]", "/"));
        if (stream == null) return Set.of();

        Set<Class> classes = new HashSet<>();
        new BufferedReader(new InputStreamReader(stream)).lines().forEach(line -> {
            if (line.endsWith(".class")) {
                classes.add(getClass(line, packageName));
            } else {
                classes.addAll(findAllClassesUsingClassLoader(packageName.isEmpty() ? line : packageName + "." + line));
            }
        });
        return classes;
    }

    private Class getClass(String className, String packageName) {
        try {
            return Class.forName(packageName + "."
                    + className.substring(0, className.lastIndexOf('.')));
        } catch (ClassNotFoundException e) {
        }
        return null;
    }

}
