package winter.parsers;

import winter.enums.RequestType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * RequestParser
 *
 * @author Баярхүү.Лув, 2022.08.02
 */
public class Request {
    private final Map<RequestType, String> map;

    private Request(Map<RequestType, String> map) {
        this.map = map;
    }

    public static Request of(String request) {
        String[] items = request.split("\n");
        if (items[0].contains("favicon")) return null;

        Map<RequestType, String> map = new HashMap<>();
        for (String s : items) {
            if (s.startsWith("GET")) {
                map.put(RequestType.URL, s.substring(s.indexOf("GET") + 4, s.indexOf("HTTP") - 1));
                map.put(RequestType.METHOD, "GET");
            } else if (s.startsWith("POST")) {
                map.put(RequestType.URL, s.substring(s.indexOf("POST") + 5, s.indexOf("HTTP") - 1));
                map.put(RequestType.METHOD, "POST");
                String json = "";
                for (int i = 0; i < items.length; i++) {
                    if (items[i].equals("\r")) {
                        json = Arrays.stream(items).skip(i + 1).collect(Collectors.joining(""));
                        break;
                    }
                }
                map.put(RequestType.BODY, json);
            } else if (s.startsWith("Host")) {
                map.put(RequestType.HOST, s.substring(s.indexOf("Host") + 6));
            } else if (s.startsWith("Accept:")) {
                map.put(RequestType.ACCEPT, s.substring(s.indexOf("Accept:") + 8));
            } else if (s.startsWith("Cookie")) {
                map.put(RequestType.COOKIE, s.substring(s.indexOf("Cookie") + 8));
            }
        }

        return new Request(map);
    }

    public String get(RequestType key) {
        return map.get(key);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        map.forEach((k, v) -> str.append(k).append(": ").append(v).append("\n"));
        return str.toString();
    }
}
