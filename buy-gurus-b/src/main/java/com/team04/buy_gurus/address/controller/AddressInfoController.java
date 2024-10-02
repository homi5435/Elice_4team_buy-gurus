package com.team04.buy_gurus.address.controller;

import com.team04.buy_gurus.address.domain.AddressInfo;
import com.team04.buy_gurus.address.dto.AddressInfoRequest;
import com.team04.buy_gurus.address.dto.AddressInfoResponse;
import com.team04.buy_gurus.address.repository.AddressInfoRepository;
import com.team04.buy_gurus.address.service.AddressInfoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user/address")
@RequiredArgsConstructor
public class AddressInfoController {
    private final AddressInfoService addressInfoService;

    @PostMapping
    public ResponseEntity<?> addAddress(@Valid @RequestBody AddressInfoRequest addressInfoRequest) {
        addressInfoService.save(addressInfoRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<?> getAllAddresses() {
        List<AddressInfo> addressInfoList = addressInfoService.getAll();
        AddressInfoResponse response = new AddressInfoResponse(addressInfoList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAddress(@PathVariable Long id) {
        AddressInfo addressInfo = addressInfoService.get(id);
        return ResponseEntity.ok(addressInfo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAddress(@PathVariable Long id, @Valid @RequestBody AddressInfoRequest addressInfoRequest) {
        addressInfoService.update(id, addressInfoRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAddress(@PathVariable Long id) {
        addressInfoService.delete(id);
        return ResponseEntity.ok().build();
    }
}
