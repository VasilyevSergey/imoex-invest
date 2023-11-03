package org.indexinvesting.getindex;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ImoexCompositionGetterFromCsv implements ImoexCompositionGetter {

    // TODO поменять путь к файлу на относительный
    private static final String FILENAME = "C:\\Users\\Имя\\IdeaProjects\\imoex-invest\\src\\main\\resources\\indexComposition.csv";
    private static final String COMMA_DELIMITER = ";";

    /**
     * Актуальный состав индекса МосБиржи для формирования файла можно узнать здесь https://www.moex.com/ru/index/IMOEX/constituents
     */
    @Override
    public List<Issuer> getImoexComposition() {
        List<Issuer> issuers = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(FILENAME))) {
            while (scanner.hasNextLine()) {
                Issuer issuer = getIssuerFromLine(scanner.nextLine());
                issuers.add(issuer);
                // System.out.println(issuer);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File " + FILENAME + " not found", e);
        }

        return issuers;
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

    private Issuer getIssuerFromLine(String line) {
        List<String> valuesInLine = getValuesFromLine(line);

        if (valuesInLine.size() < 3) {
            throw new RuntimeException("Некорректный файл с составом индекса МосБиржи");
        }

        Issuer issuer = new Issuer();
        for (int i = 0; i < 4; i++) {
            String value = valuesInLine.get(i);
            switch (i) {
                case 0:
                    issuer.setName(value);
                    break;
                case 1:
                    issuer.setTicker(value);
                    break;
                case 2:
                    issuer.setWeight(Double.valueOf(value));
                    break;
                case 3:
                    issuer.setPrice(new BigDecimal(value));
                    break;
                default:
            }
        }

        return issuer;
    }
}
