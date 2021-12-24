package com.huhoot.admin.manage.host;

import com.huhoot.exception.UsernameExistedException;
import com.huhoot.vue.vdatatable.paging.PageResponse;
import com.huhoot.vue.vdatatable.paging.VDataTablePagingConverter;
import com.huhoot.vue.vdatatable.paging.VDataTablePagingRequest;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
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

        return ResponseEntity.ok(manageHostService.findAllHostAccount(pageable1));

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
