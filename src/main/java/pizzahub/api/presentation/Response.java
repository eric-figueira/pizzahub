package pizzahub.api.presentation;

public record Response (
    boolean isError,
    String message,
    Object body
) {}
