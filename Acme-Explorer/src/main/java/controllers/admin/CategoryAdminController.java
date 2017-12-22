
package controllers.admin;

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

import services.CategoryService;
import services.TripService;
import controllers.AbstractController;
import domain.Category;

@Controller
@RequestMapping("/category/admin")
public class CategoryAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	CategoryService	categoryService;

	@Autowired
	TripService		tripService;


	// Editing ---------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.GET)
	public ModelAndView edit(@RequestParam final int categoryId) {
		ModelAndView result;
		Category category;

		try {
			category = this.categoryService.findOne(categoryId);
			Assert.notNull(category);
			result = this.createEditModelAndView(category);
		} catch (final Throwable oops) {
			result = new ModelAndView("redirect:/misc/403");
		}

		return result;
	}

	// Creating ---------------------------------------------------------

	@RequestMapping(value = "/create", method = RequestMethod.GET)
	public ModelAndView create() {
		ModelAndView result;
		Category category;

		category = this.categoryService.create();
		result = this.createEditModelAndView(category);

		return result;
	}

	// Saving -------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "save")
	public ModelAndView save(@Valid final Category category, final BindingResult binding) {
		ModelAndView result;

		if (binding.hasErrors())
			result = this.createEditModelAndView(category, "params.error");
		else
			try {
				this.categoryService.save(category);
				result = new ModelAndView("redirect:/category/list.do");

			} catch (final Throwable oops) {
				result = this.createEditModelAndView(category, "commit.error");
			}

		return result;
	}

	// Deleting ------------------------------------------------------------------------

	@RequestMapping(value = "/edit", method = RequestMethod.POST, params = "delete")
	public ModelAndView delete(final Category category, final BindingResult binding) {
		ModelAndView result;

		try {
			this.categoryService.delete(category);
			result = new ModelAndView("redirect:/category/list.do");

		} catch (final Throwable oops) {
			result = this.createEditModelAndView(category, "commit.error");
		}

		return result;
	}

	// Ancillary methods --------------------------------------------------

	protected ModelAndView createEditModelAndView(final Category category) {
		ModelAndView result;

		result = this.createEditModelAndView(category, null);

		return result;
	}

	protected ModelAndView createEditModelAndView(final Category category, final String messageCode) {
		ModelAndView result;
		final Category rootCategory;
		final Collection<Category> categories;

		//trips = this.tripService.findAllTripsByCategoryId(categoryId);

		rootCategory = this.categoryService.getRootCategory();
		categories = this.categoryService.findAll();

		result = new ModelAndView("category/edit");
		//result.addObject("trips", trips);
		result.addObject("rootCategory", rootCategory);
		result.addObject("categories", categories);
		result.addObject("category", category);

		result.addObject("message", messageCode);

		return result;

	}
}
