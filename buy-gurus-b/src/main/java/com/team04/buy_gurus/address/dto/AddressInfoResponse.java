package com.team04.buy_gurus.address.dto;

import com.team04.buy_gurus.address.domain.AddressInfo;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class AddressInfoResponse {
    private final List<AddressInfoDetail> addressInfoDetailList;

    public AddressInfoResponse(List<AddressInfo> addressInfoList) {
        addressInfoDetailList = addressInfoList.stream()
                .map(AddressInfoDetail::new)
                .collect(Collectors.toList());
    }

    @Getter
    public static class AddressInfoDetail {
        private final Long id;
        private final String address;
        private final String name;
        private final String phoneNum;
        private final boolean recent;

        public AddressInfoDetail(AddressInfo addressInfo) {
            this.id = addressInfo.getId();
            this.address = addressInfo.getAddress();
            this.name = addressInfo.getName();
            this.phoneNum = addressInfo.getPhoneNum();
            this.recent = addressInfo.isRecent();
        }
    }
}
