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
        int supportedAppSize = supportedApp.size();

        Map<String, List<String>> receivers = alarmRequest.getReceivers();

        if (receivers == null)
            return;

        if (!validateReceiverSize(errors, supportedAppSize, receivers))
            return;

        if (!validateSupportedApp(errors, supportedApp, receivers))
            return;

        validateReceiverType(errors, receivers);
    }

    private boolean validateReceiverSize(Errors errors, int supportedAppSize, Map<String, List<String>> receivers) {
        if (supportedAppSize < receivers.size()) {
            errors.rejectValue("receivers", "SizeLimit", new Object[]{supportedAppSize}, null);
            return false;
        }

        return true;
    }

    private boolean validateSupportedApp(Errors errors, Set<String> supported, Map<String, List<String>> receivers) {
        List<String> notSupportedApp = receivers.keySet().stream().filter(appName -> !supported.contains(appName)).collect(Collectors.toList());

        if (notSupportedApp.isEmpty())
            return true;

        notSupportedApp.forEach(appName -> {
            errors.rejectValue("receivers", "NotSupported", new Object[]{appName}, "해당 앱은 발송을 지원하지 않습니다");
        });

        return false;
    }

    private void validateReceiverType(Errors errors, Map<String, List<String>> receivers) {
        receivers.forEach((appName, addresses) -> {
            String regex = appProperties.getApplications().get(appName).getRegex();
            Pattern pattern = Pattern.compile(regex);

            if (addresses.stream().anyMatch(address -> !pattern.matcher(address).matches()))
                errors.rejectValue("receivers", String.format("Type.%s", appName), new Object[]{appName}, null);
        });
    }
}
