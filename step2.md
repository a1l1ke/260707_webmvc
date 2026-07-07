# Step 2: Controller 구현, 데이터 전달(Model/DTO) 및 정적 리소스 설정

이 가이드는 스프링 MVC에서 컨트롤러를 통해 데이터를 뷰(JSP)로 전달하는 방법, 정적 리소스(CSS 등)를 처리하는 경로 설정, 그리고 Java Record를 DTO로 활용하는 방법을 설명합니다.

---

## 1. 🐣 초심자를 위한 비유로 이해하기

스프링 웹 MVC의 데이터 전달과 리소스 설정을 **"스마트 호텔의 룸서비스와 인테리어"**에 비유해 볼 수 있습니다.

| 개념 | 호텔에서의 비유 | 설명 |
| :--- | :--- | :--- |
| **`Model`** (전달 모델) | **룸서비스 배달 쟁반** | 주방 담당 지배인(`Controller`)이 요리를 완성해서 객실로 보낼 때, 요리를 얹어 보내는 쟁반입니다. 이 쟁반에 얹어둔 음식은 객실(JSP)에 도착해서 손님이 바로 먹을 수 있습니다. |
| **`FoodDTO`** (Record) | **정형화된 메뉴 정보 카드** | 음식 이름과 가격이 깔끔하게 인쇄된 전용 메뉴 정보 카드입니다. 정보가 흐트러지지 않게 단단히 고정(불변)되어 있습니다. |
| **`ResourceHandler`** (정적 리소스 설정) | **호텔 자재용 다이렉트 통로** | 손님이 객실 벽지(CSS)나 비품(이미지)을 원할 때, 컨시어지(`DispatcherServlet`)의 복잡한 절차를 거치지 않고 자재 창고(`/resources/`)에서 다이렉트로 가져갈 수 있도록 뚫어놓은 전용 지름길입니다. |

---

## 2. 💻 주니어를 위한 핵심 원리 설명

### ① 컨트롤러에서 뷰로 데이터 전달 (`Model`과 JSP EL)
스프링 MVC는 컨트롤러에서 가공한 데이터를 뷰에 전달할 때 `Model` 객체를 사용합니다.
```java
@GetMapping
public String index(Model model) {
    model.addAttribute("food", new FoodDTO("미소라멘", 12_000));
    return "index";
}
```
* **동작 원리**: 컨트롤러 메서드의 파라미터로 `Model`을 선언하면, 스프링의 `HandlerAdapter`가 실행 시 `BindingAwareModelMap` 인스턴스를 주입해 줍니다.
* **서블릿 수준 변환**: `model.addAttribute("key", value)`로 등록된 데이터는 뷰 렌더링 직전에 서블릿의 `HttpServletRequest.setAttribute("key", value)`로 복사됩니다.
* **JSP에서의 표현**: JSP 내에서 표현 언어(EL)인 `${food.name}`을 사용하면 내부적으로 `request.getAttribute("food")`를 찾고, 해당 객체의 `getName()` 메서드를 리플렉션으로 호출하여 화면에 값을 출력합니다.

### ② Java 16+ `record`와 DTO의 바인딩 규약
데이터 전송용 객체(DTO)로 Java의 신기능인 `record`를 사용했습니다.
```java
public record FoodDTO(String name, int price) {
    public String getName() { return name; }
    public int getPrice() { return price; }
}
```
* **Record의 강점**: 클래스 선언만으로 컴파일러가 모든 필드를 `private final`로 선언하고, 생성자, `equals()`, `hashCode()`, `toString()`, 그리고 필드명과 동일한 게터 메서드(`name()`, `price()`)를 자동으로 만들어 줍니다.
* **JSP 표준 규약 호환성**: JSP의 EL 표현식(`${food.name}`)은 자바의 전통적인 자바빈즈 규약(Getter 명명 규칙: `get+필드명`)을 기준으로 리플렉션을 수행합니다. 따라서 record가 자동 생성하는 `name()` 메서드는 인식하지 못하므로, JSP 호환을 위해 명시적으로 `getName()`, `getPrice()` 메서드를 정의해 주었습니다.

