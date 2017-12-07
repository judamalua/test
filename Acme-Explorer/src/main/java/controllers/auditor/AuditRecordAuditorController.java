
package controllers.auditor;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.AuditRecordService;
import services.AuditorService;
import controllers.AbstractController;
import domain.AuditRecord;

@Controller
@RequestMapping("/auditRecord/auditor")
public class AuditRecordAuditorController extends AbstractController {

	@Autowired
	AuditorService		auditorService;
	@Autowired
	ActorService		actorService;
	@Autowired
	AuditRecordService	auditRecordService;


	// Constructors -----------------------------------------------------------

	public AuditRecordAuditorController() {
		super();
	}
	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;

		result = new ModelAndView("auditRecord/list");

		final Collection<AuditRecord> auditRecords = this.auditRecordService.findRecordsByAuditorID();
		result.addObject("auditRecords", auditRecords);
		result.addObject("requestUri", "auditRecord/auditor/list.do");

		return result;
	}

	// Creating -------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		AuditRecord auditRecord;

		auditRecord = this.auditRecordService.create();
		result = this.createEditModelAndView(auditRecord);

		return result;
	}

	// Editing --------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int auditRecordId) {
		ModelAndView result;
		AuditRecord auditRecord;

		auditRecord = this.auditRecordService.findOne(auditRecordId);
		Assert.notNull(auditRecord);

		result = this.createEditModelAndView(auditRecord);

		return result;
	}

	// Saving --------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveDraft")
	public ModelAndView saveDraft(@Valid final AuditRecord auditRecord, final BindingResult binding) {

		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(auditRecord);
		else
			try {
				this.auditRecordService.save(auditRecord);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(auditRecord, "auditRecord.commit.error");

			}

		return result;
	}

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "saveFinal")
	public ModelAndView saveFinal(@Valid final AuditRecord auditRecord, final BindingResult binding) {
		auditRecord.setIsFinalMode(true);
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(auditRecord);
		else
			try {
				this.auditRecordService.save(auditRecord);
				result = new ModelAndView("redirect:list.do");
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(auditRecord, "auditRecord.commit.error");

			}

		return result;
	}

	// Deleting --------------------------------------------

	@RequestMapping(value = "edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final AuditRecord auditRecord, final BindingResult binding) {
		ModelAndView result;

		try {
			this.auditRecordService.delete(auditRecord);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(auditRecord, "auditRecord.commit.error");
		}

		return result;
	}

	// Ancillary methods -------------------------------------

	protected ModelAndView createEditModelAndView(final AuditRecord auditRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(auditRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final AuditRecord auditRecord, final String messageCode) {

		final ModelAndView result;

		result = new ModelAndView("auditRecord/edit");
		result.addObject("auditRecord", auditRecord);

		return result;
	}
}
