
package controllers.manager;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AuditRecordService;
import services.ManagerService;
import services.TripService;
import controllers.AbstractController;
import domain.AuditRecord;

@Controller
@RequestMapping("/auditRecord/manager")
public class AuditRecordManagerController extends AbstractController {

	@Autowired
	ManagerService		managerService;
	@Autowired
	ActorService		actorService;
	@Autowired
	AuditRecordService	auditRecordService;
	@Autowired
	TripService			tripService;


	// Constructors -----------------------------------------------------------

	public AuditRecordManagerController() {
		super();
	}
	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;

		result = new ModelAndView("auditRecord/list");

		final Collection<AuditRecord> auditRecords = this.auditRecordService.findAllAuditsByManagerID();
		result.addObject("auditRecords", auditRecords);
		result.addObject("requestUri", "auditRecord/manager/list.do");

		return result;
	}
}
