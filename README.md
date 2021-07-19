# alarm distribution service
> 사용자의 알림 발송 요청을 파싱하여 알맞은 토픽으로 알림 메세지를 적재하는 카프카 프로듀서


## API

### 1. 알림 발송 API
* endpoint: `/`
* 기능: `email`, `sms`, `slack` 토픽에 메세지를 적재합니다.
    * 이후 email, sms, slack 토픽을 구독하는 컨슈머가 메세지를 가져가서 알림을 발송합니다.
    * 메세지의 적재 성공 여부와 서드 파티 앱의 알림 발송 결과는 `log service`에서 조회할 수 있습니다.

**request header**  
* `user-id`: 사용자의 ID
* `trace-id`: 마이크로 서비스간 추적을 위해 `api gateway`에서 요청에 부여되는 ID

**request body** 
```json
{
    "groupId":1,
    "title": "메세지 제목",
    "content": "메세지 내용",
    "receivers":{
        "email": ["nameks17@gmail.com", "nys@gabia.com"],
        "sms" : ["01047552361", "01098237645"],
        "slack" : ["C023WJKCPUM"]
    }
}
```

**응답**
```json
{
    "message": "알림 전송 요청 완료",
    "result": null
}
```

**email topic으로 적재되는 알림 메세지**
* 해당 메세지를 email 컨슈머가 가져가서 이메일을 발송한다
* sms, slack도 아래와 같은 형태를 가지고 있습니다
```json
{
    "groupId":1,
    "userId": 1,
    "traceId": "989e7dc5-6150-4764-8a34-cd929631018e",
    "title": "메세지 제목",
    "content": "메세지 내용",
    "addresses": ["nameks17@gmail.com", "nys@gabia.com"]
}
```
<br><br>

## 새로운 서비스 추가하기
> alarm distribution service는 새로운 서드파티 애플리케이션을 코드 수정 없이 추가할 수 있습니다.

### 서비스 추가 전
**application.yml**
* 현재 email, sms, slack 서드 파티 애플리케이션을 지원함
* `alarm.applications.{서드 파티 앱 이름}.regex`: 서드 파티의 발신자 주소의 형식을 체크하는 정규 표현식
```yml
alarm:
  applications:
    email:
      regex: ^(.+)@(.+)$
    sms:
      regex: ^01(?:0|1|[6-9])[.-]?(\d{3}|\d{4})[.-]?(\d{4})$
    slack:
      regex: ^[CUT]\w*$
```
**errors.properties**
* 오류 메세지를 아래와 같이 에러 코드로 관리함
```properties
NotBlank=비어 있을 수 없습니다

NotSupported={0}은 지원하지 않습니다

NotNull={0} is not null
#NotNull.alarmRequest.groupId={0} is not null ggggg
#NotNull.alarmRequest.userId={0} is not null

NotEmpty=비어 있을 수 없습니다
Type.slack={0}:유효한 아이디 값이 아닙니다
Type.email={0}:메일 형식만 지원합니다
Type.sms={0}:전화번호 형식만 지원합니다

SizeLimit.receivers=receivers의 사이즈는 {0}이하입니다
```

### 서비스 추가 후
**application.yml**
* 새로운 서드 파티 애플리케이션 카카오가 발신자 주소의 형식을 전화번호로 사용한다고 가정하고 추가해보겠습니다.
* `alarm.applications.kakao`: 카카오 서비스 추가
* `alarm.applications.kakao.regex=^01(?:0|1|[6-9])[.-]?(\d{3}|\d{4})[.-]?(\d{4})$`:전화번호 형식을 체크하는 정규표현식
```yml
alarm:
  applications:
    email:
      regex: ^(.+)@(.+)$
    sms:
      regex: ^01(?:0|1|[6-9])[.-]?(\d{3}|\d{4})[.-]?(\d{4})$
    slack:
      regex: ^[CUT]\w*$
    kakao:
      regex: ^01(?:0|1|[6-9])[.-]?(\d{3}|\d{4})[.-]?(\d{4})$
```

**errors.properties**
```properties
NotBlank=비어 있을 수 없습니다

NotSupported={0}은 지원하지 않습니다

NotNull={0} is not null
#NotNull.alarmRequest.groupId={0} is not null ggggg
#NotNull.alarmRequest.userId={0} is not null

NotEmpty=비어 있을 수 없습니다
Type.slack={0}:유효한 아이디 값이 아닙니다
Type.email={0}:메일 형식만 지원합니다
Type.sms={0}:전화번호 형식만 지원합니다
# 추가
Type.kakao={0}:전화번호 형식만 지원합니다

SizeLimit.receivers=receivers의 사이즈는 {0}이하입니다
```
<br><br>

## 요청 검증

### AlarmRequestValidator
* `com.gabia.alarmdistribution.dto.request.AlarmRequest`에 대한 추가적인 검증 기능을 제공합니다.

**AlarmRequest.java**
```java
public class AlarmRequest {

    @NotNull
    private Long groupId;

    @NotBlank
    private String title;

    @NotBlank
    private String content;

    @NotEmpty
    private Map<String, List<String>> receivers;

    @Builder
    public AlarmRequest(Long groupId, String title, String content, Map<String, List<String>> receivers) {
        this.groupId = groupId;
        this.title = title;
        this.content = content;
        this.receivers = receivers;
    }
}
```

**AlarmRequestValidator.java**
```java
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
          
          // receivers의 사이즈를 체크 현재는 3가지의 서드 파티 앱을 지원하기 때문에 3을 초과하면 에러
          if (!validateReceiverSize(errors, supportedAppSize, receivers))
              return;
          
          // 지원하는 서드 파티 앱(email, sms, slack)인지 체크 아니면 에러
          if (!validateSupportedApp(errors, supportedApp, receivers))
              return;

          // 서드 파티 앱마다 발신자 주소 형식이 맞는지 체크 맞지 않으면 에러
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
```
