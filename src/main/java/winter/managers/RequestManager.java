package winter.managers;

import winter.WinterApplication;
import winter.annotations.Body;
import winter.annotations.Get;
import winter.annotations.Param;
import winter.annotations.Post;
import winter.enums.HTTP;
import winter.log.Log;
import winter.log.LogType;
import winter.models.ControllerWrapper;
import winter.utils.Common;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;

/**
 * GetManager
 *
 * @author Bayarkhuu.Luv, 2022.08.02
 */
public class RequestManager {


    public static HTTP get(ControllerWrapper model, String[] url) {
        if (!model.getMethod().isAnnotationPresent(Get.class)) {
            return HTTP.METHOD_NOT_ALLOWED;
        }

        try {
            Object invoke;
            if (url.length == 2) {
                Map<String, Object> requestParams = Common.paramExtract(url[1]);
                Object[] parameters = new Object[model.getMethod().getParameterCount()];

                Parameter[] params = model.getMethod().getParameters();
                int i = 0;
                for (Parameter parameter : params) {
                    if (parameter.isAnnotationPresent(Body.class)) {
                        Log.println("Body parameter is not allowed in GET method -> " + model.getObject().getClass().getSimpleName() + "." + model.getMethod().getName(), LogType.ERROR);
                        return HTTP.INTERNAL_SERVER_ERROR;
                    }

                    Param annotation = parameter.getAnnotation(Param.class);
                    String param = annotation.value();
                    if (annotation.required() && requestParams.get(param) == null) {
                        return HTTP.BAD_REQUEST.setResponse("Required parameter '" + param + "' is missing");
                    }

                    parameters[i] = requestParams.get(param);
                    i++;
                }

                invoke = model.getMethod().invoke(model.getObject(), parameters);
            } else {
                invoke = model.getMethod().invoke(model.getObject());
            }

            return HTTP.OK.setResponse(Common.isPrimitive(invoke) ? String.valueOf(invoke) : WinterApplication.gson.toJson(invoke));
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return HTTP.BAD_REQUEST.setResponse("Internal server error");
        }
    }

    public static HTTP post(ControllerWrapper model, String[] url, String data) {
        if (!model.getMethod().isAnnotationPresent(Post.class)) {
            return HTTP.METHOD_NOT_ALLOWED;
        }

        Object[] parameters = new Object[model.getMethod().getParameterCount()];
        Object invoke;

        if (url.length == 2) {
            Map<String, Object> requestParams = Common.paramExtract(url[1]);

            int i = 0;
            for (Parameter parameter : model.getMethod().getParameters()) {
                if (parameter.isAnnotationPresent(Param.class)) {
                    Param annotation = parameter.getAnnotation(Param.class);
                    String param = annotation.value();
                    if (annotation.required() && requestParams.get(param) == null) {
                        return HTTP.BAD_REQUEST.setResponse("Required parameter '" + param + "' is missing");
                    }
                    parameters[i] = Common.parse(requestParams.get(param), parameter.getType());
                } else if (parameter.isAnnotationPresent(Body.class)) {
                    parameters[i] = WinterApplication.gson.fromJson(data, parameter.getType());
                }
                i++;
            }
        } else {
            parameters[0] = WinterApplication.gson.fromJson(data, model.getMethod().getParameters()[0].getType());
        }

        try {
            invoke = model.getMethod().invoke(model.getObject(), parameters);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
            return HTTP.BAD_REQUEST.setResponse("Internal server error");
        }

        return HTTP.OK.setResponse(Common.isPrimitive(invoke) ? String.valueOf(invoke) : WinterApplication.gson.toJson(invoke));
    }
}
