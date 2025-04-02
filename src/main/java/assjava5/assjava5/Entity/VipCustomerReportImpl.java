package assjava5.assjava5.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VipCustomerReportImpl implements VipCustomerReport {
    private String customerName;
    private Double totalSpent;
    private LocalDateTime firstPurchaseDate;
    private LocalDateTime lastPurchaseDate;
}
