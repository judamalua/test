
package controllers.ranger;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
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

		result = new ModelAndView("curriculum/list");

		final Curriculum curriculum = this.curriculumService.findCurriculumByRangerID();
		if (curriculum != (null)) {
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
		}

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
	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Curriculum c, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(c, "curriculum.params.error");
		else
			try {
				this.curriculumService.save(c);
				result = new ModelAndView("redirect:list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(c, "curriculum.commit.error");
			}

		return result;
	}

	// Deleting --------------------------------------------

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public ModelAndView delete(@RequestParam final int curriculumId) {
		ModelAndView result;
		Curriculum c;

		c = this.curriculumService.findOne(curriculumId);
		try {
			this.curriculumService.delete(c);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:list.do");
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

		result = new ModelAndView("curriculum/edit");
		result.addObject("curriculum", curriculum);

		return result;
	}
}
