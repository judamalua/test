
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
		ModelAndView result;
		final String ratioApplicationsPending, ratioApplicationsDue, ratioApplicationsAccepted, ratioApplicationsCancelled, ratioTripsCancelled, ratioAuditRecordTrip, ratioRangerCurricula, ratioRangerEndorsedCurricula, ratioSuspiciousManager, ratioSuspiciousRangers;
		Collection<String> applicationsPerTrip;
		final Collection<String> numberReferencesLegalText, numberNotesPerTrip, auditRecordsPerTrip;
		final Collection<String> tripsManagedPerManager, tripsPrice, tripsGuidedPerRanger;
		Collection<Trip> tripsMoreApplications;

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
		result.addObject("applicationAverage", applicationsPerTrip.toArray()[0]);
		result.addObject("applicationMinimum", applicationsPerTrip.toArray()[1]);
		result.addObject("applicationMaximum", applicationsPerTrip.toArray()[2]);
		result.addObject("applicationStandardDeviation", applicationsPerTrip.toArray()[3]);

		result.addObject("tripManagerAverage", tripsManagedPerManager.toArray()[0]);
		result.addObject("tripManagerMinimum", tripsManagedPerManager.toArray()[1]);
		result.addObject("tripManagerMaximum", tripsManagedPerManager.toArray()[2]);
		result.addObject("tripManagerStandardDeviation", tripsManagedPerManager.toArray()[3]);

		result.addObject("tripPriceAverage", tripsPrice.toArray()[0]);
		result.addObject("tripPriceMinimum", tripsPrice.toArray()[1]);
		result.addObject("tripPriceMaximum", tripsPrice.toArray()[2]);
		result.addObject("tripPriceStandardDeviation", tripsPrice.toArray()[3]);

		result.addObject("tripsMoreApplications", tripsMoreApplications);
		result.addObject("numberReferecesLegalText", numberReferencesLegalText);

		result.addObject("tripRangerAverage", tripsGuidedPerRanger.toArray()[0]);
		result.addObject("tripRangerMinimum", tripsGuidedPerRanger.toArray()[1]);
		result.addObject("tripRangerMaximum", tripsGuidedPerRanger.toArray()[2]);
		result.addObject("tripRangerStandardDeviation", tripsGuidedPerRanger.toArray()[3]);

		result.addObject("ratioApplicationPending", ratioApplicationsPending);
		result.addObject("ratioApplicationDue", ratioApplicationsDue);
		result.addObject("ratioApplicationAccepted", ratioApplicationsAccepted);
		result.addObject("ratioApplicationCancelled", ratioApplicationsCancelled);

		result.addObject("tripRangerAverage", ratioTripsCancelled);
		result.addObject("numTrips", this.tripService.findAll().size());

		result.addObject("tripNoteAverage", numberNotesPerTrip.toArray()[0]);
		result.addObject("tripNoteMinimum", numberNotesPerTrip.toArray()[1]);
		result.addObject("tripRangerMaximum", numberNotesPerTrip.toArray()[2]);
		result.addObject("tripRangerStandardDeviation", numberNotesPerTrip.toArray()[3]);

		result.addObject("tripAuditRecordAverage", auditRecordsPerTrip.toArray()[0]);
		result.addObject("tripAuditRecordMinimum", auditRecordsPerTrip.toArray()[1]);
		result.addObject("tripAuditRecordMaximum", auditRecordsPerTrip.toArray()[2]);
		result.addObject("tripAuditRecordStandardDeviation", auditRecordsPerTrip.toArray()[3]);

		result.addObject("tripAuditRecordRatio", ratioAuditRecordTrip);
		result.addObject("CurriculaRatio", ratioRangerCurricula);
		result.addObject("EndorsedCurriculaRatio", ratioRangerEndorsedCurricula);
		result.addObject("ratioSuspiciousManager", ratioSuspiciousManager);
		result.addObject("ratioSuspiciousRanger", ratioSuspiciousRangers);

		return result;
	}
}
