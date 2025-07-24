package module.remnant.datapanel.data.validation;

import java.util.List;

public interface FormatDetect extends CommonValidate{
	
	boolean detectFormat(List<String> strings);
}
