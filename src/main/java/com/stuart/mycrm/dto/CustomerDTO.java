package com.stuart.mycrm.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
}
