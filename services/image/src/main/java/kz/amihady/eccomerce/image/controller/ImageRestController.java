package kz.amihady.eccomerce.image.controller;


import kz.amihady.eccomerce.image.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageRestController {
    private final ImageService imageService;

    @PostMapping("/{productId}")
    public ResponseEntity<UUID> addImage(
            @PathVariable UUID productId,
            @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(imageService.addImage(productId,file));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable("id") UUID id){
        imageService.deleteImage(id);
        return ResponseEntity.noContent().build();
    }


}
