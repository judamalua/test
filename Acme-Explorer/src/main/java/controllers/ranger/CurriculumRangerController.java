
package controllers.ranger;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.CurriculumService;
import services.RangerService;
import controllers.AbstractController;
import domain.Curriculum;
import domain.EducationRecord;
import domain.EndorserRecord;
import domain.MiscellaneousRecord;
import domain.PersonalRecord;
import domain.ProfessionalRecord;

@Controller
@RequestMapping("/curriculum/ranger")
public class CurriculumRangerController extends AbstractController {

	@Autowired
	CurriculumService	curriculumService;
	@Autowired
	ActorService		actorService;
	@Autowired
	RangerService		rangerService;


	// Constructors -----------------------------------------------------------

	public CurriculumRangerController() {
		super();
	}
	// Listing ---------------------------------------------------------------		

	@RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView result;
		final Collection<ProfessionalRecord> professional;
		final PersonalRecord personal;
		final Collection<EndorserRecord> endorser;
		final Collection<MiscellaneousRecord> miscellaneous;
		final Collection<EducationRecord> education;

		result = new ModelAndView("curriculum/ranger/list");

		final Curriculum curriculum = this.curriculumService.findCurriculumByRangerID();
		professional = curriculum.getProfessionalRecords();
		endorser = curriculum.getEndorserRecords();
		personal = curriculum.getPersonalRecord();
		education = curriculum.getEducationRecords();
		miscellaneous = curriculum.getMiscellaneousRecords();

		result.addObject("curriculum", curriculum);
		result.addObject("professionalRecords", professional);
		result.addObject("endorserRecords", endorser);
		result.addObject("educationRecords", education);
		result.addObject("personalRecord", personal);
		result.addObject("miscellaneousRecords", miscellaneous);

		result.addObject("requestUri", "curriculum/ranger/list.do");

		return result;
	}

	// Creating -------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Curriculum curriculum;

		curriculum = this.curriculumService.create();
		result = this.createEditModelAndView(curriculum);

		return result;
	}

	// Editing ---------------------------------------------

	// Saving ----------------------------------------------

	// Deleting --------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Curriculum curriculum, final BindingResult binding) {
		ModelAndView result;

		try {
			this.curriculumService.delete(curriculum);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(curriculum, "curriculum.commit.error");
		}

		return result;
	}

	// Ancillary methods -------------------------------------

	protected ModelAndView createEditModelAndView(final Curriculum curriculum) {
		ModelAndView result;

		result = this.createEditModelAndView(curriculum, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Curriculum curriculum, final String messageCode) {

		final ModelAndView result;

		result = new ModelAndView("curriculum/ranger/edit");
		result.addObject("curriculum", curriculum);

		return result;
	}
}
