## Chapter09. 스프링 통합하기

## 1. Spring Integration?

Spring Integration은 스프링 기반 어플리케이션에서 경량 메세지를 사용 가능하도록 지원하고 외부 시스템을 선언적으로 어택터로 쉽게 통합할 수 있는 기능을 제공한다. 이런 어댑터들은 높은 수준의 추상화
레벨을 제공해 어댑터를 통해 비즈니스에 집중할 수 있도록 한다.

<br>

## 2. Spring Integration dependency in maven

```maven
<dependency>
	<groupId>org.springframework.boot</groupId>
  <artifactId>spring-boot-starter-integration</artifactId>
</dependency>
<dependency>
	<groupId>org.springframework.integration</groupId>
	<artifactId>spring-integration-mqtt</artifactId>
</dependency>

```

<br>

## 3. Spring Integration Component

- **`채널(Channel)`** : 한 요소로부터 다른 요소로 메세지를 전달한다.
- **`필터(Filter)`** : 조건에 맞는 메세지가 플로우를 통과하게 해준다.
- **`변환기(Transformer)`** : 메세지 값을 변경하거나 메시지 페이로드의 타입을 다른 타입으로 변환한다.
- **`라우터(Router)`** : 여러 채널 중 하나로 메세지를 전달하며 대개 메세지 헤더를 기반으로 한다.
- **`분배기(Splitter)`** : 들어오는 메세지를 두 개 이상의 메세지를 분할하며, 분할된 각 메세지는 다른 채널로 전송.
- **`집적기(Aggregator)`** : 분배기와 상반된 것으로 별개의 채널로부터 전달되는 다수의 메세지를 하나의 메세지로 결합한다.
- **`서비스 액티베이터(Service Activator)`** : 메세지를 처리하도록 자바 메서드에 메세지를 넘겨준 후, 메서드의 반환값을 출력 채널로 전송한다.
- **`채널 어댑터(Channel Adapter)`** : 외부 시스템에 채널을 연결한다. 외부 시스템으로부터 입력을 받거나 쓸 수 있다.
- **`게이트웨이(Gateway)`** : 인터페이스를 통해 통합 플로우로 데이터를 전달한다.

<br>
<br>

### 3-1. 메세지 채널(Message channel)

- 메세지 체널은 통합 파이프라인을 통해서 메세지가 이동하는 수단이다.

  (채널은 스프링 통합의 다른 부분을 연결하는 통로이다.)

<br>

- Channel 종류
  1. **`PublishSubscriberChannel`** : 전송되는 메세지는 하나 이상의 컨슈머를 소비하는 컴포넌트(소비하는 컴포넌트 혹은 어플리케이션)로 전달된다.
  2. **`QueueChannel`** : FIFO 방식으로 컨슈머가 가져갈 때 큐에 저장된다. 컨슈머가 어렷일 때, 하나의 컨슈머만 해당 메세지를 수신한다. 
  3. **`PriorityChannel`** : QueueChannel과 유사하지만, FIFO 방식 대신 메시지의 priority 헤더를 기반으로 컨슈머가 메세지를 가져간다.
  4. **`RendezvousChannel`** :QueueChannel과 유사하지만, 컨슈머가 메세지를 수신할 때까지 메세지 전송자가 채널을 차단한다는 것이 다르다. 
  5. **`DirectChannel`** : PublisherSubscriberChannel과 유사하지만, 전송자와 동일한 스레드로 실행되는 컨슈머를 호출하며 단일 컨슈머에게 
     메세지를 전송한다. 이 채널은 트랜잭션을 지원한다.
  6. **`ExecutorChannel`** : DirectChannel과 유사하지만, TaskExecutor를 통해서 메세지가 전송된다.
  7. **`FluxMessageChannel`** : 프로젝트 리액터의 플럭스 기반으로 하는 리액티브 스트림즈 퍼블리셔 채널이다.
  
