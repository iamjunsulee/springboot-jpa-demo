package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    /*
    @Embedded를 사용하면 객체의 이름이 아니라 객체 안의 필드 이름으로 column을 생성하므로 객체 안의 column 재정의가 필요하다.
     */
    @Embedded
    @AttributeOverride(name="address1", column = @Column(name="home_address1"))
    @AttributeOverride(name="address2", column = @Column(name="home_address2"))
    @AttributeOverride(name="zipcode", column = @Column(name="home_zipcode"))
    private Address homeAddress;

    @Embedded
    @AttributeOverride(name="address1", column = @Column(name="company_address1"))
    @AttributeOverride(name="address2", column = @Column(name="company_address2"))
    @AttributeOverride(name="zipcode", column = @Column(name="company_zipcode"))
    private Address companyAddress;

    /*
    mappedBy 속성을 사용해서 속성의 값으로 연관관계의 주인을 설정
    연관관계의 주인은 mappedBy를 사용할 수 없다.
    아래에서 작성한 member는 Order Entity에서 Member를 참조할 때 작성한 필드명
     */
    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}
