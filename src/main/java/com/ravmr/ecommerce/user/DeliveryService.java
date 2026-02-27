package com.ravmr.ecommerce.user;

import org.springframework.stereotype.Service;

import com.ravmr.ecommerce.user.DeliveryController.DeliveryRequest;
import com.ravmr.ecommerce.user.DeliveryController.DeliveryRequestUpdate;

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
    public Delivery addDeliveryInfo(User user, DeliveryRequest req) {
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
        repo.findByUserIdAndIsDefaultTrue(user.getId()).ifPresent(existing -> {
            existing.setIsDefault(false);
            repo.save(existing);
        });
        return repo.save(delivery);
    }

    @Transactional
    public void removeDeliveryInfo(Long deliveryId, User user) {
        Delivery delivery = repo.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found with ID: " + deliveryId));
        if (!delivery.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to delete this delivery info");
        }
        repo.delete(delivery);
    }

    @Transactional
    public Delivery updateDeliveryInfo(Long deliveryId, User user, DeliveryRequestUpdate req) {
        Delivery delivery = repo.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found with ID: " + deliveryId));
        if (!delivery.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to update this delivery info");
        }
        if (req.name() != null) delivery.setName(req.name());
        if (req.street() != null) delivery.setStreet(req.street());
        if (req.city() != null) delivery.setCity(req.city());
        if (req.houseNumber() != null) delivery.setHouseNumber(req.houseNumber());
        if (req.apartmentNumber() != null) delivery.setApartmentNumber(req.apartmentNumber());
        if (req.postalCode() != null) delivery.setPostalCode(req.postalCode());
        if (req.phoneNumber() != null) delivery.setPhoneNumber(req.phoneNumber());
        return repo.save(delivery);
    }

    @Transactional
    public void setDefaultDeliveryInfo(Long deliveryId, User user) {
        Delivery delivery = repo.findById(deliveryId)
                .orElseThrow(() -> new RuntimeException("Delivery not found with ID: " + deliveryId));
        if (!delivery.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized to set this delivery info as default");
        }
        repo.findByUserIdAndIsDefaultTrue(user.getId()).ifPresent(existing -> {
            existing.setIsDefault(false);
            repo.save(existing);
        });
        delivery.setIsDefault(true);
        repo.save(delivery);
    }

    @Transactional
    public java.util.List<Delivery> getAllDeliveryInfoForUser(Long userId) {
        return repo.findAll().stream()
                .filter(delivery -> delivery.getUser().getId().equals(userId))
                .toList();
    }
}
