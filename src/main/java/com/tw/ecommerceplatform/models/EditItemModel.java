package com.tw.ecommerceplatform.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Data
@RequiredArgsConstructor
public class EditItemModel extends CreateItemModel {
    Long id;

    public EditItemModel(String name, int quantity) {
        super(name, quantity);
    }
}
