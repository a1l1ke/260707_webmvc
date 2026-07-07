# Step 1: Spring Web MVC 초기 설정 (Java-based Config)

이 가이드는 `web.xml` 설정 없이 자바 코드를 기반으로 Spring Web MVC를 구동하는 첫 걸음을 설명합니다.

---

## 1. 🐣 초심자를 위한 비유로 이해하기

스프링 웹 MVC의 초기 설정을 **"새로 오픈하는 스마트 호텔"**에 비유해 볼 수 있습니다.

| 스프링 컴포넌트 | 호텔에서의 비유 | 설명 |
| :--- | :--- | :--- |
| **Servlet Container (톰캣)** | **호텔 건물 및 설비** | 웹 애플리케이션이 실제로 돌아가는 기반 시설입니다. |
| **`WebApplicationInitializer`** | **호텔 개업 매뉴얼 & 체크리스트** | 호텔 문을 열 때(서버 시작) 가장 먼저 실행되어 호텔의 필수 부서와 매니저를 배치하는 매뉴얼입니다. |
| **`DispatcherServlet`** | **총괄 컨시어지 (프론트 데스크)** | 호텔 입구에서 모든 고객(HTTP 요청)을 맞이하고, 적절한 부서나 객실(Controller)로 안내하는 단 한 명의 안내 데스크 직원입니다. |
| **`WebConfig`** | **호텔 내부 부서 배치도 및 인테리어 규칙** | 호텔 내에 어떤 부서(컨트롤러)가 있고, 객실 뷰(View Resolver)를 어떻게 찾을지 적어둔 청사진입니다. |
| **`AnnotationConfigWebApplicationContext`** | **호텔 관리 센터 (스프링 컨테이너)** | 호텔에 필요한 가구, 장비, 직원(Bean)들을 등록하고 관리하는 중심 센터입니다. |

---

## 2. 💻 주니어를 위한 핵심 원리 설명

### ① Servlet 3.0+ SPI와 `WebApplicationInitializer`
과거에는 서블릿 스펙에서 `web.xml`이라는 XML 파일을 통해 서블릿을 등록했습니다. 하지만 **Servlet 3.0**부터는 자바 코드 기반 설정을 지원하기 위해 **SPI(Service Provider Interface)** 메커니즘을 제공합니다.
* 서블릿 컨테이너(톰캣 등)는 구동 시 `META-INF/services/jakarta.servlet.ServletContainerInitializer` 파일을 읽어 구동 클래스를 찾습니다.
* 스프링은 `SpringServletContainerInitializer`를 등록해 두었으며, 이 클래스는 다시 클래스패스에서 `WebApplicationInitializer` 인터페이스를 구현한 클래스들을 찾아 `onStartup()` 메소드를 호출합니다.
* 따라서 `WebAppInitializer` 클래스가 톰캣이 켜질 때 자동으로 인식되어 실행되는 것입니다.

### ② Front Controller 패턴과 `DispatcherServlet`
Spring MVC는 **Front Controller 패턴**을 따릅니다.
* 모든 HTTP 요청을 하나의 서블릿(`DispatcherServlet`)이 받아서 공통 처리를 수행한 후, 적절한 세부 컨트롤러에게 요청을 위임(Dispatch)합니다.
* `WebAppInitializer`에서 `DispatcherServlet`을 생성하고, mapping 경로를 `/`로 설정하여 모든 웹 요청이 이 서블릿을 거치도록 만들었습니다.

### ③ 두 개의 Context (Root Context vs Servlet Context)
기본적인 스프링 웹 애플리케이션은 계층형 컨텍스트를 가집니다.
* **Root ApplicationContext** (`ContextLoaderListener`에 의해 생성): 웹 기술에 독립적인 서비스(Service), 데이터 액세스(Repository), DB 커넥션 풀 등을 관리합니다.
* **Servlet ApplicationContext** (`DispatcherServlet`에 의해 생성): 컨트롤러(Controller), 뷰 리졸버(ViewResolver) 등 웹 MVC와 관련된 빈을 관리합니다.
* 이번 실습(Step 1)에서는 단순함을 위해 하나의 `AnnotationConfigWebApplicationContext`에 `WebConfig`를 등록하고, 이를 `ContextLoaderListener`와 `DispatcherServlet`이 공유하도록 설정하였습니다.

