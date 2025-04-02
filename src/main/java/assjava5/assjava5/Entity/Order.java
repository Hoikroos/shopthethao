package assjava5.assjava5.Entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;

import lombok.*;

@Entity
@Table(name = "Orders")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String address;

    @Column(name = "createdate")
    private LocalDateTime createDate = LocalDateTime.now();

    @Transient
    private Double totalAmount;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private Account account;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderDetail> orderDetails = new ArrayList<>();

    public void updateTotalAmount() {
        if (orderDetails != null) {
            this.totalAmount = orderDetails.stream()
                    .mapToDouble(detail -> detail.getPrice() * detail.getQuantity())
                    .sum();
        }
    }
}
