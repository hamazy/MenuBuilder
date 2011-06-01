package menubuilder;

import org.antlr.runtime.Token;
import org.antlr.runtime.tree.CommonTreeAdaptor;

public class MyNodeAdaptor extends CommonTreeAdaptor {

	@Override
	public Object create(Token token) {

		return new MfTree(token);
	}

}
