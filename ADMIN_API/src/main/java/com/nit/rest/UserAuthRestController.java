package com.nit.rest;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nit.entity.UserEntity;
import com.nit.model.AuthenticationResponse;
import com.nit.model.ForgotPWDForm;
import com.nit.model.LoginForm;
import com.nit.model.UnlockAccForm;
import com.nit.security.JwtUtil;
import com.nit.services.IUserAuthService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/Auth")
public class UserAuthRestController {

	@Autowired
	private IUserAuthService userAuthService;

	@Autowired
	private JwtUtil jwtUtil;

	private Logger logger = LoggerFactory.getLogger(UserAuthRestController.class);

	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/")
	public ResponseEntity<String> getmessage() {
		return new ResponseEntity<String>("IES_APPLICATION", HttpStatus.OK);
	}

	// -----------------------------------------------------------------------------------------

	@Operation(summary = "User login", description = "Authenticates user credentials and returns a JWT token.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User logged in successfully."),
			@ApiResponse(responseCode = "401", description = "Invalid credentials."),
			@ApiResponse(responseCode = "500", description = "Internal server error.") })
	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@RequestBody LoginForm form) {
		System.out.println("Login-1");
		try {
			if (form != null) {
				UserDetails user = userAuthService.login(form);

				if (passwordEncoder.matches(form.getPassword(), user.getPassword())) {
					Authentication authentication = new UsernamePasswordAuthenticationToken(user, null,
							user.getAuthorities());
					SecurityContextHolder.getContext().setAuthentication(authentication);
					String token = jwtUtil.generateToken(authentication);
					return ResponseEntity.ok(new AuthenticationResponse(token, "SignIn Sucess", LocalDateTime.now()));
				}
			}
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
		} catch (Exception e) {
			logger.error("Error during login: {}", e.getLocalizedMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error!!");
		}
	}
	// -----------------------------------------------------------------------------------------

	@Operation(summary = "Forgot password", description = "Handles password recovery requests by sending an email.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Recovery email sent."),
			@ApiResponse(responseCode = "404", description = "Email not found."),
			@ApiResponse(responseCode = "500", description = "Internal server error.") })
	@PostMapping("/forgot/password")
	public ResponseEntity<String> forgotPassword(@RequestBody ForgotPWDForm form) {
		if (form != null) {
			boolean recoverPass = userAuthService.recoverPass(form);
			return recoverPass ? new ResponseEntity<>("Please check your Email.We send your password", HttpStatus.OK)
					: new ResponseEntity<>("Email Not Found!!", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
	}
	// -----------------------------------------------------------------------------------------

	/**
	 * Unlocks a user account based on the provided form data.
	 *
	 * @param form the form containing unlock account details
	 * @return ResponseEntity containing a message and appropriate HTTP status
	 */
	@Operation(summary = "Unlock user account", description = "Unlocks a user account based on the provided details.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Account unlocked successfully."),
			@ApiResponse(responseCode = "400", description = "Account is already unlocked."),
			@ApiResponse(responseCode = "404", description = "Account not found.") })
	@PostMapping("/unlock")
	public ResponseEntity<String> unlockAccount(@RequestBody UnlockAccForm form) {
		logger.debug("Unlock account process started for user: {}", form.getEmail());

		String unlockUserAccount = userAuthService.activeAccount(form);
		HttpStatus status;
		String responseMessage;

		switch (unlockUserAccount) {
		case "Account Unlock successful":
			logger.info("User account successfully unlocked for userId: {}", form.getEmail());
			responseMessage = "Account unlocked successfully.";
			status = HttpStatus.OK; // Use 200 OK for success
			break;
		case "Your Account is already Unlock. Please SignIn":
			logger.warn("Failed to unlock account for userId: {}", form.getEmail());
			responseMessage = "Your Account is already unlocked. Please SignIn.";
			status = HttpStatus.BAD_REQUEST; // Use appropriate status
			break;
		default:
			responseMessage = "Account not found!";
			status = HttpStatus.NOT_FOUND;
			break;
		}

		return new ResponseEntity<>(responseMessage, status);
	}
}
