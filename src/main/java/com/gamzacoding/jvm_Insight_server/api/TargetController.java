package com.gamzacoding.jvm_Insight_server.api;

import com.gamzacoding.jvm_Insight_server.domain.target.Target;
import com.gamzacoding.jvm_Insight_server.domain.target.TargetRepository;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/api/targets")
public class TargetController {

    private final TargetService targetService;
    private final TargetRepository targetRepository;

    @PostMapping
    public TargetResponse creat(@RequestBody CreateTargetRequest request) {
        Target savedTarget = targetService.create(request.pid(), request.displayName());
        return TargetResponse.from(savedTarget);
    }

    @GetMapping
    public List<TargetResponse> list() {
        return targetRepository.findAll().stream()
                .map(TargetResponse::from)
                .toList();
    }

    public record CreateTargetRequest(
            @Min(1) int pid,
            @Size(max = 100) String displayName
    ){}

    public record TargetResponse(Long id, int pid, String displayName, Instant createAt) {
        static TargetResponse from(Target target) {
            return new TargetResponse(target.getId(), target.getPid(), target.getDisplayName(), target.getCreatedAt());
        }
    }
}
