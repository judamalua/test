
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
import services.RangerService;
import controllers.AbstractController;
import domain.Curriculum;
import domain.EducationRecord;
import domain.EndorserRecord;
import domain.MiscellaneousRecord;
import domain.PersonalRecord;
import domain.ProfessionalRecord;
import domain.Ranger;

@Controller
@RequestMapping("/curriculum")
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

	@RequestMapping("/ranger/list")
	public ModelAndView list() {
		ModelAndView result;
		final Collection<ProfessionalRecord> professional;
		final PersonalRecord personal;
		final Collection<EndorserRecord> endorser;
		final Collection<MiscellaneousRecord> miscellaneous;
		final Collection<EducationRecord> education;
		final Boolean curriculumRanger = true;

		result = new ModelAndView("curriculum/list");
		result.addObject("curriculumRanger", curriculumRanger);

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

	@RequestMapping(value = "/show", method = RequestMethod.GET)
	public ModelAndView listWithoutRangerLogin(@RequestParam final int rangerId, @RequestParam(defaultValue = "false") final Boolean isRanger) {
		ModelAndView result;
		final Collection<ProfessionalRecord> professional;
		final PersonalRecord personal;
		final Collection<EndorserRecord> endorser;
		final Collection<MiscellaneousRecord> miscellaneous;
		final Collection<EducationRecord> education;
		Boolean curriculumRanger = false;
		final Curriculum curriculum;

		try {

			result = new ModelAndView("curriculum/list");

			final Ranger ranger = this.rangerService.findOne(rangerId);
			if (ranger.getCurriculum() != null)
				curriculum = this.curriculumService.findOne(ranger.getCurriculum().getId());
			else
				curriculum = null;
			Ranger r1;
			if (isRanger) {
				r1 = (Ranger) this.actorService.findActorByPrincipal();
				if (r1.equals(ranger))
					curriculumRanger = true;
			}
			result.addObject("curriculumRanger", curriculumRanger);
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

			result.addObject("requestUri", "curriculum/show.do");

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
	// Creating -------------------------------------

	@RequestMapping(value = "/ranger/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Curriculum curriculum;

		curriculum = this.curriculumService.create();
		result = this.createEditModelAndView(curriculum);

		return result;
	}

	// Editing ---------------------------------------------

	// Saving ----------------------------------------------
	@RequestMapping(value = "/ranger/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Curriculum c, final BindingResult binding) {
		ModelAndView result;
		Ranger ranger;

		if (binding.hasErrors())
			result = this.createEditModelAndView(c, "curriculum.params.error");
		else
			try {
				ranger = (Ranger) this.actorService.findActorByPrincipal();
				result = new ModelAndView("redirect:list.do");
				Assert.isTrue(c.getRanger().equals(ranger));

				this.curriculumService.save(c);
			} catch (final Throwable oops) {
				result = this.createEditModelAndView(c, "curriculum.commit.error");
			}

		return result;
	}

	// Deleting --------------------------------------------

	@RequestMapping(value = "/ranger/delete", method = RequestMethod.GET)
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
