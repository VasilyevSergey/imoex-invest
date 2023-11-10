package org.indexinvesting.getindex;

import org.indexinvesting.securities.Security;
import org.indexinvesting.securities.SecurityGetter;
import org.indexinvesting.securities.SecurityGetterImpl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

public class ImoexCompositionGetterFromSmartlab implements ImoexCompositionGetter {
    private static final String SMARTLAB_PAGE_URL_WITH_IMOEX_COMPOSITION = "https://smart-lab.ru/q/index_stocks/IMOEX/";

    private final Logger logger = Logger.getLogger(getClass().getName());
    private final SecurityGetter securityGetter = new SecurityGetterImpl();

    @Override
    public List<Issuer> get() {
        List<Issuer> issuers = new ArrayList<>();
        Document smartlab;
        try {
            smartlab = getSmartlabPage();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Element table =  smartlab.select("table").first();
        if (table == null) {
            throw new RuntimeException("Не удалось загрузить таблицу с составом индекса МосБиржи с сайта smart-lab.ru");
        }
        Elements rows = table.select("tr"); // разбиваем таблицу на строки по тегу

        for (int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements columns = row.select("td");

            String shortName = columns.get(1).text();
            Double weight = getWeight(row);
            Security security = securityGetter.getSecurityByShortName(shortName);

            issuers.add(new Issuer(security, weight));
        }

        return issuers;
    }

    /**
     * Получение состава индекса МосБиржи
     */
    private Document getSmartlabPage() throws IOException {
        try {
            return Jsoup.connect(SMARTLAB_PAGE_URL_WITH_IMOEX_COMPOSITION).get();
        } catch (IOException exception) {
            logger.log(SEVERE, "Не удалось загрузить smart-lab.ru. {}", exception.getMessage());
            throw exception;
        }
    }

    /**
     * Получение веса эмитента
     *
     * @param row строка таблицы с составом индекса МосБиржи
     * @return вес
     */
    private Double getWeight(Element row) {
        String weightAsString = row.select("td").get(2).text(); // получаем значение веса из строки таблицы
        String weight = weightAsString.replace("%", ""); // удаляем символ % из строки
        return Double.parseDouble(weight) / 100;
    }
}