### ④ `@EnableWebMvc`와 `WebMvcConfigurer`
* **`@EnableWebMvc`**: 스프링 웹 MVC에 필요한 기본 빈 설정들(HandlerMapping, HandlerAdapter 등)을 자동으로 등록해 줍니다.
* **`WebMvcConfigurer`**: 스프링이 제공하는 기본 MVC 설정을 유지하면서, 개발자가 필요한 커스텀 설정(인터셉터, 리소스 핸들러 등)을 자바 코드로 재정의할 수 있게 해주는 인터페이스입니다.

### ⑤ `ViewResolver` (뷰 해결사)
* 컨트롤러가 처리 결과를 보여줄 "뷰 이름"(예: `"index"`)을 반환하면, 이를 실제 서비스할 물리적 파일 경로(예: `/WEB-INF/views/index.jsp`)로 매핑해 주는 역할을 합니다.
* 본 실습에서는 기본적인 `InternalResourceViewResolver`를 등록하였습니다.

---

## 3. 🎯 면접 대비 예상 질문 & 모범 답변

### Q1. `web.xml` 없이 자바 설정만으로 스프링 웹 애플리케이션이 구동되는 원리를 설명해 주세요.
> **답변:** 
> Servlet 3.0+ 스펙부터 도입된 **SPI(Service Provider Interface)** 덕분입니다. 서블릿 컨테이너(Tomcat)가 구동될 때 클래스패스에 있는 `ServletContainerInitializer` 구현체를 찾아서 실행하는데, 스프링이 구현한 `SpringServletContainerInitializer`가 실행됩니다. 이 클래스는 애플리케이션 내에서 `WebApplicationInitializer` 인터페이스를 구현한 클래스들을 찾아서 이들의 `onStartup()` 메서드를 호출함으로써 `web.xml` 없이도 서블릿 등록 및 스프링 컨텍스트 설정을 프로그래밍 방식으로 완료할 수 있습니다.

### Q2. `DispatcherServlet`이란 무엇이며, 왜 사용하나요?
> **답변:** 
> `DispatcherServlet`은 Spring MVC의 핵심 서블릿으로, **Front Controller 패턴**을 구현한 것입니다. 웹 애플리케이션으로 들어오는 모든 HTTP 요청을 가장 전면에서 받아 공통 작업을 처리(한글 필터링, 멀티파트 핸들링 등)하고, 요청의 URL 정보 등을 바탕으로 알맞은 컨트롤러(Handler)에게 작업을 위임하는 역할을 합니다. 이를 통해 각 컨트롤러가 서블릿 스펙에 종속되지 않고 비즈니스 로직에만 집중할 수 있게 해줍니다.

### Q3. `ContextLoaderListener`와 `DispatcherServlet`이 각각 로딩하는 Context의 차이는 무엇인가요?
> **답변:** 
> 전통적인 스프링 웹 애플리케이션 구조에서 두 클래스는 부모-자식 관계의 컨텍스트를 생성합니다.
> * **`ContextLoaderListener`**는 웹 영역과 무관한 서비스(`@Service`), 데이터 접근(`@Repository`), DB 설정 등 비즈니스 로직에 필요한 공통 빈들을 담은 **Root Context**를 생성합니다.
> * **`DispatcherServlet`**은 웹 요청을 처리하는 데 필요한 컨트롤러(`@Controller`), 뷰 리졸버(`ViewResolver`) 등의 빈들을 담은 **Servlet Context**를 생성합니다.
> * 자식 컨텍스트(Servlet Context)는 부모 컨텍스트(Root Context)의 빈을 참조할 수 있지만, 반대는 불가능하여 레이어 간의 책임을 명확히 분리합니다.

### Q4. `@EnableWebMvc` 어노테이션의 역할은 무엇인가요?
> **답변:** 
> `@EnableWebMvc`는 스프링 MVC 구동에 필수적인 기본 컴포넌트들(예: `RequestMappingHandlerMapping`, `RequestMappingHandlerAdapter` 등)을 자동으로 빈으로 등록하고 활성화해 줍니다. 또한 JSON 변환을 위한 메시지 컨버터 설정 등 현대적인 웹 개발에 필요한 다양한 인프라 설정을 자동으로 구성해 주는 편리한 어노테이션입니다.
