package pizzahub.api.presentation;

public record Response (
    String message,
    Object body
) {}
