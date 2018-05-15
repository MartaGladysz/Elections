package com.app.service.election;

import com.app.model.dto.ConstituencyDto;
import java.util.Map;

public interface ElectionService {
    Map<ConstituencyDto, String> findWinners();
    Boolean isConfigurationSet();
}
