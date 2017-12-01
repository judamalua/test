
package services;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import repositories.CategoryRepository;
import domain.Category;
import domain.Trip;

@Service
@Transactional
public class CategoryService {

	// Managed repository --------------------------------------------------

	@Autowired
	private CategoryRepository	categoryRepository;

	// Supporting services --------------------------------------------------

	@Autowired
	private ActorService		actorService;

	@Autowired
	private TripService			tripService;


	// Simple CRUD methods --------------------------------------------------

	public Category create() {
		this.actorService.checkUserLogin();

		Category result;
		final Collection<Trip> trips = new HashSet<Trip>();
		final Collection<Category> categories = new HashSet<Category>();

		result = new Category();
		result.setTrips(trips);
		result.setCategories(categories);

		return result;
	}

	public Collection<Category> findAll() {

		Collection<Category> result;

		Assert.notNull(this.categoryRepository);
		result = this.categoryRepository.findAll();
		Assert.notNull(result);

		return result;

	}

	public Category findOne(final int categoryId) {

		Category result;

		result = this.categoryRepository.findOne(categoryId);

		return result;

	}

	public Category save(final Category category) {
		this.actorService.checkUserLogin();
		this.checkCategory(category);

		assert category != null;
		if (category.getFatherCategory().equals(null))
			// Si no tiene categoría padre, ponemos la categoría por defecto CATEGORY como
			// categoría padre.
			category.setFatherCategory(this.categoryRepository.findRootCategory());

		final Collection<Category> childrenCategories = category.getFatherCategory().getCategories();

		// El nombre de la categoría debe ser único dentro de los hijos de un mismo padre.
		for (final Category c : childrenCategories)
			if (!c.equals(category))
				Assert.isTrue(!category.getName().equals(c.getName()));

		Category result;
		final Collection<Trip> trips = category.getTrips();

		result = this.categoryRepository.save(category);
		for (final Trip t : trips)
			t.setCategory(result);

		return result;

	}
	public void delete(final Category category) {
		this.actorService.checkUserLogin();

		assert category != null;
		assert category.getId() != 0;
		final Collection<Trip> trips = category.getTrips();

		Assert.isTrue(this.categoryRepository.exists(category.getId()));

		// TODO: Preguntar lunes.
		// Al eliminar una categoría, referenciamos los viajes a su categoría padre.
		for (final Trip t : trips) {
			t.setCategory(category.getFatherCategory());
			this.tripService.save(t);
		}

		this.categoryRepository.delete(category);

	}

	// Other methods --------------------------------------------------

	private void checkCategory(final Category category) {

		final boolean result;
		final Map<String, Category> mem;

		mem = new HashMap<String, Category>();
		result = this.getRootFather(category, mem);

		Assert.isTrue(result);

	}
	private boolean getRootFather(final Category category, final Map<String, Category> mem) {

		boolean result;
		Category rootCategory;

		rootCategory = this.categoryRepository.findRootCategory();
		Assert.notNull(rootCategory);

		mem.put(category.getName(), category);

		if (category.getFatherCategory().equals(rootCategory))
			result = true;
		else if (mem.get(category.getFatherCategory().getName()) != null || mem.keySet().contains(category.getFatherCategory().getName()))
			result = false;
		else
			result = this.getRootFather(category.getFatherCategory(), mem);

		Assert.notNull(result);
		return result;
	}

	/**
	 * 
	 * @return children categories from root category.
	 */
	public Collection<Category> getChildrenFromRoot() {

		Collection<Category> categories;
		Category category;

		category = this.categoryRepository.findRootCategory();
		Assert.notNull(category);

		categories = category.getCategories();
		Assert.notNull(categories);

		return categories;

	}

}
