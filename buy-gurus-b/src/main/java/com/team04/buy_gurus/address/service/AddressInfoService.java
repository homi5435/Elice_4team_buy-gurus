package com.team04.buy_gurus.address.service;

import com.team04.buy_gurus.address.domain.AddressInfo;
import com.team04.buy_gurus.address.dto.AddressInfoRequest;
import com.team04.buy_gurus.address.repository.AddressInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AddressInfoService {
    private final AddressInfoRepository addressInfoRepository;

    @Autowired
    public AddressInfoService(AddressInfoRepository addressInfoRepository) {
        this.addressInfoRepository = addressInfoRepository;
    }

    public void save(AddressInfoRequest addressInfoRequest) {
        AddressInfo addressInfo = new AddressInfo(addressInfoRequest);
        addressInfoRepository.save(addressInfo);
    }

    public AddressInfo get(Long id) {
        return addressInfoRepository.findById(id).orElse(null);
    }

    public List<AddressInfo> getAll() {
        return addressInfoRepository.findAll();
    }

    @Transactional
    public void update(Long id, AddressInfoRequest addressInfoRequest) {
        AddressInfo addressInfo = addressInfoRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 주소 내역은 존재하지 않습니다."));
        if (addressInfo != null) {
            addressInfo.setAddress(addressInfoRequest.getAddress());
            addressInfo.setName(addressInfoRequest.getName());
            addressInfo.setPhoneNum(addressInfoRequest.getPhoneNum());
        }
    }

    @Transactional
    public void delete(Long id) {
        AddressInfo addressInfo = addressInfoRepository.findById(id).orElseThrow(() -> new RuntimeException("해당 주소 내역은 존재하지 않습니다."));
        addressInfoRepository.delete(addressInfo);
    }
}
