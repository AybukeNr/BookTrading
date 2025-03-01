package org.example.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import weka.associations.Apriori;
import weka.associations.AssociationRules;
import weka.associations.AssociationRule;
import weka.core.SerializationHelper;
import weka.associations.Item;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

@Service
@Slf4j
public class RecommendationService {


    @Value("${apriori.model.file.path}")
    private String modelFilePath; // Örn: "classpath:apriori.model"

    private AssociationRules associationRules;

    private final ResourceLoader resourceLoader;

    public RecommendationService(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() throws Exception {
        Resource resource = resourceLoader.getResource(modelFilePath);
        if (!resource.exists()) {
            throw new Exception("Model dosyası bulunamadı: " + modelFilePath);
        }
        try (InputStream is = resource.getInputStream()) {
            Object model = SerializationHelper.read(is);
            if (model instanceof Apriori) {
                Apriori apriori = (Apriori) model;
                associationRules = apriori.getAssociationRules();
            } else {
                throw new Exception("Yüklenen model Apriori tipi değil!");
            }
        }
    }

    /**
     * Kullanıcının ziyaret ettiği kategoriler temelinde öneriler üretir.
     * Gelen set, örneğin {"Biography=1", "History=1", "Science=1"} şeklinde olup,
     * bu da kullanıcının bu kategorilerde aktif olduğunu gösterir.
     *
     * Servis, kural setinde antecedent kısmı bu öğelerle eşleşiyorsa,
     * consequence kısmında "1" değerine sahip fakat kullanıcının henüz ziyaret etmediği kategorileri önerir.
     *
     * @param transactionItems Kullanıcının ziyaret ettiği kategorileri içeren set (ör: "Biography=1")
     * @return Önerilen kategori isimlerinin seti (ör: "Fiction", "Self-Help", …)
     */
    public Set<String> getRecommendations(Set<String> transactionItems) {
        Set<String> recommendations = new HashSet<>();

        // Tüm association rule'lar üzerinde dönüyoruz.
        for (AssociationRule rule : associationRules.getRules()) {
            boolean matches = false;
            // Kuralın antecedent (önkoşul) kısmındaki öğelerden en az biri kullanıcının transaction'ında varsa yeterli.
            for (Item item : rule.getPremise()) {
                String premiseRepresentation = item.getAttribute().name() + "=" + item.getItemValueAsString();
                if (transactionItems.contains(premiseRepresentation)) {
                    matches = true;
                    break;
                }
            }
            if (matches) {
                // Kuralın sonuç (consequence) kısmını kontrol ediyoruz.
                for (Item item : rule.getConsequence()) {
                    // Yalnızca sonucunun değeri "1" olan kategorileri öneriye ekle.
                    if ("1".equals(item.getItemValueAsString())) {
                        String attributeName = item.getAttribute().name();
                        // Eğer kullanıcı zaten bu kategoride aktif değilse öneriye ekle.
                        if (!transactionItems.contains(attributeName + "=1")) {
                            recommendations.add(attributeName);
                        }
                    }
                }
            }
        }
        log.info("Recommentations: {} {}",recommendations,recommendations.size());
        return recommendations;
    }

    public Set<String> getFilteredRecommendations(Set<String> visitedCategories) {
        // Önerileri ve ilgili en yüksek confidence değerlerini tutacak harita
        Map<String, Double> recMap = new HashMap<>();

        // Gelen ziyaret edilmiş kategori isimlerini "Category=1" formatına dönüştür.
        Set<String> visitedItems = visitedCategories.stream()
                .map(cat -> cat + "=1")
                .collect(Collectors.toSet());

        // Filtre eşik değerleri
        double minConfidenceThreshold = 0.90; // kuralın confidence değeri %95 veya daha yüksek
        double minLiftThreshold = 1.1;        // kuralın lift değeri 1.5'in üzerinde olmalı

        // Tüm association rule'lar üzerinden dön
        for (AssociationRule rule : associationRules.getRules()) {
            // Primary metric değeri (genellikle confidence) al
            double confidence = rule.getPrimaryMetricValue();
            double lift = 0;
            try {
                lift = rule.getNamedMetricValue("lift");
            } catch (Exception e) {
                // Eğer lift alınamıyorsa kuralı atla
                continue;
            }

            if (confidence < minConfidenceThreshold || lift < minLiftThreshold) {
                continue;
            }

            // Antecedent (önkoşul) öğelerinden herhangi birinin kullanıcının transaction'ında bulunup bulunmadığını kontrol et.
            boolean matches = false;
            for (Item item : rule.getPremise()) {
                String premiseRepresentation = item.getAttribute().name() + "=" + item.getItemValueAsString();
                if (visitedItems.contains(premiseRepresentation)) {
                    matches = true;
                    break;
                }
            }

            if (matches) {
                // Eğer kuralın önkoşul kısmından en az biri sağlanıyorsa,
                // sonuç (consequence) kısmındaki, değeri "1" olan ve kullanıcının henüz ziyaret etmediği kategorileri ekle.
                for (Item item : rule.getConsequence()) {
                    if ("1".equals(item.getItemValueAsString())) {
                        String attributeName = item.getAttribute().name();
                        if (!visitedItems.contains(attributeName + "=1")) {
                            // Aynı kategori birden fazla kural tarafından önerilebileceğinden, en yüksek confidence değerini sakla.
                            recMap.put(attributeName, Math.max(recMap.getOrDefault(attributeName, 0.0), confidence));
                        }
                    }
                }
            }
        }

        // Eğer öneri sayısı 10'dan fazla ise, en yüksek confidence değerine göre sıralayıp ilk 10 tanesini döndür.
        List<Map.Entry<String, Double>> sortedRecs = recMap.entrySet().stream()
                .sorted((e1, e2) -> -Double.compare(e1.getValue(), e2.getValue()))
                .collect(Collectors.toList());

        Set<String> finalRecommendations = new HashSet<>();
        int count = 0;
        for (Map.Entry<String, Double> entry : sortedRecs) {
            if (count >= 10) break;
            finalRecommendations.add(entry.getKey());
            count++;
        }

        log.info("Recommendations: {} count : {}",finalRecommendations,finalRecommendations.size());

        return finalRecommendations;
    }



}
