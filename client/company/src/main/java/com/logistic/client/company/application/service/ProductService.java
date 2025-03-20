package com.logistic.client.company.application.service;

import com.logistic.client.company.application.dto.product.*;
import com.logistic.client.company.domain.model.Product;
import com.logistic.client.company.domain.repository.ProductRepository;
import com.logistic.client.company.infrastructure.client.HubClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    //
    private final CompanyService companyService;
    private final HubClient hubClient;

    @Transactional
    public ProductCreateResponseDto createProduct(ProductCreateRequestDto requestDto) {

        //중복 검사 (??)

        //존재하는 업체인지
        companyService.findByCompanyId(requestDto.getCompanyId());

        //존재하는 허브인지
//        if(!hubClient.existsHub(requestDto.getHudId())) {
//            throw new HubNotFoundException();
//        }

        Product product = new Product(requestDto);
        productRepository.save(product);
        return new ProductCreateResponseDto(product);

    }

}
