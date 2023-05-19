package pl.maryana.conference.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.maryana.conference.dto.response.ApiExceptionDto;
import pl.maryana.conference.dto.response.UserInfoDto;
import pl.maryana.conference.service.GenerateStatisticsByteStreamService;
import pl.maryana.conference.service.LectureThematicPathService;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/summary")
public class SummaryController {

    private final LectureThematicPathService lectureThematicPathService;
    private final GenerateStatisticsByteStreamService fileService;

    @Autowired
    public SummaryController(LectureThematicPathService lectureThematicPathService, GenerateStatisticsByteStreamService fileService) {
        this.lectureThematicPathService = lectureThematicPathService;
        this.fileService = fileService;
    }


    @GetMapping("/lectures/file")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary="User with role ADMIN can generate statistics", description = "Wygenerowanie zestawienia dla organizatora:\n" +
            "-  zestawienie wykładów wg zainteresowania (procentowy udział uczestników w danym wykładzie)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Participation statistics for each lecture", content = @Content(schema
                    = @Schema(implementation = UserInfoDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema =
            @Schema(implementation = ApiExceptionDto.class)))
    })
    public ResponseEntity<Resource> generateSummaryForLecture(HttpServletResponse response){

        byte[] data = fileService.generateLectureParticipationInfo(lectureThematicPathService.findAll());

        ByteArrayResource resource = new ByteArrayResource(data);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=lectures_summary.txt");

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(data.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);

    }

    @GetMapping("/paths/file")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary="User with role ADMIN can generate statistics", description = "Wygenerowanie zestawienia dla organizatora:\n" +
            "zestawienie ścieżek tematycznych wg zainteresowania (procentowy udział)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "participation statistics for each thematic path", content = @Content(schema
                    = @Schema(implementation = UserInfoDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error", content = @Content(schema =
            @Schema(implementation = ApiExceptionDto.class)))
    })
    public ResponseEntity<Resource> generateSummaryForPath(){

        byte[] data = fileService.generateThematicPathParticipationInfo(lectureThematicPathService.findAllThematicPaths());

        ByteArrayResource resource = new ByteArrayResource(data);

        HttpHeaders header = new HttpHeaders();
        header.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=paths_summary.txt");

        return ResponseEntity.ok()
                .headers(header)
                .contentLength(data.length)
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(resource);
    }
}
