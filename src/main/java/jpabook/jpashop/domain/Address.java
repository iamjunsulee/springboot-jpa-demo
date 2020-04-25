package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

/*
jpa entity 안에서 Column을 하나의 객체로 사용하려고 할 때, @Embedded, @Embeddable annotation을 사용한다.
 */
@Embeddable
@Getter
public class Address {
    private String address1;
    private String address2;
    private String zipcode;

    protected Address(){}

    public Address(String address1, String address2, String zipcode){
        this.address1 = address1;
        this.address2 = address2;
        this.zipcode = zipcode;
    }
}
