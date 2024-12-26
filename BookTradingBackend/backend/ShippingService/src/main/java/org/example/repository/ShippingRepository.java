package org.example.repository;

import org.example.entity.Shippings;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShippingRepository extends JpaRepository<Shippings,Long> {
    Optional<Shippings> findById(Long id);
    Optional<Shippings> findBySerialNumber(String serialNumber);
}
