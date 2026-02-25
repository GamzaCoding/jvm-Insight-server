package com.gamzacoding.jvm_Insight_server.api;

import com.gamzacoding.jvm_Insight_server.domain.target.Target;
import com.gamzacoding.jvm_Insight_server.domain.target.TargetRepository;
import com.gamzacoding.jvm_Insight_server.error.ApiException;
import com.gamzacoding.jvm_Insight_server.error.ErrorCode;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class TargetService {

    private final TargetRepository targetRepository;

    public Target create(int pid, String displayName) {
        validatePidPositive(pid);
        return targetRepository.save(new Target(pid, displayName));
    }

    private void validatePidPositive(int pid) {
        if (pid <= 0) {
            throw new ApiException(ErrorCode.PIP_NOT_NEGATIVE);
        }
    }

    public List<Target> finaAll() {
        return targetRepository.findAll();
    }
}
