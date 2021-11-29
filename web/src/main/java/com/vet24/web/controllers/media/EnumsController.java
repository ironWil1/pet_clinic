package com.vet24.web.controllers.media;

import com.vet24.web.util.ReflectionUtil;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@Tag(name = "Enums Controller", description = "Return all Enum's n return all constant's of the \"nameEnum\"")
public class EnumsController {

    private final ReflectionUtil reflectionUtil;

    @Autowired
    public EnumsController(ReflectionUtil reflectionUtil) {
        this.reflectionUtil = reflectionUtil;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return all of the Enum's"),
            @ApiResponse(responseCode = "40x", description = "Something went wrong")
    })
    @GetMapping("/api/enums")
    public ResponseEntity<List<String>> findAllEnums() {
        return new ResponseEntity<>(reflectionUtil.getAllEnums(), HttpStatus.OK);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Return all constant's of the \"nameEnum\""),
            @ApiResponse(responseCode = "40x", description = "Something went wrong")
    })
    @GetMapping("/api/enums/{enumName}")
    public ResponseEntity<List<String>> getEnumNameList(@Valid @PathVariable String enumName) {
        return new ResponseEntity<>(reflectionUtil.getEnumConsts(enumName), HttpStatus.OK);
    }
}