`java config 방법`
```java
@Bean
public MessageChannel orderChannel() {
    return new PublishSubscriberChannel();
}
```
```java
@ServiceActivator(inputChannel="orderChannel")
```
- 우선 Bean을 통해 Channel을 선언하고, 해당 채널을 해당 컴포넌트로 사용한다면 컴포넌트 어노테이션 상단 inputChannel에 해당 채널의 명칭을 명명한다.
- 자바 구성과 자바 DSL 구성 모두에서 입력 채널은 기본적으로 DefaultChannel()로 자동 생성된다.

<br><br>

### 3-2. 필터(Filter)
- 통합 파이프라인의 중간에 위치할 수 있으며, 플로우의 전 단계로부터 다음 단계로의 메세지 전달을 허용 또는 불허한다.



`java config 방법`
```java
@Filter(inputChannel="numberChannel", outpubChannel="evenChannel")
public boolean evenNumberFilter(Integer number) {
    return number % 2 == 0;
}
```
- 정수 값을 갖는 메세지가 numberChannel이라는 채널로 입력되고, 짝수인 경우만 evenNumberFilterChannel이라는 이름의 채널로 전달된다.

<br><br>

### 3-3. 변환기(Transformer)
- 변환기는 메세지 값의 변경이나 타입을 변환하는 일을 수행한다.

`java config`
```java
@Bean
@Transformer(inputChannel="numberChannel", outputChannel="romanNumberChannel")
public GenericTransformer<Integer, String> romanNumTransformer() {
    return RomanNumbers::toRoman;
}
```
- 정수 값을 포함하는 메세지가 numberChannel이라는 이름의 채널로 입력되고, 이 숫자를 로마 숫자를 포함하는 문자열로 바꾼다.

<br><br>

### 3-4. 라우터(Router)
- 라우터는 전달 조건을 기반으로 통합 플로우 내부를 분기(서로 다른 채널로 메세지를 전달)한다.

`java config`
```java
  @Bean
  @Router(inputChannel = "numberChannel")
  public AbstractMessageRouter evenOddRouter() {
    return new AbstractMessageRouter() {
      @Override
      protected Collection<MessageChannel> determineTargetChannels(Message<?> message) {
        Integer number = message.getPayload();
        if (number % 2 == 0) {
          return Collections.singleton(evenChannel());
        }
        return Collections.singleton(oddChannel());
      }
    }
  }

@Bean
public MessageChannel evenChannel() {
    return new DirectChannel();
}

@Bean
public MessageChannel oddChannel() {
        return new DirectChannel();
}
```
- 메세지 페이로드가 짝수일 때, evenChannel로 이동하고 메세지 페이로드가 홀수일 때, oddChannel로 이동한다.

<br><br>

### 3-5. 분배기(Splitter)
- 통합 플로우에서 하나의 메세지를 여러 개 분할하여 독립적으로 처리하는 것이 유용하다.
  - 메세지 페이롣가 같은 컬렉션 항목들을 포함하며, 각 메세지 페이로드 별로 처리하고자 할 때
  - 연관된 정보를 함께 전달하는 하나의 메세지 페이로드는 두 개 이상의 서로 다른 타입 메세지로 분할될 수 있다.

`java config`

```java
public class OrderSplitter {
  public Collection<Object> splitOrderIntoParts(PurchaseOrder po) {
    List<Object> parts = new ArrayList<>();
    parts.add(po.getBillingInfo());
    parts.add(po.getLineItems());
    return parts;
  }
}
```
```java
@Bean
@Splitter(inputChannel="poChannel", outputChannel="splitOrderChannel")
public OrderSplitter orderSplitter() {
    return new OrderSplitter();
}
```
- 주문 메세지가 poChannel이라는 이름의 채널로 도착해 OrderSplitter에 의해 분할된다. 그 다음에 컬렉션으로 반환되는 각 항목은 spliterOrderChannel이라는 이름의 채널에 별도의 메세지를 전달한다.

<br>

