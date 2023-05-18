package pl.maryana.conference.controller;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.maryana.conference.dto.response.LectureDto;
import pl.maryana.conference.service.LectureService;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lectures")
public class LectureController {

    private final LectureService lectureService;

    @Autowired
    public LectureController(LectureService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping
    @Operation(summary="List of lectures", description = "Użytkownik może obejrzeć plan konferencji.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of lectures", content = @Content(array =
                                                @ArraySchema(schema = @Schema(implementation = LectureDto.class))))
    })
    public Map<String, List<LectureDto>> getAllLectures(){
        return lectureService.findAll().stream().map(LectureDto::new).collect(Collectors.groupingBy(LectureDto::getStartTime));
    }

}
