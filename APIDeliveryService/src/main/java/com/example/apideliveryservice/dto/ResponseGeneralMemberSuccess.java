package com.example.apideliveryservice.dto;

import com.example.apideliveryservice.entity.GeneralMemberEntity;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGeneralMemberSuccess {

    private int status;
    private List<GeneralMemberDto> list;
    private GeneralMemberDto generalMemberDto;
}