```java
@Bean
@Router
public MessageRouter splitOrderRouter() {
    PayloadTypeRouter router = new PayloadTypeRouter();
    router.setChannelMapping(BillingInfo.class.getName(), "billingInfoChannel");
    router.setChannelMapping(List.class.getName(), "lineItermsChannel");
    return router;
}
```
- PayloadTypeRouter는 각 페이로드 타입을 기반으로 서로 다른 채널에 메세지를 전달한다.
- BillingInfo 타입의 페이로드는 billingInfoChannel로 전달되어 처리되며, java.util.List 컬렉션에 저장된 주문 항목들은 List 타입으로 lineItemsChannel에 전달된다.

<br>

```java
@Splitter(inputChannel="lineItermsChannel", outputChannel="lineItemchannel")
public List<LineItem> lineItermSplitter(List<LineItem> lineItems) {
    return lineItems;
}
```
- 만약 LineIterm을 별도로 처리하고 싶은 경우, @Splitter 어노테잉션을 지정한 메서드를 작성하고 이 메서드에서는 처리된 LineItem이 저장된 컬렉션을 반환하면 된다.

<br><br>

### 3-6. 서비스 액티베이터(ServiceActivator)
- 서비스 액티베이터는 입력 채널로부터 메세지를 수신하고 이 메세지를 MessageHandler 인터페이스를 구현한 클래스에 전달한다.
- 서비스 액티베이터는 메세지를 받는 즉시 MessageHandler를 통해 서비스를 호출한다.

`java config`
```java
@Bean
@ServiceActivator
public MessageHandler sysoutHandler() {
    return message -> {
        System.out.println("Message payload: " + message.getPayload());
    }
}
```
- someChannel 채널로 받을 메세지를 람다를 사용해 페이로드의 표준 출력 스트림으로 보낸다.

```java
@Bean
@ServiceActivator(inputchannel="orderChannel", outputChannel="completeChannel")
public GenericHandler<order> orderHandler(OrderRepository orderRepo) {
    return (payload, headers) -> {
        return orderRepo.save(payload);
    }
}
```
- 주문 메세지가 도착하면 리퍼지터리를 통해 저장하고, Order 객체가 반환됨ㄴ completeChannel이라는 이름의 출력 채널로 전달된다.

<br><br>

### 3-7. 게이트웨이(Gateway)
- 어플리케이션이 통합 플로우로 데이터를 제출하고, 선택적으로 플로우의 처리 결과인 응답을 받을 수 있는 수단이다.

`java config`
```java
@Component
@Messagegateway(defaultReqeustChannel="inChannel", defaultReplyChannel="outChannel")
public interface UppercaseGateway {
    String uppercase(String in);
}
```
- 인터페이스를 구현할 필요가 없다. 지정된 채널을 통해 데이터를 전송하고 수신하는 구현체를 Spring Integration이 런타임 시에 자동으로 제공한다.
- uppercase()가 호출되면 지정된 문자열이 통합 플로우의 inChannel로 전달된다. 그리고 플로우가 어떻게 정의되고 무슨 일을 하는 지와 상관없이, 데이터가 outChannel로 도착하면 uppercase() 메서드로부터 반환된다.

<br><br>

### 3-8. 채널 어댑터(channel adapter)
- 채널 어댑터는 통합 플로우의 입구와 출구를 나타낸다.
- 인바운드 채널 어댑터를 통해 통합 플로우로 데이터가 들어온다.
- 아웃바운드 채널 어댑터를 통해 통합 플로우에서 나간다.

<br>

`java config`
```java
@Bean
@InboundChannelAdapter(
        poller=@Poller(fixedRate="1000"), channel="numberChannel")
public MessageSource<INteger> numberSource(AtomicINteger source) {
    return () -> {
        return new GenericMessage<>(source.getAndIncrement());
    };
)
```
- @InboundChannelAdapter 에노테이션이 지정되었으므로 인바운드 채널 어댑터 빈으로 선언된다.
- 이 빈은 주입된 AtomicInteger로 부터 numberChannel이라는 이름의 채널로 매초마다 한번씩 숫자를 전달한다.

<br><br>

### 3-9. 엔드포인트 모듈
- Spring Integration은 외부 시스템과의 통합을 위해 채널 어댑터가 포함된 24개 이상의 엔드 포인트 모듈(인바운드와 아웃바운드 모두)을 spring integration이 제공한다.