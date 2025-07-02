package com.example.Autodrive.controller;


import com.example.Autodrive.Requests.CourseRequestDTO;
import com.example.Autodrive.Requests.DriverStatusUpdateRequest;
import com.example.Autodrive.model.Course;
import com.example.Autodrive.model.Driver;
import com.example.Autodrive.repository.DriverRepository;
import com.example.Autodrive.service.CourseService;
import com.mongodb.client.model.geojson.Point;
import com.mongodb.client.model.geojson.Position;
import lombok.AllArgsConstructor;
import org.springframework.boot.origin.Origin;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final DriverRepository driverRepository;

    @PostMapping("/request")
    public ResponseEntity<?> requestCourse(@RequestBody CourseRequestDTO dto) {
        GeoJsonPoint depart = new GeoJsonPoint(dto.getDepart().getLongitude(), dto.getDepart().getLatitude());
        GeoJsonPoint destination = new GeoJsonPoint(dto.getDestination().getLongitude(), dto.getDestination().getLatitude());

        Course course = courseService.requestCourse(dto.getUserId(), depart, destination);

        if (course == null) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Aucun chauffeur trouvé à proximité.");
            return ResponseEntity.ok(response);
        }
        return ResponseEntity.ok(course);
    }



    @GetMapping("/for-driver/{driverId}")
    public ResponseEntity<List<Course>> getCoursesForDriver(
            @PathVariable String driverId,
            @RequestParam double latitude,
            @RequestParam double longitude) {

        List<Course> courses = courseService.getCoursesForDriver(driverId, latitude, longitude);
        return ResponseEntity.ok(courses);
    }

    @PutMapping("/accept/{id}")
    public ResponseEntity<Course> acceptCourse(@PathVariable String id,
                                               @RequestParam String driverId) {
        return ResponseEntity.ok(courseService.acceptCourse(id, driverId));
    }
    @GetMapping("/status/{userId}")
    public ResponseEntity<Course> getStatus(@PathVariable String userId) {
        return ResponseEntity.ok(courseService.getLatestCourseStatus(userId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Course> getByIdWithVoiture(@PathVariable String id) {
        Course course = courseService.getByIdWithVoiture(id);
        return course != null ? ResponseEntity.ok(course) : ResponseEntity.notFound().build();
    }



}
