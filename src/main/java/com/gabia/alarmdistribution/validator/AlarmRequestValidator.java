package com.gabia.alarmdistribution.validator;

import com.gabia.alarmdistribution.dto.request.AlarmRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.NotEmpty;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Component
public class AlarmRequestValidator implements Validator {
    private static final String emailRegex = "^(.+)@(.+)$";
    private static final String smsRegex = "^01(?:0|1|[6-9])[.-]?(\\d{3}|\\d{4})[.-]?(\\d{4})$";
    private static final String slackRegex = "^[CUT]\\w*$";


    @Override
    public boolean supports(Class<?> clazz) {
        return AlarmRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {

        List<String> supported = Arrays.asList("slack", "email", "sms");
        AlarmRequest alarmRequest = (AlarmRequest) target;

        @NotEmpty
        Map<String, List<String>> raws = alarmRequest.getRaws();


        if (raws == null)
            return;

        raws.forEach((key, value) -> {
            if (!supported.contains(key)){
                errors.rejectValue("raws", "NotSupported", new Object[]{key}, "해당 앱은 발송을 지원하지 않습니다");
            }
        });

        if (raws.containsKey("slack")){
            Pattern pattern = Pattern.compile(slackRegex);
            if (raws.get("slack").stream().anyMatch(slack -> !pattern.matcher(slack).matches())) {
                errors.rejectValue("raws", "Type.slack", null, null);
            }
        }

        if (raws.containsKey("email")) {
            Pattern pattern = Pattern.compile(emailRegex);
            if (raws.get("email").stream().anyMatch(email -> !pattern.matcher(email).matches())) {
                errors.rejectValue("raws", "Type", new Object[]{"email", "email"}, null);
            }
        }

        if (raws.containsKey("sms")) {
            Pattern pattern = Pattern.compile(smsRegex);
            if (raws.get("sms").stream().anyMatch(sms -> !pattern.matcher(sms).matches())) {
                errors.rejectValue("raws", "Type", new Object[]{"sms", "전화번호"}, null);
            }
        }
    }
}
