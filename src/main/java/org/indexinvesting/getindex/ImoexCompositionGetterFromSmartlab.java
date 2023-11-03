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
    private static final String SMARTLAB_PAGE_URL_WITH_IMOEX_COMPOSITION = "https://smart-lab.ru/q/index_stocks/IMOEX/";
    private static final String SECURITIES_PAGE_URL = "https://iss.moex.com/iss/engines/stock/markets/shares/boards/TQBR/securities.html";
    private Logger logger = Logger.getLogger(getClass().getName());

    @Override
    public List<Issuer> getImoexComposition() {
        List<Issuer> issuers = getPoorImoexComposition();
        List<Security> securities = getSecurities();

        // TODO добавить обогащение issuers данными из securities (тикер)
        
        return issuers;
    }

    private List<Issuer> getPoorImoexComposition() {
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
            BigDecimal price = new BigDecimal(columns.get(5).text());

            issuers.add(new Issuer(shortName, weight, price));
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

    private List<Security> getSecurities() {
        Document securitiesPage;
        try {
            securitiesPage = getSecuritiesPage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


        Element table =  securitiesPage.select("table").first();
        if (table == null) {
            throw new RuntimeException("Не удалось загрузить таблицу с ценными бумагами с %s" + SECURITIES_PAGE_URL);
        }

        Elements rows = table.select("tr"); // разбиваем таблицу на строки по тегу
        List<Security> securities = new ArrayList<>();

        for (int i = 1; i < rows.size(); i++) {
            Element row = rows.get(i);
            Elements columns = row.select("td");

            String secId = columns.get(0).text();
            String shortName = columns.get(2).text();
            double prevPrice = Double.parseDouble(columns.get(3).text());
            int lotSize = Integer.parseInt(columns.get(4).text());
            String secName = columns.get(9).text();

            Security security = new Security(secId, shortName, prevPrice, lotSize, secName);
            securities.add(security);
            System.out.println(security);
        }

        return securities;
    }

    /**
     * Получение дополнительных данных о ценных бумагах
     */
    private Document getSecuritiesPage() throws IOException {
        try {
            return Jsoup.connect(SECURITIES_PAGE_URL).get();
        } catch (IOException exception) {
            logger.log(SEVERE, "Не удалось загрузить информацию о ценных бумагах. {}", exception.getMessage());
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
