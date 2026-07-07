package org.example.webmvc.step3.dto;

import lombok.*;

//@Data // 복합 구성 되어 있어서 컨트롤이 잘 안되는 느낌
@Getter // 필드(멤버변수) 있는 값에 접근자 Getter - 값을 읽어주는 메서드
@Setter // set*** -> 값을 바꿔줌 (최근에는 Setter 사용을 지양) - 검증하는 걸 요구하는 추세
@NoArgsConstructor // 기본 생성자 new 클래스명()
@AllArgsConstructor // 모든 필드를 매개변수로 받는 생성자
@ToString // 인스턴스를 문자열로 변환 시 데이터 구조가 나타나게 함
public class ShirtDTO {
    private String name; // getName
    private int price; // getPrice

//    ShirtDTO();
//    ShirtDTO(String name, int price) {}
}
