package org.example.repository;

import org.example.entity.Shippings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ShippingRepository extends JpaRepository<Shippings,Long> {
    Optional<Shippings> findById(Long id);
    Optional<Shippings> findByShippingSerialNumber(String serialNumber);

    Optional<Shippings> findBySenderIdAndListId(String senderId, String listId);
    List<Shippings> findAllBySenderIdOrRecieverId(String senderId, String recieverId);

}
