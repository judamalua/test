
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
import services.EndorserRecordService;
import services.RangerService;
import controllers.AbstractController;
import domain.Curriculum;
import domain.EndorserRecord;

@Controller
@RequestMapping("/endorserRecord/ranger")
public class EndorserRecordCurriculumController extends AbstractController {

	@Autowired
	CurriculumService		curriculumService;
	@Autowired
	ActorService			actorService;
	@Autowired
	RangerService			rangerService;
	@Autowired
	EndorserRecordService	endorserRecordService;


	// Constructors -----------------------------------------------------------

	public EndorserRecordCurriculumController() {
		super();
	}
	// Listing ---------------------------------------------------------------		

	@RequestMapping(value = "/list", method = RequestMethod.GET, params = "curriculumId")
	public ModelAndView list(@RequestParam("curriculumId") final int cID) {
		ModelAndView result;

		result = new ModelAndView("endorserRecord/list");
		final Curriculum c = this.curriculumService.findOne(cID);

		final Collection<EndorserRecord> endorserRecords = c.getEndorserRecords();
		result.addObject("requestUri", "endorserRecord/list.do");
		result.addObject("curriculum.endorserRecord", endorserRecords);

		return result;
	}

	// Creating -------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		EndorserRecord pr;

		pr = this.endorserRecordService.create();
		result = this.createEditModelAndView(pr);

		return result;
	}

	// Editing ---------------------------------------------
	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int endorserRecordId) {
		ModelAndView result;
		EndorserRecord endorserRecord;

		endorserRecord = this.endorserRecordService.findOne(endorserRecordId);
		Assert.notNull(endorserRecord);

		result = this.createEditModelAndView(endorserRecord);

		return result;
	}
	// Saving ----------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView reject(@Valid final EndorserRecord endorserRecord, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(endorserRecord, "endorserRecord.params.error");
		else
			try {
				this.endorserRecordService.save(endorserRecord);
				result = new ModelAndView("redirect:/curriculum/ranger/list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(endorserRecord, "endorserRecord.commit.error");
			}

		return result;
	}

	// Deleting --------------------------------------------

	@RequestMapping(value = "edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final EndorserRecord endorserRecord, final BindingResult binding) {
		ModelAndView result;

		try {
			this.endorserRecordService.delete(endorserRecord);
			result = new ModelAndView("redirect:list.do");
		} catch (final Throwable oops) {
			result = this.createEditModelAndView(endorserRecord, "endorserRecord.commit.error");
		}

		return result;
	}

	// Ancillary methods -------------------------------------

	protected ModelAndView createEditModelAndView(final EndorserRecord endorserRecord) {
		ModelAndView result;

		result = this.createEditModelAndView(endorserRecord, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final EndorserRecord endorserRecord, final String messageCode) {

		final ModelAndView result;

		result = new ModelAndView("endorserRecord/edit");
		result.addObject("endorserRecord", endorserRecord);

		return result;
	}
}
