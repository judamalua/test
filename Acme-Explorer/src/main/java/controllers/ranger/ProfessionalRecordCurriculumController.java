
package controllers.ranger;

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
import services.CurriculumService;
import services.ProfessionalRecordService;
import services.RangerService;
import controllers.AbstractController;
import domain.Curriculum;
import domain.ProfessionalRecord;
import domain.Ranger;

@Controller
@RequestMapping("/professionalRecord/ranger")
public class ProfessionalRecordCurriculumController extends AbstractController {

	@Autowired
	CurriculumService			curriculumService;
	@Autowired
	ActorService				actorService;
	@Autowired
	RangerService				rangerService;
	@Autowired
	ProfessionalRecordService	professionalRecordService;


	// Constructors -----------------------------------------------------------

	public ProfessionalRecordCurriculumController() {
		super();
	}
	// Listing ---------------------------------------------------------------		

	@RequestMapping(value = "/list", method = RequestMethod.GET, params = "curriculumId")
	public ModelAndView list(@RequestParam("curriculumId") final int cID) {
		ModelAndView result;

		result = new ModelAndView("professionalRecord/list");
		final Curriculum c = this.curriculumService.findOne(cID);

		final Collection<ProfessionalRecord> professionalRecords = c.getProfessionalRecords();
		result.addObject("requestUri", "professionalRecord/list.do");
		result.addObject("curriculum.professionalRecord", professionalRecords);

		return result;
	}

	// Creating -------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		ProfessionalRecord pr;

		pr = this.professionalRecordService.create();
		result = this.createEditModelAndView(pr);

		return result;
	}

	// Editing ---------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int professionalRecordId) {
		ModelAndView result;
		ProfessionalRecord professionalRecord;
		Ranger ranger;

		try {
			professionalRecord = this.professionalRecordService.findOne(professionalRecordId);
			ranger = (Ranger) this.actorService.findActorByPrincipal();
			Assert.notNull(professionalRecord);
			Assert.isTrue(ranger.getCurriculum().getProfessionalRecords().contains(professionalRecord));
			result = this.createEditModelAndView(professionalRecord);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
	// Saving ----------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView reject(@Valid final ProfessionalRecord professionalRecord, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(professionalRecord, "professionalRecord.params.error");
		else
			try {
				this.professionalRecordService.save(professionalRecord);
				result = new ModelAndView("redirect:/curriculum/ranger/list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(professionalRecord, "professionalRecord.commit.error");
			}

		return result;
	}

	// Deleting --------------------------------------------

	@RequestMapping(value = "edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final ProfessionalRecord professionalRecord, final BindingResult binding) {
		ModelAndView result;

		try {
			this.professionalRecordService.delete(professionalRecord);
			result = new ModelAndView("redirect:/curriculum/ranger/list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(professionalRecord, "professionalRecord.commit.error");
		}

		return result;
	}

	// Ancillary methods -------------------------------------

	protected ModelAndView createEditModelAndView(final ProfessionalRecord professionalRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(professionalRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final ProfessionalRecord professionalRecord, final String messageCode) {

		final ModelAndView result;

		result = new ModelAndView("professionalRecord/edit");
		result.addObject("professionalRecord", professionalRecord);

		return result;
	}
}
