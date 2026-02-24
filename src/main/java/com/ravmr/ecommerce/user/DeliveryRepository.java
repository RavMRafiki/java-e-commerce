package com.ravmr.ecommerce.user;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;


public interface DeliveryRepository extends JpaRepository<Delivery, Long> {
    Optional<Delivery> findByUserIdAndIsDefaultTrue(Long userId);

    
}
