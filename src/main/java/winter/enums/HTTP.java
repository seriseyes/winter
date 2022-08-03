package winter.enums;

public enum HTTP {
    OK("HTTP/1.1 200 OK\r\n\r\n"),
    BAD_REQUEST("HTTP/1.1 400 Bad Request\r\n\r\n"),
    NOT_FOUND("HTTP/1.1 404 Not Found\r\n\r\n"),
    METHOD_NOT_ALLOWED("HTTP/1.1 405 Method Not Allowed\r\n\r\n"),
    INTERNAL_SERVER_ERROR("HTTP/1.1 500 Internal Server Error\r\n\r\n"),
    ;

    private final String value;
    private String response;

    HTTP(String value) {
        this.value = value;
    }

    public HTTP setResponse(String response) {
        this.response = response;
        return this;
    }

    public String get() {
        return value + (response == null ? "" : response);
    }

    public byte[] getBytes() {
        return get().getBytes();
    }
}
