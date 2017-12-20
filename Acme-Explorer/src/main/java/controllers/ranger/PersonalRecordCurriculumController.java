
package controllers.ranger;

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
import services.PersonalRecordService;
import services.RangerService;
import controllers.AbstractController;
import domain.Curriculum;
import domain.PersonalRecord;
import domain.Ranger;

@Controller
@RequestMapping("/personalRecord/ranger")
public class PersonalRecordCurriculumController extends AbstractController {

	@Autowired
	CurriculumService		curriculumService;
	@Autowired
	ActorService			actorService;
	@Autowired
	RangerService			rangerService;
	@Autowired
	PersonalRecordService	personalRecordService;


	// Constructors -----------------------------------------------------------

	public PersonalRecordCurriculumController() {
		super();
	}
	// Listing ---------------------------------------------------------------		

	@RequestMapping(value = "/list", method = RequestMethod.GET, params = "curriculumId")
	public ModelAndView list(@RequestParam("curriculumId") final int cID) {
		ModelAndView result;

		result = new ModelAndView("personalRecord/list");
		final Curriculum c = this.curriculumService.findOne(cID);

		final PersonalRecord personalRecord = c.getPersonalRecord();
		result.addObject("requestUri", "personalRecord/list.do");
		result.addObject("curriculum.personalRecord", personalRecord);

		return result;
	}

	// Creating -------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		PersonalRecord pr;

		pr = this.personalRecordService.create();
		result = this.createEditModelAndView(pr);

		return result;
	}

	// Editing ---------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int personalRecordId) {
		ModelAndView result;
		PersonalRecord personalRecord;
		Ranger ranger;

		try {
			personalRecord = this.personalRecordService.findOne(personalRecordId);
			ranger = (Ranger) this.actorService.findActorByPrincipal();
			Assert.notNull(personalRecord);
			Assert.isTrue(ranger.getCurriculum().getPersonalRecord().equals(personalRecord));
			result = this.createEditModelAndView(personalRecord);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
	// Saving ----------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView reject(@Valid final PersonalRecord personalRecord, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(personalRecord, "personalRecord.params.error");
		else
			try {
				this.personalRecordService.save(personalRecord);
				result = new ModelAndView("redirect:/curriculum/ranger/list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(personalRecord, "personalRecord.commit.error");
			}

		return result;
	}

	// Deleting --------------------------------------------

	// Ancillary methods -------------------------------------

	protected ModelAndView createEditModelAndView(final PersonalRecord personalRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(personalRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final PersonalRecord personalRecord, final String messageCode) {

		final ModelAndView result;

		result = new ModelAndView("personalRecord/edit");
		result.addObject("personalRecord", personalRecord);

		return result;
	}
}
