package com.auction.portal;

import com.auction.portal.config.properties.ImageUploadProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableScheduling
@EnableConfigurationProperties({
		ImageUploadProperties.class
})
public class OnlineAuctionPortalApplication {


	public static void main(String[] args) {
		SpringApplication.run(OnlineAuctionPortalApplication.class, args);
	}

}
