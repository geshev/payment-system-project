package com.example.payment.util;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.core.io.ClassPathResource;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class CSVUtils {

    public static <T> List<T> loadCSV(String file, Class<T> type) throws IOException {
        ClassPathResource resource = new ClassPathResource(file);
        try (Reader reader = new FileReader(resource.getFile())) {
            CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
                    .withType(type)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();
            return csvToBean.parse();
        }
    }
}
