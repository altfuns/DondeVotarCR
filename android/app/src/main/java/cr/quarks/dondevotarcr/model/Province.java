package cr.quarks.dondevotarcr.model;

import com.suigeneris.android.core.bo.BaseBusinessObject;
import com.suigeneris.android.core.data.DatabaseField;
import com.suigeneris.android.core.data.DatabaseTable;

/**
 * Created by luisa on 2/5/16.
 */
@DatabaseTable(name = "provincias")
public class Province extends BaseBusinessObject{
    @DatabaseField(name = "nombre")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
