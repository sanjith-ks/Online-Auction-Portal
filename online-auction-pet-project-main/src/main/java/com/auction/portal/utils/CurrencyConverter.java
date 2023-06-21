package com.auction.portal.utils;


import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.log4j.Log4j2;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * @author Dhinesh
 * currency conversion
 */
@Service
@Log4j2
public class CurrencyConverter {
    @Autowired
    private RestTemplate restTemplate;

    @Value("${apikey}")
    private String apiKey;

    @Value("${currency.base}")
    private String BASE;

    private static final DecimalFormat roundOff = new DecimalFormat("0.00");

    /**
     * @param currency
     * @param quantity
     * @return currencyToUser value
     */
    @Cacheable(key = "#currency", value = "conversionRate")
    @Retry(name = "CurrencyToUser", fallbackMethod = "converterFallback")
    public double convertIntoUser(String currency, double quantity) {
        String fromTo = BASE + "_" + currency;
        String s = restTemplate.getForObject("https://free.currconv.com/api/v7/convert?q=" + fromTo + "," + fromTo + "&compact=ultra&apiKey=" + apiKey,
                String.class);
        JSONObject jsonObject = new JSONObject(s);
        double conversionRate = jsonObject.getDouble(fromTo);
        roundOff.setRoundingMode(RoundingMode.UP);
        double converted = Double.parseDouble(roundOff.format(conversionRate * quantity));
        return converted;
    }

    /**
     * @param currency
     * @param quantity
     * @return currencyToBase value
     */
    @Cacheable(key = "#currency", value = "conversionRate")
    @Retry(name = "CurrencyToBase", fallbackMethod = "converterFallback")
    public double convertIntoBase(String currency, double quantity) {
        String fromTo = currency + "_" + BASE;
        String s = restTemplate.getForObject("https://free.currconv.com/api/v7/convert?q=" + fromTo + "," + fromTo + "&compact=ultra&apiKey=" + apiKey,
                String.class);
        JSONObject jsonObject = new JSONObject(s);
        double conversionRate = jsonObject.getDouble(fromTo);
        roundOff.setRoundingMode(RoundingMode.UP);
        double converted = Double.parseDouble(roundOff.format(conversionRate * quantity));
        return converted;
    }

    /**
     * @param currency, @param quantity
     *                  returns fallback value
     */
    @Cacheable(key = "#currency", value = "conversionRate")
    public double converterFallback(String currency, double quantity, Exception e) {

        return 1d;
    }


}