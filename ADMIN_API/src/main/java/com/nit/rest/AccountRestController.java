package com.nit.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.nit.model.UnlockAccForm;
import com.nit.model.UserAccountForm;
import com.nit.services.IAccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("/Account")
public class AccountRestController {

	@Autowired
	private IAccountService accService;

	private Logger logger = LoggerFactory.getLogger(AccountRestController.class);

	// ------------------------------------------------------------------------------------------

	/**
	 * Creates a new user account.
	 *
	 * @param form the user account form containing registration details
	 * @return ResponseEntity with appropriate HTTP status and message
	 */
	@Operation(summary = "Create a new user account", description = "Creates a new user account with the provided registration details.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Account registration successfully completed."),
			@ApiResponse(responseCode = "400", description = "Invalid request data."),
			@ApiResponse(responseCode = "409", description = "Email is already registered."),
			@ApiResponse(responseCode = "500", description = "Internal Server Error.") })
	@PostMapping("/create")
	public ResponseEntity<String> createAccount(@RequestBody UserAccountForm form) {
		if (form == null) {
			logger.warn("Received null form for account creation");
			return new ResponseEntity<>("Invalid request data", HttpStatus.BAD_REQUEST);
		}

		String email = form.getEmailId().toLowerCase();
		logger.info("Attempting to create account for email: {}", email);

		if (accService.findEmail(email)) {
			logger.info("Email {} is already registered", email);
			return new ResponseEntity<>("Email is already registered! Please sign in", HttpStatus.CONFLICT);
		}

		boolean userCreated = accService.creatUserAcc(form);
		if (userCreated) {
			logger.info("Account registration successful for email: {}", email);
			return new ResponseEntity<>("Account registration successfully completed.", HttpStatus.CREATED);
		}

		logger.error("Account registration failed for email: {}", email);
		return new ResponseEntity<>("Internal Server Error! Please try again later", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// ------------------------------------------------------------------------------------------

	/**
	 * Retrieves all user accounts with pagination.
	 *
	 * @param page the page number to retrieve (default is 0)
	 * @return ResponseEntity containing a page of UserAccountForms or a message if
	 *         no data is found
	 */
	@Operation(summary = "Get all user accounts", description = "Retrieves a paginated list of all user accounts.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User accounts retrieved successfully."),
			@ApiResponse(responseCode = "204", description = "No data found.") })
	@GetMapping("/all")
	public ResponseEntity<?> getAllAccounts(@RequestParam(defaultValue = "0") Integer page) {
		logger.debug("Fetching User Accounts process started for page: {}", page);

		Page<UserAccountForm> allUserAcc = accService.getAllUserAcc(page);

		if (allUserAcc != null && allUserAcc.hasContent()) {
			logger.info("Successfully retrieved user accounts for page: {}", page);
			return new ResponseEntity<>(allUserAcc, HttpStatus.OK);
		} else {
			logger.warn("No data found for page: {}", page);
			return new ResponseEntity<>("No Data found", HttpStatus.NO_CONTENT);
		}
	}

	// ------------------------------------------------------------------------------------------

	/**
	 * Retrieves a user account by user ID.
	 *
	 * @param userId the ID of the user
	 * @return ResponseEntity containing the UserAccountForm and appropriate HTTP
	 *         status
	 */
	@Operation(summary = "Get user account by ID", description = "Retrieves a user account based on the provided user ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User account fetched successfully."),
			@ApiResponse(responseCode = "404", description = "User account not found.") })
	@GetMapping("/user/{userId}")
	public ResponseEntity<?> getUser(@PathVariable Integer userId) {
		logger.debug("Fetching user account for userId: {}", userId);
		UserAccountForm userAccount = accService.getUserAccByID(userId);

		if (userAccount == null) {
			logger.warn("User account not found for userId: {}", userId);
			return new ResponseEntity<>("User Not found!!!", HttpStatus.OK);
		}

		logger.info("User account fetched successfully for userId: {}", userId);
		return new ResponseEntity<>(userAccount, HttpStatus.OK);
	}

	// ------------------------------------------------------------------------------------------

	
	 /**
     * Switches the status of a user account and returns the updated list of accounts.
     *
     * @param userId the ID of the user account to update
     * @param status the new status to set for the user account
     * @param page the page number to retrieve after status update
     * @return ResponseEntity containing the updated list of UserAccountForms or a message if the account is not found
     */
    @Operation(summary = "Switch user account status", 
               description = "Updates the status of a specific user account and retrieves the updated list.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User account status updated successfully."),
        @ApiResponse(responseCode = "404", description = "User account not found.")
    })
    @PutMapping("/user/{userId}/{status}")
    public ResponseEntity<?> switchAccStatus(@PathVariable("userId") Integer userId,
                                             @PathVariable("status") String status,
                                             @RequestParam(defaultValue = "0") Integer page) {
        logger.debug("User account update process started for userId: {} with status: {}", userId, status);

        boolean isUpdated = accService.switchAccountStatus(userId, status);

        if (!isUpdated) {
            logger.warn("Failed to update user account status for userId: {}", userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User account not found!");
        }

        logger.info("User account status updated successfully for userId: {}", userId);

        Page<UserAccountForm> allUserAcc = accService.getAllUserAcc(page);
        return ResponseEntity.ok(allUserAcc); // Returns the updated list of user accounts
    }

	// ------------------------------------------------------------------------------------------

	
}
