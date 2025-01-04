package org.example.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.dto.response.ExchangeResponse;

import org.example.entity.enums.ExchangeStatus;
import org.example.external.TransactionsManager;
import org.example.mapper.ShippingMapper;
import org.example.repository.ExchangeRepository;
import org.example.service.IExchangeService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
@Slf4j
public class ExchangeServiceImpl {
    private final ExchangeRepository exchangeRepository;
    private final ShippingMapper shippingMapper;
    private final TransactionsManager transactionsManager;





}
