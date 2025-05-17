package org.example.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.external.UserManager;
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
    private String modelFilePath; // application.yml "classpath:apriori.model"

    private AssociationRules associationRules;

    private final ResourceLoader resourceLoader;

    private final UserManager usersManager;

    public RecommendationService(ResourceLoader resourceLoader, UserManager usersManager) {
        this.resourceLoader = resourceLoader;
        this.usersManager = usersManager;
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

    public Set<String> getFilteredRecommendations(Set<String> transactionItems) {
        Map<String, Double> recMap = new HashMap<>();
        Map<String, List<Double>> ruleDetails = new HashMap<>(); // Önerinin hangi confidence değerlerine sahip olduğunu saklayacak.

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
                double confidence = rule.getPrimaryMetricValue();

                // Kuralın consequence (sonuç) kısmını kontrol ediyoruz.
                for (Item item : rule.getConsequence()) {
                    // Yalnızca sonucunun değeri "1" olan kategorileri öneriye ekle.
                    if ("1".equals(item.getItemValueAsString())) {
                        String attributeName = item.getAttribute().name();

                        // Eğer kullanıcı zaten bu kategoride aktif değilse öneriye ekle.
                        if (!transactionItems.contains(attributeName + "=1")) {
                            // Aynı kategori birden fazla kural tarafından önerilebileceğinden, en yüksek confidence değerini sakla.
                            recMap.put(attributeName, Math.max(recMap.getOrDefault(attributeName, 0.0), confidence));

                            // Öneri için kullanılan kural confidence değerlerini sakla.
                            ruleDetails.computeIfAbsent(attributeName, k -> new ArrayList<>()).add(confidence);
                        }
                    }
                }
            }
        }

        // Eğer öneri sayısı 10'dan fazla ise, en yüksek confidence değerine göre sıralayıp ilk 10 tanesini döndürelim.
        List<Map.Entry<String, Double>> sortedRecs = recMap.entrySet().stream()
                .sorted((e1, e2) -> -Double.compare(e1.getValue(), e2.getValue()))
                .collect(Collectors.toList());

        Set<String> finalRecommendations = new HashSet<>();
        int count = 0;
        for (Map.Entry<String, Double> entry : sortedRecs) {
            if (count >= 6) break;
            finalRecommendations.add(entry.getKey());
            count++;
        }

        // Konsola hangi önerilerin hangi kurallardan geldiğini yazdır.
        for (String recommendation : finalRecommendations) {
            log.info("Recommended Category: {} | Confidence Values: {}", recommendation, ruleDetails.get(recommendation));
        }

        log.info("Final Recommendations: {} | Count: {}", finalRecommendations, finalRecommendations.size());
        return finalRecommendations;
    }

}
