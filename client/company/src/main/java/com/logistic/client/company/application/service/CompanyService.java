package com.logistic.client.company.application.service;

import com.logistic.client.company.application.dto.common.CompanyExistResponseDto;
import com.logistic.client.company.application.dto.common.UserDto;
import com.logistic.client.company.application.dto.company.*;
import com.logistic.client.company.domain.exception.common.HubNotFoundException;
import com.logistic.client.company.domain.exception.common.UnauthorizedAccessException;
import com.logistic.client.company.domain.exception.company.CompanyDuplicatedException;
import com.logistic.client.company.domain.exception.company.CompanyNotFoundException;
import com.logistic.client.company.domain.model.company.Address;
import com.logistic.client.company.domain.model.company.Company;
import com.logistic.client.company.domain.repository.CompanyRepository;
import com.logistic.client.company.infrastructure.client.HubClient;
import com.logistic.client.company.infrastructure.client.UserClient;
import com.logistic.client.company.presentation.request.CompanyCreateRequestDto;
import com.logistic.client.company.presentation.request.CompanyUpdateRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final HubClient hubClient;
    private final UserClient userClient;

    @Transactional
    public CompanyCreateResponseDto createCompany(
            CompanyCreateRequestDto requestDto,
            UUID userId,
            String username,
            String role
    ) {

        UserDto userDto = userClient.getHubId(username);

        if (userDto == null || userDto.getHubId() == null) {
            throw new HubNotFoundException();
        }

        if(
                !("MASTER".equals(role))  //마스터 관리자가 아니고
                        && !("HUB_MANAGER".equals(role) //허브 관리자도 아니고
                        && userDto.getHubId().equals(requestDto.getHubId())) //허브 관리자여도 담당 허브가 아니라면
        ) {
            throw new UnauthorizedAccessException();
        }

        //중복된 업체인지 (업체명 + 전화번호로 확인)
        boolean isDuplicated = companyRepository.isDuplicateStore(
                requestDto.getCompanyName(),
                requestDto.getCompanyTel()
        );

        if(isDuplicated) {
            throw new CompanyDuplicatedException();
        }

        //존재하는 허브인지
        if(hubClient.getHub(requestDto.getHubId()) == null) {
            throw new HubNotFoundException();
        }

        Company company = new Company(new CompanyCreateRequestDto(requestDto), userId);
        companyRepository.save(company);
        return new CompanyCreateResponseDto(company);

    }

    public CompanyResponseDto getCompany(UUID companyId) {
        Company company = findByCompanyId(companyId);
        return new CompanyResponseDto(company);
    }

    public Page<CompanyListResponseDto> getCompanies(int page, int limit, String sortBy, String order) {
        if(limit != 10 && limit != 30 && limit != 50) {
            limit = 10;
        }
        Pageable pageable = PageRequest.of(page, limit);
        Page<Company> companies = companyRepository.getCompanies(pageable, sortBy, order);
        return companies.map(CompanyListResponseDto::new);
    }

    public Page<CompanyListResponseDto> getSearchCompanies(String key, int page, int limit, String sortBy, String order) {
        if(limit != 10 && limit != 30 && limit != 50) {
            limit = 10;
        }
        Pageable pageable = PageRequest.of(page, limit);
        Page<Company> companies = companyRepository.getSearchCompanies(key, pageable, sortBy, order);
        return companies.map(CompanyListResponseDto::new);
    }

    @Transactional
    public CompanyUpdateResponseDto updateCompany(
            UUID companyId,
            CompanyUpdateRequestDto requestDto,
            UUID userId,
            String username,
            String role
    ) {

        UserDto userDto = userClient.getHubId(username);

        if (userDto == null || userDto.getHubId() == null) {
            throw new HubNotFoundException();
        }

        Company company = findByCompanyId(companyId);

        if(
                !("MASTER".equals(role)) //마스터 관리자가 아니고
                        && !("HUB_MANAGER".equals(role) //허브 관리자도 아니고
                        && userDto.getHubId().equals(company.getHubId()))  //허브 관리자여도 담당 허브가 아니고
                        &&!userId.equals(company.getUserId()) //업체 담당자도 아니라면
        ) {
            throw new UnauthorizedAccessException();
        }

        //허브
        if(requestDto.getHubId() != null && hubClient.getHub(requestDto.getHubId()) != null) {
            company.changeHub(requestDto.getHubId());
        }

        if(requestDto.getCompanyType() != null) {
            company.changeCompanyType(requestDto.getCompanyType());
        }

        if(requestDto.getCompanyName() != null && !requestDto.getCompanyName().isBlank()) {
            company.changeCompanyName(requestDto.getCompanyName());
        }

        if(requestDto.getCompanyTel() != null && !requestDto.getCompanyTel().isBlank()) {
            company.changeCompanyTel(requestDto.getCompanyTel());
        }

        String postalCode = company.getAddress().getPostalCode();
        String streetAddress = company.getAddress().getStreetAddress();
        String detailAddress = company.getAddress().getDetailAddress();

        if(requestDto.getPostalCode() != null && !requestDto.getPostalCode().isBlank()) {
            postalCode = requestDto.getPostalCode();
        }

        if(requestDto.getStreetAddress() != null && !requestDto.getStreetAddress().isBlank()) {
            streetAddress = requestDto.getStreetAddress();
        }

        if(requestDto.getDetailAddress() != null) {
            detailAddress = requestDto.getDetailAddress().isBlank() ? "" : requestDto.getDetailAddress();
        }

        Address newAddress = new Address(postalCode, streetAddress, detailAddress);

        if(!company.getAddress().equals(newAddress)) {
            company.changeAddress(newAddress);
        }

        return new CompanyUpdateResponseDto(company);
    }

    @Transactional
    public CompanyDeleteResponseDto deleteCompany(
            UUID companyId,
            UUID userId,
            String username,
            String role
    ) {

        UserDto userDto = userClient.getHubId(username);

        if (userDto == null || userDto.getHubId() == null) {
            throw new HubNotFoundException();
        }

        Company company = findByCompanyId(companyId);

        if(
                !("MASTER".equals(role)) //마스터 관리자가 아니고
                        && !("HUB_MANAGER".equals(role) //허브 관리자도 아니고
                        && userDto.getHubId().equals(company.getHubId()))  //허브 관리자여도 담당 허브가 아니라면
        ) {
            throw new UnauthorizedAccessException();
        }

        company.delete(userId);

        return new CompanyDeleteResponseDto(company);
    }

    public Company findByCompanyId(UUID companyId) {
        return companyRepository
                .findByCompanyIdAndDeletedAtIsNull(companyId)
                .orElseThrow(CompanyNotFoundException::new);
    }

    public CompanyExistResponseDto getCompanyById(UUID companyId) {
        Company company = findByCompanyId(companyId);
        return new CompanyExistResponseDto(company);
    }

}
