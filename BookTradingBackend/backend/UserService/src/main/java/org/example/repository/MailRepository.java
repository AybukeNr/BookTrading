package org.example.repository;

import org.example.entity.Mail;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface MailRepository extends MongoRepository<Mail, String> {
}
