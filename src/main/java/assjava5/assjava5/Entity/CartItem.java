package assjava5.assjava5.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    private Product product;
    private int quantity;

    public double getTotalPrice() {
        return (product.getPrice() > 0) ? product.getPrice() * quantity : 0.0;
    }
}
