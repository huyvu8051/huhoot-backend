package com.huhoot.admin.host;

import com.huhoot.dto.*;
import com.huhoot.exception.UsernameExistedException;
import lombok.AllArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("admin")
@AllArgsConstructor
public class ManageHostController {

    private final ManageHostService manageHostService;

    private final VDataTablePagingConverter vDataTablePagingConverter;


    @PostMapping("/host/findAll")
    public ResponseEntity<PageResponse<HostResponse>> getAll(@RequestBody VDataTablePagingRequest pagingRequest) {

        Pageable pageable1 = vDataTablePagingConverter.toPageable(pagingRequest);

        Pageable pageable = PageRequest.of(pagingRequest.getPage() - 1, pagingRequest.getItemsPerPage(), Sort.Direction.fromString("DESC"), "createdDate");

        return ResponseEntity.ok(manageHostService.findAllHostAccount(pageable1));

    }


    @GetMapping("/host/search")
    public ResponseEntity<PageResponse<HostResponse>> search(@Length(min = 1, max = 15) @RequestParam String username,
                                                             @RequestParam(defaultValue = "1") int page,
                                                             @RequestParam(defaultValue = "12") int size,
                                                             @RequestParam(defaultValue = "createdDate") String sortBy,
                                                             @RequestParam(defaultValue = "DESC") String direction) {

        Pageable pageable = PageRequest.of(page - 1, size, Sort.Direction.fromString(direction), sortBy);

        return ResponseEntity.ok(manageHostService.searchHostAccountByUsername(username, pageable));

    }

    @PostMapping("/host")
    public ResponseEntity<List<HostAddErrorResponse>> addMany(@RequestBody List<HostAddRequest> request) {

        return ResponseEntity.ok(manageHostService.addManyHostAccount(request));

    }

    @PatchMapping("/host")
    public void update(@Valid @RequestBody HostUpdateRequest request) throws UsernameExistedException {

        manageHostService.updateHostAccount(request);
    }



}
