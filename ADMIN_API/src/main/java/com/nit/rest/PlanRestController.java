package com.nit.rest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import com.nit.entity.PlanEntity;
import com.nit.model.PlanForm;

import com.nit.services.IPlanService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/Plans")
public class PlanRestController {

	@Autowired
	private IPlanService planService;
	private Logger logger = LoggerFactory.getLogger(AccountRestController.class);

	// ------------------------------------------------------------------------------------------

	/**
	 * Creates a new account plan based on the provided details in the PlanForm.
	 *
	 * @param form The details of the plan to be created. Must include planName,
	 *             description, price, and duration.
	 * @return A ResponseEntity containing a message and the appropriate HTTP
	 *         status: - 201 Created: if the plan is successfully registered. - 400
	 *         Bad Request: if the request data is null or invalid. - 409 Conflict:
	 *         if the plan already exists. - 500 Internal Server Error: if an
	 *         unexpected error occurs during processing.
	 */
	@Operation(summary = "Create a new plan", description = "Creates a new account plan based on the provided details.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Plan registration successfully completed."),
			@ApiResponse(responseCode = "400", description = "Invalid request data."),
			@ApiResponse(responseCode = "409", description = "Plan is already registered."),
			@ApiResponse(responseCode = "500", description = "Internal Server Error.") })

	@PostMapping("/create")

	public ResponseEntity<String> createAccount(@RequestBody PlanForm form) {
		if (form == null) {
			logger.warn("Received null form for plan creation");
			return new ResponseEntity<>("Invalid request data", HttpStatus.BAD_REQUEST);
		}

		PlanEntity planByName = planService.findPlanByName(form.getPlanName());

		if (planByName != null && planByName.getPlanName().equals(form.getPlanName())) {
			logger.info("Attempted to create a plan that already exists: {}", form.getPlanName());
			return new ResponseEntity<>("Plan is already registered! Please Check it!!", HttpStatus.CONFLICT);
		}

		boolean plan = planService.createPlan(form);
		if (plan) {
			logger.info("Plan registration successful for: {}", form.getPlanName());
			return new ResponseEntity<>("Plan registration successfully completed.", HttpStatus.CREATED);
		}

		logger.error("Failed to register plan for: {}. Internal server error.", form.getPlanName());
		return new ResponseEntity<>("Internal Server Error! Please try again later", HttpStatus.INTERNAL_SERVER_ERROR);
	}

	// ------------------------------------------------------------------------------------------

	/**
	 * Retrieves all plans with pagination.
	 *
	 * @param page the page number to retrieve
	 * @return ResponseEntity containing a page of PlanForms or a message if no data
	 *         is found
	 */
	@Operation(summary = "Get all plans", description = "Retrieves a paginated list of all available plans.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Plans retrieved successfully."),
			@ApiResponse(responseCode = "204", description = "No data found."),
			@ApiResponse(responseCode = "500", description = "Internal Server Error.") })

	@GetMapping("/all")

	public ResponseEntity<?> getAllPlan(@RequestParam(defaultValue = "0") Integer page) {
		logger.debug("Fetching all plans for page: {}", page);
		Page<PlanForm> allPlan = planService.getAllPlan(page);
		if (allPlan != null && allPlan.hasContent()) {
			logger.info("Successfully retrieved plans for page: {}", page);
			return new ResponseEntity<>(allPlan, HttpStatus.OK);
		} else {
			logger.warn("No data found for page: {}", page);
			return new ResponseEntity<>("No Data found", HttpStatus.NO_CONTENT);
		}
	}

	// ------------------------------------------------------------------------------------------

	/**
	 * Retrieves a plan by its ID.
	 *
	 * @param planId the ID of the plan to retrieve
	 * @return ResponseEntity containing the PlanForm or a message if the plan is
	 *         not found
	 */
	@Operation(summary = "Get plan by ID", description = "Retrieves a specific plan based on the provided plan ID.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Plan retrieved successfully."),
			@ApiResponse(responseCode = "404", description = "Plan not found.") })

	@GetMapping("/plan/{planId}")

	public ResponseEntity<?> getUser(@PathVariable Integer planId) {
		logger.debug("Fetching plan for planId: {}", planId);
		PlanForm planByID = planService.getPlanByID(planId);

		if (planByID == null) {
			logger.warn("Plan not found for planId: {}", planId);
			return new ResponseEntity<>("Plan Not found!!!", HttpStatus.NOT_FOUND);
		}

		logger.info("Plan retrieved successfully for planId: {}", planId);
		return new ResponseEntity<>(planByID, HttpStatus.OK);
	}

	// ------------------------------------------------------------------------------------------

	/**
	 * Switches the status of a plan and returns the updated list of plans.
	 *
	 * @param planId the ID of the plan to update
	 * @param status the new status to set for the plan
	 * @param pageNo the page number to retrieve after status update
	 * @return ResponseEntity containing the updated list of PlanForms or a message
	 *         if the plan is not found
	 */
	@Operation(summary = "Switch plan status", description = "Updates the status of a specific plan based on the provided plan ID and returns the updated list.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Plan status updated successfully."),
			@ApiResponse(responseCode = "404", description = "Plan not found.") })
	@PutMapping("/plan/{planId}/{status}/{pageNo}")
	public ResponseEntity<?> switchAccStatus(@PathVariable("planId") Integer planId,
			@PathVariable("status") String status, @PathVariable Integer pageNo) {
		logger.debug("Switching status for planId: {} to status: {}", planId, status);

		boolean switchPlanStatus = planService.switchPlanStatus(planId, status);

		if (!switchPlanStatus) {
			logger.warn("Plan not found for planId: {}", planId);
			return new ResponseEntity<>("Plan Not found!!!", HttpStatus.NOT_FOUND);
		}

		Page<PlanForm> allPlan = planService.getAllPlan(pageNo);
		logger.info("Plan status updated successfully for planId: {}", planId);
		return new ResponseEntity<>(allPlan, HttpStatus.OK); // Returns the updated list of plans
	}

	// ------------------------------------------------------------------------------------------

	/**
	 * Deletes a plan by its ID and retrieves the updated list of plans.
	 *
	 * @param planId the ID of the plan to delete
	 * @param page   the page number to retrieve
	 * @return ResponseEntity containing the updated list of PlanForms or a message
	 *         if no data is found
	 */
	@Operation(summary = "Delete a plan", description = "Deletes a specific plan based on the provided plan ID and returns the updated list.")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Plan deleted successfully."),
			@ApiResponse(responseCode = "404", description = "No data found.") })
	@GetMapping("/delete/{planId}")
	public ResponseEntity<?> deletePlan(@PathVariable Integer planId, @RequestParam(defaultValue = "0") Integer page) {
		logger.debug("Deleting plan for planId: {} on page: {}", planId, page);
		Page<PlanForm> deletePlan = planService.deletePlan(planId, page);

		if (deletePlan == null || deletePlan.isEmpty()) {
			logger.warn("No data found for planId: {}", planId);
			return new ResponseEntity<>("No Data found!!", HttpStatus.NOT_FOUND);
		} else {
			logger.info("Plan deleted successfully for planId: {}", planId);
			return new ResponseEntity<>(deletePlan, HttpStatus.OK);
		}
	}

}
