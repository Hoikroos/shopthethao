package assjava5.assjava5.Service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import assjava5.assjava5.DAO.ProductDAO;
import assjava5.assjava5.Entity.CartItem;
import assjava5.assjava5.Entity.Product;

@SessionScope
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private SessionService sessionService;

    private Map<Integer, CartItem> cart = new LinkedHashMap<>();

    @Override
    public CartItem add(Integer id) {
        String username = sessionService.get("username", "");
        if (username == null || username.isEmpty()) {
            throw new RuntimeException("Bạn cần đăng nhập để thêm sản phẩm vào giỏ hàng.");
        }
        Product product = productDAO.findById(id)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại."));
        CartItem cartItem = cart.get(id);
        if (cartItem == null) {
            cartItem = new CartItem(product, 1);
            cart.put(id, cartItem);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + 1);
        }
        return cartItem;
    }
    @Override
    public void remove(Integer id) {
        cart.remove(id);
    }

    @Override
    public CartItem update(Integer id, int qty) {
        CartItem cartItem = cart.get(id);
        if (cartItem != null) {
            cartItem.setQuantity(qty);
        }
        return cartItem;
    }

    @Override
    public void clear() {
        cart.clear();
    }

    @Override
    public Collection<CartItem> getItems() {
        return cart.values();
    }

    @Override
    public int getCount() {
        return cart.values().stream().mapToInt(CartItem::getQuantity).sum();
    }

    @Override
    public double getAmount() {
        return cart.values().stream()
                .map(item -> BigDecimal.valueOf(item.getTotalPrice()))
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .doubleValue();
    }

    @Override
    public int getTotalQuantity() {
        return cart.values().stream().mapToInt(CartItem::getQuantity).sum();
    }

    @Override
    public void clearCart() {
        cart.clear();
    }

}
