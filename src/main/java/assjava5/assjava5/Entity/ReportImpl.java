package assjava5.assjava5.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReportImpl implements Report {
    private String group;
    private Double sum;
    private Long count;
    private Double maxPrice;
    private Double minPrice;
    private Double avgPrice; 
}
