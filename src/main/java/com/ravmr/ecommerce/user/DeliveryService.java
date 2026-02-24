package com.ravmr.ecommerce.user;

import org.springframework.stereotype.Service;

import com.ravmr.ecommerce.user.DeliveryController.DeliveryRequest;

import jakarta.transaction.Transactional;

@Service
public class DeliveryService {
    private final DeliveryRepository repo;

    public DeliveryService(DeliveryRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public Delivery getDefaultDeliveryForUser(Long userId) {
        return repo.findByUserIdAndIsDefaultTrue(userId)
                .orElseThrow(() -> new RuntimeException("Default delivery not found for user ID: " + userId));
    }

    @Transactional
    public void addDeliveryInfo(User user, DeliveryRequest req) {
        Delivery delivery = new Delivery();
        delivery.setName(req.name() != null ? req.name() : user.getFirstName() + " " + user.getLastName());
        delivery.setStreet(req.street());
        delivery.setCity(req.city());
        delivery.setHouseNumber(req.houseNumber());
        delivery.setApartmentNumber(req.apartmentNumber());
        delivery.setPostalCode(req.postalCode());
        delivery.setPhoneNumber(req.phoneNumber());
        delivery.setIsDefault(true);
        delivery.setUser(user);
        repo.save(delivery);
    }
}
