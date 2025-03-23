package com.logistic.client.company.application.service;

import static org.mockito.BDDMockito.*;

import com.logistic.client.company.application.dto.common.HubDto;
import com.logistic.client.company.application.dto.company.*;
import com.logistic.client.company.domain.model.company.Company;
import com.logistic.client.company.domain.model.company.CompanyType;
import com.logistic.client.company.domain.repository.CompanyRepository;
import com.logistic.client.company.infrastructure.client.HubClient;
import com.logistic.client.company.presentation.request.CompanyCreateRequestDto;
import com.logistic.client.company.presentation.request.CompanyUpdateRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceTest {

    @Mock
    CompanyRepository companyRepository;

    @Mock
    private HubClient hubClient;

    @InjectMocks
    CompanyService companyService;

    private Company company;
    private UUID companyId;
    private String role = "HUB_MANAGER";

    @BeforeEach
    void setUp() {

        UUID hubId = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        CompanyType companyType = CompanyType.producer;
        String companyName = "업체1";
        String companyTel = "07012345678";
        String postalCode = "01234";
        String streetAddress = "서울시 중구";
        String detailAddress = "1층";

        CompanyCreateRequestDto requestDto = new CompanyCreateRequestDto(
                hubId, userId, companyType, companyName, companyTel, postalCode, streetAddress, detailAddress);

        company = new Company(requestDto);
        companyId = UUID.randomUUID();
        ReflectionTestUtils.setField(company, "companyId", companyId);
    }

    @Test
    @DisplayName("업체 생성 성공")
    void createCompanySuccess() {

        CompanyCreateRequestDto requestDto = new CompanyCreateRequestDto(
                company.getHubId(),
                company.getUserId(),
                company.getCompanyType(),
                company.getCompanyName(),
                company.getCompanyTel(),
                company.getAddress().getPostalCode(),
                company.getAddress().getStreetAddress(),
                company.getAddress().getDetailAddress()
                );
        HubDto hubDto = new HubDto();
        ReflectionTestUtils.setField(hubDto, "id", company.getHubId());
        ReflectionTestUtils.setField(hubDto, "name", "허브1");

        given(companyRepository.isDuplicateStore(anyString(), anyString())).willReturn(false);
        given(hubClient.getHub(requestDto.getHubId())).willReturn(hubDto);
        doNothing().when(companyRepository).save(any(Company.class));

        CompanyCreateResponseDto responseDto = companyService.createCompany(
                requestDto, company.getUserId(), company.getHubId(), role);

        assertNotNull(responseDto);
        assertEquals("업체1", responseDto.getCompanyName());
        verify(companyRepository).save(any(Company.class));
    }

    @Test
    @DisplayName("업체 단일 조회 성공")
    void getCompanySuccess() {

        given(companyRepository.findByCompanyIdAndDeletedAtIsNull(companyId)).willReturn(Optional.of(company));

        CompanyResponseDto responseDto = companyService.getCompany(companyId);

        assertNotNull(responseDto);
        assertEquals("업체1", responseDto.getCompanyName());
    }

    @Test
    @DisplayName("업체 목록 조회 성공")
    void getCompaniesSuccess() {

        int page = 0;
        int limit = 10;
        String sortBy = "updatedBy";
        String order = "asc";
        Pageable pageable = PageRequest.of(page, limit);

        List<Company> companyList = List.of(company);
        Page<Company> companyPage = new PageImpl<>(companyList);

        given(companyRepository.getCompanies(pageable, sortBy, order)).willReturn(companyPage);

        Page<CompanyListResponseDto> responseDtos = companyService.getCompanies(page, limit, sortBy, order);

        assertEquals(0, responseDtos.getNumber());
        assertEquals("업체1", responseDtos.getContent().get(0).getCompanyName());
    }

    @Test
    @DisplayName("업체 검색 조회 성공")
    void getSearchCompaniesSuccess() {

        String key = "업체1";
        int page = 0;
        int limit = 10;
        String sortBy = "updatedBy";
        String order = "asc";
        Pageable pageable = PageRequest.of(page, limit);

        List<Company> companyList = List.of(company);
        Page<Company> companyPage = new PageImpl<>(companyList);

        given(companyRepository.getSearchCompanies(key, pageable, sortBy, order)).willReturn(companyPage);

        Page<CompanyListResponseDto> responseDtos = companyService.getSearchCompanies(key, page, limit, sortBy, order);

        assertNotNull(responseDtos);
        assertEquals("업체1", responseDtos.getContent().get(0).getCompanyName());
    }

    @Test
    @DisplayName("업체 수정 성공")
    void updateCompanySuccess() {

        CompanyUpdateRequestDto requestDto = new CompanyUpdateRequestDto(
                UUID.randomUUID(),
                CompanyType.supplier,
                "업체2",
                "07022222222",
                "22222",
                "서울시 종로구",
                "2층"
        );

        given(companyRepository.findByCompanyIdAndDeletedAtIsNull(companyId)).willReturn(Optional.of(company));

        CompanyUpdateResponseDto responseDto = companyService.updateCompany(
                companyId, requestDto, company.getUserId(), company.getHubId(), role);

        assertNotNull(responseDto);
        assertEquals("업체2", responseDto.getCompanyName());
    }

    @Test
    @DisplayName("업체 삭제 성공")
    void deleteCompanySuccess() {

        given(companyRepository.findByCompanyIdAndDeletedAtIsNull(companyId)).willReturn(Optional.of(company));

        CompanyDeleteResponseDto responseDto = companyService.deleteCompany(
                companyId, company.getUserId(), company.getHubId(), role);

        assertNotNull(responseDto);
        assertEquals(company.getUserId(), responseDto.getDeletedBy());
    }

    @Test
    @DisplayName("companyId로 업체 조회 성공")
    void findByCompanyIdSuccess() {

        given(companyRepository.findByCompanyIdAndDeletedAtIsNull(companyId)).willReturn(Optional.of(company));

        Company company1 = companyService.findByCompanyId(companyId);

        assertNotNull(company1);
        assertEquals("업체1", company1.getCompanyName());
    }
}