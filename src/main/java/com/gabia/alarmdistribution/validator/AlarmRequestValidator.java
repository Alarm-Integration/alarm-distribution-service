package com.gabia.alarmdistribution.validator;

import com.gabia.alarmdistribution.config.AppProperties;
import com.gabia.alarmdistribution.dto.request.AlarmRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class AlarmRequestValidator implements Validator {

    private final AppProperties appProperties;

    @Override
    public boolean supports(Class<?> clazz) {
        return AlarmRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AlarmRequest alarmRequest = (AlarmRequest) target;
        Set<String> supportedApp = appProperties.getApplications().keySet();
        Map<String, List<String>> raws = alarmRequest.getRaws();

        if (raws == null)
            return;

        if(!validateSupportedApp(errors, supportedApp, raws))
            return;

        validateReceiverType(errors, raws);
    }

    private boolean validateSupportedApp(Errors errors, Set<String> supported, Map<String, List<String>> raws) {
        List<String> notSupportedApp = raws.keySet().stream().filter(appName -> !supported.contains(appName)).collect(Collectors.toList());

        if (notSupportedApp.isEmpty())
            return true;

        notSupportedApp.forEach(appName -> {
            errors.rejectValue("raws", "NotSupported", new Object[]{appName}, "해당 앱은 발송을 지원하지 않습니다");
        });

        return false;
    }

    private void validateReceiverType(Errors errors, Map<String, List<String>> raws) {
        raws.forEach((appName, receivers) -> {
            String regex = appProperties.getApplications().get(appName).getRegex();
            Pattern pattern = Pattern.compile(regex);

            if(receivers.stream().anyMatch(receiver -> !pattern.matcher(receiver).matches()))
                errors.rejectValue("raws", String.format("Type.%s", appName), new Object[]{appName}, null);
        });
    }
}
