
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
import services.MiscellaneousRecordService;
import services.RangerService;
import controllers.AbstractController;
import domain.Curriculum;
import domain.MiscellaneousRecord;
import domain.Ranger;

@Controller
@RequestMapping("/miscellaneousRecord/ranger")
public class MiscellaneousRecordCurriculumController extends AbstractController {

	@Autowired
	CurriculumService			curriculumService;
	@Autowired
	ActorService				actorService;
	@Autowired
	RangerService				rangerService;
	@Autowired
	MiscellaneousRecordService	miscellaneousRecordService;


	// Constructors -----------------------------------------------------------

	public MiscellaneousRecordCurriculumController() {
		super();
	}
	// Listing ---------------------------------------------------------------		

	@RequestMapping(value = "/list", method = RequestMethod.GET, params = "curriculumId")
	public ModelAndView list(@RequestParam("curriculumId") final int cID) {
		ModelAndView result;

		result = new ModelAndView("miscellaneousRecord/list");
		final Curriculum c = this.curriculumService.findOne(cID);

		final Collection<MiscellaneousRecord> professionalRecords = c.getMiscellaneousRecords();
		result.addObject("requestUri", "professionalRecord/list.do");
		result.addObject("curriculum.professionalRecord", professionalRecords);

		return result;
	}

	// Creating -------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		MiscellaneousRecord pr;

		pr = this.miscellaneousRecordService.create();
		result = this.createEditModelAndView(pr);

		return result;
	}

	// Editing ---------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int miscellaneousRecordId) {
		ModelAndView result;
		MiscellaneousRecord miscellaneousRecord;
		Ranger ranger;

		try {
			miscellaneousRecord = this.miscellaneousRecordService.findOne(miscellaneousRecordId);
			ranger = (Ranger) this.actorService.findActorByPrincipal();
			Assert.notNull(miscellaneousRecord);
			Assert.isTrue(ranger.getCurriculum().getMiscellaneousRecords().contains(miscellaneousRecord));
			result = this.createEditModelAndView(miscellaneousRecord);

		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}
	// Saving ----------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView reject(@Valid final MiscellaneousRecord miscellaneousRecord, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(miscellaneousRecord, "miscellaneousRecord.params.error");
		else
			try {
				this.miscellaneousRecordService.save(miscellaneousRecord);
				result = new ModelAndView("redirect:/curriculum/ranger/list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(miscellaneousRecord, "miscellaneousRecord.commit.error");
			}

		return result;
	}

	// Deleting --------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final MiscellaneousRecord miscellaneousRecord, final BindingResult binding) {
		ModelAndView result;

		try {
			this.miscellaneousRecordService.delete(miscellaneousRecord);
			result = new ModelAndView("redirect:/curriculum/ranger/list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(miscellaneousRecord, "miscellaneousRecord.commit.error");
		}

		return result;
	}

	// Ancillary methods -------------------------------------

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord miscellaneousRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(miscellaneousRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final MiscellaneousRecord miscellaneousRecord, final String messageCode) {

		final ModelAndView result;

		result = new ModelAndView("miscellaneousRecord/edit");
		result.addObject("miscellaneousRecord", miscellaneousRecord);

		return result;
	}
}
