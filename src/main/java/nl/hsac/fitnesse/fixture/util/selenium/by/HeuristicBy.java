package nl.hsac.fitnesse.fixture.util.selenium.by;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.function.Function;

/**
 * FirstElementBy which if no interactable element is found returns the first element matched
 * (which was originally filtered out).
 */
public class HeuristicBy extends FirstElementBy {
    /**
     * Creates new, using {@link IsInteractableFilter}.
     * (First By is separate so compiler will ensure at least one By is passed.)
     * @param firstNested first By to be wrapped.
     * @param extraNestedBys optional extra Bys to be wrapped.
     */
    protected HeuristicBy(By firstNested, By... extraNestedBys) {
        this(new IsInteractableFilter(), firstNested, extraNestedBys);
    }

    /**
     * Creates new.
     * (First By is separate so compiler will ensure at least one By is passed.)
     * @param postProcessor post processor to use.
     * @param firstNested first By to be wrapped.
     * @param extraNestedBys optional extra Bys to be wrapped.
     */
    protected HeuristicBy(IsInteractableFilter postProcessor, By firstNested, By... extraNestedBys) {
        super(postProcessor, firstNested, extraNestedBys);
    }

    @Override
    public WebElement findElement(SearchContext context) {
        WebElement element = super.findElement(context);
        if (element == null) {
            // no interactable element found
            Function<WebElement, WebElement> postProcessor = getPostProcessor();
            if (postProcessor instanceof IsInteractableFilter) {
                element = ((IsInteractableFilter) postProcessor).getFirstFound();
            }
        }
        return element;
    }
}