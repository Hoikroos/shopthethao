package assjava5.assjava5.Entity;

import java.time.LocalDateTime;

public interface VipCustomerReport {
    String getCustomerName();
    Double getTotalSpent();
    LocalDateTime getFirstPurchaseDate();
    LocalDateTime getLastPurchaseDate();
}
