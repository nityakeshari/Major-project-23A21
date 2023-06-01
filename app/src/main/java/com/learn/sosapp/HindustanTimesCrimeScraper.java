package com.learn.sosapp;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class HindustanTimesCrimeScraper {
    public static void main(String[] args) {
        try {
            String url = "https://www.hindustantimes.com/topic/crime";

            Document doc = Jsoup.connect(url).get();

            // Extract crime news
            Elements articles = doc.select(".row.ptb15");
            for (Element article : articles) {
                String title = article.select(".media-heading").text();
                String description = article.select(".media-ellipsis").text();
                String date = article.select(".time-dt").text();
                String articleUrl = article.select("a").attr("href");

                // Extract latitude and longitude from the article URL
                String[] urlParts = articleUrl.split("/");
                String lastPart = urlParts[urlParts.length - 1];
                String[] latLongParts = lastPart.split("-");
                String latitude = latLongParts[0];
                String longitude = latLongParts[1];

                System.out.println("Title: " + title);
                System.out.println("Description: " + description);
                System.out.println("Date: " + date);
                System.out.println("Latitude: " + latitude);
                System.out.println("Longitude: " + longitude);
                System.out.println("URL: " + articleUrl);
                System.out.println();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
