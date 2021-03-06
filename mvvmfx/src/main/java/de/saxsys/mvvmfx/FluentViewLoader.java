package de.saxsys.mvvmfx;

import java.util.ResourceBundle;

import de.saxsys.mvvmfx.internal.viewloader.FxmlViewLoader;
import de.saxsys.mvvmfx.internal.viewloader.JavaViewLoader;

/**
 * Fluent API for loading Views. 
 * <br>
 * 
 * The typical usage will look like this:
 * 
 * <br>
 * 
 * <pre>
 * public class MyCoolViewModel implements ViewModel {...}
 * public class MyCoolView implements FxmlView{@code <MyCoolViewModel>} {...}
 * 
 * 
 * public class MyApp extends Application {
 *     
 *    {@literal @}Override
 *     public void start(Stage stage) {
 * 		ViewTuple{@code <MyCoolView, MyCoolViewModel>} viewTuple 
 * 				= FluentViewLoader.fxmlView(MyCoolView.class).load(); 
 * 			
 * 		Parent root = viewTuple.getView();
 * 		stage.setScene(new Scene(root));
 * 		stage.show();	
 *     }
 * }
 *     
 * </pre>
 * 
 * 
 * This class is implemented as a Step-Builder. You can choose between {@link FxmlView} and {@link JavaView} with the
 * first method call. After that you will only get builder-methods that are suitable for the view type you have chosen.
 * 
 * @author manuel.mauky
 */
public class FluentViewLoader {
	
	/**
	 * This class is the builder step to load a java based view. It is accessed from the {@link FluentViewLoader} with the
	 * method {@link FluentViewLoader#javaView(Class)}.
	 * 
	 * @param <ViewType> the generic type of the View that should be loaded. This type has to implement {@link de.saxsys.mvvmfx.JavaView}.
	 * @param <ViewModelType> the generic type of the ViewModel. This type has to implement {@link de.saxsys.mvvmfx.ViewModel}.
	 */
	public static class JavaViewStep<ViewType extends JavaView<? extends ViewModelType>, ViewModelType extends ViewModel> {
		
		private Class<? extends ViewType> viewType;
		private ResourceBundle resourceBundle;
		
		private ViewModelType viewModel;
		
		JavaViewStep(Class<? extends ViewType> viewType) {
			this.viewType = viewType;
		}
		
		/**
		 * @param resourceBundle
		 *            the resource bundle that is used while loading the view.
		 * @return this instance of the builder step.
		 */
		public JavaViewStep<ViewType, ViewModelType> resourceBundle(ResourceBundle resourceBundle) {
			this.resourceBundle = resourceBundle;
			return this;
		}

		/**
		 * This param is used to define an existing viewModel instance to be used when loading the view.<br>
		 *
		 * A typical use case is when you like to have two or more views that are sharing the same viewModel.
		 *
		 * @param viewModel the viewModel instance that is used to load the java view.
		 * @return this instance of the builder step.
		 */
		public JavaViewStep<ViewType, ViewModelType> viewModel(ViewModelType viewModel){
			this.viewModel = viewModel;
			return this;
		}
		
		
		/**
		 * The final step of the Fluent API. This method loads the view based on the given params.
		 *
		 * @return a view tuple containing the loaded view.
		 */
		public ViewTuple<ViewType, ViewModelType> load() {
			JavaViewLoader javaViewLoader = new JavaViewLoader();
			
			return javaViewLoader.loadJavaViewTuple(viewType, resourceBundle, viewModel);
		}
	}
	
	/**
	 * This class is the builder step to load a fxml based view. It is accessed from the {@link FluentViewLoader} with the
	 * method {@link FluentViewLoader#fxmlView(Class)}.
	 *
	 * @param <ViewType> the generic type of the View that should be loaded. This type has to implement {@link de.saxsys.mvvmfx.FxmlView}.
	 * @param <ViewModelType> the generic type of the ViewModel. This type has to implement {@link de.saxsys.mvvmfx.ViewModel}.
	 */
	public static class FxmlViewStep<ViewType extends FxmlView<? extends ViewModelType>, ViewModelType extends ViewModel> {
		
