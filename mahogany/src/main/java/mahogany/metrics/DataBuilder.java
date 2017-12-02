package mahogany.metrics;

import java.util.List;
import mahogany.entities.Elections;

public interface DataBuilder<T extends AbstractStateData<?>> {

	T generateDataObject(List<Elections> electionList);
	
}
