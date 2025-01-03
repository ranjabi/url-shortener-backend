package com.ranjabi.urlshortener.url;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ranjabi.urlshortener.authentication.UserAdapter;
import com.ranjabi.urlshortener.dto.response.ErrorResponse;
import com.ranjabi.urlshortener.dto.response.Response;
import com.ranjabi.urlshortener.dto.response.SuccessResponse;
import com.ranjabi.urlshortener.entities.Url;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(path = "/urls")
public class UrlController {
    private final UrlService urlService;

    public UrlController(UrlService urlService) {
        this.urlService = urlService;
    }

    @Operation(summary = "Get all user's short codes", security = { @SecurityRequirement(name = "bearer-key") })
    @ApiResponses({
            @ApiResponse(responseCode = "200", useReturnTypeSchema = true),
            @ApiResponse(responseCode = "401", description = "Unauthorized access", content = @Content) })
    @GetMapping
    public ResponseEntity<Response<List<Url>>> getAllUrls() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserAdapter principal = (UserAdapter) authentication.getPrincipal();
        List<Url> urls = urlService.getAllUrlsByUserId(principal.getUser().getId());

        return ResponseEntity.ok(SuccessResponse.ofBody(urls));
    }

    @GetMapping("/{shortCode}")
    public ResponseEntity<Object> redirectToOriginalUrl(@PathVariable String shortCode, HttpServletResponse response)
            throws IOException {
        try {
            String originalUrl = urlService.getOriginalUrl(shortCode);
            response.sendRedirect(originalUrl);

            return ResponseEntity.status(HttpStatus.FOUND).build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponse.ofMessage("Url not found"));
        }
    }

    // TODO request validation
    @Operation(summary = "Add new url", security = { @SecurityRequirement(name = "bearer-key") })
    @ApiResponses({
            @ApiResponse(responseCode = "201", useReturnTypeSchema = true) })
    @PostMapping
    public ResponseEntity<Response<Url>> addNewUrl(
            @Parameter(description = "Original url to be shortened", required = true) @RequestParam String path) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Url newUrl;

        if (authentication instanceof AnonymousAuthenticationToken) {
            newUrl = urlService.save(new Url(path));
        } else {
            UserAdapter principal = (UserAdapter) authentication.getPrincipal();
            newUrl = urlService.save(new Url(path), principal.getUser());
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(SuccessResponse.of("Short url has been created", newUrl));
    }

    @GetMapping("/protected")
    public String protectedRoute() {
        return "Protected";
    }
}
