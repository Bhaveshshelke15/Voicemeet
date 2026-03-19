package com.voicemeet.voicemeetbackend.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateMeetingRequest {

    private List<String> userIds;
}
