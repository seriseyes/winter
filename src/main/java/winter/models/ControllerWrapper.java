package winter.models;

import java.lang.reflect.Method;

/**
 * ControllerWrapper
 *
 * @author Баярхүү.Лув, 2022.08.02
 */
public class ControllerWrapper {
    private final String baseUrl;
    private final String url;
    private final Object object;
    private final Method method;

    public ControllerWrapper(String baseUrl, String url, Object object, Method method) {
        this.baseUrl = baseUrl;
        this.url = url;
        this.object = object;
        this.method = method;
    }

    public String getUrl() {
        return baseUrl + url;
    }

    public Method getMethod() {
        return method;
    }

    public Object getObject() {
        return object;
    }
}
