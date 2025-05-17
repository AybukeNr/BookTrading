package org.example;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import weka.associations.Apriori;
import weka.core.Instances;

import weka.core.SerializationHelper;
import weka.core.converters.ConverterUtils;

@SpringBootApplication
@EnableFeignClients
public class RecommendationServiceApplication {
    public static void main(String[] args) throws Exception {

          SpringApplication.run(RecommendationServiceApplication.class);
//        // Veri setini yükle
//        Instances data = ConverterUtils.DataSource.read("balanced_dataset.arff");
//        // İsteğe bağlı: veri setinde bir sınıf (class) varsa ayarla
//        // data.setClassIndex(data.numAttributes()-1);
//
//        // Apriori algoritması oluştur
//        Apriori apriori = new Apriori();
//        String[] options = {"-N", "500", "-C", "0.9", "-D", "0.05", "-M", "0.05", "-S", "-1"};
//        apriori.setOptions(options);
//        apriori.buildAssociations(data);
//
//        // Modeli kaydet (binary formatta)
//        SerializationHelper.write("apriori.model", apriori);
//
//        System.out.println("Apriori modeli seri hale getirildi: apriori.model");
    }
}