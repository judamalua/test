
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.ApplicationService;
import services.LegalTextService;
import services.ManagerService;
import services.NoteService;
import services.RangerService;
import services.TripService;
import controllers.AbstractController;
import domain.Trip;

@Controller
@RequestMapping("/dashboard/admin")
public class DashboardAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	TripService			tripService;

	@Autowired
	ApplicationService	applicationService;

	@Autowired
	NoteService			noteService;

	@Autowired
	ManagerService		managerService;

	@Autowired
	RangerService		rangerService;

	@Autowired
	LegalTextService	legalTextService;


	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list() {
		final ModelAndView result;
		final String ratioApplicationsPending, ratioApplicationsDue, ratioApplicationsAccepted, ratioApplicationsCancelled, ratioTripsCancelled, ratioAuditRecordTrip, ratioRangerCurricula, ratioRangerEndorsedCurricula, ratioSuspiciousManager, ratioSuspiciousRangers;
		final String applicationsPerTrip;
		final String numberNotesPerTrip, auditRecordsPerTrip;
		final String tripsManagedPerManager, tripsPrice, tripsGuidedPerRanger;
		final Collection<Trip> tripsMoreApplications;
		Collection<String> numberReferencesLegalText;

		applicationsPerTrip = this.tripService.getApplicationsInfoFromTrips();
		tripsManagedPerManager = this.managerService.getTripsInfoFromManager();
		tripsPrice = this.tripService.getPriceInfoFromTrips();
		tripsGuidedPerRanger = this.rangerService.getTripsInfoFromRanger();

		ratioApplicationsPending = this.applicationService.getRatioPendingApplications();
		ratioApplicationsDue = this.applicationService.getRatioDueApplications();
		ratioApplicationsAccepted = this.applicationService.getRatioAcceptedApplications();
		ratioApplicationsCancelled = this.applicationService.getRatioCancelledApplications();

		ratioTripsCancelled = this.tripService.getRatioTripsCancelled();
		tripsMoreApplications = this.tripService.getTripsTenPercentMoreApplications();
		numberReferencesLegalText = this.tripService.getNumberOfReferencesLegalTexts();

		numberNotesPerTrip = this.tripService.getInfoNotesFromTrips();
		auditRecordsPerTrip = this.tripService.getInfoAuditsFromTrips();
		ratioAuditRecordTrip = this.tripService.getRatioAuditsFromTrips();
		ratioRangerCurricula = this.rangerService.getRatioRangersWithCurriculum();
		ratioRangerEndorsedCurricula = this.rangerService.getRatioEndorsersRangers();

		ratioSuspiciousManager = this.managerService.getRatioSuspiciousManagers();
		ratioSuspiciousRangers = this.rangerService.getRatioSuspiciousRangers();

		result = new ModelAndView("dashboard/list");
		result.addObject("applicationAverage", applicationsPerTrip.split(",")[2]);
		result.addObject("applicationMinimum", applicationsPerTrip.split(",")[0]);
		result.addObject("applicationMaximum", applicationsPerTrip.split(",")[1]);
		result.addObject("applicationStandardDeviation", applicationsPerTrip.split(",")[3]);

		result.addObject("tripManagerAverage", tripsManagedPerManager.split(",")[2]);
		result.addObject("tripManagerMinimum", tripsManagedPerManager.split(",")[0]);
		result.addObject("tripManagerMaximum", tripsManagedPerManager.split(",")[1]);
		result.addObject("tripManagerStandardDeviation", tripsManagedPerManager.split(",")[3]);

		result.addObject("tripPriceAverage", tripsPrice.split(",")[2]);
		result.addObject("tripPriceMinimum", tripsPrice.split(",")[0]);
		result.addObject("tripPriceMaximum", tripsPrice.split(",")[1]);
		result.addObject("tripPriceStandardDeviation", tripsPrice.split(",")[3]);

		result.addObject("tripsMoreApplications", tripsMoreApplications);
		result.addObject("numberReferencesLegalText", numberReferencesLegalText);

		result.addObject("tripRangerAverage", tripsGuidedPerRanger.split(",")[2]);
		result.addObject("tripRangerMinimum", tripsGuidedPerRanger.split(",")[0]);
		result.addObject("tripRangerMaximum", tripsGuidedPerRanger.split(",")[1]);
		result.addObject("tripRangerStandardDeviation", tripsGuidedPerRanger.split(",")[3]);

		result.addObject("ratioApplicationPending", ratioApplicationsPending);
		result.addObject("ratioApplicationDue", ratioApplicationsDue);
		result.addObject("ratioApplicationAccepted", ratioApplicationsAccepted);
		result.addObject("ratioApplicationCancelled", ratioApplicationsCancelled);

		result.addObject("tripRangerAverage", ratioTripsCancelled);
		result.addObject("numTrips", this.tripService.findAll().size());

		result.addObject("tripNoteAverage", numberNotesPerTrip.split(",")[2]);
		result.addObject("tripNoteMinimum", numberNotesPerTrip.split(",")[0]);
		result.addObject("tripRangerMaximum", numberNotesPerTrip.split(",")[1]);
		result.addObject("tripRangerStandardDeviation", numberNotesPerTrip.split(",")[3]);

		result.addObject("tripAuditRecordAverage", auditRecordsPerTrip.split(",")[2]);
		result.addObject("tripAuditRecordMinimum", auditRecordsPerTrip.split(",")[2]);
		result.addObject("tripAuditRecordMaximum", auditRecordsPerTrip.split(",")[1]);
		result.addObject("tripAuditRecordStandardDeviation", auditRecordsPerTrip.split(",")[3]);

		result.addObject("tripAuditRecordRatio", ratioAuditRecordTrip);
		result.addObject("CurriculaRatio", ratioRangerCurricula);
		result.addObject("EndorsedCurriculaRatio", ratioRangerEndorsedCurricula);
		result.addObject("ratioSuspiciousManager", ratioSuspiciousManager);
		result.addObject("ratioSuspiciousRanger", ratioSuspiciousRangers);

		return result;
	}
}