		private Class<? extends ViewType> viewType;
		private ResourceBundle resourceBundle;
		private Object root;
		private ViewType codeBehind;
		private ViewModelType viewModel;

		FxmlViewStep(Class<? extends ViewType> viewType) {
			this.viewType = viewType;
		}
		
		/**
		 * @param resourceBundle
		 *            the resource bundle that is used while loading the view.
		 * @return this instance of the builder step.
		 */
		public FxmlViewStep<ViewType, ViewModelType> resourceBundle(ResourceBundle resourceBundle) {
			this.resourceBundle = resourceBundle;
			return this;
		}
		
		/**
		 * This param is used to define a JavaFX node that is used as the root element when loading the fxml file. <br>
		 * 
		 * This can be useful when creating custom controls with the fx:root element.
		 * 
		 * @param root
		 *            the root element that is used to load the fxml file.
		 * @return this instance of the builder step.
		 */
		public FxmlViewStep<ViewType, ViewModelType> root(Object root) {
			this.root = root;
			return this;
		}
		
		/**
		 * This param is used to define an existing instance of the codeBehind class that is used instead of creating a
		 * new one while loading. <br>
		 *
		 * This can be useful when creating custom controls with the fx:root element.
		 *
		 * @param codeBehind
		 *            the codeBehind instance that is used to load the fxml file.
		 * @return this instance of the builder step.
		 */
		public FxmlViewStep<ViewType, ViewModelType> codeBehind(ViewType codeBehind) {
			this.codeBehind = codeBehind;
			return this;
		}

		/**
		 * This param is used to define an existing viewModel instance to be used when loading the view.<br>
		 * 
		 * A typical use case is when you like to have two or more views that are sharing the same viewModel.
		 * 
		 * @param viewModel the viewModel instance that is used to load the fxml file.
		 * @return this instance of the builder step.
		 */
		public FxmlViewStep<ViewType, ViewModelType> viewModel(ViewModelType viewModel){
			this.viewModel = viewModel;
			return this;
		}
		
		/**
		 * The final step of the Fluent API. This method loads the view based on the given params.
		 * 
		 * @return a view tuple containing the loaded view.
		 */
		public ViewTuple<ViewType, ViewModelType> load() {
			FxmlViewLoader fxmlViewLoader = new FxmlViewLoader();
			
			return fxmlViewLoader.loadFxmlViewTuple(viewType, resourceBundle, codeBehind, root, viewModel);
		}
	}
	
	
	/**
	 * This method is the entry point of the Fluent API to load a java based view.
	 * 
	 * @param viewType the type of the view that should be loaded.
	 * @param <ViewType> the type of the View that should be loaded. This type has to implement {@link de.saxsys.mvvmfx.JavaView}.
	 * @param <ViewModelType> the type of the ViewModel. This type has to implement {@link de.saxsys.mvvmfx.ViewModel}.
	 *                       
	 * @return a builder step that can be further configured and then load the actual view.
	 */
	public static <ViewType extends JavaView<? extends ViewModelType>, ViewModelType extends ViewModel>
			JavaViewStep<ViewType, ViewModelType> javaView(Class<? extends ViewType> viewType) {
		return new JavaViewStep<>(viewType);
	}
	
	/**
	 * This method is the entry point of the Fluent API to load a fxml based View.
	 * 
	 * @param viewType the type of the view that should be loaded.
	 * @param <ViewType> the generic type of the View that should be loaded. This type has to implement {@link de.saxsys.mvvmfx.FxmlView}.
	 * @param <ViewModelType> the generic type of the ViewModel. This type has to implement {@link de.saxsys.mvvmfx.ViewModel}.
	 *                       
	 * @return a builder step that can be further configured and then load the actual view.
	 */
	public static <ViewType extends FxmlView<? extends ViewModelType>, ViewModelType extends ViewModel>
			FxmlViewStep<ViewType, ViewModelType> fxmlView(Class<? extends ViewType> viewType) {
		return new FxmlViewStep<>(viewType);
	}
	
}
