package com.academy.fintech.pe.core.service.agreement.db.product;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "code")
    int code;

    @Column(name = "min_term")
    private int minTerm;

    @Column(name = "max_term")
    private int maxTerm;

    @Column(name = "min_principal_amount")
    private BigDecimal minPrincipalAmount;

    @Column(name = "max_principal_amount")
    private BigDecimal maxPrincipalAmount;

    @Column(name = "min_interest")
    private BigDecimal minInterest;

    @Column(name = "max_interest")
    private BigDecimal maxInterest;

    @Column(name = "min_origination_amount")
    private BigDecimal minOriginationAmount;

    @Column(name = "max_origination_amount")
    private BigDecimal maxOriginationAmount;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "product") // not sure about mappedBy
    private List<Agreement> agreements;
}
