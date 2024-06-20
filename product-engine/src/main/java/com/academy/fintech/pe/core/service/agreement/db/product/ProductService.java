package com.academy.fintech.pe.core.service.agreement.db.product;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service which works with {@link ProductRepository}.
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    /**
     * Gets {@link Product} by product's code from {@link ProductRepository}.
     *
     * @param code of the product.
     * @return {@link Product} with given product's code.
     */
    public Product getProductByCode(int code) {
        return productRepository.getProductByCode(code);
    }
}
