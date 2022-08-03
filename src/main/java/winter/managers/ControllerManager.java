package winter.managers;

import winter.annotations.Controller;
import winter.annotations.Get;
import winter.annotations.Post;
import winter.enums.HTTP;
import winter.enums.RequestType;
import winter.models.ControllerWrapper;
import winter.parsers.Request;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * ControllerManager
 *
 * @author Баярхүү.Лув, 2022.08.02
 */
public class ControllerManager {
    private final List<ControllerWrapper> controllers = new ArrayList<>();

    public ControllerManager(List<Class> controllerClass) {
        for (Class c : controllerClass) {
            Controller controller = (Controller) c.getAnnotation(Controller.class);
            String baseUrl = controller.value();

            for (Method method : c.getDeclaredMethods()) {
                method.setAccessible(true);
                String url;
                if (method.isAnnotationPresent(Get.class)) {
                    url = method.getAnnotation(Get.class).value();
                } else if (method.isAnnotationPresent(Post.class)) {
                    url = method.getAnnotation(Post.class).value();
                } else {
                    continue;
                }

                try {
                    Object o = c.getConstructors()[0].newInstance();
                    controllers.add(new ControllerWrapper(baseUrl, url, o, method));
                } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public HTTP control(Request request) {
        String[] url = request.get(RequestType.URL).split("\\?");

        if (url.length > 2) {
            return HTTP.BAD_REQUEST.setResponse("URL contains two or more '?'");
        }

        ControllerWrapper model = controllers.stream().filter(f -> f.getUrl().equals(url[0])).findFirst().orElse(null);

        if (model != null) {
            if (model.getMethod().isAnnotationPresent(Get.class)) {
                return RequestManager.get(model, url);
            } else if (model.getMethod().isAnnotationPresent(Post.class)) {
                return RequestManager.post(model, url, request.get(RequestType.BODY));
            }
        }

        return HTTP.NOT_FOUND;
    }
}
