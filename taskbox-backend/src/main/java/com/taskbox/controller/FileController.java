package com.taskbox.controller;

import com.taskbox.entity.Submission;
import com.taskbox.entity.Assignment;
import com.taskbox.entity.Student;
import com.taskbox.repository.SubmissionRepository;
import com.taskbox.repository.AssignmentRepository;
import com.taskbox.repository.StudentRepository;
import com.taskbox.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jakarta.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class FileController {

    private final FileStorageService fileStorageService;
    private final SubmissionRepository submissionRepository;
    private final AssignmentRepository assignmentRepository;
    private final StudentRepository studentRepository;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "submissionId", required = false) String submissionId,
            @RequestParam("assignmentId") String assignmentId,
            @RequestParam("studentId") String studentId,
            @RequestParam(value = "comments", required = false) String comments) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            // Get or create submission
            Submission submission;
            Assignment assignment = assignmentRepository.findById(assignmentId)
                .orElseThrow(() -> new RuntimeException("Assignment not found"));
            Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
            
            if (submissionId != null) {
                submission = submissionRepository.findById(submissionId)
                    .orElse(null);
                if (submission != null) {
                    // Resubmission - increment version and delete old file
                    submission.setVersion(submission.getVersion() + 1);
                    if (submission.getFilePath() != null) {
                        try {
                            fileStorageService.deleteFile(submission.getFilePath());
                        } catch (IOException e) {
                            // Ignore if file doesn't exist
                        }
                    }
                }
            } else {
                submission = submissionRepository
                    .findByAssignmentAndStudent(assignment, student)
                    .orElse(null);
                if (submission != null && submission.getFilePath() != null) {
                    // Resubmission - increment version
                    submission.setVersion(submission.getVersion() + 1);
                    try {
                        fileStorageService.deleteFile(submission.getFilePath());
                    } catch (IOException e) {
                        // Ignore if file doesn't exist
                    }
                }
            }
            
            // Create new submission if doesn't exist
            if (submission == null) {
                submission = new Submission();
                long count = submissionRepository.count();
                submission.setId("SUB" + String.format("%03d", count + 1));
                submission.setAssignment(assignment);
                submission.setStudent(student);
                submission.setVersion(1);
            }
            
            submissionId = submission.getId();

            // Store file
            String storedFileName = fileStorageService.storeFile(file, submissionId);
            String fileSize = String.format("%.2f MB", file.getSize() / (1024.0 * 1024.0));
            
            // Update submission with file info
            submission.setFileName(file.getOriginalFilename());
            submission.setFilePath(storedFileName);
            submission.setFileSize(fileSize);
            submission.setSubmittedAt(LocalDateTime.now());
            submission.setIsLate(LocalDateTime.now().isAfter(assignment.getDueDate()));
            if (comments != null && !comments.trim().isEmpty()) {
                submission.setComments(comments);
            }
            
            submissionRepository.save(submission);

            return ResponseEntity.ok().body(java.util.Map.of(
                "message", "File uploaded successfully",
                "fileName", storedFileName,
                "fileSize", fileSize,
                "submissionId", submissionId,
                "version", submission.getVersion()
            ));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/download/{submissionId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String submissionId) {
        try {
            Submission submission = submissionRepository.findById(submissionId)
                .orElseThrow(() -> new RuntimeException("Submission not found"));

            if (submission.getFilePath() == null) {
                return ResponseEntity.notFound().build();
            }

            Path filePath = fileStorageService.loadFile(submission.getFilePath());
            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists() && resource.isReadable()) {
                String contentType = Files.probeContentType(filePath);
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }

                return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + submission.getFileName() + "\"")
                    .body(resource);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/download-all/{assignmentId}")
    public void downloadAllSubmissions(
            @PathVariable String assignmentId,
            HttpServletResponse response) throws IOException {
        
        List<Submission> submissions = submissionRepository.findByAssignmentId(assignmentId);
        
        if (submissions.isEmpty()) {
            response.setStatus(HttpStatus.NOT_FOUND.value());
            return;
        }

        response.setContentType("application/zip");
        response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
            "attachment; filename=\"submissions_" + assignmentId + ".zip\"");

        try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream())) {
            for (Submission submission : submissions) {
                if (submission.getFilePath() != null) {
                    try {
                        Path filePath = fileStorageService.loadFile(submission.getFilePath());
                        if (Files.exists(filePath)) {
                            ZipEntry zipEntry = new ZipEntry(submission.getFileName());
                            zos.putNextEntry(zipEntry);
                            Files.copy(filePath, zos);
                            zos.closeEntry();
                        }
                    } catch (IOException e) {
                        // Skip files that can't be read
                        System.err.println("Error adding file to zip: " + submission.getFileName());
                    }
                }
            }
        }
    }
}

