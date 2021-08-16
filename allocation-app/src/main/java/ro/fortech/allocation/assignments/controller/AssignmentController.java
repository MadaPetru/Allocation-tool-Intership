package ro.fortech.allocation.assignments.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;
import ro.fortech.allocation.assignments.dto.AssignmentRequestDto;
import ro.fortech.allocation.assignments.dto.AssignmentResponseDto;
import ro.fortech.allocation.assignments.service.AssignmentService;

import java.text.ParseException;
import java.util.List;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@CrossOrigin
public class AssignmentController implements AssignmentApi {

    private final AssignmentService assignmentService;


    @Override
    public ResponseEntity<AssignmentResponseDto> addAssignment(AssignmentRequestDto assignmentDto) throws ParseException {
        return new ResponseEntity<>(assignmentService.save(assignmentDto), HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<?> deleteAssignmentByUid(String assignmentUid) {
        assignmentService.deleteByUid(assignmentUid);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<AssignmentResponseDto> editAssignment(AssignmentRequestDto assignmentDto, String assignmentUid) throws ParseException { //, String assignmentUid
        return new ResponseEntity<>(assignmentService.update(assignmentDto, assignmentUid), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> assignmentValidation(AssignmentRequestDto assignmentDto) throws ParseException {
        assignmentService.validate(assignmentDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<List<AssignmentResponseDto>> findAssignmentsByProject(String projectUid) {
        return new ResponseEntity<>(assignmentService.findAssignmentsByProject(projectUid),HttpStatus.OK);
    }
}