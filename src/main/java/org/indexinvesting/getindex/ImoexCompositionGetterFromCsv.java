package org.indexinvesting.getindex;

import org.indexinvesting.securities.Security;
import org.indexinvesting.securities.SecurityGetter;
import org.indexinvesting.securities.SecurityGetterImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ImoexCompositionGetterFromCsv implements ImoexCompositionGetter {

    private static final String FILENAME = "src/main/resources/indexComposition.csv";
    private static final String COMMA_DELIMITER = ";";

    private final SecurityGetter securityGetter = new SecurityGetterImpl();

    @Override
    public List<Issuer> get() {
        List<Issuer> issuers = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(FILENAME))) {
            while (scanner.hasNextLine()) {
                Issuer issuer = getIssuerFromLine(scanner.nextLine());
                issuers.add(issuer);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException("File " + FILENAME + " not found", e);
        }

        return issuers;
    }

    private Issuer getIssuerFromLine(String line) {
        List<String> valuesInLine = getValuesFromLine(line);

        if (valuesInLine.size() != 2) {
            throw new RuntimeException("Некорректный файл с составом индекса МосБиржи");
        }

        Issuer issuer = new Issuer();
        for (int i = 0; i < 2; i++) {
            String value = valuesInLine.get(i);
            switch (i) {
                case 0:
                    Security security = securityGetter.getSecurityById(value);
                    issuer.setSecurity(security);
                    break;
                case 1:
                    issuer.setWeight(Double.valueOf(value));
                    break;
                default:
            }
        }

        return issuer;
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
