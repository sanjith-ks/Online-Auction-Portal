package com.auction.portal.country.dto;

import com.auction.portal.common.dto.CommonDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CountryDto extends CommonDto {
    
    private String country;
    
    private String currency;

    private String currencyCode;
}