### ③ 정적 리소스 설정 (`WebMvcConfigurer.addResourceHandlers`)
`DispatcherServlet`의 매핑 경로를 `/`로 지정하면 HTML, JSP뿐만 아니라 이미지, CSS, JS 파일에 대한 모든 요청도 스프링이 가로채게 됩니다. 이로 인해 정적 리소스를 불러올 때 404 에러가 나거나 매핑되는 컨트롤러가 없다는 오류가 발생합니다.
이를 해결하기 위해 `WebConfig`에서 `addResourceHandlers` 설정을 추가했습니다.
```java
@Override
public void addResourceHandlers(ResourceHandlerRegistry registry) {
    registry
            .addResourceHandler("/resources/**") // 이 경로로 요청이 들어오면
            .addResourceLocations("/resources/"); // 웹 루트의 이 물리 폴더에서 바로 제공함
}
```
* **동작 원리**: `/resources/**` 패턴의 URL 요청이 들어오면 `DispatcherServlet`은 요청을 컨트롤러로 넘기지 않고, `ResourceHttpRequestHandler`를 가동해 `/src/main/webapp/resources/` 폴더에 위치한 실제 정적 자원(CSS/JS)을 즉시 응답으로 전송합니다.
* **JSP에서의 경로 매핑**: JSP 파일에서 정적 리소스를 호출할 때는 컨텍스트 경로(Context Path)를 포함해야 유연하게 경로가 매핑됩니다.
  ```html
  <link rel="stylesheet" href="${pageContext.request.contextPath}/resources/style.css">
  ```

---

## 3. 🎯 면접 대비 예상 질문 & 모범 답변

### Q1. 스프링 MVC 컨트롤러에서 메서드 파라미터로 `Model`을 주입받아 사용하는데, 이 데이터가 어떻게 JSP 뷰까지 전달되는지 원리를 설명해 주세요.
> **답변:** 
> 스프링 MVC 프레임워크가 핸들러 메서드를 호출할 때 파라미터 타입을 분석하여 `Model` 객체(실제 구현체는 `BindingAwareModelMap`)를 주입해 줍니다. 개발자가 `model.addAttribute()`를 통해 객체를 담으면, 뷰를 렌더링하기 전 단계인 `DispatcherServlet`의 `render()` 과정에서 뷰 객체(예: `InternalResourceView`)에게 제어권이 넘어갑니다. 이때 스프링은 `Model` 내부의 데이터를 꺼내 `HttpServletRequest.setAttribute()`에 전부 복사해 넣습니다. 이후 서블릿 컨테이너의 포워드 처리가 수행되므로 JSP에서는 `${키값}` 형태로 요청 스코프에 담긴 데이터에 쉽게 접근할 수 있게 됩니다.

### Q2. Java 16+의 `record`를 JSP의 EL 표현식과 함께 사용하려 할 때 발생할 수 있는 문제점과 해결책은 무엇인가요?
> **답변:** 
> Java의 `record`는 필드 조회를 위한 게터 메서드를 만들 때 필드명과 똑같은 이름(예: `name()`, `price()`)으로 생성합니다. 하지만 JSP의 표현 언어(EL) 표준 규약은 자바빈즈 명세에 의존하기 때문에, 필드 조회 시 반드시 `getName()`이나 `getPrice()` 같이 접두어 `get`이 붙은 메서드를 찾아 리플렉션을 수행합니다. 이로 인해 일반 `record`만 사용하면 EL에서 속성을 인식하지 못하는 에러가 발생합니다. 해결책은 `record` 내부에 자바빈즈 표준 게터 메서드인 `getName()`, `getPrice()` 등을 명시적으로 정의해 주어 JSP 호환성을 갖추는 것입니다.

### Q3. `DispatcherServlet` 매핑 경로가 `/`일 때, CSS나 JS 같은 정적 자원(Static Resources) 요청 시 발생하는 문제와 해결책은 무엇인가요?
> **답변:** 
> `DispatcherServlet`의 매핑 경로가 `/`가 되면 웹 애플리케이션으로 들어오는 모든 정적/동적 요청을 가로채게 됩니다. 이로 인해 브라우저가 CSS나 JS 파일을 요청해도 스프링은 이를 일반적인 컨트롤러 매핑 경로로 오인하여 매핑되는 컨트롤러가 없으므로 404 에러를 유발합니다. 이를 해결하기 위해 `WebMvcConfigurer`를 구현하여 `addResourceHandlers()`를 오버라이딩합니다. 특정 URL 경로(예: `/resources/**`)로 들어오는 요청에 대해서는 스프링 컨트롤러를 타지 않고 실제 서버의 특정 물리적 폴더(예: `/resources/`)에서 자원을 바로 다운로드할 수 있도록 리소스 핸들러 설정을 추가해 해결할 수 있습니다.
