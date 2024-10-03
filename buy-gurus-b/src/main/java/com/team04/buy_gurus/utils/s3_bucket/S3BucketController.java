package com.team04.buy_gurus.utils.s3_bucket;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/s3/bucket/test")
@RequiredArgsConstructor
public class S3BucketController {
    private final S3BucketService s3BucketService;

    @PostMapping("/one")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("path") String path) {
        try {
            String fileUrl = s3BucketService.upload(file, path);
            return new ResponseEntity<>(fileUrl, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/many")
    public ResponseEntity<?> uploadFiles(@RequestParam("file") MultipartFile[] file, @RequestParam("path") String path) {
        try {
            List<String> fileUrl = s3BucketService.upload(file, path);
            return new ResponseEntity<>(fileUrl, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteBucket(@RequestParam("filename") String filename) {
        try {
            s3BucketService.remove(filename);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>("Hello World!", HttpStatus.OK);
    }
}