package com.gabia.alarmdistribution.validator;

import com.gabia.alarmdistribution.dto.request.AlarmRequest;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Arrays;
import java.util.List;

@Component
public class AlarmRequestValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return AlarmRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        List<String> supported = Arrays.asList("slack", "email", "sms");
        AlarmRequest alarmRequest = (AlarmRequest) target;

        if (alarmRequest.getRaws() == null)
            return;

        alarmRequest.getRaws().forEach((key, value) -> {
            if (!supported.contains(key)){
                errors.rejectValue("raws", "NotSupported", new Object[]{key}, "해당 앱은 발송을 지원하지 않습니다");
            }
        });
    }
}
