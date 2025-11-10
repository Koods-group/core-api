package ci.koodysgroup.core.controllers;


import ci.koodysgroup.core.controllers.contexts.media.content.UpdateContentContext;
import ci.koodysgroup.domains.dtms.ContentDtm;
import ci.koodysgroup.domains.types.LocalizedText;
import ci.koodysgroup.features.content.commands.CreateContentCommand;
import ci.koodysgroup.features.content.commands.UpdateContentCommand;
import ci.koodysgroup.features.content.commands.UpdateContentCoverCommand;
import ci.koodysgroup.interfaces.bus.CommandBus;
import ci.koodysgroup.utils.response.ApiResponse;
import ci.koodysgroup.utils.response.ApiResponseUtil;
import ci.koodysgroup.utils.response.CommandResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/content")
public class ContentController {

    private final CommandBus bus;
    private final ObjectMapper objectMapper;

    public ContentController(CommandBus bus, ObjectMapper objectMapper) {
        this.bus = bus;
        this.objectMapper = objectMapper;
    }


    @PutMapping("/to-update")
    public ResponseEntity<ApiResponse<ContentDtm>> toUpdate(@RequestBody UpdateContentContext context)
    {
        UpdateContentCommand command = new UpdateContentCommand(context.getId(),context.getTitle(),context.getDescription(),context.isMustBeSeen(),context.getReleaseDate());
        CommandResponse<ContentDtm> response = this.bus.send(command);

        if(!response.isSuccess())
        {
            return switch (response.getCode()){
                case "not_found" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseUtil.notFound(response.getMessage()));
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseUtil.internalError(response.getMessage()));
            };
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseUtil.ok(response.getTitle(),response.getContent(), response.getMessage(),"success"));
    }


    @PutMapping(value = "/to-change-cover" , consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ContentDtm>> toChangeCover(
            @RequestParam("id") UUID id,
            @RequestParam(value = "cover") MultipartFile cover
    )
    {
        UpdateContentCoverCommand command = new UpdateContentCoverCommand(id , cover);
        CommandResponse<ContentDtm> response = this.bus.send(command);

        if(!response.isSuccess())
        {
            return switch (response.getCode()){
                case "not_found" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseUtil.notFound(response.getMessage()));
                case "bad_request" -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseUtil.notFound(response.getMessage()));
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseUtil.internalError(response.getMessage()));
            };
        }

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponseUtil.ok(response.getTitle(),response.getContent(), response.getMessage(),"success"));
    }

    @PostMapping(value = "/to-saved", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ContentDtm>> toSaved(
            @RequestParam("title") String titlePayload,
            @RequestParam("description") String descriptionPayload,
            @RequestParam("releaseDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate releaseDate,
            @RequestParam("mediaType") UUID mediaType,
            @RequestParam("mustBeSeen") boolean mustBeSeen,
            @RequestParam(value = "cover") MultipartFile cover
    ) {
        LocalizedText title;
        LocalizedText description;

        try {
            title = objectMapper.readValue(titlePayload, LocalizedText.class);
            description = objectMapper.readValue(descriptionPayload, LocalizedText.class);
        } catch (JsonProcessingException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ApiResponseUtil.badRequest("Invalid payload", "LocalizedText payload must be valid JSON", exception.getOriginalMessage()));
        }

        CreateContentCommand command = new CreateContentCommand(title, description, releaseDate, mediaType, mustBeSeen, cover);
        CommandResponse<ContentDtm> response = this.bus.send(command);

        if(!response.isSuccess())
        {
            return switch (response.getCode()){
                case "not_found" -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(ApiResponseUtil.notFound(response.getMessage()));
                case "bad_request" -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ApiResponseUtil.notFound(response.getMessage()));
                default -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ApiResponseUtil.internalError(response.getMessage()));
            };
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponseUtil.ok(response.getTitle(),response.getContent(), response.getMessage(),"success"));
    }
}
