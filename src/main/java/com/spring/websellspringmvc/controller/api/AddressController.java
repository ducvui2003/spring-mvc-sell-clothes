package com.spring.websellspringmvc.controller.api;

import com.spring.websellspringmvc.controller.exception.AppException;
import com.spring.websellspringmvc.controller.exception.ErrorCode;
import com.spring.websellspringmvc.dto.ApiResponse;
import com.spring.websellspringmvc.dto.request.AddressRequest;
import com.spring.websellspringmvc.dto.response.AddressResponse;
import com.spring.websellspringmvc.services.address.AddressServices;
import com.spring.websellspringmvc.session.SessionManager;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/user/address")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AddressController {
    AddressServices addressServices;
    SessionManager sessionManager;

    @GetMapping
    public ResponseEntity<ApiResponse<List<AddressResponse>>> getAll() {
        int userId = sessionManager.getUser().getId();
        List<AddressResponse> addressList = addressServices.getAddress(userId);
        return ResponseEntity.ok(new ApiResponse<>(HttpServletResponse.SC_OK, "Get address", addressList));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<?>> addAddress(@RequestBody @Valid AddressRequest request) throws URISyntaxException, IOException {
        int userId = sessionManager.getUser().getId();
        boolean isValid = addressServices.validate(request);
        if (!isValid) {
            throw new AppException(ErrorCode.NOT_VALID);
        }
        addressServices.createAddress(request, userId);
        return ResponseEntity.ok(new ApiResponse<>(HttpServletResponse.SC_OK, "Add address", null));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> updateAddress(@RequestBody @Valid AddressRequest request, @PathVariable("id") int addressId) throws IOException, URISyntaxException {
        int userId = sessionManager.getUser().getId();
        boolean isValid = addressServices.validate(request);
        if (!isValid) {
            throw new AppException(ErrorCode.NOT_VALID);
        }
        addressServices.updateAddress(request, userId, addressId);
        return ResponseEntity.ok(new ApiResponse<>(HttpServletResponse.SC_OK, "Add address", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<?>> deleteAddress(@PathVariable("id") Integer addressId) {
        int userId = sessionManager.getUser().getId();
        addressServices.deleteAddress(addressId, userId);
        return ResponseEntity.ok(new ApiResponse<>(HttpServletResponse.SC_OK, "Delete address", null));
    }
}
