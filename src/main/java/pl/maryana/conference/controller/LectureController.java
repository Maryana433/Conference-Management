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
import pl.maryana.conference.service.LectureThematicPathService;
import io.swagger.v3.oas.annotations.Operation;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/lectures")
public class LectureController {

    private final LectureThematicPathService lectureThematicPathService;

    @Autowired
    public LectureController(LectureThematicPathService lectureThematicPathService) {
        this.lectureThematicPathService = lectureThematicPathService;
    }

    @GetMapping
    @Operation(summary="List of lectures", description = "Użytkownik może obejrzeć plan konferencji.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of lectures", content = @Content(array =
                                                @ArraySchema(schema = @Schema(implementation = LectureDto.class))))
    })
    public Map<String, List<LectureDto>> getAllLectures(){
        String dateTimePattern = "dd-MM-yyyy H:mm";
        return lectureThematicPathService.findAll().stream().map(LectureDto::new).collect(Collectors.groupingBy(e -> e.getStartTime().format(DateTimeFormatter.ofPattern(dateTimePattern))));
    }

}
