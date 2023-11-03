package org.indexinvesting.getindex;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

public class ImoexCompositionGetterFromSmartlab implements ImoexCompositionGetter {
    private static final String SMARTLAB_PAGE_ADDRESS_WITH_IMOEX_COMPOSITION = "https://smart-lab.ru/q/index_stocks/IMOEX/";
    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public List<Issuer> getImoexComposition() {
        List<Issuer> issuers = new ArrayList<>();
        Document doc;
        try {
            doc = getSmartlabPage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Element table =  doc.select("table").first();
        Elements rows = table.select("tr");// разбиваем нашу таблицу на строки по тегу

        for (int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements columns = row.select("td");

            String name = columns.get(1).text();
            String ticker = getPreliminaryValue(row);
            Double weight = getWeight(row);
            BigDecimal price = new BigDecimal(columns.get(5).text());

            issuers.add(new Issuer(name, ticker, weight, price));
            System.out.println(String.format("%s;%s;%s;%s", name, ticker, weight, price));
        }

        return issuers;
    }

    private Document getSmartlabPage() throws IOException {
        try {
            return Jsoup.connect(SMARTLAB_PAGE_ADDRESS_WITH_IMOEX_COMPOSITION).get();
        } catch (IOException exception) {
            logger.log(SEVERE, "Can't get document from smartlab site. {}", exception.getMessage());
            throw exception;
        }
    }

    /**
     * Получение предварительного тикер (не различает обычные акции и префы)
     *
     * @param row строка таблицы с составом индекса МосБиржи
     * @return предварительный тикер (не различает обычные акции и префы)
     */
    private String getPreliminaryValue(Element row) {
        String link = row.select("td").get(1).getElementsByAttribute("href").getFirst().attr("href");
        String[] path = link.split("/");
        return path[path.length - 1];
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
        return Double.parseDouble(weight);
    }
}
