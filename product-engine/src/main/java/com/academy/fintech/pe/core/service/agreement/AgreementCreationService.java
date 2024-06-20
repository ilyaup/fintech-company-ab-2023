package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.agreement_creation.AgreementCreationRequest;
import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementService;
import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import com.academy.fintech.pe.core.service.agreement.db.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * Service for creating agreements, basing on {@link  AgreementCreationRequest}
 */
@Service
@RequiredArgsConstructor
public class AgreementCreationService {
    private final AgreementService agreementService;
    private final ProductService productService;

    /**
     * Checks whether the agreement information from {@link AgreementCreationRequest} fit {@link Product} criteria.
     * If it fits, then creates agreement basing on this information and saves it, using {@link AgreementService} and
     * returns agreement's id.
     * If not, then just returns null.
     *
     * @param request which contains information for creating an agreement.
     * @return the agreement's id if the agreement is created, null if not.
     */
    public Integer create(AgreementCreationRequest request) {
        final Product product = productService.getProductByCode(request.getProductCode());
        if (validateAgreementCreationRequest(product, request)) {
            Agreement agreement = Agreement.builder()
                    .product(product)
                    .clientId(request.getClientId())
                    .term(request.getTerm())
                    .principalAmount(new BigDecimal(request.getDisbursementAmount())
                            .add(product.getMaxOriginationAmount()))
                    .originationAmount(product.getMaxOriginationAmount())
                    .interest(new BigDecimal(request.getInterest()))
                    .status("NEW")
                    .disbursementDate(null)
                    .nextPaymentDate(null)
                    .build();
            return agreementService.create(agreement);
        } else {
            return null;
        }
    }

    private boolean validateAgreementCreationRequest(Product product, AgreementCreationRequest request) {
        return validateInterest(product, request)
                && validateTerm(product, request)
                && validatePrincipalAmount(product, request);
    }

    private boolean validatePrincipalAmount(Product product, AgreementCreationRequest request) {
        return product.getMinPrincipalAmount()
                .compareTo(new BigDecimal(request.getDisbursementAmount())
                        .add(product.getMaxOriginationAmount())) <= 0
                && product.getMaxPrincipalAmount()
                .compareTo(new BigDecimal(request.getDisbursementAmount())
                        .add(product.getMaxOriginationAmount())) >= 0;
    }

    private boolean validateTerm(Product product, AgreementCreationRequest request) {
        return product.getMinTerm() <= request.getTerm()
                && product.getMaxTerm() >= request.getTerm();
    }

    private boolean validateInterest(Product product, AgreementCreationRequest request) {
        return product.getMinInterest().compareTo(new BigDecimal(request.getInterest())) <= 0
                && product.getMaxInterest().compareTo(new BigDecimal(request.getInterest())) >= 0;
    }

}
