package pizzahub.api.entities.order;

public enum OrderStatus {
    PENDING,
    PREPARING,
    READY,
    OUT_FOR_DELIVERY,
    COMPLETED,
    ON_HOLD,
    CANCELLED
}
