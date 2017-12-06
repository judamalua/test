
package controllers.manager;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import services.StageService;
import controllers.AbstractController;
import domain.Stage;

@Controller
@RequestMapping("/stage/manager")
public class StageManagerController extends AbstractController {

	@Autowired
	StageService	stageService;


	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int stageId) {
		ModelAndView result;
		Stage stage;

		stage = this.stageService.findOne(stageId);
		Assert.notNull(stage);
		result = this.createEditModelAndView(stage);

		return result;
	}

	// Creating ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Stage stage;

		stage = this.stageService.create();
		result = this.createEditModelAndView(stage);

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Stage stage, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(stage, "stage.params.error");
		else
			try {
				this.stageService.save(stage);
				result = new ModelAndView("redirect:/category/list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(stage, "stage.commit.error");
			}

		return result;
	}

	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Stage stage, final BindingResult binding) {
		ModelAndView result;

		try {
			this.stageService.delete(stage);
			result = new ModelAndView("redirect:/trip/manager/list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(stage, "category.commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Stage stage) {
		ModelAndView result;

		result = this.createEditModelAndView(stage, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Stage stage, final String messageCode) {
		ModelAndView result;

		result = new ModelAndView("stage/edit");
		result.addObject("stage", stage);

		result.addObject("message", messageCode);

		return result;

	}

}
