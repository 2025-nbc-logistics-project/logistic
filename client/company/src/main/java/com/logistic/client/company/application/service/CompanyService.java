package com.logistic.client.company.application.service;

import com.logistic.client.company.application.dto.company.*;
import com.logistic.client.company.domain.exception.company.CompanyDuplicatedException;
import com.logistic.client.company.domain.model.Company;
import com.logistic.client.company.domain.repository.CompanyRepository;
import com.logistic.client.company.infrastructure.client.HubClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final HubClient hubClient;

    @Transactional
    public CompanyCreateResponseDto createCompany(CompanyCreateRequestDto requestDto) {
        //이미 업체가 존재하는 유저인지 (??)

        //중복된 업체인지 (업체명 + 전화번호로 확인)
        boolean isDuplicated = companyRepository.isDuplicateStore(
                requestDto.getCompanyName(),
                requestDto.getCompanyTel()
        );

        if(isDuplicated) {
            throw new CompanyDuplicatedException();
        }

        //존재하는 허브인지
//        if(!hubClient.existsHub(requestDto.getHudId())) {
//            throw new HubNotFoundException();
//        }

        Company company = new Company(requestDto);
        companyRepository.save(company);
        return new CompanyCreateResponseDto(company);

    }

}
