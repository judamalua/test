
package controllers.admin;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import services.ActorService;
import services.SearchService;
import controllers.AbstractController;
import domain.Search;

@Controller
@RequestMapping("/search/admin")
public class SearchAdminController extends AbstractController {

	// Services -------------------------------------------------------

	@Autowired
	ActorService	actorService;

	@Autowired
	SearchService	searchService;


	// Listing ---------------------------------------------------------

	@RequestMapping(value = "/list", method = RequestMethod.GET)
	public ModelAndView list() {
		ModelAndView result;

		result = new ModelAndView("search/list");
		final Collection<Search> searchs = this.searchService.findAll();
		result.addObject("requestUri", "search/list.do");
		result.addObject("searchs", searchs);

		return result;
	}

	// Ancillary methods --------------------------------------------------

}
