package lib.database;

import java.lang.reflect.Field;

public class FieldObject {

	Object param;
	SqlField field;

	public FieldObject(Object param, Field field) {
		this.param = param;
		this.field = SqlField.getInstance(field);
	}

	public Object getParam() {
		return param;
	}

	public String getColumnName() {
		return field.getColumnName();
	}

}
