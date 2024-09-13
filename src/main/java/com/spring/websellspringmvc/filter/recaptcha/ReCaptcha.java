package com.spring.websellspringmvc.filter.recaptcha;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ReCaptcha {
    private boolean success;
    private double score;
    private String hostname;
    private String action;
    private String challengeTs;
    private String[] errorCodes;
}
