package org.indexinvesting.correctionfactor;

import java.io.File;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CorrectionFactorGetterFromCsv implements CorrectionFactorGetter {

    private static final String FILENAME = "src/main/resources/correctionFactors.csv";
    private static final String COMMA_DELIMITER = ";";
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

    @Override
    public List<CorrectionFactor> get() {
        List<CorrectionFactor> correctionFactors = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(FILENAME))) {
            while (scanner.hasNextLine()) {
                CorrectionFactor correctionFactor = getCorrectionFactorsFromLine(scanner.nextLine());
                if (correctionFactor != null) {
                    correctionFactors.add(correctionFactor);
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File " + FILENAME + " not found", e);
        }

        return correctionFactors;
    }

    private CorrectionFactor getCorrectionFactorsFromLine(String line) {
        if (line == null || line.isBlank()) {
            return null;
        }

        List<String> valuesInLine = getValuesFromLine(line);

        if (valuesInLine.size() < 3 || valuesInLine.size() > 4) {
            throw new RuntimeException("Некорректный файл с поправочными коэффициентами");
        }

        CorrectionFactor correctionFactor = new CorrectionFactor();
        for (int i = 0; i < valuesInLine.size(); i++) {
            String value = valuesInLine.get(i);

            switch (i) {
                case 0:
                    correctionFactor.setSecId(value);
                    break;
                case 1:
                    correctionFactor.setCorrectionFactor(Double.valueOf(value));
                    break;
                case 2:
                    LocalDate date = LocalDate.parse(value, formatter);
                    correctionFactor.setDate(date);
                    break;
                case 3:
                    correctionFactor.setComment(value);
                    break;
                default:
            }
        }

        return correctionFactor;
    }

    private List<String> getValuesFromLine(String line) {
        List<String> values = new ArrayList<>();
        try (Scanner rowScanner = new Scanner(line)) {
            rowScanner.useDelimiter(COMMA_DELIMITER);
            while (rowScanner.hasNext()) {
                values.add(rowScanner.next());
            }
        }
        return values;
    }
}
