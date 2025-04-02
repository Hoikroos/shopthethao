package assjava5.assjava5.Entity;

import java.io.Serializable;

public interface Report {
    Serializable getGroup(); 
    Double getSum();
    Long getCount();
    Double getMaxPrice();
    Double getMinPrice();
    Double getAvgPrice();
}
