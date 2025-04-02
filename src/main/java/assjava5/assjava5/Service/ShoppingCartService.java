package assjava5.assjava5.Service;

import java.util.Collection;

import assjava5.assjava5.Entity.CartItem;

public interface ShoppingCartService {
    CartItem add(Integer id);

    void remove(Integer id);

    CartItem update(Integer id, int qty);

    void clear();

    Collection<CartItem> getItems();

    int getCount();

    double getAmount();

    int getTotalQuantity();

    void clearCart();

}
